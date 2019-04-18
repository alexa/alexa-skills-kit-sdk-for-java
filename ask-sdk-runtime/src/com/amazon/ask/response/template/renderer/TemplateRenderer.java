/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.renderer;

import com.amazon.ask.exception.template.TemplateRendererException;
import com.amazon.ask.response.template.TemplateContentData;

import java.util.Map;

/**
 * Renderer interface for template rendering and response conversion.
 */
public interface TemplateRenderer<Output> {

    /**
     * Render template and data, and convert to skill response output.
     *
     * @param templateContentData {@link TemplateContentData} that contains template content
     * @param dataMap map that contains injecting data to template
     * @return response to skill
     * @throws TemplateRendererException if fail to render and convert template
     */
   Output render(TemplateContentData templateContentData, Map<String, Object> dataMap) throws TemplateRendererException;

}
