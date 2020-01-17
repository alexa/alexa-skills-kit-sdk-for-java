/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.util.JsonMarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Jackson Marshaller to serialize a given object. Implements {@link JsonMarshaller}.
 * @param <Type> input type.
 */
public class JacksonJsonMarshaller<Type> implements JsonMarshaller<Type> {

    /**
     * Static instance of {@link ObjectMapper}.
     */
    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    /**
     * Returns an instance of JacksonJsonMarshaller.
     * @param type class from which the JSON is marshalled from.
     * @param <Type> input type.
     * @return {@link JacksonJsonMarshaller}.
     */
    public static <Type> JacksonJsonMarshaller<Type> forType(final Class<Type> type) {
        return new JacksonJsonMarshaller<>();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void marshall(final Type t, final OutputStream stream) {
        try {
            MAPPER.writeValue(stream, t);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error", e);
        }
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public byte[] marshall(final Type t) {
        try {
            return MAPPER.writeValueAsBytes(t);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error", e);
        }
    }

}
