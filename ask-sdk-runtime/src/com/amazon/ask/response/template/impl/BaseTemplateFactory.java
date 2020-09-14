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

import static org.slf4j.LoggerFactory.getLogger;

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

/**
 * {@link TemplateFactory} implementation to chain {@link TemplateLoader} and {@link TemplateRenderer}.
 * It is responsible to pass in template name, data map and Input to get response output for skill request.
 * @param <Input> Skill input type.
 * @param <Output> Skill output type.
 */
public class BaseTemplateFactory<Input, Output> implements TemplateFactory<Input, Output> {

    /**
     * Logger instance to log information for debugging purposes.
     */
    private static final Logger LOGGER = getLogger(BaseTemplateFactory.class);

    /**
     * List of loader interfaces for template loading from data store.
     */
    protected final List<TemplateLoader<Input>> templateLoaders;

    /**
     * Renderer interface for template rendering and response conversion.
     */
    protected final TemplateRenderer<Output> templateRenderer;

    /**
     * Constructor to build an instance of BaseTemplateFactory.
     * @param templateLoaders template loaders.
     * @param templateRenderer template renderer.
     */
    protected BaseTemplateFactory(final List<TemplateLoader<Input>> templateLoaders, final TemplateRenderer<Output> templateRenderer) {
        this.templateLoaders = templateLoaders;
        this.templateRenderer = templateRenderer;
    }

    /**
     * Return Builder instance.
     * @param input class of type input.
     * @param output class of type output.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @param <Self> of type Builder class or any parent class of Builder.
     * @return {@link Builder}.
     */
    public static <Input, Output, Self extends Builder<Input, Output, Self>> Builder<Input, Output, Self> forTypes(
            final Class<Input> input, final Class<Output> output) {
        return new Builder<>();
    }

    /**
     * Return Builder instance.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @return {@link Builder}.
     */
    public static <Input, Output> Builder<Input, Output, ?> builder() {
        return new Builder<>();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Output processTemplate(final String responseTemplateName, final Map<String, Object> dataMap, final Input input)
            throws TemplateFactoryException {
        if (templateLoaders == null || templateLoaders.isEmpty() || templateRenderer == null) {
            String message = "Template Loader list is null or empty, or Template Renderer is null.";
            LOGGER.error(message);
            throw new TemplateFactoryException(message);
        }
        TemplateContentData templateContentData = loadTemplate(responseTemplateName, input);
        Output response = renderResponse(templateContentData, dataMap);
        return response;
    }

    /**
     * Loads the template with the given name.
     * @param responseTemplateName template name.
     * @param input Skill input type.
     * @return {@link TemplateContentData}.
     * @throws TemplateFactoryException is thrown when template loading fails.
     */
    private TemplateContentData loadTemplate(final String responseTemplateName, final Input input) throws TemplateFactoryException {
        Optional<TemplateContentData> templateContentData;
        for (TemplateLoader templateLoader : templateLoaders) {
            try {
                templateContentData = templateLoader.load(responseTemplateName, input);
                if (templateContentData != null && templateContentData.isPresent()) {
                    return templateContentData.get();
                }
            } catch (TemplateFactoryException e) {
                LOGGER.error(String.format("Fail to load template: %s using %s with error: %s.", responseTemplateName,
                        templateLoader, e.getMessage()));
                throw e;
            }
        }
        String message = String.format("Unable to load template: %s using provided Loader(s).", responseTemplateName);
        LOGGER.error(message);
        throw new TemplateFactoryException(message);
    }

    /**
     * Render the response obtained by merging data map with template.
     * @param templateContentData data retrieved from the specified template.
     * @param dataMap data to be added to the template.
     * @return Output Skill output type.
     * @throws TemplateFactoryException is thrown when template rendering fails.
     */
    private Output renderResponse(final TemplateContentData templateContentData, final Map<String, Object> dataMap)
            throws TemplateFactoryException {
        try {
            return templateRenderer.render(templateContentData, dataMap);
        } catch (TemplateFactoryException e) {
            LOGGER.error(String.format("Fail to render template: %s using %s with error: %s.", templateContentData,
                    templateRenderer, e.getMessage()));
            throw e;
        }
    }

    /**
     * Base Template Factory Builder.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @param <Self> of type Builder class.
     */
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        /**
         * List of loader interfaces for template loading from data store.
         */
        protected List<TemplateLoader<Input>> templateLoaders;

        /**
         * Renderer interface for template rendering and response conversion.
         */
        protected TemplateRenderer<Output> templateRenderer;

        /**
         * Constructor for Builder class.
         */
        protected Builder() {
            this.templateLoaders = new ArrayList<>();
        }

        /**
         * Add a Template Loader to BaseTemplateFactory instance.
         * @param templateLoader template loader.
         * @return {@link Builder}.
         */
        public Self addTemplateLoader(final TemplateLoader<Input> templateLoader) {
            this.templateLoaders.add(templateLoader);
            return (Self) this;
        }

        /**
         * Add multiple Template Loaders to BaseTemplateFactory instance.
         * @param templateLoaders list of TemplateLoader instances.
         * @return {@link Builder}.
         */
        public Self addTemplateLoaders(final List<TemplateLoader<Input>> templateLoaders) {
            this.templateLoaders.addAll(templateLoaders);
            return (Self) this;
        }

        /**
         * Add Template Renderer to BaseTemplateFactory instance.
         * @param templateRenderer Template renderer.
         * @return {@link Builder}.
         */
        public Self withTemplateRenderer(final TemplateRenderer<Output> templateRenderer) {
            this.templateRenderer = templateRenderer;
            return (Self) this;
        }

        /**
         * Builder method to build an instance of BaseTemplateFactory.
         * @return {@link BaseTemplateFactory}.
         */
        public BaseTemplateFactory<Input, Output> build() {
            return new BaseTemplateFactory<>(templateLoaders, templateRenderer);
        }
    }

}
