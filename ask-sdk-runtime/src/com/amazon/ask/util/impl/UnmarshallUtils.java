/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util.impl;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * Utility class, exposes a method to retrieve namespace discriminator.
 */
final class UnmarshallUtils {

    /**
     * Header field name.
     */
    static final String HEADER_FIELD_NAME = "header";

    /**
     * Namespace field name.
     */
    static final String NAMESPACE_FIELD_NAME = "namespace";

    /**
     * Field name for name key in a JSON node.
     */
    static final String NAME_FIELD_NAME = "name";

    /**
     * Prevent instantiation.
     */
    private UnmarshallUtils() { }

    /**
     * Computes the target discriminator value for a namespaced request by traversing
     * the root JSON node to the {@link #HEADER_FIELD_NAME} object and concatenating the
     * {@link #NAMESPACE_FIELD_NAME} and {@link #NAME_FIELD_NAME} properties.
     *
     * @param root root request JSON node
     * @return an {@link Optional} containing the computed discriminator value, or empty if
     *         none could be found.
     */
    static Optional<String> getNamespaceDiscriminator(final JsonNode root) {
        if (!root.has(HEADER_FIELD_NAME)) {
            return Optional.empty();
        }

        final JsonNode header = root.get(HEADER_FIELD_NAME);
        if (!header.has(NAMESPACE_FIELD_NAME) || !header.get(NAMESPACE_FIELD_NAME).isTextual()) {
            return Optional.empty();
        }
        if (!header.has(NAME_FIELD_NAME) || !header.get(NAME_FIELD_NAME).isTextual()) {
            return Optional.empty();
        }

        final String targetNamespace = header.get(NAMESPACE_FIELD_NAME).asText();
        final String targetName = header.get(NAME_FIELD_NAME).asText();

        return Optional.of(targetNamespace + "." + targetName);
    }

}
