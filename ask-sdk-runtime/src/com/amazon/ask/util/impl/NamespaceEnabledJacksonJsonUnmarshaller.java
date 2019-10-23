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
import java.util.Map;
import java.util.Optional;

public class NamespaceEnabledJacksonJsonUnmarshaller<Type> implements JsonUnmarshaller<Type> {

    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    private final Class<Type> outputType;
    private final Map<String, Class> validTypes;

    protected NamespaceEnabledJacksonJsonUnmarshaller(Class<Type> outputType, Map<String, Class> validTypes) {
        this.outputType = outputType;
        this.validTypes = validTypes;
    }

    public static <Output> NamespaceEnabledJacksonJsonUnmarshaller<Output> withTypeBinding(Class<Output> outputType,
                                                                                           Map<String, Class> validTypes) {
        return new NamespaceEnabledJacksonJsonUnmarshaller<>(outputType, validTypes);
    }

    @Override
    public Optional<UnmarshalledRequest<Type>> unmarshall(byte[] in) {
        try {
            JsonNode root = MAPPER.readTree(in);
            Optional<String> namespaceDiscriminator = UnmarshallUtils.getNamespaceDiscriminator(root);
            if (!namespaceDiscriminator.isPresent() || !validTypes.containsKey(namespaceDiscriminator.get())) {
                return Optional.empty();
            }
            Class targetType = validTypes.get(namespaceDiscriminator.get());
            UnmarshalledRequest<Type> unmarshalledRequest = new BaseUnmarshalledRequest<>((Type)MAPPER.treeToValue(root, targetType), root);
            return Optional.of(unmarshalledRequest);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }


}
