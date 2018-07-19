/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler.impl;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericRequestHandlerChain implements RequestHandlerChain {
    protected final Object handler;
    protected final List<RequestInterceptor> requestInterceptors;
    protected final List<ResponseInterceptor> responseInterceptors;
    protected final List<ExceptionHandler> exceptionHandlers;

    protected GenericRequestHandlerChain(Object handler, List<RequestInterceptor> requestInterceptors,
                                         List<ResponseInterceptor> responseInterceptors,
                                         List<ExceptionHandler> exceptionHandlers) {
        this.handler = ValidationUtils.assertNotNull(handler, "handler");
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors = responseInterceptors != null ? responseInterceptors : new ArrayList<>();
        this.exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Object getRequestHandler() {
        return handler;
    }

    @Override
    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    @Override
    public List<ResponseInterceptor> getResponseInterceptors() {
        return responseInterceptors;
    }

    @Override
    public List<ExceptionHandler> getExceptionHandlers() { return exceptionHandlers; }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder<T>> {
        protected Object handler;
        protected List<RequestInterceptor> requestInterceptors;
        protected List<ResponseInterceptor> responseInterceptors;
        protected List<ExceptionHandler> exceptionHandlers;

        protected Builder() {
        }

        public T withRequestHandler(Object handler) {
            this.handler = handler;
            return (T) this;
        }

        public T addRequestInterceptor(RequestInterceptor requestInterceptor) {
            if (requestInterceptors == null) {
                requestInterceptors = new ArrayList<>();
            }
            requestInterceptors.add(requestInterceptor);
            return (T) this;
        }

        public T withRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (T) this;
        }

        public T addResponseInterceptor(ResponseInterceptor responseInterceptor) {
            if (responseInterceptors == null) {
                responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (T) this;
        }

        public T withResponseInterceptor(List<ResponseInterceptor> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (T) this;
        }

        public T withExceptionHandlers(List<ExceptionHandler> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return (T) this;
        }

        public T addExceptionHandler(ExceptionHandler exceptionHandler) {
            if (exceptionHandlers == null) {
                exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(exceptionHandler);
            return (T) this;
        }

        public GenericRequestHandlerChain build() {
            return new GenericRequestHandlerChain(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
        }
    }
}
