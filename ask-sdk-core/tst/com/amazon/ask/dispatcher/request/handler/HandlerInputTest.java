/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler;

import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import org.junit.Test;

import java.util.function.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HandlerInputTest {

    @Test
    public void matches_calls_given_predicate() {
        Predicate<HandlerInput> mockPredicate = mock(Predicate.class);
        Request request = IntentRequest.builder().withIntent(Intent.builder().withName("fooIntent").build()).build();
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(request).build();
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(envelope)
                .build();
        input.matches(mockPredicate);
        verify(mockPredicate).test(input);
    }

}
