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

import com.amazon.ask.Skill;
import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.exception.impl.DefaultExceptionMapper;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultRequestHandlerChain;
import com.amazon.ask.dispatcher.request.mapper.impl.DefaultRequestMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SkillBuilder<T extends SkillBuilder<T>> {

    protected final List<RequestHandler> requestHandlers;
    protected final List<ExceptionHandler> exceptionHandlers;
    protected final List<RequestInterceptor> requestInterceptors;
    protected final List<ResponseInterceptor> responseInterceptors;

    protected String skillId;

    public SkillBuilder() {
        this.requestHandlers = new ArrayList<>();
        this.exceptionHandlers = new ArrayList<>();
        this.requestInterceptors = new ArrayList<>();
        this.responseInterceptors = new ArrayList<>();
    }

    public T addRequestHandler(RequestHandler handler) {
        addRequestHandlers(handler);
        return getThis();
    }

    public T addRequestHandlers(List<RequestHandler> handlers) {
        addRequestHandlers(handlers.toArray(new RequestHandler[handlers.size()]));
        return getThis();
    }

    public T addRequestHandlers(RequestHandler... handlers) {
        requestHandlers.addAll(Arrays.asList(handlers));
        return getThis();
    }

    public T addRequestInterceptor(RequestInterceptor interceptor) {
        addRequestInterceptors(interceptor);
        return getThis();
    }

    public T addRequestInterceptors(List<RequestInterceptor> interceptors) {
        addRequestInterceptors(interceptors.toArray(new RequestInterceptor[interceptors.size()]));
        return getThis();
    }

    public T addRequestInterceptors(RequestInterceptor... interceptors) {
        requestInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    public T addResponseInterceptor(ResponseInterceptor interceptor) {
        addResponseInterceptors(interceptor);
        return getThis();
    }

    public T addResponseInterceptors(List<ResponseInterceptor> interceptors) {
        addResponseInterceptors(interceptors.toArray(new ResponseInterceptor[interceptors.size()]));
        return getThis();
    }

    public T addResponseInterceptors(ResponseInterceptor... interceptors) {
        responseInterceptors.addAll(Arrays.asList(interceptors));
        return getThis();
    }

    public T addExceptionHandler(ExceptionHandler handler) {
        addExceptionHandlers(handler);
        return getThis();
    }

    public T addExceptionHandlers(List<ExceptionHandler> handlers) {
        addExceptionHandlers(handlers.toArray(new ExceptionHandler[handlers.size()]));
        return getThis();
    }

    public T addExceptionHandlers(ExceptionHandler... handler) {
        exceptionHandlers.addAll(Arrays.asList(handler));
        return getThis();
    }

    public T withSkillId(String skillId) {
        this.skillId = skillId;
        return getThis();
    }

    @SuppressWarnings("unchecked")
    private T getThis() {
        return (T) this;
    }

    protected SkillConfiguration.Builder getConfigBuilder() {
        List<DefaultRequestHandlerChain> requestHandlerChains = requestHandlers.stream()
                .map(handler -> DefaultRequestHandlerChain.builder()
                        .withRequestHandler(handler).build())
                .collect(Collectors.toList());

        RequestMapper mapper = DefaultRequestMapper.builder()
                .withRequestHandlerChains(requestHandlerChains)
                .build();

        ExceptionMapper exceptionMapper = DefaultExceptionMapper.builder()
                .withExceptionHandlers(exceptionHandlers)
                .build();

        return SkillConfiguration.builder()
                .withRequestMappers(Collections.singletonList(mapper))
                .withHandlerAdapters(Collections.singletonList(new DefaultHandlerAdapter()))
                .withExceptionMapper(exceptionMapper)
                .withRequestInterceptors(requestInterceptors)
                .withResponseInterceptors(responseInterceptors)
                .withSkillId(skillId);
    }

    public abstract Skill build();

}
