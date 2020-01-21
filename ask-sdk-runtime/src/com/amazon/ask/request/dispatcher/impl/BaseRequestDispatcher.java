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

import static org.slf4j.LoggerFactory.getLogger;

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
 * @param <Input> handler input type.
 * @param <Output> handler output type.
 */
public class BaseRequestDispatcher<Input, Output> implements GenericRequestDispatcher<Input, Output> {

    /**
     * Logger instance to log information for debugging purposes.
     */
    private static final Logger LOGGER = getLogger(BaseRequestDispatcher.class);

    /**
     * Collection of request mappers.
     */
    protected final Collection<GenericRequestMapper<Input, Output>> requestMappers;

    /**
     * Exception mapper.
     */
    protected final GenericExceptionMapper<Input, Output> exceptionMapper;

    /**
     * Collection of handler adapters.
     */
    protected final Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters;

    /**
     * Collection of request interceptors.
     */
    protected final Collection<GenericRequestInterceptor<Input>> requestInterceptors;

    /**
     * Collection of response interceptors.
     */
    protected final Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors;

    /**
     *
     * @param requestMappers Collection of request mappers.
     * @param exceptionMapper Exception mapper.
     * @param handlerAdapters Collection of handler adapters.
     * @param requestInterceptors Collection of request interceptors.
     * @param responseInterceptors Collection of response interceptors.
     */
    protected BaseRequestDispatcher(final Collection<GenericRequestMapper<Input, Output>> requestMappers,
                                    final GenericExceptionMapper<Input, Output> exceptionMapper,
                                    final Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters,
                                    final Collection<GenericRequestInterceptor<Input>> requestInterceptors,
                                    final Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
        this.requestMappers = ValidationUtils.assertNotEmpty(requestMappers, "requestMappers");
        this.exceptionMapper = exceptionMapper;
        this.handlerAdapters = ValidationUtils.assertNotNull(handlerAdapters, "handlerAdapters");
        this.requestInterceptors = requestInterceptors != null ? requestInterceptors : new ArrayList<>();
        this.responseInterceptors = responseInterceptors != null ? responseInterceptors : new ArrayList<>();
    }

