/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.module;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.builder.SkillConfiguration;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.response.template.TemplateFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Context object passed to {@link SdkModule} implementations during module setup.
 * Allows modules to configure themselves against the skill instance.
 */
public class SdkModuleContext {

    /**
     * Skill configuration builder.
     */
    protected final SkillConfiguration.Builder skillConfigBuilder;

    /**
     * Constructor for SdkModuleContext.
     * @param skillConfigBuilder Skill configuration builder.
     */
    public SdkModuleContext(final SkillConfiguration.Builder skillConfigBuilder) {
        this.skillConfigBuilder = skillConfigBuilder;
    }

    /**
     * Adds multiple request mappers to SkillConfiguration Builder.
     * @param requestMapper request mappers.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addRequestMappers(final List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMapper) {
        requestMapper.forEach(skillConfigBuilder::addRequestMapper);
        return this;
    }

    /**
     * Adds request mapper to SkillConfiguration Builder.
     * @param requestMapper request mapper.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addRequestMapper(final GenericRequestMapper<HandlerInput, Optional<Response>> requestMapper) {
        skillConfigBuilder.addRequestMapper(requestMapper);
        return this;
    }

    /**
     * Getter for Request Mappers.
     * @return list of {@link GenericRequestMapper}.
     */
    public List<GenericRequestMapper<HandlerInput, Optional<Response>>> getRequestMappers() {
        return Collections.unmodifiableList(skillConfigBuilder.getRequestMappers());
    }

    /**
     * Adds multiple handler adapters to SkillConfiguration Builder.
     * @param handlerAdapter handler adapters.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addHandlerAdapters(final List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapter) {
        handlerAdapter.forEach(skillConfigBuilder::addHandlerAdapter);
        return this;
    }

    /**
     * Adds handler adapter to SkillConfiguration Builder.
     * @param handlerAdapter handler adapter.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addHandlerAdapter(final GenericHandlerAdapter<HandlerInput, Optional<Response>> handlerAdapter) {
        skillConfigBuilder.addHandlerAdapter(handlerAdapter);
        return this;
    }

    /**
     * Getter for Handler Adapters.
     * @return list of {@link GenericHandlerAdapter}.
     */
    public List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> getHandlerAdapter() {
        return skillConfigBuilder.getHandlerAdapters() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getHandlerAdapters());
    }

    /**
     * Adds Request interceptor to SkillConfiguration Builder.
     * @param requestInterceptor request interceptor.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addRequestInterceptor(final GenericRequestInterceptor<HandlerInput> requestInterceptor) {
        skillConfigBuilder.addRequestInterceptor(requestInterceptor);
        return this;
    }

    /**
     * Getter for Request interceptors.
     * @return list of {@link GenericRequestInterceptor}.
     */
    public List<GenericRequestInterceptor<HandlerInput>> getRequestInterceptors() {
        return skillConfigBuilder.getRequestInterceptors() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getRequestInterceptors());
    }

    /**
     * Adds Response interceptor to SkillConfiguration Builder.
     * @param responseInterceptor response interceptor.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext addResponseInterceptor(final GenericResponseInterceptor<HandlerInput, Optional<Response>> responseInterceptor) {
        skillConfigBuilder.addResponseInterceptor(responseInterceptor);
        return this;
    }

    /**
     * Getter for Response interceptors.
     * @return list of {@link GenericResponseInterceptor}.
     */
    public List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> getResponseInterceptors() {
        return skillConfigBuilder.getResponseInterceptors() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getResponseInterceptors());
    }

    /**
     * Adds exception mapper to SkillConfiguration Builder.
     * @param exceptionMapper exception mapper.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext setExceptionMapper(final GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper) {
        skillConfigBuilder.withExceptionMapper(exceptionMapper);
        return this;
    }

    /**
     * Adds Persistence Adapter to SkillConfiguration Builder.
     * @param persistenceAdapter Store skill attributes to a persistence layer.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext setPersistenceAdapter(final PersistenceAdapter persistenceAdapter) {
        if (skillConfigBuilder.getPersistenceAdapter() != null) {
            throw new AskSdkException("Conflicting persistence adapter configuration: "
                    + "module attempting to override previously set value.");
        }
        skillConfigBuilder.withPersistenceAdapter(persistenceAdapter);
        return this;
    }

    /**
     * Adds Api client to SkillConfiguration Builder.
     * @param apiClient client to make API calls.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext setApiClient(final ApiClient apiClient) {
        if (skillConfigBuilder.getApiClient() != null) {
            throw new AskSdkException("Conflicting API client configuration: "
                    + "module attempting to override previously set value.");
        }
        skillConfigBuilder.withApiClient(apiClient);
        return this;
    }

    /**
     * Adds TemplateFactory to SkillConfiguration Builder.
     * @param templateFactory Interface to process template and data to generate skill response.
     * @return {@link SdkModuleContext}.
     */
    public SdkModuleContext setTemplateFactory(final TemplateFactory templateFactory) {
        if (skillConfigBuilder.getTemplateFactory() != null) {
            throw new AskSdkException("Conflicting Template factory configuration: "
                    + "module attempting to override previously set value.");
        }
        skillConfigBuilder.withTemplateFactory(templateFactory);
        return this;
    }

}
