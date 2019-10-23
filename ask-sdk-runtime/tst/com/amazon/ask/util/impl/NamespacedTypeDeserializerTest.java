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

import com.amazon.ask.request.UnmarshalledRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class NamespacedTypeDeserializerTest extends BaseUnmarshallerTest {

    @Before
    public void setUp() {
        super.setUp();
        jsonUnmarshaller = new NamespaceEnabledJacksonJsonUnmarshaller<>(BaseFoo.class, subtypes);
    }

    @Test
    public void namespace_discriminator_not_present_returns_empty() {
        String payload = "{\"validField\":\"foo\"}";
        assertEquals(jsonUnmarshaller.unmarshall(payload.getBytes()), Optional.empty());
    }

    @Test
    public void discriminator_not_in_subtypes_returns_empty() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("invalid", "name"));
        assertEquals(unmarshalledRequest, Optional.empty());
    }

    @Test
    public void unmarshalled_instance_correct() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("foo", "sub"));
        BaseFoo baseFoo = unmarshalledRequest.get().getUnmarshalledRequest();
        assertEquals(baseFoo.getHeader().get("namespace"), "foo");
        assertEquals(baseFoo.getHeader().get("name"), "sub");
        SubFoo sub = (SubFoo)baseFoo;
        assertEquals(sub.getValidField(), "bar");
    }

    @Test
    public void unmarshalled_json_correct() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("foo", "sub"));
        JsonNode json = unmarshalledRequest.get().getRequestJson();
        assertEquals(json.get("validField").asText(), "bar");
        assertEquals(json.get("header").get("namespace").asText(), "foo");
        assertEquals(json.get("header").get("name").asText(), "sub");
    }

    private byte[] getPayload(String namespace, String name) {
        String payload = String.format("{\"validField\":\"bar\", \"header\":{\"namespace\":\"%s\", \"name\":\"%s\"}}", namespace, name);
        return payload.getBytes();
    }

}
