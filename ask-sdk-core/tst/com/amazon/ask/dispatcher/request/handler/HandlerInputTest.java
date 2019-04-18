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

import com.amazon.ask.exception.template.TemplateFactoryException;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.template.impl.BaseTemplateFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HandlerInputTest {

    private static final String templateName = "fooTemplateName";
    private static final Map<String, Object> dataMap = new HashMap<>();

    private RequestEnvelope requestEnvelope;
    private BaseTemplateFactory mockTemplateFactory;

    @Before
    public void setup() {
        Request request = IntentRequest.builder().withIntent(Intent.builder().withName("fooIntent").build()).build();
        requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        mockTemplateFactory = mock(BaseTemplateFactory.class);
    }

    @Test
    public void matches_calls_given_predicate() {
        Predicate<HandlerInput> mockPredicate = mock(Predicate.class);
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .build();
        input.matches(mockPredicate);
        verify(mockPredicate).test(input);
    }

    @Test
    public void generate_template_response_success() {
        Response response  = Response.builder().build();
        when(mockTemplateFactory.processTemplate(anyString(), anyMap(), any(HandlerInput.class))).thenReturn((response));
        HandlerInput handlerInput = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .withTemplateFactory(mockTemplateFactory)
                .build();
        Optional<Response> outputResponse = handlerInput.generateTemplateResponse(templateName, dataMap);
        assertTrue(outputResponse.isPresent());
        assertEquals(outputResponse.get(), response);
        verify(mockTemplateFactory).processTemplate(templateName, dataMap, handlerInput);
    }

    @Test (expected = TemplateFactoryException.class)
    public void generate_template_response_with_factory_exception() {
        when(mockTemplateFactory.processTemplate(anyString(), anyMap(), any(HandlerInput.class))).thenThrow(mock(TemplateFactoryException.class));
        HandlerInput handlerInput = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .withTemplateFactory(mockTemplateFactory)
                .build();
        handlerInput.generateTemplateResponse(templateName, dataMap);
    }

}
