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
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.services.ApiClient;

import java.util.Collections;
import java.util.List;

/**
 * Context object passed to {@link SdkModule} implementations during module setup.
 * Allows modules to configure themselves against the skill instance.
 */
public class SdkModuleContext {

    protected final SkillConfiguration.Builder skillConfigBuilder;

    public SdkModuleContext(SkillConfiguration.Builder skillConfigBuilder) {
        this.skillConfigBuilder = skillConfigBuilder;
    }

    public SdkModuleContext addRequestMappers(List<RequestMapper> requestMapper) {
        requestMapper.forEach(skillConfigBuilder::addRequestMapper);
        return this;
    }

    public SdkModuleContext addRequestMapper(RequestMapper requestMapper) {
        skillConfigBuilder.addRequestMapper(requestMapper);
        return this;
    }

    public List<RequestMapper> getRequestMappers() {
        return Collections.unmodifiableList(skillConfigBuilder.getRequestMappers());
    }

    public SdkModuleContext addHandlerAdapters(List<HandlerAdapter> handlerAdapter) {
        handlerAdapter.forEach(skillConfigBuilder::addHandlerAdapter);
        return this;
    }

    public SdkModuleContext addHandlerAdapter(HandlerAdapter handlerAdapter) {
        skillConfigBuilder.addHandlerAdapter(handlerAdapter);
        return this;
    }

    public List<HandlerAdapter> getHandlerAdapter() {
        return skillConfigBuilder.getHandlerAdapters() == null
            ? null
            : Collections.unmodifiableList(skillConfigBuilder.getHandlerAdapters());
    }

    public SdkModuleContext addRequestInterceptor(RequestInterceptor requestInterceptor) {
        skillConfigBuilder.addRequestInterceptor(requestInterceptor);
        return this;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return skillConfigBuilder.getRequestInterceptors() == null
            ? null
            : Collections.unmodifiableList(skillConfigBuilder.getRequestInterceptors());
    }

    public SdkModuleContext addResponseInterceptor(ResponseInterceptor responseInterceptor) {
        skillConfigBuilder.addResponseInterceptor(responseInterceptor);
        return this;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return skillConfigBuilder.getResponseInterceptors() == null
            ? null
            : Collections.unmodifiableList(skillConfigBuilder.getResponseInterceptors());
    }

    public SdkModuleContext setExceptionMapper(ExceptionMapper exceptionMapper) {
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

    public SdkModuleContext appendCustomUserAgent(String customUserAgent) {
        skillConfigBuilder.appendCustomUserAgent(customUserAgent);
        return this;
    }

}
