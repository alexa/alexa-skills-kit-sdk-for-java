/*
    Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.utils;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import com.amazon.ask.attributes.persistence.impl.utils.ItemUtilsV2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ItemUtilsV2Test {

    @Test
    public void to_simple_map_value_string_attribute() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().s("test").build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), "test");
    }

    @Test
    public void to_simple_map_value_null_true_attribute() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().nul(true).build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void to_simple_map_value_null_false_attribute_throws_exception() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().nul(false).build());
        ItemUtilsV2.toSimpleMapValue(values);
    }

    @Test
    public void to_simple_map_value_bool_attribute() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().bool(true).build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), true);
    }

    @Test
    public void to_simple_map_value_bigdecimal_attribute() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().n("1238126387123").build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), new BigDecimal("1238126387123"));
    }

    @Test
    public void to_simple_map_value_byte_attribute() {
        byte[] test = "test".getBytes();
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().b(SdkBytes.fromByteArray(test)).build());
        assertArrayEquals((byte[])ItemUtilsV2.toSimpleMapValue(values).get("test"), test);
    }

    @Test
    public void to_simple_map_value_string_set_attribute() {
        Set<String> test = Collections.singleton("test");
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().ss(test).build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), test);
    }

    @Test
    public void to_simple_map_value_number_set_attribute() {
        Set<String> test = Collections.singleton("1.234");
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().ns(test).build());
        Set<BigDecimal> expected = Collections.singleton(new BigDecimal("1.234"));
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), expected);
    }

    @Test
    public void to_simple_map_value_binary_set_attribute() {
        byte[] byteArray = "test".getBytes();
        List<SdkBytes> test = Collections.singletonList(SdkBytes.fromByteArray(byteArray));
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().bs(test).build());
        Set<byte[]> expected = Collections.singleton(byteArray);
        assertArrayEquals(((Set<byte[]>)ItemUtilsV2.toSimpleMapValue(values).get("test")).iterator().next(), expected.iterator().next());
    }

    @Test
    public void to_simple_map_value_list_attribute() {
        AttributeValue test = AttributeValue.builder().s("test").build();
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().l(test).build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), Arrays.asList("test"));
    }

    @Test
    public void to_simple_map_value_map_attribute() {
        Map<String, AttributeValue> testMap = Collections.singletonMap("testKey", AttributeValue.builder().s("test").build());
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().m(testMap).build());
        assertEquals(ItemUtilsV2.toSimpleMapValue(values).get("test"), Collections.singletonMap("testKey", "test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void empty_attribute_value_throws_exception() {
        Map<String, AttributeValue> values = Collections.singletonMap("test", AttributeValue.builder().build());
        ItemUtilsV2.toSimpleMapValue(values);
    }

    @Test
    public void from_simple_map_string_attribute() {
        Map<String, Object> simpleMap = Collections.singletonMap("test", "foo");
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().s("foo").build());
    }

    @Test
    public void from_simple_map_null_attribute() {
        Map<String, Object> simpleMap = Collections.singletonMap("test", null);
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().nul(true).build());
    }

    @Test
    public void from_simple_map_boolean_attribute() {
        Map<String, Object> simpleMap = Collections.singletonMap("test", true);
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().bool(true).build());
    }

    @Test
    public void from_simple_map_bigdecimal_attribute() {
        Map<String, Object> simpleMap = Collections.singletonMap("test", new BigDecimal("1.234"));
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().n("1.234").build());
    }

    @Test
    public void from_simple_map_byte_attribute() {
        byte[] test = "test".getBytes();
        Map<String, Object> simpleMap = Collections.singletonMap("test", test);
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().b(SdkBytes.fromByteArray(test)).build());
    }

    @Test
    public void from_simple_map_string_set_attribute() {
        Set<String> testSet = Collections.singleton("test");
        Map<String, Object> simpleMap = Collections.singletonMap("test", testSet);
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().ss(testSet).build());
    }

    @Test
    public void from_simple_map_number_set_attribute() {
        Set<Double> testSet = Collections.singleton(1.234);
        Map<String, Object> simpleMap = Collections.singletonMap("test", testSet);
        Set<String> expected = Collections.singleton("1.234");
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().ns(expected).build());
    }

    @Test
    public void from_simple_map_binary_set_attribute() {
        byte[] test = "test".getBytes();
        Map<String, Object> simpleMap = Collections.singletonMap("test", Collections.singleton(test));
        Set<SdkBytes> expected = Collections.singleton(SdkBytes.fromByteArray(test));
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().bs(expected).build());
    }

    @Test
    public void from_simple_map_list_attribute() {
        List<String> test = Collections.singletonList("1");
        Map<String, Object> simpleMap = Collections.singletonMap("test", test);
        List<AttributeValue> expected = Collections.singletonList(AttributeValue.builder().s("1").build());
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().l(expected).build());
    }

    @Test
    public void from_simple_map_map_attribute() {
        Map<String, String> test = Collections.singletonMap("foo", "bar");
        Map<String, Object> simpleMap = Collections.singletonMap("test", test);
        Map<String, AttributeValue> expected = Collections.singletonMap("foo", AttributeValue.builder().s("bar").build());
        assertEquals(ItemUtilsV2.fromSimpleMap(simpleMap).get("test"), AttributeValue.builder().m(expected).build());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void from_simple_map_unsupported_type_throws_exception() {
        Map<String, Object> simpleMap = Collections.singletonMap("test", AttributeValue.builder().build());
        ItemUtilsV2.fromSimpleMap(simpleMap);
    }

    public void from_simple_map_value() {

    }
    
}