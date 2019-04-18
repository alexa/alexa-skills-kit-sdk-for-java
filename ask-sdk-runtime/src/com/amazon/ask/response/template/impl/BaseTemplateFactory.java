/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.impl;

import com.amazon.ask.exception.template.TemplateFactoryException;
import com.amazon.ask.response.template.TemplateContentData;
import com.amazon.ask.response.template.TemplateFactory;
import com.amazon.ask.response.template.loader.TemplateLoader;
import com.amazon.ask.response.template.renderer.TemplateRenderer;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@link TemplateFactory} implementation to chain {@link TemplateLoader} and {@link TemplateRenderer}.
 * It is responsible to pass in template name, data map and Input to get response output for skill request.
 */
public class BaseTemplateFactory<Input, Output> implements TemplateFactory<Input, Output> {

    private static final Logger LOGGER = getLogger(BaseTemplateFactory.class);

    protected final List<TemplateLoader<Input>> templateLoaders;
    protected final TemplateRenderer<Output> templateRenderer;

    protected BaseTemplateFactory(List<TemplateLoader<Input>> templateLoaders, TemplateRenderer<Output> templateRenderer) {
        this.templateLoaders = templateLoaders;
        this.templateRenderer = templateRenderer;
    }

    public static <Input, Output, Self extends Builder<Input, Output, Self>> Builder<Input, Output, Self> forTypes(
            Class<Input> input, Class<Output> output) {
        return new Builder<>();
    }

    public static <Input, Output> Builder<Input, Output, ?> builder() {
        return new Builder<>();
    }

    /**
     * Process template and data using provided {@link TemplateLoader} and {@link TemplateRenderer} to generate skill response output.
     *
     * @param responseTemplateName name of response template
     * @param dataMap map contains injecting data
     * @param input skill input
     * @return Output skill response output if loading and rendering successfully
     * @throws TemplateFactoryException if fail to load or render template
     */
    @Override
    public Output processTemplate(String responseTemplateName, Map<String, Object> dataMap, Input input) throws TemplateFactoryException {
        if (templateLoaders == null || templateLoaders.isEmpty() || templateRenderer == null) {
            String message = "Template Loader list is null or empty, or Template Renderer is null.";
            LOGGER.error(message);
            throw new TemplateFactoryException(message);
        }
        TemplateContentData templateContentData = loadTemplate(responseTemplateName, input);
        Output response = renderResponse(templateContentData, dataMap);
        return response;
    }

    private TemplateContentData loadTemplate(String responseTemplateName, Input input) throws TemplateFactoryException {
        Optional<TemplateContentData> templateContentData;
        for (TemplateLoader templateLoader : templateLoaders) {
            try {
                templateContentData = templateLoader.load(responseTemplateName, input);
                if (templateContentData != null && templateContentData.isPresent()) {
                    return templateContentData.get();
                }
            } catch (TemplateFactoryException e) {
                LOGGER.error(String.format("Fail to load template: %s using %s with error: %s.", responseTemplateName, templateLoader, e.getMessage()));
                throw e;
            }
        }
        String message = String.format("Unable to load template: %s using provided Loader(s).", responseTemplateName);
        LOGGER.error(message);
        throw new TemplateFactoryException(message);
    }

    private Output renderResponse(TemplateContentData templateContentData, Map<String, Object> dataMap) throws TemplateFactoryException {
        try {
            return templateRenderer.render(templateContentData, dataMap);
        } catch (TemplateFactoryException e) {
            LOGGER.error(String.format("Fail to render template: %s using %s with error: %s.", templateContentData, templateRenderer, e.getMessage()));
            throw e;
        }
    }

    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        protected List<TemplateLoader<Input>> templateLoaders;
        protected TemplateRenderer<Output> templateRenderer;

        protected Builder() {
            this.templateLoaders = new ArrayList<>();
        }

        public Self addTemplateLoader(TemplateLoader<Input> templateLoader) {
            this.templateLoaders.add(templateLoader);
            return (Self)this;
        }

        public Self addTemplateLoaders(List<TemplateLoader<Input>> templateLoaders) {
            this.templateLoaders.addAll(templateLoaders);
            return (Self)this;
        }

        public Self withTemplateRenderer(TemplateRenderer<Output> templateRenderer) {
            this.templateRenderer = templateRenderer;
            return (Self)this;
        }

        public BaseTemplateFactory<Input, Output> build() {
            return new BaseTemplateFactory<>(templateLoaders, templateRenderer);
        }
    }

}
