/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.exception;

import com.amazon.ask.dispatcher.exception.impl.DefaultExceptionMapper;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import org.junit.Test;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultExceptionMapperTest {

    private HandlerInput handlerInput = HandlerInput.builder()
            .withRequestEnvelope(RequestEnvelope.builder()
                    .withRequest(IntentRequest.builder().build())
                    .build())
            .build();

    @Test
    public void no_matching_exception_handler_returns_optional_empty() {
        ExceptionMapper chain = DefaultExceptionMapper.builder().build();
        assertEquals(Optional.empty(), chain.getHandler(handlerInput, new IllegalArgumentException()));
    }

    @Test
    public void chain_order_enforced() {
        ExceptionHandler handler1 = mock(ExceptionHandler.class);
        when(handler1.canHandle(any(), any())).thenReturn(true);
        when(handler1.handle(any(), any())).thenReturn(Optional.empty());
        ExceptionHandler handler2 = mock(ExceptionHandler.class);
        when(handler2.canHandle(any(), any())).thenReturn(true);
        when(handler2.handle(any(), any())).thenReturn(Optional.empty());

        ExceptionMapper chain = DefaultExceptionMapper.builder()
                .addExceptionHandler(handler1)
                .addExceptionHandler(handler2)
                .build();
        assertEquals(chain.getHandler(handlerInput, new IllegalArgumentException()).get(), handler1);
    }

}
