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

import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
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
 * {@inheritDoc}
 *
 * This implementation routes incoming requests to a {@link RequestMapper} to find a {@link RequestHandlerChain}.
 * A {@link HandlerAdapter} is used to execute the discovered request handler type.
 *
 * {@link RequestInterceptor} and {@link ResponseInterceptor} instances may be configured globally on this dispatcher
 * to be executed for all requests. Interceptors set on the {@link RequestHandlerChain} level are also supported.
 *
 * An {@link ExceptionMapper} is used to find exception handlers in the event of an unhandled exception during
 * request processing.
 */
@Deprecated
public class DefaultRequestDispatcher extends BaseRequestDispatcher<HandlerInput, Optional<Response>> implements RequestDispatcher {

    protected DefaultRequestDispatcher(Collection<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                       Collection<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                       GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper) {
        this(handlerAdapters, requestMappers, exceptionMapper, null, null);
    }

    protected DefaultRequestDispatcher(Collection<GenericHandlerAdapter<HandlerInput, Optional<Response>>> handlerAdapters,
                                       Collection<GenericRequestMapper<HandlerInput, Optional<Response>>> requestMappers,
                                       GenericExceptionMapper<HandlerInput, Optional<Response>> exceptionMapper,
                                       Collection<GenericRequestInterceptor<HandlerInput>> requestInterceptors,
                                       Collection<GenericResponseInterceptor<HandlerInput, Optional<Response>>> responseInterceptors) {
        super(requestMappers, exceptionMapper, handlerAdapters, requestInterceptors, responseInterceptors);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends BaseRequestDispatcher.Builder<HandlerInput, Optional<Response>, Builder> {
        private Builder() {
        }

        public DefaultRequestDispatcher build() {
            return new DefaultRequestDispatcher(handlerAdapters, requestMappers, exceptionMapper, requestInterceptors, responseInterceptors);
        }
    }

}
