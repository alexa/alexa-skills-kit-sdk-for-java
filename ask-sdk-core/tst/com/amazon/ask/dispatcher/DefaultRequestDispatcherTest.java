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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
    private List<RequestInterceptor> requestInterceptors;
    private List<ResponseInterceptor> responseInterceptors;

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
        requestInterceptors = new ArrayList<>();
        responseInterceptors = new ArrayList<>();
        dispatcher = DefaultRequestDispatcher.builder()
                .addRequestMapper(mockMapper)
                .withExceptionMapper(mockExceptionMapper)
                .addHandlerAdapter(mockAdapter)
                .withRequestInterceptors(requestInterceptors)
                .withResponseInterceptors(responseInterceptors)
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
            fail("Expected exception was not thrown");
        } catch (UnhandledSkillException ex) {
            assertEquals(ex.getClass(), UnhandledSkillException.class);
            assertEquals(ex.getCause().getClass(), IllegalStateException.class);
        }
    }

    @Test
    public void handler_chain_level_request_interceptor_called_after_global_interceptor() {
        RequestInterceptor globalInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainInterceptor));
        dispatcher.dispatch(handlerInput);
        InOrder inOrder = inOrder(globalInterceptor, chainInterceptor, mockAdapter);
        inOrder.verify(globalInterceptor).process(handlerInput);
        inOrder.verify(chainInterceptor).process(handlerInput);
        inOrder.verify(mockAdapter).execute(any(), any());
    }

    @Test
    public void handler_chain_level_response_interceptor_called_before_global_interceptor() {
        ResponseInterceptor globalInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainInterceptor));
        dispatcher.dispatch(handlerInput);
        InOrder inOrder = inOrder(globalInterceptor, chainInterceptor, mockAdapter);
        inOrder.verify(mockAdapter).execute(any(), any());
        inOrder.verify(chainInterceptor).process(handlerInput, mockOutput);
        inOrder.verify(globalInterceptor).process(handlerInput, mockOutput);
    }

    @Test
    public void request_handler_not_found() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(mockMapper.getRequestHandlerChain(any())).thenReturn(Optional.empty());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor, never()).process(handlerInput);
            verify(chainResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter, never()).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
        }
    }

    @Test
    public void handler_adapter_not_found() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(mockAdapter.supports(any())).thenReturn(false);
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor, never()).process(handlerInput);
            verify(chainResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
        }
    }

    @Test
    public void exception_in_global_request_interceptor() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        doThrow(e).when(globalRequestInterceptor).process(any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor, never()).process(handlerInput);
            verify(chainResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper, never()).getRequestHandlerChain(any());
            verify(mockAdapter, never()).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
        }
    }

    @Test
    public void exception_in_handler_chain_level_request_interceptor() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        doThrow(e).when(chainRequestInterceptor).process(any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor).process(handlerInput);
            verify(chainResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
        }
    }

    @Test
    public void exception_in_request_handler() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(HandlerInput.class), any(RequestHandler.class))).thenThrow(e);
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor).process(handlerInput);
            verify(chainResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
        }
    }

    @Test
    public void exception_in_handler_chain_level_response_interceptor() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        doThrow(e).when(chainResponseInterceptor).process(any(), any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor).process(handlerInput);
            verify(chainResponseInterceptor).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor, never()).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
        }
    }

    @Test
    public void exception_in_global_response_interceptor() {
        RequestInterceptor globalRequestInterceptor = mock(RequestInterceptor.class);
        RequestInterceptor chainRequestInterceptor = mock(RequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        ResponseInterceptor globalResponseInterceptor = mock(ResponseInterceptor.class);
        ResponseInterceptor chainResponseInterceptor = mock(ResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        doThrow(e).when(globalResponseInterceptor).process(any(), any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(handlerInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (UnhandledSkillException ex) {
            verify(globalRequestInterceptor).process(handlerInput);
            verify(chainRequestInterceptor).process(handlerInput);
            verify(chainResponseInterceptor).process(handlerInput, mockOutput);
            verify(globalResponseInterceptor).process(handlerInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
        }
    }

}
