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

import com.amazon.ask.dispatcher.RequestDispatcher;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.exception.UnhandledSkillException;
import com.amazon.ask.model.Response;
import com.amazon.ask.util.ValidationUtils;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Using a list of {@link RequestMapper} this class tries to find
 * a handler for a request, then delegates the invocation to a {@link HandlerAdapter} that
 * supports such a handler.
 */
public class DefaultRequestDispatcher implements RequestDispatcher {
    private static Logger logger = getLogger(DefaultRequestDispatcher.class);

    protected final Collection<HandlerAdapter> handlerAdapters;
    protected final Collection<RequestMapper> requestMappers;
    protected final ExceptionMapper exceptionMapper;
    protected final Collection<RequestInterceptor> requestInterceptors;
    protected final Collection<ResponseInterceptor> responseInterceptors;

    protected DefaultRequestDispatcher(Collection<HandlerAdapter> handlerAdapters, Collection<RequestMapper> requestMappers, ExceptionMapper exceptionMapper) {
        this(handlerAdapters, requestMappers, exceptionMapper, null, null);
    }

    protected DefaultRequestDispatcher(Collection<HandlerAdapter> handlerAdapters, Collection<RequestMapper> requestMappers, ExceptionMapper exceptionMapper,
                                       Collection<RequestInterceptor> requestInterceptors, Collection<ResponseInterceptor> responseInterceptors) {
        this.handlerAdapters = ValidationUtils.assertNotNull(handlerAdapters, "handlerAdapters");
        this.requestMappers = ValidationUtils.assertNotEmpty(requestMappers, "requestMappers");
        this.exceptionMapper = exceptionMapper;
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors = responseInterceptors != null ? responseInterceptors : new ArrayList<>();
    }

    public Optional<Response> dispatch(HandlerInput input) throws AskSdkException {
        RequestEnvelope requestEnvelope = input.getRequestEnvelope();
        String requestId = requestEnvelope.getRequest().getRequestId();
        if (logger.isDebugEnabled()) {
            logger.debug("Dispatch request ID {} received", requestId);
        }

        try {
            return doDispatch(input);
        } catch (Exception e) {
            Optional<ExceptionHandler> exceptionHandler = exceptionMapper != null
                    ? exceptionMapper.getHandler(input, e) : Optional.empty();
            if (exceptionHandler.isPresent()) {
                logger.debug("[{}] Found suitable exception handler", requestId);
                return exceptionHandler.get().handle(input, e);
            } else {
                logger.debug("[{}] No suitable exception handler found", requestId);
                throw new UnhandledSkillException(e);
            }
        }
    }

    private Optional<Response> doDispatch(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        String requestId = request.getRequestId();

        // execute any global request interceptors
        for (RequestInterceptor requestInterceptor : requestInterceptors) {
            requestInterceptor.process(input);
        }

        Optional<RequestHandlerChain> handlerChain = Optional.empty();
        // first we query the mappers to find a handler chain for the current request
        for (RequestMapper mapper : requestMappers) {
            handlerChain = mapper.getRequestHandlerChain(input);
            if (handlerChain.isPresent()) {
                break;
            }
        }

        if (handlerChain.isPresent()) {
            logger.debug("[{}] Found matching handler", requestId);
        } else {
            String message = String.format("[%s] Unable to find a suitable request handler", requestId);
            logger.error(message);
            throw new AskSdkException(message);
        }

        Object requestHandler = handlerChain.get().getRequestHandler();
        // find an adapter that supports the discovered handler
        HandlerAdapter handlerAdapter = null;
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(requestHandler)) {
                handlerAdapter = adapter;
                logger.debug("[{}] Found compatible adapter {}", requestId, adapter.getClass().getName());
                break;
            }
        }

        if (handlerAdapter == null) {
            String message = String.format("[%s] Unable to find a suitable handler adapter", requestId);
            logger.error(message);
            throw new AskSdkException(message);
        }

        // execute any request interceptors attached to the handler chain
        for (RequestInterceptor requestInterceptor : handlerChain.get().getRequestInterceptors()) {
            requestInterceptor.process(input);
        }

        // invoke request handler using the adapter
        Optional<Response> response = handlerAdapter.execute(input, requestHandler);

        // first execute any response interceptors attached to the handler chain, then any global ones
        Stream.concat(handlerChain.get().getResponseInterceptors().stream(), responseInterceptors.stream())
                .forEach(responseInterceptor -> responseInterceptor.process(input, response));

        return response;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Collection<HandlerAdapter> handlerAdapters;
        private Collection<RequestMapper> requestMappers;
        private ExceptionMapper exceptionMapper;
        private Collection<RequestInterceptor> requestInterceptors;
        private Collection<ResponseInterceptor> responseInterceptors;

        private Builder() {}

        public Builder withHandlerAdapters(Collection<HandlerAdapter> adapters) {
            this.handlerAdapters = adapters;
            return this;
        }

        public Builder withHandlerAdapters(HandlerAdapter... adapters) {
            this.handlerAdapters = Arrays.asList(adapters);
            return this;
        }

        public Builder addHandlerAdapter(HandlerAdapter adapter) {
            if (handlerAdapters == null) {
                handlerAdapters = new ArrayList<>();
            }
            handlerAdapters.add(adapter);
            return this;
        }

        public Builder withRequestMappers(Collection<RequestMapper> requestMappers) {
            this.requestMappers = requestMappers;
            return this;
        }

        public Builder withRequestMappers(RequestMapper... requestMappers) {
            this.requestMappers = Arrays.asList(requestMappers);
            return this;
        }

        public Builder addRequestMapper(RequestMapper requestMapper) {
            if (requestMappers == null) {
                requestMappers = new ArrayList<>();
            }
            requestMappers.add(requestMapper);
            return this;
        }

        public Builder withExceptionMapper(ExceptionMapper exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return this;
        }

        public Builder withRequestInterceptors(Collection<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return this;
        }

        public Builder withRequestInterceptors(RequestInterceptor... requestInterceptors) {
            this.requestInterceptors = Arrays.asList(requestInterceptors);
            return this;
        }

        public Builder addRequestInterceptor(RequestInterceptor requestInterceptor) {
            if (requestInterceptors == null) {
                requestInterceptors = new ArrayList<>();
            }
            requestInterceptors.add(requestInterceptor);
            return this;
        }

        public Builder withResponseInterceptors(Collection<ResponseInterceptor> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return this;
        }

        public Builder withResponseInterceptors(ResponseInterceptor... responseInterceptors) {
            this.responseInterceptors = Arrays.asList(responseInterceptors);
            return this;
        }

        public Builder addResponseInterceptor(ResponseInterceptor responseInterceptor) {
            if (responseInterceptors == null) {
                responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return this;
        }

        public DefaultRequestDispatcher build() {
            return new DefaultRequestDispatcher(handlerAdapters, requestMappers, exceptionMapper, requestInterceptors, responseInterceptors);
        }
    }
}
