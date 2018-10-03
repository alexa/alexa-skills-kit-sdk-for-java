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

import com.amazon.ask.builder.GenericSkillConfiguration;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSkillConfiguration<Input, Output> implements GenericSkillConfiguration<Input, Output> {

    protected final List<GenericRequestMapper<Input, Output>> requestMappers;
    protected final List<GenericHandlerAdapter<Input, Output>> handlerAdapters;
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;
    protected final GenericExceptionMapper<Input, Output> exceptionMapper;

    protected AbstractSkillConfiguration(List<GenericRequestMapper<Input, Output>> requestMappers,
                                         List<GenericHandlerAdapter<Input, Output>> handlerAdapters,
                                         List<GenericRequestInterceptor<Input>> requestInterceptors,
                                         List<GenericResponseInterceptor<Input, Output>> responseInterceptors,
                                         GenericExceptionMapper<Input, Output> exceptionMapper) {
        this.requestMappers = requestMappers;
        this.handlerAdapters = handlerAdapters;
        this.requestInterceptors = requestInterceptors;
        this.responseInterceptors = responseInterceptors;
        this.exceptionMapper = exceptionMapper;
    }

    public List<GenericRequestMapper<Input, Output>> getRequestMappers() {
        return requestMappers;
    }

    public List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters() { return handlerAdapters; }

    public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
        return requestInterceptors;
    }

    public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
        return responseInterceptors;
    }

    public GenericExceptionMapper<Input, Output> getExceptionMapper() {
        return exceptionMapper;
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<Input, Output, Self extends Builder> {
        protected List<GenericRequestMapper<Input, Output>> requestMappers;
        protected List<GenericHandlerAdapter<Input, Output>> handlerAdapters;
        protected List<GenericRequestInterceptor<Input>> requestInterceptors;
        protected List<GenericResponseInterceptor<Input, Output>> responseInterceptors;
        protected GenericExceptionMapper<Input, Output> exceptionMapper;

        protected Builder() {
        }

        public Self addRequestMapper(GenericRequestMapper<Input, Output> requestMapper) {
            if (requestMappers == null) {
                requestMappers = new ArrayList<>();
            }
            requestMappers.add(requestMapper);
            return (Self) this;
        }

        public Self withRequestMappers(List<GenericRequestMapper<Input, Output>> requestMappers) {
            this.requestMappers = requestMappers;
            return (Self) this;
        }

        public List<GenericRequestMapper<Input, Output>> getRequestMappers() {
            return requestMappers;
        }

        public Self addHandlerAdapter(GenericHandlerAdapter<Input, Output> handlerAdapter) {
            if (handlerAdapters == null) {
                handlerAdapters = new ArrayList<>();
            }
            handlerAdapters.add(handlerAdapter);
            return (Self) this;
        }

        public Self withHandlerAdapters(List<GenericHandlerAdapter<Input, Output>> handlerAdapters) {
            this.handlerAdapters = handlerAdapters;
            return (Self) this;
        }

        public List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters() {
            return handlerAdapters;
        }

        public Self addRequestInterceptor(GenericRequestInterceptor<Input> requestInterceptor) {
            if (requestInterceptors == null) {
                requestInterceptors = new ArrayList<>();
            }
            requestInterceptors.add(requestInterceptor);
            return (Self) this;
        }

        public Self withRequestInterceptors(List<GenericRequestInterceptor<Input>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (Self) this;
        }

        public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
            return requestInterceptors;
        }

        public Self addResponseInterceptor(GenericResponseInterceptor<Input, Output> responseInterceptor) {
            if (responseInterceptors == null) {
                this.responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (Self) this;
        }

        public Self withResponseInterceptors(List<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self) this;
        }

        public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
            return responseInterceptors;
        }

        public Self withExceptionMapper(GenericExceptionMapper<Input, Output> exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return (Self) this;
        }

        public abstract GenericSkillConfiguration<Input, Output> build();
    }

}
