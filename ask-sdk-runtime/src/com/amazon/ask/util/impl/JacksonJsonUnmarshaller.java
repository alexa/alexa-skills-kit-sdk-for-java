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
import com.amazon.ask.request.UnmarshalledRequest;
import com.amazon.ask.request.impl.BaseUnmarshalledRequest;
import com.amazon.ask.util.JsonUnmarshaller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JacksonJsonUnmarshaller<Type> implements JsonUnmarshaller<Type> {

    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    private final Class<? extends Type> outputType;
    private final List<String> discriminatorPath;
    private final Map<String, Class> validTypes;

    protected JacksonJsonUnmarshaller(Class<? extends Type> outputType, List<String> discriminatorPath, Map<String, Class> validTypes) {
        this.outputType = outputType;
        this.discriminatorPath = discriminatorPath;
        this.validTypes = validTypes;
    }

    public static <Output> JacksonJsonUnmarshaller<Output> withTypeBinding(Class<Output> outputType) {
        return new JacksonJsonUnmarshaller<>(outputType, null, null);
    }

    public static <Output> JacksonJsonUnmarshaller<Output> withTypeBinding(Class<Output> outputType,
                                                                           String requiredField) {
        return new JacksonJsonUnmarshaller<>(outputType, Arrays.asList(requiredField), null);
    }

    public static <Type> JacksonJsonUnmarshaller<Type> withTypeBinding(Class<? extends Type> outputType,
                                                                       List<String> discriminatorPath,
                                                                       Map<String, Class> validTypes) {
        return new JacksonJsonUnmarshaller<>(outputType, discriminatorPath, validTypes);
    }

    @Override
    public Optional<UnmarshalledRequest<Type>> unmarshall(byte[] in) {
        try {
            JsonNode json = MAPPER.readTree(in);
            if (discriminatorPath != null) {
                JsonNode discriminatorNode = json;
                for (String path : discriminatorPath) {
                    discriminatorNode = discriminatorNode.path(path);
                    if (discriminatorNode.isMissingNode()) {
                        return Optional.empty();
                    }
                }

                if (!discriminatorNode.isTextual()) {
                    throw new AskSdkException("Discriminator property is not text type");
                }

                String discriminatorValue = discriminatorNode.asText();
                if (validTypes != null && !validTypes.containsKey(discriminatorValue)) {
                    return Optional.empty();
                }
            }
            UnmarshalledRequest<Type> unmarshalledRequest = new BaseUnmarshalledRequest<>(MAPPER.treeToValue(json, outputType), json);
            return Optional.of(unmarshalledRequest);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

}
