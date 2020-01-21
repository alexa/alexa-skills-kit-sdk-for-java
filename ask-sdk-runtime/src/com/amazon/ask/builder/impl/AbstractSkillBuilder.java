/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder.impl;

import com.amazon.ask.AlexaSkill;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.impl.BaseRequestMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstraction over Skill Builders.
 * Provides {@link GenericExceptionHandler}, {@link GenericRequestHandler}, {@link GenericRequestInterceptor}
 * and {@link GenericResponseInterceptor}
 * @param <Input> Skill input type.
 * @param <Output> Skill output type.
 * @param <Self> of type AbstractSkillBuilder or any parent class of AbstractSkillBuilder.
 */
public abstract class AbstractSkillBuilder<Input, Output, Self extends AbstractSkillBuilder<Input, Output, Self>> {

    /**
     * List of request handlers.
     */
    protected final List<GenericRequestHandler<Input, Output>> requestHandlers;

    /**
     * List of exception handlers.
     */
    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

    /**
     * List of request interceptors.
     */
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;

    /**
     * List of response interceptors.
     */
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    /**
     * Constructor to build an instance of AbstractSkillBuilder.
     */
    public AbstractSkillBuilder() {
        this.requestHandlers = new ArrayList<>();
        this.exceptionHandlers = new ArrayList<>();
        this.requestInterceptors = new ArrayList<>();
        this.responseInterceptors = new ArrayList<>();
    }

    /**
     * Add a request handler to Skill config.
     * @param handler request handler.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestHandler(final GenericRequestHandler<Input, Output> handler) {
        requestHandlers.add(handler);
        return getThis();
    }

    /**
     * Add multiple request handlers to Skill config.
     * @param handlers list of request handlers.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestHandlers(final List<GenericRequestHandler<Input, Output>> handlers) {
        requestHandlers.addAll(handlers);
        return getThis();
    }

    /**
     * Add multiple request handlers to Skill config.
     * @param handlers request handlers.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestHandlers(final GenericRequestHandler<Input, Output>... handlers) {
        requestHandlers.addAll(Arrays.asList(handlers));
        return getThis();
    }

    /**
     * Add request interceptor to Skill config.
     * @param interceptor request interceptor.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestInterceptor(final GenericRequestInterceptor<Input> interceptor) {
        requestInterceptors.add(interceptor);
        return getThis();
    }

    /**
     * Add multiple request interceptors to Skill config.
     * @param interceptors list of request interceptors.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestInterceptors(final List<GenericRequestInterceptor<Input>> interceptors) {
        requestInterceptors.addAll(interceptors);
        return getThis();
    }

    /**
     * Add multiple request interceptors to Skill config.
     * @param interceptors request interceptors.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addRequestInterceptors(final GenericRequestInterceptor<Input>... interceptors) {
        requestInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    /**
     * Add a response interceptor to Skill config.
     * @param interceptor response interceptor.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addResponseInterceptor(final GenericResponseInterceptor<Input, Output> interceptor) {
        responseInterceptors.add(interceptor);
        return getThis();
    }

    /**
     * Add multiple response interceptors to Skill config.
     * @param interceptors list of response interceptors.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addResponseInterceptors(final List<GenericResponseInterceptor<Input, Output>> interceptors) {
        responseInterceptors.addAll(interceptors);
        return getThis();
    }

    /**
     * Add multiple response interceptors to Skill config.
     * @param interceptors response interceptors.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addResponseInterceptors(final GenericResponseInterceptor<Input, Output>... interceptors) {
        responseInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    /**
     * Add an exception handler to Skill config.
     * @param handler exception handler.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addExceptionHandler(final GenericExceptionHandler<Input, Output> handler) {
        exceptionHandlers.add(handler);
        return getThis();
    }

    /**
     * Add multiple exception handlers to Skill config.
     * @param handlers list of exception handlers.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addExceptionHandlers(final List<GenericExceptionHandler<Input, Output>> handlers) {
        exceptionHandlers.addAll(handlers);
        return getThis();
    }

    /**
     * Add multiple exception handlers to Skill config.
     * @param handler exception handlers.
     * @return {@link AbstractSkillBuilder}.
     */
    public Self addExceptionHandlers(final GenericExceptionHandler<Input, Output>... handler) {
        exceptionHandlers.addAll(Arrays.asList(handler));
        return getThis();
    }

    /**
     * Typecasts the current instance to Self.
     * @return {@link AbstractSkillBuilder}
     */
    @SuppressWarnings("unchecked")
    private Self getThis() {
        return (Self) this;
    }

    /**
     * Adds config data to Skill configuration.
     * @param config Skill config.
     * @param <T> config of type T.
     */
    protected <T extends AbstractSkillConfiguration.Builder<Input, Output, T>> void populateConfig(final T config) {
        if (!requestHandlers.isEmpty()) {
            List<BaseRequestHandlerChain<Input, Output>> requestHandlerChains = requestHandlers.stream()
                    .map(handler -> BaseRequestHandlerChain.<Input, Output>builder()
                            .withRequestHandler(handler).build())
                    .collect(Collectors.toList());
            config.withRequestMappers(Collections.singletonList(BaseRequestMapper.<Input, Output>builder()
                    .withRequestHandlerChains(requestHandlerChains)
                    .build()));
        }

        if (!exceptionHandlers.isEmpty()) {
            GenericExceptionMapper<Input, Output> exceptionMapper = BaseExceptionMapper.<Input, Output>builder()
                    .withExceptionHandlers(exceptionHandlers)
                    .build();
            config.withExceptionMapper(exceptionMapper);
        }

        config.withRequestInterceptors(requestInterceptors);
        config.withResponseInterceptors(responseInterceptors);
    }

    /**
     * Abstract method to return an instance of AlexaSkill.
     * @return {@link AlexaSkill}.
     */
    protected abstract AlexaSkill build();

}
