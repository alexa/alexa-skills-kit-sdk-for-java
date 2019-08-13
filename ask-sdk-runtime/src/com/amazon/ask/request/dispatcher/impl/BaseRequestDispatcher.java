/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.dispatcher.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.request.dispatcher.GenericRequestDispatcher;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.exception.UnhandledSkillException;
import com.amazon.ask.util.ValidationUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@inheritDoc}
 *
 * This implementation routes incoming requests to a {@link GenericRequestMapper} to find a {@link GenericRequestHandlerChain}.
 * A {@link GenericHandlerAdapter} is used to execute the discovered request handler type.
 *
 * {@link GenericRequestInterceptor} and {@link GenericResponseInterceptor} instances may be configured globally on this dispatcher
 * to be executed for all requests. Interceptors set on the {@link GenericRequestHandlerChain} level are also supported.
 *
 * A {@link GenericRequestMapper} is used to find exception handlers in the event of an unhandled exception during
 * request processing.
 */
public class BaseRequestDispatcher<Input, Output> implements GenericRequestDispatcher<Input, Output> {

    private static final Logger logger = getLogger(BaseRequestDispatcher.class);

    protected final Collection<GenericRequestMapper<Input, Output>> requestMappers;
    protected final GenericExceptionMapper<Input, Output> exceptionMapper;
    protected final Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters;
    protected final Collection<GenericRequestInterceptor<Input>> requestInterceptors;
    protected final Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    protected BaseRequestDispatcher(Collection<GenericRequestMapper<Input, Output>> requestMappers,
                                    GenericExceptionMapper<Input, Output> exceptionMapper,
                                    Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters,
                                    Collection<GenericRequestInterceptor<Input>> requestInterceptors,
                                    Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
        this.requestMappers = ValidationUtils.assertNotEmpty(requestMappers, "requestMappers");
        this.exceptionMapper = exceptionMapper;
        this.handlerAdapters = ValidationUtils.assertNotNull(handlerAdapters, "handlerAdapters");
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors = responseInterceptors != null ? responseInterceptors : new ArrayList<>();
    }

    public Output dispatch(Input input) throws AskSdkException {
        try {
            return doDispatch(input);
        } catch (Exception e) {
            Optional<GenericExceptionHandler<Input, Output>> exceptionHandler = exceptionMapper != null
                    ? exceptionMapper.getHandler(input, e) : Optional.empty();
            if (exceptionHandler.isPresent()) {
                return exceptionHandler.get().handle(input, e);
            } else {
                throw new UnhandledSkillException(e);
            }
        }
    }

