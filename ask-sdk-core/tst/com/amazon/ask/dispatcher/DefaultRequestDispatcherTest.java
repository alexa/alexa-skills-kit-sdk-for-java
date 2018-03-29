/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher;

import com.amazon.ask.dispatcher.impl.DefaultRequestDispatcher;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.dispatcher.exception.impl.DefaultExceptionMapper;
import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.exception.UnhandledSkillException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultRequestDispatcherTest {

    private RequestHandler mockHandler;
    private RequestHandlerChain mockHandlerChain;
    private HandlerAdapter mockAdapter;
    private RequestMapper mockMapper;
    private ExceptionMapper mockExceptionMapper;
    private Response mockResponse;
    private Optional<Response> mockOutput;
    private DefaultRequestDispatcher dispatcher;
    private HandlerInput handlerInput;

    @Before
    public void setup() {
        mockAdapter = mock(HandlerAdapter.class);
        when(mockAdapter.supports(any())).thenReturn(true);
        mockMapper = mock(RequestMapper.class);
        mockHandler = mock(RequestHandler.class);
        mockHandlerChain = mock(RequestHandlerChain.class);
        when(mockHandlerChain.getRequestHandler()).thenReturn(mockHandler);
        when(mockMapper.getRequestHandlerChain(any(HandlerInput.class))).thenReturn(Optional.of(mockHandlerChain));
        mockResponse = Response.builder().build();
        mockOutput = Optional.of(mockResponse);
        mockExceptionMapper = mock(ExceptionMapper.class);
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenReturn(mockOutput);
        dispatcher = DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .withExceptionMapper(mockExceptionMapper)
                .addHandlerAdapter(mockAdapter)
                .build();
        handlerInput = mock(HandlerInput.class);
        when(handlerInput.getRequestEnvelope()).thenReturn(RequestEnvelope.builder()
                .withRequest(IntentRequest.builder().build())
                .build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_request_mapper_throws_exception() {
        DefaultRequestDispatcher.builder()
                .withHandlerAdapters(mockAdapter)
                .withExceptionMapper(mockExceptionMapper)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_exception_mapper_throws_exception() {
        DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .withHandlerAdapters(mockAdapter)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_handler_adapters_throws_exception() {
        DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .withExceptionMapper(mockExceptionMapper)
                .build();
    }

    @Test(expected = UnhandledSkillException.class)
    public void no_handler_chain_throws_exception() {
        when(mockMapper.getRequestHandlerChain(any(HandlerInput.class))).thenReturn(Optional.empty());
        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        when(mockExceptionMapper.getHandler(any(), throwableCaptor.capture())).thenReturn(Optional.empty());
        dispatcher.dispatch(handlerInput);
        assertTrue(throwableCaptor.getValue() instanceof AskSdkException);
    }

    @Test(expected = UnhandledSkillException.class)
    public void adapter_does_not_support_handler_throws_exception() {
        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        when(mockExceptionMapper.getHandler(any(), throwableCaptor.capture())).thenReturn(Optional.empty());
        when(mockAdapter.supports(any())).thenReturn(false);
        dispatcher.dispatch(handlerInput);
        assertTrue(throwableCaptor.getValue() instanceof AskSdkException);
    }

    @Test
    public void adapter_supports_handler_returns_response() {
        Response output = dispatcher.dispatch(handlerInput).get();
        assertEquals(output, mockResponse);
    }

    @Test
    public void adapter_supports_handler_returns_empty_output() {
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenReturn(Optional.empty());
        Optional<Response> output = dispatcher.dispatch(handlerInput);
        assertEquals(output, Optional.empty());
    }

    @Test
    public void exception_handler_returns_response() {
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        when(exceptionHandler.canHandle(any(), any())).thenReturn(true);
        when(exceptionHandler.handle(any(), any())).thenReturn(Optional.of(mockResponse));
        dispatcher = DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(DefaultExceptionMapper.builder().addExceptionHandler(exceptionHandler).build())
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenThrow(e);
        Response output = dispatcher.dispatch(handlerInput).get();
        assertEquals(output, mockResponse);
        verify(exceptionHandler).canHandle(any(), any());

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<HandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(HandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequestEnvelope(), handlerInput.getRequestEnvelope());
    }

    @Test
    public void exception_handler_returns_empty_output() {
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        when(exceptionHandler.handle(any(), any())).thenReturn(Optional.empty());

        ExceptionMapper exceptionMapper = mock(ExceptionMapper.class);
        when(exceptionMapper.getHandler(any(), any())).thenReturn(Optional.of(exceptionHandler));
        dispatcher = DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(exceptionMapper)
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenThrow(e);
        Optional<Response> output = dispatcher.dispatch(handlerInput);
        assertEquals(output, Optional.empty());

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<HandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(HandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequestEnvelope(), handlerInput.getRequestEnvelope());
    }

    @Test
    public void exception_handler_not_found_wrapped_original_exception_thrown() {
        ExceptionMapper exceptionMapper = mock(ExceptionMapper.class);
        when(exceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        dispatcher = DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(exceptionMapper)
                .build();
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenThrow(new IllegalStateException());
        try {
            dispatcher.dispatch(handlerInput);
            Assert.fail("Expected exception was not thrown");
        } catch (UnhandledSkillException ex) {
            assertEquals(ex.getClass(), UnhandledSkillException.class);
            assertEquals(ex.getCause().getClass(), IllegalStateException.class);
        }
    }

    @Test
    public void request_interceptor_called() {
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(requestInterceptor));
        dispatcher.dispatch(handlerInput);
        verify(requestInterceptor).process(handlerInput);
    }

    @Test
    public void response_interceptor_called() {
        ResponseInterceptor responseInterceptor = mock(ResponseInterceptor.class);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(responseInterceptor));
        dispatcher.dispatch(handlerInput);
        verify(responseInterceptor).process(handlerInput, mockOutput);
    }

}
