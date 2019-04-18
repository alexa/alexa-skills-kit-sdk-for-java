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

import com.amazon.ask.exception.template.TemplateFactoryException;
import com.amazon.ask.exception.template.TemplateLoaderException;
import com.amazon.ask.exception.template.TemplateRendererException;
import com.amazon.ask.response.template.impl.BaseTemplateFactory;
import com.amazon.ask.response.template.loader.TemplateLoader;
import com.amazon.ask.response.template.renderer.TemplateRenderer;
import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseTemplateFactoryTest {

    private static final String templateIdentifier = "identifier";
    private static final String responseTemplateName = "responseTemplateName";
    private static final Map<String, Object> dataMap = new HashMap<>();

    private TestHandlerInput mockInput;
    private TestHandlerOutput mockOutput;
    private TemplateLoader<TestHandlerInput> mockLoader;
    private TemplateRenderer<TestHandlerOutput> mockRenderer;
    private TemplateContentData mockTemplateContentData;

    @Before
    public void setup() {
        mockInput = mock(TestHandlerInput.class);
        mockOutput = mock(TestHandlerOutput.class);
        mockLoader = mock(TemplateLoader.class);
        mockRenderer = mock(TemplateRenderer.class);
        mockTemplateContentData = mock(TemplateContentData.class);
    }

    @Test
    public void process_template_success() {
        when(mockLoader.load(anyString(), any())).thenReturn(Optional.of(mockTemplateContentData));
        when(mockRenderer.render(any(TemplateContentData.class), anyMap())).thenReturn(mockOutput);
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList(mockLoader))
                .withTemplateRenderer(mockRenderer)
                .build();
        TestHandlerOutput output = (TestHandlerOutput) templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        assertNotNull(output);
        assertEquals(output, mockOutput);
        verify(mockLoader).load(responseTemplateName, mockInput);
        verify(mockRenderer).render(mockTemplateContentData, dataMap);
    }

    @Test (expected = TemplateFactoryException.class)
    public void process_template_with_null_loaders() {
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .withTemplateRenderer(mockRenderer)
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockRenderer, never()).render(any(), anyMap());
    }

    @Test (expected = TemplateFactoryException.class)
    public void process_template_with_empty_loaders() {
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList())
                .withTemplateRenderer(mockRenderer)
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockRenderer, never()).render(any(), anyMap());
    }

    @Test (expected = TemplateFactoryException.class)
    public void process_template_with_no_matching_loaders() {
        when(mockLoader.load(anyString(), any())).thenReturn(Optional.empty());
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList(mockLoader))
                .withTemplateRenderer(mockRenderer)
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockLoader).load(responseTemplateName, mockInput);
        verify(mockRenderer, never()).render(any(), anyMap());
    }

    @Test (expected = TemplateLoaderException.class)
    public void process_template_with_loader_exception() {
        when(mockLoader.load(anyString(), any())).thenThrow(mock(TemplateLoaderException.class));
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList(mockLoader))
                .withTemplateRenderer(mockRenderer)
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockLoader).load(responseTemplateName, mockInput);
        verify(mockRenderer, never()).render(any(), anyMap());
    }

    @Test (expected = TemplateFactoryException.class)
    public void process_template_with_null_renderers() {
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList(mockLoader))
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockLoader, never()).load(anyString(),any());
    }

    @Test (expected = TemplateRendererException.class)
    public void process_template_with_renderer_exception() {
        when(mockLoader.load(anyString(), any())).thenReturn(Optional.of(mockTemplateContentData));
        when(mockRenderer.render(any(TemplateContentData.class), anyMap())).thenThrow(mock(TemplateRendererException.class));
        TemplateFactory templateFactory = BaseTemplateFactory.forTypes(TestHandlerInput.class, TestHandlerOutput.class)
                .addTemplateLoaders(Arrays.asList(mockLoader))
                .withTemplateRenderer(mockRenderer)
                .build();
        templateFactory.processTemplate(responseTemplateName, dataMap, mockInput);
        verify(mockLoader).load(responseTemplateName, mockInput);
        verify(mockRenderer).render(mockTemplateContentData, dataMap);
    }

}
