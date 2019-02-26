package com.amazon.ask.util.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.request.UnmarshalledRequest;
import com.amazon.ask.util.JsonUnmarshaller;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JacksonJsonUnmarshallerTest {

    private JsonUnmarshaller<FooType> jsonUnmarshaller;

    @Before
    public void setUp() {
        jsonUnmarshaller = new JacksonJsonUnmarshaller<>(FooType.class, "validField");
    }

    @Test
    public void required_field_missing_returns_optional_empty() {
        assertEquals(jsonUnmarshaller.unmarshall(getPayload("invalidField")), Optional.empty());
    }

    @Test
    public void required_field_present_returns_unmarshalled_type() {
        Optional<UnmarshalledRequest<FooType>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("validField"));
        assertTrue(unmarshalledRequest.isPresent());
    }

    @Test
    public void unmarshalled_instance_correct() {
        Optional<UnmarshalledRequest<FooType>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("validField"));
        FooType fooType = unmarshalledRequest.get().getUnmarshalledRequest();
        assertEquals(fooType.getValidField(), "foo");
    }

    @Test
    public void unmarshalled_json_correct() {
        Optional<UnmarshalledRequest<FooType>> unmarshalledRequest = jsonUnmarshaller.unmarshall(getPayload("validField"));
        JsonNode json = unmarshalledRequest.get().getRequestJson();
        assertEquals(json.get("validField").asText(), "foo");
    }

    @Test(expected = AskSdkException.class)
    public void unmarshaller_exception_wrapped_in_sdk_exception() {
        jsonUnmarshaller.unmarshall("{foo}".getBytes());
    }

    private byte[] getPayload(String fieldName) {
        String payload = String.format("{\"%s\":\"foo\"}", fieldName);
        return payload.getBytes();
    }

    private static class FooType {
        private String validField;

        public String getValidField() {
            return validField;
        }

        public void setValidField(String validField) {
            this.validField = validField;
        }
    }

}
