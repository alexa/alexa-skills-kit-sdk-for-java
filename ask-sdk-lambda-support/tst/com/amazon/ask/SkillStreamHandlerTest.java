/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class SkillStreamHandlerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RequestEnvelope TEST_REQUEST_ENVELOPE =
            RequestEnvelope.builder()
                    .withRequest(LaunchRequest.builder()
                            .withRequestId("rId")
                            .withLocale("en-US")
                            .build())
                    .build();

    private Skill skill;

    @Before
    public void setup() {
        skill = mock(Skill.class);
    }

    @Test
    public void handleRequest_with_valid_request_response_not_null() throws IOException {
        OutputSpeech outputSpeech = PlainTextOutputSpeech.builder().withText("Foo").build();
        when(skill.invoke(TEST_REQUEST_ENVELOPE)).thenReturn(ResponseEnvelope.builder().withResponse(Response.builder().withOutputSpeech(outputSpeech).build()).build());

        String output = getHandlerOutput(TEST_REQUEST_ENVELOPE);
        assertFalse(output.isEmpty());
    }

    @Test(expected = AskSdkException.class)
    public void handleRequest_unable_to_deserialize_throws_exception() throws IOException {
        getHandlerOutput("foo");
    }

    @Test
    public void expected_request_passed_in() throws IOException {
        ArgumentCaptor<RequestEnvelope> captor = ArgumentCaptor.forClass(RequestEnvelope.class);
        when(skill.invoke(captor.capture())).thenReturn(ResponseEnvelope.builder().build());
        getHandlerOutput(TEST_REQUEST_ENVELOPE);
        assertEquals(captor.getValue(), TEST_REQUEST_ENVELOPE);
    }

    private final class TestSkillStreamHandler extends SkillStreamHandler {
        public TestSkillStreamHandler() {
            super(skill);
        }
    }

    private String getHandlerOutput(Object envelope) throws IOException {
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(envelope);
        InputStream is = new ByteArrayInputStream(json);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        SkillStreamHandler streamHandler = new TestSkillStreamHandler();

        streamHandler.handleRequest(is, os, null);
        String output = new String(os.toByteArray(), Charset.defaultCharset());
        return output;
    }

}
