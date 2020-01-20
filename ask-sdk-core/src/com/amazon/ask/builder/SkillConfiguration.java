/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.builder.impl.AbstractSkillConfiguration;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.response.template.TemplateFactory;
import com.amazon.ask.util.CustomUserAgent;

import java.util.List;
import java.util.Optional;

/**
 * Represents the configured used to build a {@link com.amazon.ask.Skill} instance.
 */
public class SkillConfiguration extends AbstractSkillConfiguration<HandlerInput, Optional<Response>> implements CustomSkillConfiguration {

    /**
     * Custom UserAgent.
     */
    protected final String customUserAgent;

    /**
     * Unique ID associated with a Skill.
     */
    protected final String skillId;

    /**
     *  Persistence adapters allow an {@link com.amazon.ask.attributes.AttributesManager} to store skill attributes to a
     *  persistence layer.
     */
    protected final PersistenceAdapter persistenceAdapter;

    /**
     * Api client to make external API calls.
     */
    protected final ApiClient apiClient;

    /**
     * Template Factory interface to process template and data to generate skill response.
     */
    protected final TemplateFactory<HandlerInput, Response> templateFactory;

    /**
     * Constructor for SkillConfiguration.
     * @param requestMappers list of request mappers.
     * @param handlerAdapters list of handler adapters.
     * @param exceptionMapper exception mapper.
     * @param persistenceAdapter store skill attributes to a persistence layer.
     * @param apiClient api client to make external API calls.
     * @param customUserAgent custom user agent.
     * @param skillId unique ID associated with a Skill.
     * @param templateFactory interface to process template and data to generate skill response.
     */
    protected SkillConfiguration(final List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                 final List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                 final GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                 final PersistenceAdapter persistenceAdapter,
                                 final ApiClient apiClient,
                                 final String customUserAgent,
                                 final String skillId,
                                 final TemplateFactory<HandlerInput, Response> templateFactory) {
        this(requestMappers, handlerAdapters, null, null, exceptionMapper,
                persistenceAdapter, apiClient, customUserAgent, skillId, templateFactory);
    }

    /**
     * Constructor for SkillConfiguration.
     * @param requestMappers list of request mappers.
     * @param handlerAdapters list of handler adapters.
     * @param requestInterceptors list of request interceptors.
     * @param responseInterceptors list of response interceptors.
     * @param exceptionMapper exception mapper.
     * @param persistenceAdapter store skill attributes to a persistence layer.
     * @param apiClient api client to make external API calls.
     * @param customUserAgent custom user agent.
     * @param skillId unique ID associated with a Skill.
     * @param templateFactory interface to process template and data to generate skill response.
     */
    protected SkillConfiguration(final List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                 final List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                 final List<GenericRequestInterceptor<HandlerInput>> requestInterceptors,
                                 final List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> responseInterceptors,
                                 final GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                 final PersistenceAdapter persistenceAdapter,
                                 final ApiClient apiClient,
                                 final String customUserAgent,
                                 final String skillId,
                                 final TemplateFactory<HandlerInput, Response> templateFactory) {
        super(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors, exceptionMapper);
        this.customUserAgent = customUserAgent;
        this.skillId = skillId;
        this.persistenceAdapter = persistenceAdapter;
        this.apiClient = apiClient;
        this.templateFactory = templateFactory;
    }

    /**
     * Static method to build an instance of Builder class.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@inheritDoc}.
     */
    public String getCustomUserAgent() {
        return customUserAgent;
    }

    /**
     * {@inheritDoc}.
     */
    public String getSkillId() {
        return skillId;
    }

    /**
     * {@inheritDoc}.
     */
    public PersistenceAdapter getPersistenceAdapter() {
        return persistenceAdapter;
    }

    /**
     * {@inheritDoc}.
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * {@inheritDoc}.
     */
    public TemplateFactory<HandlerInput, Response> getTemplateFactory() {
        return templateFactory;
    }

    /**
     * Skill configuration Builder.
     */
    public static final class Builder extends AbstractSkillConfiguration.Builder<HandlerInput, Optional<Response>, Builder> {
        /**
         * Store skill attributes to a persistence layer.
         */
        private PersistenceAdapter persistenceAdapter;

        /**
         * Api client to make external API calls.
         */
        private ApiClient apiClient;

        /**
         * Custom UserAgent.
         */
        private String customUserAgent;

        /**
         * Unique ID associated with a Skill.
         */
        private String skillId;

        /**
         * Template Factory interface to process template and data to generate skill response.
         */
        private TemplateFactory<HandlerInput, Response> templateFactory;

        /**
         * Prevent instantiation.
         */
        public Builder() { }

        /**
         * Adds PersistenceAdapter to Skill configuration.
         * @param persistenceAdapter Store skill attributes to a persistence layer.
         * @return {@link Builder}.
         */
        public Builder withPersistenceAdapter(final PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        /**
         * Getter for Persistence Adapter.
         * @return {@link PersistenceAdapter}.
         */
        public PersistenceAdapter getPersistenceAdapter() {
            return persistenceAdapter;
        }

        /**
         * Adds ApiClient to a Skill.
         * @param apiClient Api client to make external API calls.
         * @return {@link Builder}.
         */
        public Builder withApiClient(final ApiClient apiClient) {
            this.apiClient = apiClient;
            return this;
        }

        /**
         * Getter for Api client.
         * @return {@link ApiClient}.
         */
        public ApiClient getApiClient() {
            return apiClient;
        }

        /**
         * Adds custom useragent to Skill configuration.
         * @param customUserAgent custom user agent.
         * @return {@link Builder}.
         */
        public Builder withCustomUserAgent(final String customUserAgent) {
            this.customUserAgent = customUserAgent;
            return this;
        }

        /**
         * Appends a custom user agent to the existing user agent.
         * @param userAgent user agent.
         * @return {@link Builder}.
         */
        public Builder appendCustomUserAgent(final String userAgent) {
            customUserAgent = customUserAgent == null ? userAgent : customUserAgent + " " + userAgent;
            return this;
        }

        /**
         * Getter for custom user agent.
         * @return custom user agent.
         */
        public String getCustomUserAgent() {
            return customUserAgent;
        }

        /**
         * Adds SkillId to Skill configuration.
         * @param skillId Unique ID associated with a Skill.
         * @return {@link Builder}.
         */
        public Builder withSkillId(final String skillId) {
            this.skillId = skillId;
            return this;
        }

        /**
         * Getter for Skill Id.
         * @return skill id.
         */
        public String getSkillId() {
            return skillId;
        }

        /**
         * Adds Template factory to Skill configuration.
         * @param templateFactory interface to process template and data to generate skill response.
         * @return {@link Builder}.
         */
        public Builder withTemplateFactory(final TemplateFactory<HandlerInput, Response> templateFactory) {
            withCustomUserAgent(CustomUserAgent.TEMPLATE_RESOLVER.getUserAgent());
            this.templateFactory = templateFactory;
            return this;
        }

        /**
         * Getter for Template factory.
         * @return {@link TemplateFactory}.
         */
        public TemplateFactory<HandlerInput, Response> getTemplateFactory() {
            return templateFactory;
        }

        /**
         * Builder method to construct skill configuration with the provided data.
         * @return {@link SkillConfiguration}.
         */
        public SkillConfiguration build() {
            return new SkillConfiguration(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors,
                    exceptionMapper, persistenceAdapter, apiClient, customUserAgent, skillId, templateFactory);
        }
    }

}
