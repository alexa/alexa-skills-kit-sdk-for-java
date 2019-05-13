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
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.request.mapper.impl.BaseRequestMapper;
import com.amazon.ask.response.template.TemplateFactory;
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        GenericRequestMapper<HandlerInput, Optional<Response>> mapper = configuration.getRequestMappers().get(0);
        assertTrue(mapper instanceof BaseRequestMapper);
        assertEquals(mockRequestHandler, mapper.getRequestHandlerChain(getInputForIntent("FooIntent")).get().getRequestHandler());
    }

    @Test
    public void error_handler_chain_configured() {
        when(mockExceptionHandler.canHandle(any(), any())).thenReturn(true);
        builder.addRequestHandler(mockRequestHandler);
        builder.addExceptionHandler(mockExceptionHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper = configuration.getExceptionMapper();
        IntentRequest request = IntentRequest.builder().build();
        assertEquals(mockExceptionHandler, exceptionMapper.getHandler(HandlerInput.builder()
                .withRequest(request)
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(request).build()).build(), new Exception()).get());
    }

    @Test
    public void default_handler_adapter_used() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertEquals(1, configuration.getHandlerAdapters().size());
        assertTrue(configuration.getHandlerAdapters().get(0) instanceof BaseHandlerAdapter);
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

    @Test
    public void template_factory_not_used_if_template_directory_path_not_provided() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertNull(configuration.getTemplateFactory());
    }

    @Test
    public void template_factory_is_used_if_template_directory_path_is_provided() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.withTemplateDirectoryPath("path").getConfigBuilder().build();
        assertNotNull(configuration.getTemplateFactory());
        assertTrue(configuration.getTemplateFactory() instanceof TemplateFactory);
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
