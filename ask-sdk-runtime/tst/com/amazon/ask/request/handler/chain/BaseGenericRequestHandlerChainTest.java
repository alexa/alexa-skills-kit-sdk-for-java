/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.handler.chain;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class BaseGenericRequestHandlerChainTest {
    private GenericRequestHandler<TestHandlerInput, TestHandlerOutput> mockHandler;
    private GenericRequestInterceptor<TestHandlerInput> requestInterceptor;
    private GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> responseInterceptor;
    private GenericRequestHandlerChain handlerChain;

    @Before
    public void setup() {
        mockHandler = mock(GenericRequestHandler.class);
        requestInterceptor = mock(GenericRequestInterceptor.class);
        responseInterceptor = mock(GenericResponseInterceptor.class);
    }

    @Test
    public void get_handler_return_handler() {
        handlerChain = TestRequestHandlerChain.builder()
                .withRequestHandler(mockHandler)
                .build();
        assertEquals(handlerChain.getRequestHandler(), mockHandler);
    }

    @Test
    public void get_request_interceptors() {
        handlerChain = TestRequestHandlerChain.builder()
                .addRequestInterceptor(requestInterceptor)
                .withRequestHandler(mockHandler)
                .build();
        assertEquals(handlerChain.getRequestInterceptors(), Collections.singletonList(requestInterceptor));
    }

    @Test
    public void get_response_interceptors() {
        handlerChain = TestRequestHandlerChain.builder()
                .addResponseInterceptor(responseInterceptor)
                .withRequestHandler(mockHandler)
                .build();
        assertEquals(handlerChain.getResponseInterceptors(), Collections.singletonList(responseInterceptor));
    }



}