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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Context object passed to {@link SdkModule} implementations during module setup.
 * Allows modules to configure themselves against the skill instance.
 */
public class SdkModuleContext {

    protected final SkillConfiguration.Builder skillConfigBuilder;

    public SdkModuleContext(SkillConfiguration.Builder skillConfigBuilder) {
        this.skillConfigBuilder = skillConfigBuilder;
    }

    public SdkModuleContext addRequestMappers(List<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMapper) {
        requestMapper.forEach(skillConfigBuilder::addRequestMapper);
        return this;
    }

    public SdkModuleContext addRequestMapper(GenericRequestMapper<HandlerInput, Optional<Response>> requestMapper) {
        skillConfigBuilder.addRequestMapper(requestMapper);
        return this;
    }

    public List<GenericRequestMapper<HandlerInput, Optional<Response>>> getRequestMappers() {
        return Collections.unmodifiableList(skillConfigBuilder.getRequestMappers());
    }

    public SdkModuleContext addHandlerAdapters(List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapter) {
        handlerAdapter.forEach(skillConfigBuilder::addHandlerAdapter);
        return this;
    }

    public SdkModuleContext addHandlerAdapter(GenericHandlerAdapter<HandlerInput, Optional<Response>> handlerAdapter) {
        skillConfigBuilder.addHandlerAdapter(handlerAdapter);
        return this;
    }

    public List<GenericHandlerAdapter<HandlerInput, Optional<Response>>> getHandlerAdapter() {
        return skillConfigBuilder.getHandlerAdapters() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getHandlerAdapters());
    }

    public SdkModuleContext addRequestInterceptor(GenericRequestInterceptor<HandlerInput> requestInterceptor) {
        skillConfigBuilder.addRequestInterceptor(requestInterceptor);
        return this;
    }

    public List<GenericRequestInterceptor<HandlerInput>> getRequestInterceptors() {
        return skillConfigBuilder.getRequestInterceptors() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getRequestInterceptors());
    }

    public SdkModuleContext addResponseInterceptor(GenericResponseInterceptor<HandlerInput, Optional<Response>> responseInterceptor) {
        skillConfigBuilder.addResponseInterceptor(responseInterceptor);
        return this;
    }

    public List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> getResponseInterceptors() {
        return skillConfigBuilder.getResponseInterceptors() == null
                ? null
                : Collections.unmodifiableList(skillConfigBuilder.getResponseInterceptors());
    }

    public SdkModuleContext setExceptionMapper(GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper) {
        skillConfigBuilder.withExceptionMapper(exceptionMapper);
        return this;
    }

    public SdkModuleContext setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
        if (skillConfigBuilder.getPersistenceAdapter() != null) {
            throw new AskSdkException("Conflicting persistence adapter configuration: " +
                    "module attempting to override previously set value.");
        }
        skillConfigBuilder.withPersistenceAdapter(persistenceAdapter);
        return this;
    }

    public SdkModuleContext setApiClient(ApiClient apiClient) {
        if (skillConfigBuilder.getApiClient() != null) {
            throw new AskSdkException("Conflicting API client configuration: " +
                    "module attempting to override previously set value.");
        }
        skillConfigBuilder.withApiClient(apiClient);
        return this;
    }

}