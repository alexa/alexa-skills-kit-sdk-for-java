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

import com.amazon.ask.builder.SkillConfiguration;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.Application;
import com.amazon.ask.model.Context;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.interfaces.system.SystemState;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillTest {

    private Skill skill;
    private RequestMapper mockRequestMapper;
    private HandlerAdapter mockAdapter;
    private ExceptionMapper mockExceptionMapper;

    @Before
    public void setup() {
        mockRequestMapper = mock(RequestMapper.class);
        mockAdapter = mock(HandlerAdapter.class);
        mockExceptionMapper = mock(ExceptionMapper.class);
        RequestHandlerChain chain = mock(RequestHandlerChain.class);
        doReturn(Optional.of(chain)).when(mockRequestMapper).getRequestHandlerChain(any());

        SkillConfiguration skillConfiguration = SkillConfiguration.builder()
                .withRequestMappers(Collections.singletonList(mockRequestMapper))
                .withHandlerAdapters(Collections.singletonList(mockAdapter))
                .withExceptionMapper(mockExceptionMapper)
                .build();
        skill = new Skill(skillConfiguration);
    }

    @Test
    public void non_empty_handler_response_wrapped_in_response_envelope() {
        Response response = Response.builder().build();
        when(mockAdapter.supports(any())).thenReturn(true);
        when(mockAdapter.execute(any(), any())).thenReturn(Optional.of(response));
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build();
        ResponseEnvelope responseEnvelope = skill.invoke(envelope);
        assertEquals(responseEnvelope.getResponse(), response);
    }

    @Test
    public void empty_handler_response_returns_envelope_with_empty_response() {
        when(mockAdapter.supports(any())).thenReturn(true);
        when(mockAdapter.execute(any(), any())).thenReturn(Optional.empty());
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build();
        ResponseEnvelope responseEnvelope = skill.invoke(envelope);
        assertNull(responseEnvelope.getResponse());
    }

    @Test
    public void session_attributes_propagated_to_response_envelope() {
        when(mockAdapter.supports(any())).thenReturn(true);
        when(mockAdapter.execute(any(), any())).thenReturn(Optional.of(Response.builder().build()));
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("Foo", "Bar");
        Session session = Session.builder()
                .withAttributes(attributes)
                .build();
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).withSession(session).build();
        ResponseEnvelope responseEnvelope = skill.invoke(envelope);
        assertEquals(responseEnvelope.getSessionAttributes(), attributes);
    }

    @Test(expected = AskSdkException.class)
    public void given_skillId_verification_fails() {
        Optional<Response> response = Optional.of(Response.builder().build());
        when(mockAdapter.supports(any())).thenReturn(true);
        when(mockAdapter.execute(any(), any())).thenReturn(response);
        Application application = Application.builder().withApplicationId("barId").build();
        SystemState systemState = SystemState.builder().withApplication(application).build();
        Context context = Context.builder().withSystem(systemState).build();
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).withContext(context).build();

        SkillConfiguration skillConfiguration = SkillConfiguration.builder()
                .withRequestMappers(Collections.singletonList(mockRequestMapper))
                .withHandlerAdapters(Collections.singletonList(mockAdapter))
                .withExceptionMapper(mockExceptionMapper)
                .withSkillId("fooId")
                .build();
        skill= new Skill(skillConfiguration);
        skill.invoke(envelope);
    }

    @Test
    public void given_skillId_verification_passes() {
        Optional<Response> response = Optional.of(Response.builder().build());
        when(mockAdapter.supports(any())).thenReturn(true);
        when(mockAdapter.execute(any(), any())).thenReturn(response);
        Application application = Application.builder().withApplicationId("fooId").build();
        SystemState systemState = SystemState.builder().withApplication(application).build();
        Context context = Context.builder().withSystem(systemState).build();
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).withContext(context).build();

        SkillConfiguration skillConfiguration = SkillConfiguration.builder()
                .withRequestMappers(Collections.singletonList(mockRequestMapper))
                .withHandlerAdapters(Collections.singletonList(mockAdapter))
                .withExceptionMapper(mockExceptionMapper)
                .withSkillId("fooId")
                .build();
        skill= new Skill(skillConfiguration);
        skill.invoke(envelope);
    }

}