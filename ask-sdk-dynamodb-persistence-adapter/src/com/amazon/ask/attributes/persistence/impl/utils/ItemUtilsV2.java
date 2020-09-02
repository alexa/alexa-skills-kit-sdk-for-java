// CHECKSTYLE:OFF
/*
    Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.persistence.impl.utils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A port of the AWS SDK for Java v1 ItemUtils class which is currently
 * unavailable in the v2 SDK and is necessary to convert between higher level
 * DynamoDB wrapped DAO types and generic Java types.
 *
 * Once this class is available in the v2 AWS SDK this implementation should be
 * removed.
 */
public class ItemUtilsV2 {

    public static <T> Map<String, T> toSimpleMapValue(
        Map<String, AttributeValue> values) {
        if (values == null) {
            return null;
        }

        Map<String, T> result = new LinkedHashMap<String, T>(values.size());
        for (Map.Entry<String, AttributeValue> entry : values.entrySet()) {
            T t = toSimpleValue(entry.getValue());
            result.put(entry.getKey(), t);
        }
        return result;
    }

    public static Map<String, AttributeValue> fromSimpleMap(
        Map<String, Object> map) {
        if (map == null)
            return null;
        // row with multiple attributes
        Map<String, AttributeValue> result = new LinkedHashMap<String, AttributeValue>();
        for (Map.Entry<String, Object> entry : map.entrySet())
            result.put(entry.getKey(), toAttributeValue(entry.getValue()));
        return result;
    }

    private static List<Object> toSimpleList(List<AttributeValue> attrValues) {
        if (attrValues == null)
            return null;
        List<Object> result = new ArrayList<Object>(attrValues.size());
        for (AttributeValue attrValue : attrValues) {
            Object value = toSimpleValue(attrValue);
            result.add(value);
        }
        return result;
    }

    private static <T> T toSimpleValue(AttributeValue value) {
        if (value == null) {
            return null;
        }
        if (Boolean.TRUE.equals(value.nul())) {
            return null;
        } else if (Boolean.FALSE.equals(value.nul())) {
            throw new UnsupportedOperationException("False-NULL is not supported in DynamoDB");
        } else if (value.bool() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) value.bool();
            return t;
        } else if (value.s() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) value.s();
            return t;
        } else if (value.n() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) new BigDecimal(value.n());
            return t;
        } else if (value.b() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) value.b().asByteArray();
            return t;
        } else if (value.ss() != null && !value.ss().isEmpty()) {
            @SuppressWarnings("unchecked")
            T t = (T) new LinkedHashSet<String>(value.ss());
            return t;
        } else if (value.ns() != null && !value.ns().isEmpty()) {
            Set<BigDecimal> set = new LinkedHashSet<BigDecimal>(value.ns().size());
            for (String s : value.ns()) {
                System.out.println(s);
                set.add(new BigDecimal(s));
            }
            @SuppressWarnings("unchecked")
            T t = (T) set;
            return t;
        } else if (value.bs() != null && !value.bs().isEmpty()) {
            Set<byte[]> set = new LinkedHashSet<byte[]>(value.bs().size());
            for (SdkBytes bb : value.bs()) {
                set.add(bb.asByteArray());
            }
            @SuppressWarnings("unchecked")
            T t = (T) set;
            return t;
        } else if (value.l() != null && !value.l().isEmpty()) {
            @SuppressWarnings("unchecked")
            T t = (T) toSimpleList(value.l());
            return t;
        } else if (value.m() != null && !value.m().isEmpty()) {
            @SuppressWarnings("unchecked")
            T t = (T) toSimpleMapValue(value.m());
            return t;
        } else {
            throw new IllegalArgumentException(
                "Attribute value must not be empty: " + value);
        }
    }

    private static AttributeValue toAttributeValue(Object value) {
        AttributeValue.Builder resultBuilder = AttributeValue.builder();
        if (value == null) {
            return resultBuilder.nul(Boolean.TRUE).build();
        } else if (value instanceof Boolean) {
            return resultBuilder.bool((Boolean)value).build();
        } else if (value instanceof String) {
            return resultBuilder.s((String) value).build();
        } else if (value instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) value;
            return resultBuilder.n(bd.toPlainString()).build();
        } else if (value instanceof Number) {
            return resultBuilder.n(value.toString()).build();
        } else if (value instanceof byte[]) {
            return resultBuilder.b(SdkBytes.fromByteArray((byte[]) value)).build();
        } else if (value instanceof ByteBuffer) {
            return resultBuilder.b(SdkBytes.fromByteBuffer((ByteBuffer) value)).build();
        } else if (value instanceof Set) {
            // default to an empty string set if there is no element
            @SuppressWarnings("unchecked")
            Set<Object> set = (Set<Object>) value;
            if (set.size() == 0) {
                return resultBuilder.ss(new LinkedHashSet<String>()).build();
            }
            Object element = set.iterator().next();
            if (element instanceof String) {
                @SuppressWarnings("unchecked")
                Set<String> ss = (Set<String>) value;
                resultBuilder.ss(new ArrayList<String>(ss));
            } else if (element instanceof Number) {
                @SuppressWarnings("unchecked")
                Set<Number> in = (Set<Number>) value;
                List<String> out = new ArrayList<String>(set.size());
                for (Number n : in) {
                    BigDecimal bd = n instanceof BigDecimal ? (BigDecimal)n : new BigDecimal(n.toString());
                    out.add(bd.toPlainString());
                }
                resultBuilder.ns(out);
            } else if (element instanceof byte[]) {
                @SuppressWarnings("unchecked")
                Set<byte[]> in = (Set<byte[]>) value;
                List<SdkBytes> out = new ArrayList<SdkBytes>(set.size());
                for (byte[] buf : in) {
                    out.add(SdkBytes.fromByteArray(buf));
                }
                resultBuilder.bs(out);
            } else if (element instanceof ByteBuffer) {
                @SuppressWarnings("unchecked")
                Set<ByteBuffer> bs = (Set<ByteBuffer>) value;
                List<SdkBytes> out = new ArrayList<SdkBytes>(set.size());
                for (ByteBuffer buf : bs) {
                    out.add(SdkBytes.fromByteBuffer(buf));
                }
                resultBuilder.bs(out);
            } else {
                throw new UnsupportedOperationException("element type: "
                                                        + element.getClass());
            }
        } else if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> in = (List<Object>) value;
            List<AttributeValue> out = new ArrayList<AttributeValue>();
            for (Object v : in) {
                out.add(toAttributeValue(v));
            }
            resultBuilder.l(out);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> in = (Map<String, Object>) value;
            if (in.size() > 0) {
                Map<String, AttributeValue> result = new HashMap<>();
                for (Map.Entry<String, Object> e : in.entrySet()) {
                    result.put(e.getKey(), toAttributeValue(e.getValue()));
                }
                resultBuilder.m(result);
            } else {    // empty map
                resultBuilder.m(new LinkedHashMap<String,AttributeValue>());
            }
        } else {
            throw new UnsupportedOperationException("value type: "
                                                    + value.getClass());
        }
        return resultBuilder.build();
    }

}
