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

import java.util.List;
import java.util.Optional;

/**
 * Represents the configured used to build a {@link com.amazon.ask.Skill} instance.
 */
public class SkillConfiguration extends AbstractSkillConfiguration<HandlerInput, Optional<Response>> implements CustomSkillConfiguration {

    protected final String customUserAgent;
    protected final String skillId;
    protected final PersistenceAdapter persistenceAdapter;
    protected final ApiClient apiClient;

    protected SkillConfiguration(List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                 List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                 GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                 PersistenceAdapter persistenceAdapter, ApiClient apiClient,
                                 String customUserAgent, String skillId) {
        this(requestMappers, handlerAdapters, null, null, exceptionMapper,
                persistenceAdapter, apiClient, customUserAgent, skillId);
    }

    protected SkillConfiguration(List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                 List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                 List<GenericRequestInterceptor<HandlerInput>> requestInterceptors,
                                 List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> responseInterceptors,
                                 GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                 PersistenceAdapter persistenceAdapter, ApiClient apiClient,
                                 String customUserAgent, String skillId) {
        super(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors, exceptionMapper);
        this.customUserAgent = customUserAgent;
        this.skillId = skillId;
        this.persistenceAdapter = persistenceAdapter;
        this.apiClient = apiClient;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCustomUserAgent() {
        return customUserAgent;
    }

    public String getSkillId() {
        return skillId;
    }

    public PersistenceAdapter getPersistenceAdapter() {
        return persistenceAdapter;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public static final class Builder extends AbstractSkillConfiguration.Builder<HandlerInput, Optional<Response>, Builder> {
        private PersistenceAdapter persistenceAdapter;
        private ApiClient apiClient;
        private String customUserAgent;
        private String skillId;

        public Builder withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        public PersistenceAdapter getPersistenceAdapter() {
            return persistenceAdapter;
        }

        public Builder withApiClient(ApiClient apiClient) {
            this.apiClient = apiClient;
            return this;
        }

        public ApiClient getApiClient() {
            return apiClient;
        }

        public Builder withCustomUserAgent(String customUserAgent) {
            this.customUserAgent = customUserAgent;
            return this;
        }

        public Builder appendCustomUserAgent(String userAgent) {
            customUserAgent = customUserAgent == null ? userAgent : customUserAgent + " " + userAgent;
            return this;
        }

        public String getCustomUserAgent() {
            return customUserAgent;
        }

        public Builder withSkillId(String skillId) {
            this.skillId = skillId;
            return this;
        }

        public String getSkillId() {
            return skillId;
        }

        public SkillConfiguration build() {
            return new SkillConfiguration(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors,
                    exceptionMapper, persistenceAdapter, apiClient, customUserAgent, skillId);
        }


    }
}