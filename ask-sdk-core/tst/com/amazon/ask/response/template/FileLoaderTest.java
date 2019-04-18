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
import com.amazon.ask.response.template.loader.TemplateCache;
import com.amazon.ask.response.template.loader.impl.LocalTemplateFileLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileLoaderTest {

    private static final String TEST_TEMPLATE = "test_template";
    private static final String INVALID_TEMPLATE = "invalid_template";
    private static final String BASE_PATH = "com/amazon/ask/response/template/";
    private static final String EXTENSION = "txt";

    private TemplateCache mockCache;
    private LocalTemplateFileLoader fileLoader;
    private HandlerInput handlerInput;

    @Before
    public void setup() {
        mockCache = mock(TemplateCache.class);
        Request request = LaunchRequest.builder().withLocale("en-US").build();
        RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        handlerInput = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
    }

    @Test
    public void load_success_assert_template_content_data() {
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath(BASE_PATH)
                .withFileExtension(EXTENSION)
                .build();
        Optional<TemplateContentData> template = fileLoader.load(TEST_TEMPLATE, handlerInput);
        assertTrue(template != null && template.isPresent());
        TemplateContentData templateContentData = template.get();
        assertNotNull(templateContentData);
        assertTrue(templateContentData.getIdentifier().contains(TEST_TEMPLATE));
        assertEquals(new String(templateContentData.getTemplateContent()), "empty");
        assertEquals(templateContentData.getTemplateBaseDir(), BASE_PATH);
    }

    @Test
    public void load_success_verify_no_cache() {
        when(mockCache.get(anyString())).thenReturn(null);
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath(BASE_PATH)
                .withFileExtension(EXTENSION)
                .withTemplateCache(mockCache)
                .build();
        Optional<TemplateContentData> template = fileLoader.load(TEST_TEMPLATE, handlerInput);
        assertTrue(template != null && template.isPresent());
        assertNotNull(template.get());
        verify(mockCache).put(anyString(), any(TemplateContentData.class));
    }

    @Test
    public void load_success_verify_cache_hit() {
        TemplateContentData mockTemplateContentData = mock(TemplateContentData.class);
        when(mockCache.get(anyString())).thenReturn(mockTemplateContentData);
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath(BASE_PATH)
                .withFileExtension(EXTENSION)
                .withTemplateCache(mockCache)
                .build();
        fileLoader.load(TEST_TEMPLATE, handlerInput);
        fileLoader.load(TEST_TEMPLATE, handlerInput);
        verify(mockCache, never()).put(anyString(), any(TemplateContentData.class));
    }

    @Test
    public void load_invalid_template() {
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath(BASE_PATH)
                .withFileExtension(EXTENSION)
                .build();
        Optional<TemplateContentData> template = fileLoader.load(INVALID_TEMPLATE, handlerInput);
        assertEquals(template, Optional.empty());
    }

    @Test
    public void load_invalid_base_directory() {
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath("invalid_base_dir")
                .withFileExtension(EXTENSION)
                .build();
        Optional<TemplateContentData> template = fileLoader.load(TEST_TEMPLATE, handlerInput);
        assertEquals(template, Optional.empty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void load_invalid_locale() {
        Request request = LaunchRequest.builder().withLocale("en").build();
        RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        handlerInput = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
        fileLoader = LocalTemplateFileLoader.builder()
                .withDirectoryPath(BASE_PATH)
                .withFileExtension(EXTENSION)
                .build();
        fileLoader.load(TEST_TEMPLATE, handlerInput);
    }

}
