/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.impl;

import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.dispatcher.impl.BaseRequestDispatcher;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.dispatcher.RequestDispatcher;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import java.util.Collection;
import java.util.Optional;

/**
 * {@inheritDoc}.
 *
 * This implementation routes incoming requests to a {@link com.amazon.ask.dispatcher.request.mapper.RequestMapper} to find a
 * {@link com.amazon.ask.dispatcher.request.handler.RequestHandlerChain}. A {@link com.amazon.ask.dispatcher.request.handler.HandlerAdapter}
 * is used to execute the discovered request handler type.
 *
 * {@link com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor} and
 * {@link com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor} instances may be configured globally on this dispatcher to be
 * executed for all requests. Interceptors set on the {@link com.amazon.ask.dispatcher.request.handler.RequestHandlerChain} level are also
 * supported.
 *
 * An {@link com.amazon.ask.dispatcher.exception.ExceptionMapper} is used to find exception handlers in the event of an unhandled exception during
 * request processing.
 */
@Deprecated
public class DefaultRequestDispatcher extends BaseRequestDispatcher<HandlerInput, Optional<Response>> implements RequestDispatcher {

    /**
     * Constructor for DefaultRequestDispatcher.
     * @param handlerAdapters collection of handler adapters.
     * @param requestMappers collection of request mappers.
     * @param exceptionMapper exception mapper.
     */
    protected DefaultRequestDispatcher(final Collection<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                       final Collection<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                       final GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper) {
        this(handlerAdapters, requestMappers, exceptionMapper, null, null);
    }

    /**
     * Constructor for DefaultRequestDispatcher.
     * @param handlerAdapters collection of handler adapters.
     * @param requestMappers collection of request mappers.
     * @param exceptionMapper exception mapper.
     * @param requestInterceptors collection of request interceptors.
     * @param responseInterceptors collection of response interceptors.
     */
    protected DefaultRequestDispatcher(final Collection<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                       final Collection<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                       final GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                       final Collection<GenericRequestInterceptor<HandlerInput>> requestInterceptors,
                                       final Collection<GenericResponseInterceptor<HandlerInput, Optional<Response>>> responseInterceptors) {
        super(requestMappers, exceptionMapper, handlerAdapters, requestInterceptors, responseInterceptors);
    }

    /**
     * Static method to build an instance of Builder.
     * @return {@link com.amazon.ask.request.dispatcher.impl.BaseRequestDispatcher.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * DefaultRequestDispatcher Builder.
     */
    public static final class Builder extends BaseRequestDispatcher.Builder<HandlerInput, Optional<Response>, Builder> {

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Builder method to construct an instance of DefaultRequestDispatcher.
         * @return {@link DefaultRequestDispatcher}.
         */
        public DefaultRequestDispatcher build() {
            return new DefaultRequestDispatcher(handlerAdapters, requestMappers, exceptionMapper, requestInterceptors, responseInterceptors);
        }
    }

}
