/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder;

import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.dispatcher.request.mapper.impl.DefaultRequestMapper;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StandardSkillBuilderTest {

    private StandardSkillBuilder builder;
    private RequestHandler mockRequestHandler;
    private ExceptionHandler mockExceptionHandler;

    @Before
    public void setup() {
        builder = new StandardSkillBuilder();
        mockRequestHandler = mock(RequestHandler.class);
        mockExceptionHandler = mock(ExceptionHandler.class);
    }

    @Test
    public void request_mapper_configured_with_handler() {
        when(mockRequestHandler.canHandle(any())).thenReturn(true);
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        RequestMapper mapper = configuration.getRequestMappers().get(0);
        assertTrue(mapper instanceof DefaultRequestMapper);
        assertEquals(mockRequestHandler, mapper.getRequestHandlerChain(getInputForIntent("FooIntent")).get().getRequestHandler());
    }

    @Test
    public void request_mapper_configured_with_default_request_interceptor() {
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        List<RequestInterceptor> requestInterceptors = Collections.singletonList(requestInterceptor);
        when(mockRequestHandler.canHandle(any())).thenReturn(true);
        builder.addRequestInterceptors(requestInterceptors);
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        RequestMapper mapper = configuration.getRequestMappers().get(0);
        assertTrue(mapper instanceof DefaultRequestMapper);
        assertEquals(requestInterceptors, mapper.getRequestHandlerChain(getInputForIntent("FooIntent")).get().getRequestInterceptors());
    }

    @Test
    public void request_mapper_configured_with_default_response_interceptor() {
        ResponseInterceptor responseInterceptor = mock(ResponseInterceptor.class);
        List<ResponseInterceptor> responseInterceptors = Collections.singletonList(responseInterceptor);
        when(mockRequestHandler.canHandle(any())).thenReturn(true);
        builder.addResponseInterceptors(responseInterceptor);
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        RequestMapper mapper = configuration.getRequestMappers().get(0);
        assertTrue(mapper instanceof DefaultRequestMapper);
        assertEquals(responseInterceptors, mapper.getRequestHandlerChain(getInputForIntent("FooIntent")).get().getResponseInterceptors());
    }

    @Test
    public void error_handler_chain_configured() {
        when(mockExceptionHandler.canHandle(any(), any())).thenReturn(true);
        builder.addRequestHandler(mockRequestHandler);
        builder.addExceptionHandler(mockExceptionHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        ExceptionMapper exceptionMapper = configuration.getExceptionMapper();
        assertEquals(mockExceptionHandler, exceptionMapper.getHandler(HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().build()).build(), new Exception()).get());
    }

    @Test
    public void default_handler_adapter_used() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertEquals(1, configuration.getHandlerAdapters().size());
        assertTrue(configuration.getHandlerAdapters().get(0) instanceof DefaultHandlerAdapter);
    }

    @Test
    public void dynamo_persistence_adapter_not_used_if_table_name_not_provided() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertNull(configuration.getPersistenceAdapter());
    }

    @Test
    public void dynamo_persistence_adapter_used_if_table_name_provided() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.withTableName("fooTable").withDynamoDbClient(mock(AmazonDynamoDB.class)).getConfigBuilder().build();
        assertTrue(configuration.getPersistenceAdapter() instanceof DynamoDbPersistenceAdapter);
    }

    @Test
    public void apache_api_client_used() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertTrue(configuration.getApiClient() instanceof ApacheHttpApiClient);
    }

    private HandlerInput getInputForIntent(String intentName) {
        return HandlerInput.builder()
                .withRequestEnvelope(getRequestEnvelopeForIntent(intentName))
                .build();
    }

    private RequestEnvelope getRequestEnvelopeForIntent(String intentName) {
        return RequestEnvelope.builder()
                .withRequest(IntentRequest.builder()
                        .withIntent(Intent.builder()
                                .withName(intentName)
                                .build())
                        .build())
                .build();
    }

}
