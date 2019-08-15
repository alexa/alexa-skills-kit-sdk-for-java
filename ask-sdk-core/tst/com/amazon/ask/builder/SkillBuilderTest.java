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

import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.module.SdkModule;
import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.response.template.TemplateFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SkillBuilderTest {

    private CustomSkillBuilder builder;
    private RequestHandler mockRequestHandler;
    private ExceptionHandler mockExceptionHandler;
    private TemplateFactory mockTemplateFactory;

    @Before
    public void setup() {
        builder = new CustomSkillBuilder();
        mockRequestHandler = mock(RequestHandler.class);
        mockExceptionHandler = mock(ExceptionHandler.class);
        mockTemplateFactory = mock(TemplateFactory.class);
    }

    @Test
    public void request_mapper_configured_with_handler() {
        when(mockRequestHandler.canHandle(any())).thenReturn(true);
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        GenericRequestMapper<HandlerInput, Optional<Response>> mapper = configuration.getRequestMappers().get(0);
        assertTrue(mapper instanceof GenericRequestMapper);
        assertEquals(mockRequestHandler, mapper.getRequestHandlerChain(getInputForIntent("FooIntent")).get().getRequestHandler());
    }

    @Test
    public void request_interceptor_used() {
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        builder.addRequestHandler(mockRequestHandler);
        builder.addRequestInterceptor(requestInterceptor);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertEquals(configuration.getRequestInterceptors().get(0), requestInterceptor);
    }

    @Test
    public void response_interceptor_used() {
        ResponseInterceptor responseInterceptor = mock(ResponseInterceptor.class);
        builder.addRequestHandler(mockRequestHandler);
        builder.addResponseInterceptor(responseInterceptor);
        GenericSkillConfiguration configuration = builder.getConfigBuilder().build();
        assertEquals(configuration.getResponseInterceptors().get(0), responseInterceptor);
    }

    @Test
    public void error_handler_chain_configured() {
        when(mockExceptionHandler.canHandle(any(), any())).thenReturn(true);
        builder.addRequestHandler(mockRequestHandler);
        builder.addExceptionHandler(mockExceptionHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper = configuration.getExceptionMapper();
        assertEquals(mockExceptionHandler, exceptionMapper.getHandler(HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder()
                        .withRequest(IntentRequest.builder().build())
                        .build()).build(), new Exception()).get());
    }

    @Test
    public void default_handler_adapter_used() {
        builder.addRequestHandler(mockRequestHandler);
        SkillConfiguration configuration = builder.getConfigBuilder().build();
        assertEquals(1, configuration.getHandlerAdapters().size());
        assertTrue(configuration.getHandlerAdapters().get(0) instanceof BaseHandlerAdapter);
    }

    @Test
    public void no_custom_persistence_adapter_null() {
        builder.addRequestHandler(mockRequestHandler);
        builder.withPersistenceAdapter(null);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertNull(config.getPersistenceAdapter());
    }

    @Test
    public void custom_persistence_adapter_used() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        builder.addRequestHandler(mockRequestHandler);
        builder.withPersistenceAdapter(persistenceAdapter);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertEquals(config.getPersistenceAdapter(), persistenceAdapter);
    }

    @Test
    public void no_custom_api_client_null() {
        builder.addRequestHandler(mockRequestHandler);
        builder.withApiClient(null);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertNull(config.getApiClient());
    }

    @Test
    public void custom_api_client_used() {
        ApiClient apiClient = mock(ApiClient.class);
        builder.addRequestHandler(mockRequestHandler);
        builder.withApiClient(apiClient);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertEquals(config.getApiClient(), apiClient);
    }

    @Test
    public void sdk_module_executed() {
        SdkModule mockModule = mock(SdkModule.class);
        builder.addRequestHandler(mockRequestHandler);
        builder.registerSdkModule(mockModule);
        builder.build();
        verify(mockModule).setupModule(any(SdkModuleContext.class));
    }

    @Test
    public void template_factory_used() {
        builder.withTemplateFactory(mockTemplateFactory);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertEquals(config.getTemplateFactory(), mockTemplateFactory);
    }

    @Test
    public void template_factory_null() {
        builder.withTemplateFactory(null);
        SkillConfiguration config = builder.getConfigBuilder().build();
        assertNull(config.getTemplateFactory());
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
