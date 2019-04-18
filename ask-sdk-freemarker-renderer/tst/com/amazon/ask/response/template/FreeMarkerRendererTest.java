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

import com.amazon.ask.exception.template.TemplateRendererException;
import com.amazon.ask.response.template.renderer.impl.FreeMarkerTemplateRenderer;
import com.amazon.ask.util.impl.JacksonJsonUnmarshaller;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FreeMarkerRendererTest {

    private static final String TEMPLATE =
            "{\n" +
            "    \"type\": \"PlainText\",\n" +
            "    \"text\": \"${outputSpeechText}\"\n" +
            "}";

    private static final String EMPTY_TEMPLATE_JSON = "{ }";

    private TemplateContentData mockTemplateContentData;
    private JacksonJsonUnmarshaller unmarshaller;
    private FreeMarkerTemplateRenderer freeMarkerTemplateRenderer;

    @Before
    public void setup() {
        mockTemplateContentData = mock(TemplateContentData.class);
        when(mockTemplateContentData.getTemplateBaseDir()).thenReturn("base_dir");
        unmarshaller = JacksonJsonUnmarshaller.withTypeBinding(TestResponse.class);
        freeMarkerTemplateRenderer = FreeMarkerTemplateRenderer.builder().withUnmarshaller(unmarshaller).build();
    }

    @Test
    public void render_success() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("outputSpeechText", "Hello");
        byte[] templateBytes = TEMPLATE.getBytes();
        when(mockTemplateContentData.getTemplateContent()).thenReturn(templateBytes);
        TestResponse response = (TestResponse) freeMarkerTemplateRenderer.render(mockTemplateContentData, dataMap);
        assertNotNull(response);
        assertEquals(response.getType(), "PlainText");
        assertEquals(response.getText(), "Hello");
    }

    @Test
    public void render_empty_template_json() {
        when(mockTemplateContentData.getTemplateContent()).thenReturn(EMPTY_TEMPLATE_JSON.getBytes());
        TestResponse response = (TestResponse) freeMarkerTemplateRenderer.render(mockTemplateContentData, new HashMap<>());
        assertNotNull(response);
        assertNull(response.getType());
    }

    @Test (expected = TemplateRendererException.class)
    public void render_process_exception() {
        byte[] templateBytes = TEMPLATE.getBytes();
        when(mockTemplateContentData.getTemplateContent()).thenReturn(templateBytes);
        freeMarkerTemplateRenderer.render(mockTemplateContentData, new HashMap<>());
    }

    @Test (expected = TemplateRendererException.class)
    public void render_convert_exception() {
        byte[] bytes = "invalid".getBytes();
        when(mockTemplateContentData.getTemplateContent()).thenReturn(bytes);
        freeMarkerTemplateRenderer.render(mockTemplateContentData, null);
    }

}
