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
import com.amazon.ask.util.JsonUnmarshaller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

public class JacksonJsonUnmarshaller<Type> implements JsonUnmarshaller<Type> {

    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    private final Class<Type> outputType;
    private final String requiredField;

    protected JacksonJsonUnmarshaller(Class<Type> outputType, String requiredField) {
        this.outputType = outputType;
        this.requiredField = requiredField;
    }

    public static <Output> JacksonJsonUnmarshaller<Output> withTypeBinding(Class<Output> outputType,
                                                                           String requiredField) {
        return new JacksonJsonUnmarshaller<>(outputType, requiredField);
    }

    @Override
    public Optional<Type> unmarshall(byte[] in) {
        try {
            JsonNode json = MAPPER.readTree(in);
            if (!json.has(requiredField)) {
                return Optional.empty();
            }
            return Optional.of(MAPPER.treeToValue(json, outputType));
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

}
