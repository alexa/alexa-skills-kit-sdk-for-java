/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.loader;

import com.amazon.ask.exception.template.TemplateLoaderException;
import com.amazon.ask.response.template.TemplateContentData;

import java.util.Optional;

/**
 * Loader interface for template loading from data store.
 */
public interface TemplateLoader<Input> {

    /**
     * Given template name, load template content from data store.
     *
     * @param responseTemplateName template name
     * @param input skill input
     * @return {@link TemplateContentData} contains template content
     * @throws TemplateLoaderException if fail to load template
     */
    Optional<TemplateContentData> load(String responseTemplateName, Input input) throws TemplateLoaderException;

}
