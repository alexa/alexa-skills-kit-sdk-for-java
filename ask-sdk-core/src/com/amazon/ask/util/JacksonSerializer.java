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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Jackson backed implementation of {@link Serializer}
 */
public final class JacksonSerializer implements Serializer {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * For testing purposes.
     */
    static void setMapper(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    @Override
    public <T> String serialize(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error", e);
        }
    }

    @Override
    public <T> void serialize(T object, OutputStream outputStream) {
        try {
            mapper.writeValue(outputStream, object);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error");
        }
    }

    @Override
    public <T> T deserialize(String s, Class<T> aClass) {
        try {
            return mapper.readValue(s, aClass);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

}
