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
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.amazon.ask.util.impl.UnmarshallUtils.HEADER_FIELD_NAME;
import static com.amazon.ask.util.impl.UnmarshallUtils.NAMESPACE_FIELD_NAME;
import static com.amazon.ask.util.impl.UnmarshallUtils.NAME_FIELD_NAME;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnmarshallUtilsTest {

    private JsonNode root;

    @Before
    public void setUp() {
        root = mock(JsonNode.class);
    }

    @Test
    public void get_namespace_discriminator_no_header_returns_empty() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(false);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.empty());
    }

    @Test
    public void get_namespace_discriminator_no_namespace_returns_empty() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(true);
        JsonNode header = mock(JsonNode.class);
        when(header.has(NAMESPACE_FIELD_NAME)).thenReturn(false);
        when(root.get(HEADER_FIELD_NAME)).thenReturn(header);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.empty());
    }

    @Test
    public void get_namespace_discriminator_namespace_not_textual_returns_empty() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(true);
        JsonNode header = mock(JsonNode.class);
        when(header.has(NAMESPACE_FIELD_NAME)).thenReturn(true);
        JsonNode namespace = mock(JsonNode.class);
        when(namespace.isTextual()).thenReturn(false);
        when(header.get(NAMESPACE_FIELD_NAME)).thenReturn(namespace);
        when(root.get(HEADER_FIELD_NAME)).thenReturn(header);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.empty());
    }

    @Test
    public void get_namespace_discriminator_no_name_returns_empty() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(true);
        JsonNode header = mock(JsonNode.class);
        when(header.has(NAMESPACE_FIELD_NAME)).thenReturn(true);
        when(header.has(NAME_FIELD_NAME)).thenReturn(false);
        JsonNode namespace = mock(JsonNode.class);
        when(namespace.isTextual()).thenReturn(true);
        when(header.get(NAMESPACE_FIELD_NAME)).thenReturn(namespace);
        when(root.get(HEADER_FIELD_NAME)).thenReturn(header);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.empty());
    }

    @Test
    public void get_namespace_discriminator_name_not_textual_returns_empty() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(true);
        JsonNode header = mock(JsonNode.class);
        when(header.has(NAMESPACE_FIELD_NAME)).thenReturn(true);
        when(header.has(NAME_FIELD_NAME)).thenReturn(false);
        JsonNode namespace = mock(JsonNode.class);
        when(namespace.isTextual()).thenReturn(true);
        when(header.get(NAMESPACE_FIELD_NAME)).thenReturn(namespace);
        JsonNode name = mock(JsonNode.class);
        when(name.isTextual()).thenReturn(true);
        when(header.get(NAME_FIELD_NAME)).thenReturn(name);
        when(root.get(HEADER_FIELD_NAME)).thenReturn(header);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.empty());
    }

    @Test
    public void target_discriminator_concat_of_namespace_name() {
        when(root.has(HEADER_FIELD_NAME)).thenReturn(true);
        JsonNode header = mock(JsonNode.class);
        when(header.has(NAMESPACE_FIELD_NAME)).thenReturn(true);
        when(header.has(NAME_FIELD_NAME)).thenReturn(true);
        JsonNode namespace = mock(JsonNode.class);
        when(namespace.isTextual()).thenReturn(true);
        when(namespace.asText()).thenReturn("foo");
        when(header.get(NAMESPACE_FIELD_NAME)).thenReturn(namespace);
        JsonNode name = mock(JsonNode.class);
        when(name.isTextual()).thenReturn(true);
        when(name.asText()).thenReturn("bar");
        when(header.get(NAME_FIELD_NAME)).thenReturn(name);
        when(root.get(HEADER_FIELD_NAME)).thenReturn(header);
        assertEquals(UnmarshallUtils.getNamespaceDiscriminator(root), Optional.of("foo.bar"));
    }

}
