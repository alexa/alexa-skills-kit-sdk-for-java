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

public abstract class AbstractSkillBuilder<Input, Output, Self extends AbstractSkillBuilder<Input, Output, Self>> {

    protected final List<GenericRequestHandler<Input, Output>> requestHandlers;
    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    public AbstractSkillBuilder() {
        this.requestHandlers = new ArrayList<>();
        this.exceptionHandlers = new ArrayList<>();
        this.requestInterceptors = new ArrayList<>();
        this.responseInterceptors = new ArrayList<>();
    }

    public Self addRequestHandler(GenericRequestHandler<Input, Output> handler) {
        requestHandlers.add(handler);
        return getThis();
    }

    public Self addRequestHandlers(List<GenericRequestHandler<Input, Output>> handlers) {
        requestHandlers.addAll(handlers);
        return getThis();
    }

    public Self addRequestHandlers(GenericRequestHandler<Input, Output>... handlers) {
        requestHandlers.addAll(Arrays.asList(handlers));
        return getThis();
    }

    public Self addRequestInterceptor(GenericRequestInterceptor<Input> interceptor) {
        requestInterceptors.add(interceptor);
        return getThis();
    }

    public Self addRequestInterceptors(List<GenericRequestInterceptor<Input>> interceptors) {
        requestInterceptors.addAll(interceptors);
        return getThis();
    }

    public Self addRequestInterceptors(GenericRequestInterceptor<Input>... interceptors) {
        requestInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    public Self addResponseInterceptor(GenericResponseInterceptor<Input, Output> interceptor) {
        responseInterceptors.add(interceptor);
        return getThis();
    }

    public Self addResponseInterceptors(List<GenericResponseInterceptor<Input, Output>> interceptors) {
        responseInterceptors.addAll(interceptors);
        return getThis();
    }

    public Self addResponseInterceptors(GenericResponseInterceptor<Input, Output>... interceptors) {
        responseInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    public Self addExceptionHandler(GenericExceptionHandler<Input, Output> handler) {
        exceptionHandlers.add(handler);
        return getThis();
    }

    public Self addExceptionHandlers(List<GenericExceptionHandler<Input, Output>> handlers) {
        exceptionHandlers.addAll(handlers);
        return getThis();
    }

    public Self addExceptionHandlers(GenericExceptionHandler<Input, Output>... handler) {
        exceptionHandlers.addAll(Arrays.asList(handler));
        return getThis();
    }

    @SuppressWarnings("unchecked")
    private Self getThis() {
        return (Self) this;
    }

    protected <T extends AbstractSkillConfiguration.Builder<Input, Output, T>> void populateConfig(T config) {
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

    protected abstract AlexaSkill build();

}
