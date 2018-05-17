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

import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;

import java.util.List;

/**
 * Represents the standard components needed to build a {@link com.amazon.ask.Skill}
 */
public class SkillConfiguration {
    protected final List<RequestMapper> requestMappers;
    protected final List<HandlerAdapter> handlerAdapters;
    protected final List<RequestInterceptor> requestInterceptors;
    protected final List<ResponseInterceptor> responseInterceptors;
    protected final ExceptionMapper exceptionMapper;
    protected final PersistenceAdapter persistenceAdapter;
    protected final ApiClient apiClient;
    protected final String customUserAgent;
    protected final String skillId;

    protected SkillConfiguration(List<RequestMapper> requestMappers, List<HandlerAdapter> handlerAdapters,
                                 ExceptionMapper exceptionMapper, PersistenceAdapter persistenceAdapter, ApiClient apiClient,
                                 String customUserAgent, String skillId) {
        this(requestMappers, handlerAdapters, null, null, exceptionMapper,
                persistenceAdapter, apiClient, customUserAgent, skillId);
    }

    protected SkillConfiguration(List<RequestMapper> requestMappers, List<HandlerAdapter> handlerAdapters,
                                 List<RequestInterceptor> requestInterceptors, List<ResponseInterceptor> responseInterceptors,
                                 ExceptionMapper exceptionMapper, PersistenceAdapter persistenceAdapter, ApiClient apiClient,
                                 String customUserAgent, String skillId) {
        this.requestMappers = requestMappers;
        this.handlerAdapters = handlerAdapters;
        this.requestInterceptors = requestInterceptors;
        this.responseInterceptors = responseInterceptors;
        this.exceptionMapper = exceptionMapper;
        this.persistenceAdapter = persistenceAdapter;
        this.apiClient = apiClient;
        this.customUserAgent = customUserAgent;
        this.skillId = skillId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<RequestMapper> getRequestMappers() {
        return requestMappers;
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return handlerAdapters;
    }

    public List<RequestInterceptor> getRequestInterceptors() { return requestInterceptors; }

    public List<ResponseInterceptor> getResponseInterceptors() { return responseInterceptors; }

    public ExceptionMapper getExceptionMapper() {
        return exceptionMapper;
    }

    public PersistenceAdapter getPersistenceAdapter() {
        return persistenceAdapter;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public String getCustomUserAgent() { return customUserAgent; }

    public String getSkillId() { return skillId; }

    public static final class Builder {
        private List<RequestMapper> requestMappers;
        private List<HandlerAdapter> handlerAdapters;
        private List<RequestInterceptor> requestInterceptors;
        private List<ResponseInterceptor> responseInterceptors;
        private ExceptionMapper exceptionMapper;
        private PersistenceAdapter persistenceAdapter;
        private ApiClient apiClient;
        private String customUserAgent;
        private String skillId;

        private Builder() {
        }

        public Builder withRequestMappers(List<RequestMapper> requestMappers) {
            this.requestMappers = requestMappers;
            return this;
        }

        public Builder withHandlerAdapters(List<HandlerAdapter> handlerAdapters) {
            this.handlerAdapters = handlerAdapters;
            return this;
        }

        public Builder withRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return this;
        }

        public Builder withResponseInterceptors(List<ResponseInterceptor> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return this;
        }

        public Builder withExceptionMapper(ExceptionMapper exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return this;
        }

        public Builder withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        public Builder withApiClient(ApiClient apiClient) {
            this.apiClient = apiClient;
            return this;
        }

        public Builder withCustomUserAgent(String customUserAgent) {
            this.customUserAgent = customUserAgent;
            return this;
        }

        public Builder withSkillId(String skillId) {
            this.skillId = skillId;
            return this;
        }

        public SkillConfiguration build() {
            return new SkillConfiguration(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors,
                    exceptionMapper, persistenceAdapter, apiClient, customUserAgent, skillId);
        }
    }
}