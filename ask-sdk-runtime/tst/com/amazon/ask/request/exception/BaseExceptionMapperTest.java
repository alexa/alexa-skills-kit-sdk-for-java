/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.exception;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseExceptionMapperTest {
    private GenericExceptionMapper<TestHandlerInput, TestHandlerOutput> exceptionMapper;
    private GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> exceptionHandler;

    @Before
    public void setup() {
        exceptionHandler = mock(GenericExceptionHandler.class);
    }

    @Test
    public void no_matching_exception_handler_returns_optional_empty() {
        exceptionMapper = BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class).build();
        assertEquals(Optional.empty(), exceptionMapper.getHandler(new TestHandlerInput(), new IllegalArgumentException()));
    }

    @Test
    public void matching_exception_handler_returns_exception_handler() {
        when(exceptionHandler.canHandle(any(), any())).thenReturn(true);
        exceptionMapper = BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addExceptionHandler(exceptionHandler)
                .build();
        assertEquals(Optional.of(exceptionHandler), exceptionMapper.getHandler(any(), any()));
    }

    @Test
    public void chain_order_enforced() {
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> handler1 = mock(GenericExceptionHandler.class);
        when(handler1.canHandle(any(), any())).thenReturn(true);
        when(handler1.handle(any(), any())).thenReturn(new TestHandlerOutput());
        GenericExceptionHandler<TestHandlerInput, TestHandlerOutput> handler2 = mock(GenericExceptionHandler.class);
        when(handler2.canHandle(any(), any())).thenReturn(true);
        when(handler2.handle(any(), any())).thenReturn(new TestHandlerOutput());

        GenericExceptionMapper<TestHandlerInput, TestHandlerOutput> chain = BaseExceptionMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addExceptionHandler(handler1)
                .addExceptionHandler(handler2)
                .build();
        assertEquals(chain.getHandler(new TestHandlerInput(), new IllegalArgumentException()).get(), handler1);
    }

}