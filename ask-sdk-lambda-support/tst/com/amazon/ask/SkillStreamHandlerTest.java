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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.request.SkillRequest;
import com.amazon.ask.response.SkillResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Matchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SkillStreamHandlerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private TestRequest testRequest;
    private TestResponse testResponse;
    private SkillResponse<TestResponse> skillResponse;
    private Context testContext;

    private AlexaSkill<TestRequest, TestResponse> skill;

    @Before
    public void setup() {
        skill = mock(AlexaSkill.class);
        testRequest = new TestRequest();
        testRequest.setRequest("request");
        testResponse = new TestResponse();
        testResponse.setResponse("response");
        skillResponse = mock(SkillResponse.class);
        testContext = mock(Context.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_skill_throws_exception() {
        new TestSkillStreamHandler((AlexaSkill)null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_skills_throws_exception() {
        new TestSkillStreamHandler(new AlexaSkill[]{});
    }

    @Test
    public void skill_called_with_expected_request() throws IOException {
        when(skillResponse.isPresent()).thenReturn(true);
        ArgumentCaptor<SkillRequest> captor = ArgumentCaptor.forClass(SkillRequest.class);
        when(skill.execute(captor.capture(), any())).thenReturn(skillResponse);
        getHandlerOutput(testRequest);
        SkillRequest skillRequest = captor.getValue();
        assertArrayEquals(skillRequest.getRawRequest(), OBJECT_MAPPER.writeValueAsBytes(testRequest));
    }

    @Test
    public void lambda_context_passed_to_skill() throws IOException {
        when(skillResponse.isPresent()).thenReturn(true);
        ArgumentCaptor<Context> captor = ArgumentCaptor.forClass(Context.class);
        when(skill.execute(any(), captor.capture())).thenReturn(skillResponse);
        getHandlerOutput(testRequest);
        Context requestContext = captor.getValue();
        assertEquals(requestContext, testContext);
    }

    @Test (expected = AskSdkException.class)
    public void no_skill_can_handle_incoming_request_throws_exception() throws IOException {
        when(skill.execute(any(), any())).thenReturn(null);
        getHandlerOutput(testRequest);
    }

    @Test
    public void skill_response_present_writes_to_stream() throws IOException {
        when(skillResponse.isPresent()).thenReturn(true);
        when(skill.execute(any(), any())).thenReturn(skillResponse);
        getHandlerOutput(testRequest);
        verify(skillResponse).writeTo(any());
    }

    @Test
    public void skill_response_not_present_does_not_write_to_stream() throws IOException {
        when(skillResponse.isPresent()).thenReturn(false);
        when(skill.execute(any(), any())).thenReturn(skillResponse);
        getHandlerOutput(testRequest);
        verify(skillResponse, never()).writeTo(any());
    }

    private final class TestSkillStreamHandler extends SkillStreamHandler {
        public TestSkillStreamHandler(AlexaSkill skill) {
            super(skill);
        }

        public TestSkillStreamHandler(AlexaSkill... skills) {
            super(skills);
        }
    }

    private String getHandlerOutput(Object envelope) throws IOException {
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(envelope);
        InputStream is = new ByteArrayInputStream(json);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        SkillStreamHandler streamHandler = new TestSkillStreamHandler(skill);

        streamHandler.handleRequest(is, os, testContext);
        String output = new String(os.toByteArray(), Charset.defaultCharset());
        return output;
    }

}
