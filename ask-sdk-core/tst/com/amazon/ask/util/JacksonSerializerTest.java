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

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.exception.AskSdkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JacksonSerializerTest {
    private JacksonSerializer serializer;
    private ObjectMapper mockMapper;
    private ResponseEnvelope responseEnvelope;
    private RequestEnvelope requestEnvelope;

    @Before
    public void setup() {
        mockMapper = mock(ObjectMapper.class);
        serializer = new JacksonSerializer();
        serializer.setMapper(mockMapper);
        responseEnvelope = ResponseEnvelope.builder().build();
        requestEnvelope = RequestEnvelope.builder().build();
    }

    @Test
    public void serialize_to_string_correct_input_to_mapper() throws IOException {
        ArgumentCaptor<ResponseEnvelope> captor = ArgumentCaptor.forClass(ResponseEnvelope.class);
        when(mockMapper.writeValueAsString(responseEnvelope)).thenReturn("foo");
        serializer.serialize(responseEnvelope);
        verify(mockMapper).writeValueAsString(captor.capture());
        assertEquals(captor.getValue(), responseEnvelope);
    }

    @Test
    public void serialize_to_string_correct_output_from_serializer() throws IOException {
        when(mockMapper.writeValueAsString(responseEnvelope)).thenReturn("foo");
        assertEquals(serializer.serialize(responseEnvelope), "foo");
    }

    @Test
    public void serialize_to_stream_correct_input_to_mapper() throws IOException {
        OutputStream os = mock(OutputStream.class);
        ArgumentCaptor<ResponseEnvelope> captor = ArgumentCaptor.forClass(ResponseEnvelope.class);
        serializer.serialize(responseEnvelope, os);
        verify(mockMapper).writeValue(any(OutputStream.class), captor.capture());
        assertEquals(captor.getValue(), responseEnvelope);
    }

    @Test
    public void deserialize_from_string_correct_input_to_serializer() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(mockMapper.readValue("foo", RequestEnvelope.class)).thenReturn(requestEnvelope);
        serializer.deserialize("foo", RequestEnvelope.class);
        verify(mockMapper).readValue(captor.capture(), any(Class.class));
        assertEquals(captor.getValue(), "foo");
    }

    @Test
    public void deserialize_from_string_correct_output_from_mapper() throws IOException {
        when(mockMapper.readValue("foo", RequestEnvelope.class)).thenReturn(requestEnvelope);
        assertEquals(serializer.deserialize("foo", RequestEnvelope.class), requestEnvelope);
    }

    @Test
    public void deserialize_from_stream_correct_input_to_serializer() throws IOException {
        InputStream is = mock(InputStream.class);
        ArgumentCaptor<InputStream> captor = ArgumentCaptor.forClass(InputStream.class);
        when(mockMapper.readValue(is, RequestEnvelope.class)).thenReturn(requestEnvelope);
        serializer.deserialize(is, RequestEnvelope.class);
        verify(mockMapper).readValue(captor.capture(), any(Class.class));
        assertEquals(captor.getValue(), is);
    }

    @Test
    public void deserialize_from_stream_correct_output_from_mapper() throws IOException {
        InputStream is = mock(InputStream.class);
        when(mockMapper.readValue(is, RequestEnvelope.class)).thenReturn(requestEnvelope);
        assertEquals(serializer.deserialize(is, RequestEnvelope.class), requestEnvelope);
    }

    @Test(expected = AskSdkException.class)
    public void serialize_from_string_ioexception_throws_sdk_exception() throws Exception {
        JsonProcessingException exception = mock(JsonProcessingException.class);
        when(mockMapper.writeValueAsString(responseEnvelope)).thenThrow(exception);
        serializer.serialize(responseEnvelope);
    }

    @Test(expected = AskSdkException.class)
    public void serialize_from_stream_ioexception_throws_sdk_exception() throws Exception {
        OutputStream os = mock(OutputStream.class);
        JsonProcessingException exception = mock(JsonProcessingException.class);
        doThrow(exception).when(mockMapper).writeValue(os, responseEnvelope);
        serializer.serialize(responseEnvelope, os);
    }

    @Test(expected = AskSdkException.class)
    public void deserialize_from_string_exception_throws_sdk_exception() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("foo");
        when(mockMapper.readValue("foo", RequestEnvelope.class)).thenThrow(new JsonParseException(jp, ""));
        serializer.deserialize("foo", RequestEnvelope.class);
    }

    @Test(expected = AskSdkException.class)
    public void deserialize_from_stream_exception_throws_sdk_exception() throws Exception {
        InputStream is = mock(InputStream.class);
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser(is);
        when(mockMapper.readValue(is, RequestEnvelope.class)).thenThrow(new JsonParseException(jp, ""));
        serializer.deserialize(is, RequestEnvelope.class);
    }
}
