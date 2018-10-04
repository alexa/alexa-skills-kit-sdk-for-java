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

    protected final GenericRequestHandler<Input, Output> handler;
    protected final List<GenericRequestInterceptor<Input>> requestInterceptors;
    protected final List<GenericResponseInterceptor<Input, Output>> responseInterceptors;
    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

    protected BaseRequestHandlerChain(GenericRequestHandler<Input, Output> handler,
                                      List<GenericRequestInterceptor<Input>> requestInterceptors,
                                      List<GenericResponseInterceptor<Input, Output>> responseInterceptors,
                                      List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
        this.handler = handler;
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors= responseInterceptors != null ? responseInterceptors : new ArrayList<>();
        this.exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : new ArrayList<>();
    }

    @Override
    public GenericRequestHandler<Input, Output> getRequestHandler() { return handler; }

    @Override
    public List<GenericRequestInterceptor<Input>> getRequestInterceptors() {
        return requestInterceptors;
    }

    @Override
    public List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors() {
        return responseInterceptors;
    }

    @Override
    public List<GenericExceptionHandler<Input, Output>> getExceptionHandlers() {
        return exceptionHandlers;
    }

    public static <Input, Output, Self extends Builder<Input, Output, Self>> Builder<Input, Output, Self> forTypes(
            Class<Input> input, Class<Output> output) {
        return new Builder<>();
    }

    public static <Input, Output> Builder<Input, Output, ?> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        protected GenericRequestHandler<Input, Output> handler;
        protected List<GenericRequestInterceptor<Input>> requestInterceptors;
        protected List<GenericResponseInterceptor<Input, Output>> responseInterceptors;
        protected List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

        public Builder() {
        }

        public Self withRequestHandler(GenericRequestHandler<Input, Output> handler) {
            this.handler = handler;
            return (Self) this;
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

        public Self addResponseInterceptor(GenericResponseInterceptor<Input, Output> responseInterceptor) {
            if (responseInterceptors == null) {
                responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (Self) this;
        }

        public Self withResponseInterceptor(List<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self) this;
        }

        public Self withExceptionHandlers(List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return (Self) this;
        }

        public Self addExceptionHandler(GenericExceptionHandler<Input, Output> exceptionHandler) {
            if (exceptionHandlers == null) {
                exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(exceptionHandler);
            return (Self) this;
        }


        public BaseRequestHandlerChain<Input, Output> build() {
            return new BaseRequestHandlerChain<>(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
        }
    }


}
