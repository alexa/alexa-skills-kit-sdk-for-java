/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util;

import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.exception.AskSdkException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.amazon.ask.util.impl.ObjectMapperFactory.MAPPER;

/**
 * Jackson backed implementation of {@link Serializer}.
 */
public final class JacksonSerializer implements Serializer {

    /**
     * Mapper used to serialize and de-serialize objects and byte streams respectively.
     */
    private static ObjectMapper mapper = MAPPER;

    /**
     * For testing purposes.
     * @param objectMapper instance of type {@link ObjectMapper}.
     */
    static void setMapper(final ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    @Override
    public <T> String serialize(final T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error", e);
        }
    }

    @Override
    public <T> void serialize(final T object, final OutputStream outputStream) {
        try {
            mapper.writeValue(outputStream, object);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error");
        }
    }

    @Override
    public <T> T deserialize(final String s, final Class<T> aClass) {
        try {
            return mapper.readValue(s, aClass);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

    @Override
    public <T> T deserialize(final InputStream inputStream, final Class<T> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

}