    private Output doDispatch(Input input) throws Exception {
        // execute any global request interceptors
        for (GenericRequestInterceptor<Input> requestInterceptor : requestInterceptors) {
            input = requestInterceptor.processRequest(input);
        }

        Optional<GenericRequestHandlerChain<Input, Output>> handlerChain = Optional.empty();
        // first we query the mappers to find a handler chain for the current request
        for (GenericRequestMapper<Input, Output> mapper : requestMappers) {
            handlerChain = mapper.getRequestHandlerChain(input);
            if (handlerChain.isPresent()) {
                break;
            }
        }

        if (handlerChain.isPresent()) {
            logger.debug("Found matching handler");
        } else {
            String message = "Unable to find a suitable request handler";
            logger.error(message);
            throw new AskSdkException(message);
        }

        Object requestHandler = handlerChain.get().getRequestHandler();
        // find an adapter that supports the discovered handler
        GenericHandlerAdapter<Input, Output> handlerAdapter = null;
        for (GenericHandlerAdapter<Input, Output> adapter : handlerAdapters) {
            if (adapter.supports(requestHandler)) {
                handlerAdapter = adapter;
                logger.debug("Found compatible adapter {}", adapter.getClass().getName());
                break;
            }
        }

        if (handlerAdapter == null) {
            String message = "Unable to find a suitable handler adapter";
            logger.error(message);
            throw new AskSdkException(message);
        }

        Output response;
        try {
            // execute any request interceptors attached to the handler chain
            for (GenericRequestInterceptor<Input> requestInterceptor : handlerChain.get().getRequestInterceptors()) {
                input = requestInterceptor.processRequest(input);
            }

            // invoke request handler using the adapter
            response = handlerAdapter.execute(input, requestHandler);

            // execute any response interceptors attached to the handler chain
            for (GenericResponseInterceptor<Input, Output> responseInterceptor : handlerChain.get().getResponseInterceptors()) {
                response = responseInterceptor.processResponse(input, response);
            }
        } catch (Exception e) {
            final Input originalInput = input;
            return handlerChain.get().getExceptionHandlers().stream()
                    .filter(exceptionHandler -> exceptionHandler.canHandle(originalInput, e))
                    .findFirst()
                    .orElseThrow(() -> e).handle(input, e);
        }

        // execute any global response interceptors
        for (GenericResponseInterceptor<Input, Output> responseInterceptor : responseInterceptors) {
            response = responseInterceptor.processResponse(input, response);
        }

        return response;
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
        protected Collection<GenericRequestMapper<Input, Output>> requestMappers;
        protected GenericExceptionMapper<Input, Output> exceptionMapper;
        protected Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters;
        protected Collection<GenericRequestInterceptor<Input>> requestInterceptors;
        protected Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors;

        protected Builder() {}

        public Self withRequestMappers(Collection<GenericRequestMapper<Input, Output>> requestMappers) {
            this.requestMappers = requestMappers;
            return (Self)this;
        }

        public Self withRequestMappers(GenericRequestMapper<Input, Output>... requestMappers) {
            this.requestMappers = Arrays.asList(requestMappers);
            return (Self)this;
        }

        public Self addRequestMapper(GenericRequestMapper<Input, Output> requestMapper) {
            if (requestMappers == null) {
                requestMappers = new ArrayList<>();
            }
            requestMappers.add(requestMapper);
            return (Self)this;
        }

        public Self withExceptionMapper(GenericExceptionMapper<Input, Output> exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return (Self)this;
        }

        public Self withHandlerAdapters(Collection<GenericHandlerAdapter<Input, Output>> adapters) {
            this.handlerAdapters = adapters;
            return (Self)this;
        }

        public Self withHandlerAdapters(GenericHandlerAdapter<Input, Output>... adapters) {
            this.handlerAdapters = Arrays.asList(adapters);
            return (Self)this;
        }

        public Self addHandlerAdapter(GenericHandlerAdapter<Input, Output> adapter) {
            if (handlerAdapters == null) {
                handlerAdapters = new ArrayList<>();
            }
            handlerAdapters.add(adapter);
            return (Self)this;
        }

        public Self withRequestInterceptors(Collection<GenericRequestInterceptor<Input>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (Self)this;
        }

        public Self withRequestInterceptors(GenericRequestInterceptor<Input>... requestInterceptors) {
            this.requestInterceptors = Arrays.asList(requestInterceptors);
            return (Self)this;
        }

        public Self addRequestInterceptor(GenericRequestInterceptor<Input> requestInterceptor) {
            if (requestInterceptors == null) {
                requestInterceptors = new ArrayList<>();
            }
            requestInterceptors.add(requestInterceptor);
            return (Self)this;
        }

        public Self withResponseInterceptors(Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self)this;
        }

        public Self withResponseInterceptors(GenericResponseInterceptor<Input, Output>... responseInterceptors) {
            this.responseInterceptors = Arrays.asList(responseInterceptors);
            return (Self)this;
        }

        public Self addResponseInterceptor(GenericResponseInterceptor<Input, Output> responseInterceptor) {
            if (responseInterceptors == null) {
                responseInterceptors = new ArrayList<>();
            }
            responseInterceptors.add(responseInterceptor);
            return (Self)this;
        }

        public GenericRequestDispatcher<Input, Output> build() {
            return new BaseRequestDispatcher<>(requestMappers, exceptionMapper, handlerAdapters, requestInterceptors,
                    responseInterceptors);
        }
    }

}
