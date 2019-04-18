/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.response.template.loader.impl.LocaleTemplateEnumerator;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocaleEnumeratorTest {

    private static final String LOCALE = "en-US";
    private static final String INVALID_LOCALE = "en";
    private static final String TEMPLATE_NAME = "template";
    private static final String TEMPLATE_CANDIDATE = TEMPLATE_NAME + File.separator + "en" + File.separator + "US";

    private Request request;
    private RequestEnvelope requestEnvelope;
    private HandlerInput mockHandlerInput;
    private LocaleTemplateEnumerator localeTemplateEnumerator;

    @Test
    public void empty_locale_enumeration() {
        request = LaunchRequest.builder().build();
        requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        mockHandlerInput = mock(HandlerInput.class);
        when(mockHandlerInput.getRequestEnvelope()).thenReturn(requestEnvelope);
        localeTemplateEnumerator = LocaleTemplateEnumerator.builder().withTemplateName(TEMPLATE_NAME).withHandlerInput(mockHandlerInput).build();
        assertTrue(localeTemplateEnumerator.hasNext());
        assertEquals(localeTemplateEnumerator.next(), TEMPLATE_NAME);
    }

    @Test
    public void valid_locale_enumeration() {
        request = LaunchRequest.builder().withLocale(LOCALE).build();
        requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        mockHandlerInput = mock(HandlerInput.class);
        when(mockHandlerInput.getRequestEnvelope()).thenReturn(requestEnvelope);
        localeTemplateEnumerator = LocaleTemplateEnumerator.builder().withTemplateName(TEMPLATE_NAME).withHandlerInput(mockHandlerInput).build();
        assertTrue(localeTemplateEnumerator.hasNext());
        assertEquals(localeTemplateEnumerator.next(), TEMPLATE_CANDIDATE);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalid_locale_enumeration() {
        request = LaunchRequest.builder().withLocale(INVALID_LOCALE).build();
        requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        mockHandlerInput = mock(HandlerInput.class);
        when(mockHandlerInput.getRequestEnvelope()).thenReturn(requestEnvelope);
        localeTemplateEnumerator = LocaleTemplateEnumerator.builder().withTemplateName(TEMPLATE_NAME).withHandlerInput(mockHandlerInput).build();
    }

}
