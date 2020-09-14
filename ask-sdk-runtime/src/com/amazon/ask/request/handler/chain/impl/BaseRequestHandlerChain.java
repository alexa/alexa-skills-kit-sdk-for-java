/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.handler.chain.impl;

import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link GenericRequestHandler}.
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public class BaseRequestHandlerChain<Input, Output> implements GenericRequestHandlerChain<Input, Output> {

    /**
     * Request handler responsible for processing an incoming request.
     */
    protected final GenericRequestHandler<Input, Output> handler;

    /**
     * List of request interceptors.
     */
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;

    /**
     * List of response interceptors.
     */
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    /**
     * List of exception handlers.
     */
    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

    /**
     * Constructor to build an instance of BaseRequestHandlerChain.
     * @param handler request handler responsible for processing an incoming request.
     * @param requestInterceptors list of request interceptors.
     * @param responseInterceptors list of response interceptors.
     * @param exceptionHandlers list of exception handlers.
     */
    protected BaseRequestHandlerChain(final GenericRequestHandler<Input, Output> handler,
                                      final List<GenericRequestInterceptor<Input>> requestInterceptors,
                                      final List<GenericResponseInterceptor<Input, Output>> responseInterceptors,
                                      final List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
        this.handler = handler;
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors = responseInterceptors != null ? responseInterceptors : new ArrayList<>();
        this.exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : new ArrayList<>();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public GenericRequestHandler<Input, Output> getRequestHandler() {
        return handler;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
        return requestInterceptors;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
        return responseInterceptors;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<GenericExceptionHandler<Input, Output>> getExceptionHandlers() {
        return exceptionHandlers;
    }

    /**
     * Returns an instance of Builder for the specified class types.
     * @param input input class type.
     * @param output output class type.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @param <Self> generic of type self.
     * @return {@link Builder}.
     */
    public static <Input, Output, Self extends Builder<Input, Output, Self>> Builder<Input, Output, Self> forTypes(
            final Class<Input> input, final Class<Output> output) {
        return new Builder<>();
    }

    /**
     * Returns an instance of Builder.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @return {@link Builder}.
     */
    public static <Input, Output> Builder<Input, Output, ?> builder() {
        return new Builder<>();
    }

    /**
     * Base Request Handler Chain Builder.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @param <Self> of type Builder class.
     */
    @SuppressWarnings("unchecked")
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        /**
         * Request handler responsible for processing an incoming request.
         */
        protected GenericRequestHandler<Input, Output> handler;

        /**
         * List of request interceptors.
         */
        protected List<GenericRequestInterceptor<Input>> requestInterceptors;

        /**
         * List of response interceptors.
         */
        protected List<GenericResponseInterceptor<Input, Output>> responseInterceptors;

        /**
         * List of exception handlers.
         */
        protected List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

        /**
         * Public constructor.
         */
        public Builder() { }

        /**
         * Adds a request handler of type {@link GenericRequestHandler} to request handler chain.
         * @param handler request handler.
         * @return {@link Builder}.
         */
        public Self withRequestHandler(final GenericRequestHandler<Input, Output> handler) {
            this.handler = handler;
            return (Self) this;
        }

        /**
         * Adds a single request interceptor of type {@link GenericRequestInterceptor} to request handler chain.
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
         * Adds multiple request interceptors of type {@link GenericRequestInterceptor} to request handler chain.
         * @param requestInterceptors list of request interceptors.
         * @return {@link Builder}.
         */
        public Self withRequestInterceptors(final List<GenericRequestInterceptor<Input>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (Self) this;
        }

        /**
         * Adds a single response interceptor of type {@link GenericResponseInterceptor} to request handler chain.
         * @param responseInterceptor response interceptor.
         * @return {@link Builder}.
         */
        public Self addResponseInterceptor(final GenericResponseInterceptor<Input, Output> responseInterceptor) {
            if (responseInterceptors == null) {
                responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (Self) this;
        }

        /**
         * Adds multiple response interceptors of type {@link GenericResponseInterceptor} to request handler chain.
         * @param responseInterceptors list of response interceptors.
         * @return {@link Builder}.
         */
        public Self withResponseInterceptor(final List<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self) this;
        }

        /**
         * Adds multiple exception handlers of type {@link BaseRequestHandlerChain} to request handler chain.
         * @param exceptionHandlers list of exception handlers.
         * @return {@link Builder}.
         */
        public Self withExceptionHandlers(final List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return (Self) this;
        }

        /**
         * Adds a single exception handler of type {@link GenericExceptionHandler} to request handler chain.
         * @param exceptionHandler exception handler.
         * @return {@link Builder}.
         */
        public Self addExceptionHandler(final GenericExceptionHandler<Input, Output> exceptionHandler) {
            if (exceptionHandlers == null) {
                exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(exceptionHandler);
            return (Self) this;
        }

        /**
         * Builder method constructs an instance of BaseRequestHandlerChain.
         * @return {@link BaseRequestHandlerChain}.
         */
        public BaseRequestHandlerChain<Input, Output> build() {
            return new BaseRequestHandlerChain<>(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
        }
    }

}
