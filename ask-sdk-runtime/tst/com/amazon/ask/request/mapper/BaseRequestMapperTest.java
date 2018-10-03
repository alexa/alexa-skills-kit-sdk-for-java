/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.mapper;

import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;
import com.amazon.ask.request.handler.chain.TestRequestHandlerChain;
import com.amazon.ask.request.mapper.impl.BaseRequestMapper;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseRequestMapperTest {

    @Test(expected = IllegalArgumentException.class)
    public void no_handlers_throws_illegal_argument_exception() {
        BaseRequestMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class).build();
    }

    @Test
    public void no_handler_registered_for_request() {
        TestHandlerInput input = new TestHandlerInput();
        ArgumentCaptor<TestHandlerInput> captor = ArgumentCaptor.forClass(TestHandlerInput.class);
        GenericRequestHandler<TestHandlerInput, TestHandlerOutput> handler = mock(GenericRequestHandler.class);
        when(handler.canHandle(captor.capture())).thenReturn(false);
        GenericRequestMapper<TestHandlerInput, TestHandlerOutput> mapper = BaseRequestMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestHandlerChain(getHandlerChain(handler))
                .build();
        Optional<? extends GenericRequestHandlerChain<TestHandlerInput, TestHandlerOutput>> result = mapper.getRequestHandlerChain(input);
        assertEquals(result, Optional.empty());
        assertEquals(captor.getValue(), input);
    }

    @Test
    public void handler_registered_for_request() {
        TestHandlerInput input = new TestHandlerInput();
        ArgumentCaptor<TestHandlerInput> captor = ArgumentCaptor.forClass(TestHandlerInput.class);
        GenericRequestHandler<TestHandlerInput, TestHandlerOutput> handler = mock(GenericRequestHandler.class);
        when(handler.canHandle(captor.capture())).thenReturn(true);
        TestRequestHandlerChain handlerChain = getHandlerChain(handler);
        GenericRequestMapper<TestHandlerInput, TestHandlerOutput> mapper = BaseRequestMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestHandlerChain(handlerChain)
                .build();
        Optional<? extends GenericRequestHandlerChain<TestHandlerInput, TestHandlerOutput>> result = mapper.getRequestHandlerChain(input);
        assertEquals(result, Optional.of(handlerChain));
        assertEquals(captor.getValue(), input);
    }


    @Test
    public void add_request_handler_chain() {
        GenericRequestHandler<TestHandlerInput, TestHandlerOutput> handler = mock(GenericRequestHandler.class);
        when(handler.canHandle(any())).thenReturn(true);
        GenericRequestMapper<TestHandlerInput, TestHandlerOutput> mapper = BaseRequestMapper.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addRequestHandlerChain(getHandlerChain(handler))
                .build();

        TestHandlerInput handlerInput = new TestHandlerInput();
        Optional<? extends GenericRequestHandlerChain<TestHandlerInput, TestHandlerOutput>> result = mapper.getRequestHandlerChain(handlerInput);
        assertEquals(handler, result.get().getRequestHandler());
    }

    private TestRequestHandlerChain getHandlerChain(GenericRequestHandler<TestHandlerInput, TestHandlerOutput> handler) {
        return TestRequestHandlerChain.builder()
                .withRequestHandler(handler)
                .build();
    }

}