/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.dispatcher;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper;
import com.amazon.ask.request.dispatcher.impl.BaseRequestDispatcher;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.exception.UnhandledSkillException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRequestDispatcherTest {

    private GenericRequestHandlerChain<TestHandlerInput, TestHandlerOutput> mockHandlerChain;
    private GenericHandlerAdapter<TestHandlerInput, TestHandlerOutput> mockAdapter;
    private GenericRequestMapper<TestHandlerInput, TestHandlerOutput> mockMapper;
    private GenericExceptionMapper<TestHandlerInput, TestHandlerOutput> mockExceptionMapper;
    private GenericRequestDispatcher<TestHandlerInput, TestHandlerOutput> dispatcher;
    private List<GenericRequestInterceptor<TestHandlerInput>> requestInterceptors;
    private List<GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput>> responseInterceptors;
    private TestHandlerInput mockInput;
    private TestHandlerOutput mockOutput;

    @Before
    public void setup() {
        mockAdapter = mock(GenericHandlerAdapter.class);
        when(mockAdapter.supports(any())).thenReturn(true);
        mockMapper = mock(GenericRequestMapper.class);
        GenericRequestHandler<TestHandlerInput, TestHandlerOutput> mockHandler = mock(GenericRequestHandler.class);
        mockHandlerChain = mock(GenericRequestHandlerChain.class);
        when(mockHandlerChain.getRequestHandler()).thenReturn(mockHandler);
        doReturn(Optional.of(mockHandlerChain)).when(mockMapper).getRequestHandlerChain(any(TestHandlerInput.class));
        mockOutput = mock(TestHandlerOutput.class);
        mockInput = mock(TestHandlerInput.class);
        mockExceptionMapper = mock(GenericExceptionMapper.class);
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenReturn(mockOutput);
        requestInterceptors = new ArrayList<>();
        responseInterceptors = new ArrayList<>();
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .withExceptionMapper(mockExceptionMapper)
                .addHandlerAdapter(mockAdapter)
                .withRequestInterceptors(requestInterceptors)
                .withResponseInterceptors(responseInterceptors)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_request_mapper_throws_exception() {
        BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .withHandlerAdapters(Collections.singletonList(mockAdapter))
                .withExceptionMapper(mockExceptionMapper)
                .build();
    }

    @Test
    public void no_exception_mapper_no_exception() {
        BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .withHandlerAdapters(mockAdapter)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void no_handler_adapters_throws_exception() {
        BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .withExceptionMapper(mockExceptionMapper)
                .build();
    }

    @Test
    public void no_handler_chain_returns_null() {
        when(mockMapper.getRequestHandlerChain(any(TestHandlerInput.class))).thenReturn(Optional.empty());
        assertNull(dispatcher.dispatch(mockInput));
    }

    @Test(expected = AskSdkException.class)
    public void adapter_does_not_support_handler_throws_exception() {
        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        when(mockExceptionMapper.getHandler(any(), throwableCaptor.capture())).thenReturn(Optional.empty());
        when(mockAdapter.supports(any())).thenReturn(false);
        dispatcher.dispatch(mockInput);
        assertTrue(throwableCaptor.getValue() instanceof AskSdkException);
    }

    @Test
    public void adapter_supports_handler_returns_response() {
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, mockOutput);
    }

    @Test
    public void adapter_supports_handler_returns_empty_output() {
        TestHandlerOutput testOutput = new TestHandlerOutput();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenReturn(testOutput);
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, testOutput);
    }

    @Test
    public void global_exception_handler_returns_response() {
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> exceptionHandler = mock(GenericExceptionHandler.class);
        when(exceptionHandler.canHandle(any(), any())).thenReturn(true);
        when(exceptionHandler.handle(any(), any())).thenReturn(mockOutput);
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                        .addExceptionHandler(exceptionHandler).build())
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(e);
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, mockOutput);
        verify(exceptionHandler).canHandle(any(), any());

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<TestHandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(TestHandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequest(), mockInput.getRequest());
    }

    @Test
    public void global_exception_handler_returns_empty_output() {
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> exceptionHandler = mock(GenericExceptionHandler.class);
        when(exceptionHandler.handle(any(), any())).thenReturn(mockOutput);

        GenericExceptionMapper<TestHandlerInput, TestHandlerOutput> exceptionMapper = mock(GenericExceptionMapper.class);
        doReturn(Optional.of(exceptionHandler)).when(exceptionMapper).getHandler(any(), any());
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(exceptionMapper)
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(e);
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, mockOutput);

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<TestHandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(TestHandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequest(), mockInput.getRequest());
    }

    @Test
    public void chain_level_exception_handler_returns_response() {
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> exceptionHandler = mock(GenericExceptionHandler.class);
        when(exceptionHandler.canHandle(any(), any())).thenReturn(true);
        when(exceptionHandler.handle(any(), any())).thenReturn(mockOutput);
        when(mockHandlerChain.getExceptionHandlers()).thenReturn(Collections.singletonList(exceptionHandler));
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class).addExceptionHandler(exceptionHandler).build())
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(e);
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, mockOutput);
        verify(exceptionHandler).canHandle(any(), any());

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<TestHandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(TestHandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        verify(mockExceptionMapper, never()).getHandler(any(), any());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequest(), mockInput.getRequest());
    }

    @Test
    public void chain_level_exception_handler_returns_empty_output() {
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> exceptionHandler = mock(GenericExceptionHandler.class);
        when(exceptionHandler.canHandle(any(), any())).thenReturn(true);
        when(exceptionHandler.handle(any(), any())).thenReturn(mockOutput);
        when(mockHandlerChain.getExceptionHandlers()).thenReturn(Collections.singletonList(exceptionHandler));
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class).addExceptionHandler(exceptionHandler).build())
                .build();
        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(e);
        TestHandlerOutput output = dispatcher.dispatch(mockInput);
        assertEquals(output, mockOutput);
        verify(exceptionHandler).canHandle(any(), any());

        ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<TestHandlerInput> handlerInputCaptor = ArgumentCaptor.forClass(TestHandlerInput.class);
        verify(exceptionHandler).handle(handlerInputCaptor.capture(), throwableCaptor.capture());

        verify(mockExceptionMapper, never()).getHandler(any(), any());

        assertEquals(throwableCaptor.getValue(), e);
        assertEquals(handlerInputCaptor.getValue().getRequest(), mockInput.getRequest());
    }

    @Test
    public void exception_handler_not_found_wrapped_original_exception_thrown() {
        BaseExceptionMapper exceptionMapper = mock(BaseExceptionMapper.class);
        when(exceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        dispatcher = BaseRequestDispatcher.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestMapper(mockMapper)
                .addHandlerAdapter(mockAdapter)
                .withExceptionMapper(exceptionMapper)
                .build();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(new IllegalStateException());
        try {
            dispatcher.dispatch(mockInput);
            fail("Expected exception was not thrown");
        } catch (UnhandledSkillException ex) {
            assertEquals(ex.getClass(), UnhandledSkillException.class);
            assertEquals(ex.getCause().getClass(), IllegalStateException.class);
        }
    }

    @Test
    public void handler_chain_level_request_interceptor_called_after_global_interceptor() {
        GenericRequestInterceptor<TestHandlerInput> globalInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainInterceptor));
        when(globalInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        dispatcher.dispatch(mockInput);
        InOrder inOrder = inOrder(globalInterceptor, chainInterceptor, mockAdapter);
        inOrder.verify(globalInterceptor).processRequest(mockInput);
        inOrder.verify(chainInterceptor).processRequest(mockInput);
        inOrder.verify(mockAdapter).execute(any(), any());
    }

    @Test
    public void handler_chain_level_response_interceptor_called_before_global_interceptor() {
        GenericResponseInterceptor globalInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor chainInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainInterceptor));
        when(globalInterceptor.processResponse(mockInput, mockOutput)).thenReturn(mockOutput);
        when(chainInterceptor.processResponse(mockInput, mockOutput)).thenReturn(mockOutput);
        dispatcher.dispatch(mockInput);
        InOrder inOrder = inOrder(globalInterceptor, chainInterceptor, mockAdapter);
        inOrder.verify(mockAdapter).execute(any(), any());
        inOrder.verify(chainInterceptor).processResponse(mockInput, mockOutput);
        inOrder.verify(globalInterceptor).processResponse(mockInput, mockOutput);
    }

    @Test
    public void request_handler_not_found() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(mockMapper.getRequestHandlerChain(any())).thenReturn(Optional.empty());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        assertNull(dispatcher.dispatch(mockInput));
    }

    @Test
    public void handler_adapter_not_found() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));
        when(mockAdapter.supports(any())).thenReturn(false);
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        when(globalRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor, never()).processRequest(mockInput);
            verify(chainResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
        }
    }

    @Test
    public void exception_in_global_request_interceptor() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        Exception e = new IllegalStateException();
        doThrow(e).when(globalRequestInterceptor).processRequest(any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor, never()).processRequest(mockInput);
            verify(chainResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(mockMapper, never()).getRequestHandlerChain(any());
            verify(mockAdapter, never()).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
            verify(mockHandlerChain, never()).getExceptionHandlers();
            verify(mockExceptionMapper).getHandler(any(), any());
        }
    }

    @Test
    public void exception_in_handler_chain_level_request_interceptor() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(globalRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);

        Exception e = new IllegalStateException();
        doThrow(e).when(chainRequestInterceptor).processRequest(mockInput);
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor).processRequest(mockInput);
            verify(chainResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter, never()).execute(any(), any());
            verify(mockHandlerChain).getExceptionHandlers();
            verify(mockExceptionMapper).getHandler(any(), any());
        }
    }

    @Test
    public void exception_in_request_handler() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(globalRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);

        Exception e = new IllegalStateException();
        when(mockAdapter.execute(any(TestHandlerInput.class), any())).thenThrow(e);
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor).processRequest(mockInput);
            verify(chainResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
            verify(mockHandlerChain).getExceptionHandlers();
            verify(mockExceptionMapper).getHandler(any(), any());
        }
    }

    @Test
    public void exception_in_handler_chain_level_response_interceptor() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(globalRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);

        Exception e = new IllegalStateException();
        doThrow(e).when(chainResponseInterceptor).processResponse(any(), any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor).processRequest(mockInput);
            verify(chainResponseInterceptor).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor, never()).processResponse(mockInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
            verify(mockHandlerChain).getExceptionHandlers();
            verify(mockExceptionMapper).getHandler(any(), any());
        }
    }

    @Test
    public void exception_in_global_response_interceptor() {
        GenericRequestInterceptor<TestHandlerInput> globalRequestInterceptor = mock(GenericRequestInterceptor.class);
        GenericRequestInterceptor<TestHandlerInput> chainRequestInterceptor = mock(GenericRequestInterceptor.class);
        requestInterceptors.add(globalRequestInterceptor);
        when(mockHandlerChain.getRequestInterceptors()).thenReturn(Collections.singletonList(chainRequestInterceptor));

        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> globalResponseInterceptor = mock(GenericResponseInterceptor.class);
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> chainResponseInterceptor = mock(GenericResponseInterceptor.class);
        responseInterceptors.add(globalResponseInterceptor);
        when(mockHandlerChain.getResponseInterceptors()).thenReturn(Collections.singletonList(chainResponseInterceptor));

        when(globalRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);
        when(chainRequestInterceptor.processRequest(mockInput)).thenReturn(mockInput);

        Exception e = new IllegalStateException();
        when(chainResponseInterceptor.processResponse(mockInput, mockOutput)).thenReturn(mockOutput);
        doThrow(e).when(globalResponseInterceptor).processResponse(any(), any());
        when(mockExceptionMapper.getHandler(any(), any())).thenReturn(Optional.empty());
        try {
            dispatcher.dispatch(mockInput);
            fail("Unhandled skill exception should have been thrown");
        } catch (AskSdkException ex) {
            verify(globalRequestInterceptor).processRequest(mockInput);
            verify(chainRequestInterceptor).processRequest(mockInput);
            verify(chainResponseInterceptor).processResponse(mockInput, mockOutput);
            verify(globalResponseInterceptor).processResponse(mockInput, mockOutput);
            verify(mockMapper).getRequestHandlerChain(any());
            verify(mockAdapter).supports(any());
            verify(mockAdapter).execute(any(), any());
            verify(mockHandlerChain, never()).getExceptionHandlers();
            verify(mockExceptionMapper).getHandler(any(), any());
        }
    }

}