    /**
     * Dispatches an incoming request to the appropriate handling code and returns any output.
     * @param input input to the dispatcher
     * @return {@link Output}.
     * @throws AskSdkException is thrown when dispatch fails.
     */
    public Output dispatch(final Input input) throws AskSdkException {
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

    /**
     * Dispatches an incoming request to the appropriate handling code and returns any output.
     * @param input input to the dispatcher
     * @return {@link Output}.
     * @throws Exception is thrown when dispatch fails.
     */
    private Output doDispatch(final Input input) throws Exception {
        // execute any global request interceptors
        Input modifiedInput = input;
        for (GenericRequestInterceptor<Input> requestInterceptor : requestInterceptors) {
            modifiedInput = requestInterceptor.processRequest(modifiedInput);
        }

        Optional<GenericRequestHandlerChain<Input, Output>> handlerChain = Optional.empty();
        // first we query the mappers to find a handler chain for the current request
        for (GenericRequestMapper<Input, Output> mapper : requestMappers) {
            handlerChain = mapper.getRequestHandlerChain(modifiedInput);
            if (handlerChain.isPresent()) {
                break;
            }
        }

        if (handlerChain.isPresent()) {
            LOGGER.debug("Found matching handler");
        } else {
            String message = "Unable to find a suitable request handler";
            LOGGER.error(message);
            throw new AskSdkException(message);
        }

        Object requestHandler = handlerChain.get().getRequestHandler();
        // find an adapter that supports the discovered handler
        GenericHandlerAdapter<Input, Output> handlerAdapter = null;
        for (GenericHandlerAdapter<Input, Output> adapter : handlerAdapters) {
            if (adapter.supports(requestHandler)) {
                handlerAdapter = adapter;
                LOGGER.debug("Found compatible adapter {}", adapter.getClass().getName());
                break;
            }
        }

        if (handlerAdapter == null) {
            String message = "Unable to find a suitable handler adapter";
            LOGGER.error(message);
            throw new AskSdkException(message);
        }

        Output response;
        try {
            // execute any request interceptors attached to the handler chain
            for (GenericRequestInterceptor<Input> requestInterceptor : handlerChain.get().getRequestInterceptors()) {
                modifiedInput = requestInterceptor.processRequest(modifiedInput);
            }

            // invoke request handler using the adapter
            response = handlerAdapter.execute(modifiedInput, requestHandler);

            // execute any response interceptors attached to the handler chain
            for (GenericResponseInterceptor<Input, Output> responseInterceptor : handlerChain.get().getResponseInterceptors()) {
                response = responseInterceptor.processResponse(modifiedInput, response);
            }
        } catch (Exception e) {
            final Input originalInput = modifiedInput;
            return handlerChain.get().getExceptionHandlers().stream()
                    .filter(exceptionHandler -> exceptionHandler.canHandle(originalInput, e))
                    .findFirst()
                    .orElseThrow(() -> e).handle(modifiedInput, e);
        }

        // execute any global response interceptors
        for (GenericResponseInterceptor<Input, Output> responseInterceptor : responseInterceptors) {
            response = responseInterceptor.processResponse(modifiedInput, response);
        }

        return response;
    }

    /**
     * Returns an instance of Builder.
     * @param input class of type Input.
     * @param output class of type Output.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @param <Self> of type Builder.
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
     * Base Request Dispatcher Builder.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @param <Self> of type Builder.
     */
    @SuppressWarnings("unchecked")
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        /**
         * Collection of request mappers.
         */
        protected Collection<GenericRequestMapper<Input, Output>> requestMappers;

        /**
         * Exception mapper.
         */
        protected GenericExceptionMapper<Input, Output> exceptionMapper;

        /**
         * Collection of handler adapters.
         */
        protected Collection<GenericHandlerAdapter<Input, Output>> handlerAdapters;

        /**
         * Collection of request interceptors.
         */
        protected Collection<GenericRequestInterceptor<Input>> requestInterceptors;

        /**
         * Collection of response interceptors.
         */
        protected Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors;

        /**
         * Constructor for Builder.
         */
        protected Builder() { }

        /**
         * Add multiple request mappers to BaseRequestDispatcher.
         * @param requestMappers Collection of request mappers.
         * @return {@link Builder}.
         */
        public Self withRequestMappers(final Collection<GenericRequestMapper<Input, Output>> requestMappers) {
            this.requestMappers = requestMappers;
            return (Self) this;
        }

        /**
         * Add multiple request mappers to BaseRequestDispatcher.
         * @param requestMappers request mappers.
         * @return {@link Builder}.
         */
        public Self withRequestMappers(final GenericRequestMapper<Input, Output>... requestMappers) {
            this.requestMappers = Arrays.asList(requestMappers);
            return (Self) this;
        }

        /**
         * Add a request mapper to BaseRequestDispatcher.
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
         * Add an exception mapper to BaseRequestDispatcher.
         * @param exceptionMapper exception mapper.
         * @return {@link Builder}.
         */
        public Self withExceptionMapper(final GenericExceptionMapper<Input, Output> exceptionMapper) {
            this.exceptionMapper = exceptionMapper;
            return (Self) this;
        }

        /**
         * Add multiple handler adapters to BaseRequestDispatcher.
         * @param adapters collection of handler adapters.
         * @return {@link Builder}.
         */
        public Self withHandlerAdapters(final Collection<GenericHandlerAdapter<Input, Output>> adapters) {
            this.handlerAdapters = adapters;
            return (Self) this;
        }

        /**
         * Add multiple handler adapters to BaseRequestDispatcher.
         * @param adapters handler adapters.
         * @return {@link Builder}.
         */
        public Self withHandlerAdapters(final GenericHandlerAdapter<Input, Output>... adapters) {
            this.handlerAdapters = Arrays.asList(adapters);
            return (Self) this;
        }

        /**
         * Add a handler adapter to BaseRequestDispatcher.
         * @param adapter handler adapter.
         * @return {@link Builder}.
         */
        public Self addHandlerAdapter(final GenericHandlerAdapter<Input, Output> adapter) {
            if (handlerAdapters == null) {
                handlerAdapters = new ArrayList<>();
            }
            handlerAdapters.add(adapter);
            return (Self) this;
        }

        /**
         * Add multiple request interceptors to BaseRequestDispatcher.
         * @param requestInterceptors collection of request interceptors.
         * @return {@link Builder}.
         */
        public Self withRequestInterceptors(final Collection<GenericRequestInterceptor<Input>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return (Self) this;
        }

        /**
         * Add multiple request interceptors to BaseRequestDispatcher.
         * @param requestInterceptors request interceptors.
         * @return {@link Builder}.
         */
        public Self withRequestInterceptors(final GenericRequestInterceptor<Input>... requestInterceptors) {
            this.requestInterceptors = Arrays.asList(requestInterceptors);
            return (Self) this;
        }

        /**
         * Add a request interceptor to BaseRequestDispatcher.
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
         * Add multiple response interceptors to BaseRequestDispatcher.
         * @param responseInterceptors collection of response interceptors.
         * @return {@link Builder}.
         */
        public Self withResponseInterceptors(final Collection<GenericResponseInterceptor<Input, Output>> responseInterceptors) {
            this.responseInterceptors = responseInterceptors;
            return (Self) this;
        }

        /**
         * Add multiple response interceptors to BaseRequestDispatcher.
         * @param responseInterceptors response interceptors.
         * @return {@link Builder}.
         */
        public Self withResponseInterceptors(final GenericResponseInterceptor<Input, Output>... responseInterceptors) {
            this.responseInterceptors = Arrays.asList(responseInterceptors);
            return (Self) this;
        }

        /**
         * Add a response interceptor to BaseRequestDispatcher.
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
         * Builder method to build an instance of BaseRequestDispatcher.
         * @return {@link GenericRequestDispatcher}.
         */
        public GenericRequestDispatcher<Input, Output> build() {
            return new BaseRequestDispatcher<>(requestMappers, exceptionMapper, handlerAdapters, requestInterceptors,
                    responseInterceptors);
        }
    }

}
