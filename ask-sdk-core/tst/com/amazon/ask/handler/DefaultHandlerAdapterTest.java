/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.handler;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultHandlerAdapterTest {

    HandlerAdapter handlerAdapter = new DefaultHandlerAdapter();

    @Test
    public void supports_returns_true_for_expected_type() {
        RequestHandler handler = mock(RequestHandler.class);
        assertTrue(handlerAdapter.supports(handler));
    }

    @Test
    public void supports_returns_false_for_incorrect_type() {
        ExceptionHandler handler = mock(ExceptionHandler.class);
        assertFalse(handlerAdapter.supports(handler));
    }

    @Test
    public void handle_passes_expected_input_to_handler() throws Exception {
        RequestHandler handler = mock(RequestHandler.class);
        HandlerInput input = mock(HandlerInput.class);
        handlerAdapter.execute(input, handler);
        verify(handler).handle(input);
    }

}
