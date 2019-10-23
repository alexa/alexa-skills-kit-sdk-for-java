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

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.request.UnmarshalledRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JacksonJsonUnmarshallerTest extends BaseUnmarshallerTest {

    @Before
    public void setUp() {
        super.setUp();
        jsonUnmarshaller = new JacksonJsonUnmarshaller<>(BaseFoo.class, Arrays.asList("header", "type"), subtypes);
    }

    @Test
    public void discriminator_path_not_exists_returns_empty() {
        assertEquals(jsonUnmarshaller.unmarshall(getPayload("invalid", "foo.bar")), Optional.empty());
    }

    @Test(expected = AskSdkException.class)
    public void non_textual_discriminator_returns_exception() {
        String payload = "{\"validField\":\"foo\", \"header\":{\"type\":123}}";
        jsonUnmarshaller.unmarshall(payload.getBytes());
    }

    @Test
    public void discriminator_not_in_subtypes_returns_empty() {
        assertEquals(jsonUnmarshaller.unmarshall(getPayload("type", "foo.invalid")), Optional.empty());
    }

    @Test
    public void discriminator_exists_correct_type_unmarshalled() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("type", "foo.member"));
        assertTrue(unmarshalledRequest.isPresent());
        assertEquals(unmarshalledRequest.get().getUnmarshalledRequest().getClass(), BaseFoo.class);
    }

    @Test
    public void unmarshalled_instance_correct() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("type", "foo.member"));
        BaseFoo fooType = unmarshalledRequest.get().getUnmarshalledRequest();
        assertEquals(fooType.getFoo().getValidField(), "foo");
        assertEquals(fooType.getHeader().get("type"), "foo.member");
    }

    @Test
    public void unmarshalled_json_correct() {
        Optional<UnmarshalledRequest<BaseFoo>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("type", "foo.member"));
        JsonNode json = unmarshalledRequest.get().getRequestJson();
        assertEquals(json.get("foo").get("validField").asText(), "foo");
        assertEquals(json.get("header").get("type").asText(), "foo.member");
    }

    private byte[] getPayload(String discriminatorProperty, String discriminatorValue) {
        String payload = String.format("{\"foo\":{\"validField\":\"foo\"}, \"header\":{\"%s\":\"%s\"}}", discriminatorProperty, discriminatorValue);
        return payload.getBytes();
    }

}
