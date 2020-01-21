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

import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.model.Response;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link RequestHandlerChain}. Handlers are typed to {@link GenericRequestHandler}.
 */
@Deprecated
public class DefaultRequestHandlerChain extends BaseRequestHandlerChain<HandlerInput, Optional<Response>> implements RequestHandlerChain {

    /**
     * Constructor to build an instance of DefaultRequestHandlerChain.
     * @param handler request handler responsible for processing an incoming request.
     * @param requestInterceptors list of request interceptors.
     * @param responseInterceptors list of response interceptors.
     * @param exceptionHandlers list of exception handlers.
     */
    protected DefaultRequestHandlerChain(final GenericRequestHandler<HandlerInput, Optional<Response>> handler,
                                         final List<GenericRequestInterceptor<HandlerInput>> requestInterceptors,
                                         final List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> responseInterceptors,
                                         final List<GenericExceptionHandler<HandlerInput, Optional<Response>>> exceptionHandlers) {
        super(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public com.amazon.ask.dispatcher.request.handler.RequestHandler getRequestHandler() {
        return (com.amazon.ask.dispatcher.request.handler.RequestHandler) handler;
    }

    /**
     * Static method to return an instance of Builder.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * DefaultRequestHandlerChain Builder.
     */
    public static final class Builder extends BaseRequestHandlerChain.Builder<HandlerInput, Optional<Response>, Builder> {

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Builder method to construct an instance of DefaultRequestHandlerChain.
         * @return {@link DefaultRequestHandlerChain}.
         */
        public DefaultRequestHandlerChain build() {
            return new DefaultRequestHandlerChain(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
        }
    }

}
