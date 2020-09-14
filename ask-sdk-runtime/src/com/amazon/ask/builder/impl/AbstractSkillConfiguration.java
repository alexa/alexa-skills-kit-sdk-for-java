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

/**
 * Abstraction over Skill Configuration.
 * Provides {@link GenericRequestMapper}, {@link GenericHandlerAdapter}, {@link GenericRequestInterceptor},
 * {@link GenericResponseInterceptor} and {@link GenericExceptionMapper}.
 * @param <Input> Skill input type.
 * @param <Output> Skill output type.
 */
public abstract class AbstractSkillConfiguration<Input, Output> implements GenericSkillConfiguration<Input, Output> {

    /**
     * List of request mappers.
     */
    protected final List<GenericRequestMapper<Input, Output>> requestMappers;

    /**
     * List of handler adapters.
     */
    protected final List<GenericHandlerAdapter<Input, Output>> handlerAdapters;

    /**
     * List of request interceptors.
     */
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;

    /**
     * List of response interceptors.
     */
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    /**
     * Exception mapper.
     */
    protected final GenericExceptionMapper<Input, Output> exceptionMapper;

    /**
     * Constructor for AbstractSkillConfiguration.
     * @param requestMappers list of request mappers.
     * @param handlerAdapters list of handler adapters.
     * @param requestInterceptors list of request interceptors.
     * @param responseInterceptors list of response interceptors.
     * @param exceptionMapper exception mapper.
     */
    protected AbstractSkillConfiguration(final List<GenericRequestMapper<Input, Output>> requestMappers,
                                         final List<GenericHandlerAdapter<Input, Output>> handlerAdapters,
                                         final List<GenericRequestInterceptor<Input>> requestInterceptors,
                                         final List<GenericResponseInterceptor<Input, Output>> responseInterceptors,
                                         final GenericExceptionMapper<Input, Output> exceptionMapper) {
        this.requestMappers = requestMappers;
        this.handlerAdapters = handlerAdapters;
        this.requestInterceptors = requestInterceptors;
        this.responseInterceptors = responseInterceptors;
        this.exceptionMapper = exceptionMapper;
    }

    /**
     * {@inheritDoc}.
     */
    public List<GenericRequestMapper<Input, Output>> getRequestMappers() {
        return requestMappers;
    }

    /**
     * {@inheritDoc}.
     */
    public List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters() {
        return handlerAdapters;
    }

    /**
     * {@inheritDoc}.
     */
    public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
        return requestInterceptors;
    }

    /**
     * {@inheritDoc}.
     */
    public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
        return responseInterceptors;
    }

    /**
     * {@inheritDoc}.
     */
    public GenericExceptionMapper<Input, Output> getExceptionMapper() {
        return exceptionMapper;
    }

    /**
     * Abstract Skill Configuration Builder.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @param <Self> of type Builder or any parent class of Builder.
     */
    @SuppressWarnings("unchecked")
    public abstract static class Builder<Input, Output, Self extends Builder> {

        /**
         * List of request mappers.
         */
        protected List<GenericRequestMapper<Input, Output>> requestMappers;

        /**
         * List of handler adapters.
         */
        protected List<GenericHandlerAdapter<Input, Output>> handlerAdapters;

        /**
         * List of request interceptors.
         */
        protected List<GenericRequestInterceptor<Input>> requestInterceptors;

        /**
         * List of response interceptors.
         */
        protected List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

        /**
         * Exception mapper.
         */
        protected GenericExceptionMapper<Input, Output> exceptionMapper;

        /**
         * Constructor to build an instance of Builder.
         */
        protected Builder() { }

        /**
         * Add a request mapper to Skill config.
         * @param requestMapper request mapper.
         * @return {@link Builder}.
         */
        public Self addRequestMapper(final GenericRequestMapper<Input, Output> requestMapper) {
            if (requestMappers == null) {
                requestMappers = new ArrayList<>();
            }
            requestMappers.add(requestMapper);
            return (Self) this;
        }

        /**
         * Add multiple request mappers to Skill config.
         * @param requestMappers list of request mappers.
         * @return {@link Builder}.
         */
        public Self withRequestMappers(final List<GenericRequestMapper<Input, Output>> requestMappers) {
            this.requestMappers = requestMappers;
            return (Self) this;
        }

        /**
         * Getter for request mappers.
         * @return List of {@link GenericRequestMapper}.
         */
        public List<GenericRequestMapper<Input, Output>> getRequestMappers() {
            return requestMappers;
        }

        /**
         * Add a handler adapter to Skill config.
         * @param handlerAdapter handler adapter.
         * @return {@link Builder}.
         */
        public Self addHandlerAdapter(final GenericHandlerAdapter<Input, Output> handlerAdapter) {
            if (handlerAdapters == null) {
                handlerAdapters = new ArrayList<>();
            }
            handlerAdapters.add(handlerAdapter);
            return (Self) this;
        }

        /**
         * Add multiple handler adapters to Skill config.
         * @param handlerAdapters list of handler adapters.
         * @return {@link Builder}.
         */
        public Self withHandlerAdapters(final List<GenericHandlerAdapter<Input, Output>> handlerAdapters) {
            this.handlerAdapters = handlerAdapters;
            return (Self) this;
        }

        /**
         * Getter for handler adapters.
         * @return List of {@link GenericHandlerAdapter}.
         */
        public List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters() {
            return handlerAdapters;
        }

        /**
         * Add a request interceptor to Skill config.
         * @param requestInterceptor request interceptor.
         * @return {@link Builder}.
         */
        public Self addRequestInterceptor(final GenericRequestInterceptor<Input> requestInterceptor) {
            if (requestInterceptors == null) {
                requestInterceptors = new ArrayList<>();
            }
            requestInterceptors.add(requestInterceptor);
            return (Self) this;
        }

        /**
         * Add multiple request interceptors to Skill config.
         * @param requestInterceptors list of request interceptors.
         * @return {@link Builder}.
         */
        public Self withRequestInterceptors(final List<GenericRequestInterceptor<Input>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (Self) this;
        }

        /**
         * Getter for request interceptors.
         * @return List of {@link GenericRequestInterceptor}.
         */
        public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
            return requestInterceptors;
        }

        /**
         * Add a response interceptor to Skill config.
         * @param responseInterceptor response interceptor.
         * @return {@link Builder}.
         */
        public Self addResponseInterceptor(final GenericResponseInterceptor<Input, Output> responseInterceptor) {
            if (responseInterceptors == null) {
                this.responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (Self) this;
        }

        /**
         * Add multiple response interceptors to Skill config.
         * @param responseInterceptors list of response interceptors.
         * @return {@link Builder}.
         */
        public Self withResponseInterceptors(final List<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self) this;
        }

        /**
         * Getter for response interceptors.
         * @return List of {@link GenericResponseInterceptor}.
         */
        public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
            return responseInterceptors;
        }

        /**
         * Add an exception mapper to Skill config.
         * @param exceptionMapper exception mapper.
         * @return {@link Builder}.
         */
        public Self withExceptionMapper(final GenericExceptionMapper<Input, Output> exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return (Self) this;
        }

        /**
         * Abstract method to return an instance of GenericSkillConfiguration.
         * @return {@link GenericSkillConfiguration}.
         */
        public abstract GenericSkillConfiguration<Input, Output> build();
    }

}
