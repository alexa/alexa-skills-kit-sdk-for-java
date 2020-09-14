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

/**
 * Jackson Unmarshaller to deserialize a given byte array based on a namespace discriminator.
 * Implements {@link JsonUnmarshaller}.
 * @param <Type> type to unmarshall
 */
public class NamespaceEnabledJacksonJsonUnmarshaller<Type> implements JsonUnmarshaller<Type> {

    /**
     * Mapper used to serialize and de-serialize objects.
     */
    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    /**
     * Output type after unmarshalling.
     */
    private final Class<Type> outputType;

    /**
     * Map of valid class types.
     */
    private final Map<String, Class> validTypes;

    /**
     * Constructor for NamespaceEnabledJacksonJsonUnmarshaller.
     * @param outputType Output type after unmarshalling.
     * @param validTypes Map of valid class types.
     */
    protected NamespaceEnabledJacksonJsonUnmarshaller(final Class<Type> outputType, final Map<String, Class> validTypes) {
        this.outputType = outputType;
        this.validTypes = validTypes;
    }

    /**
     * Return an instance of NamespaceEnabledJacksonJsonUnmarshaller.
     * @param outputType Output type after unmarshalling.
     * @param validTypes Map of valid class types.
     * @param <Output> Type of NamespaceEnabledJacksonJsonUnmarshaller.
     * @return {@link NamespaceEnabledJacksonJsonUnmarshaller}.
     */
    public static <Output> NamespaceEnabledJacksonJsonUnmarshaller<Output> withTypeBinding(final Class<Output> outputType,
                                                                                           final Map<String, Class> validTypes) {
        return new NamespaceEnabledJacksonJsonUnmarshaller<>(outputType, validTypes);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Optional<UnmarshalledRequest<Type>> unmarshall(final byte[] in) {
        try {
            JsonNode root = MAPPER.readTree(in);
            Optional<String> namespaceDiscriminator = UnmarshallUtils.getNamespaceDiscriminator(root);
            if (!namespaceDiscriminator.isPresent() || !validTypes.containsKey(namespaceDiscriminator.get())) {
                return Optional.empty();
            }
            Class targetType = validTypes.get(namespaceDiscriminator.get());
            UnmarshalledRequest<Type> unmarshalledRequest = new BaseUnmarshalledRequest<>((Type) MAPPER.treeToValue(root, targetType), root);
            return Optional.of(unmarshalledRequest);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }


}
