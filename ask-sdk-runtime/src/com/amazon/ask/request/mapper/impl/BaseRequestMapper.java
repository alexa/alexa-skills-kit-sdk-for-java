/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.mapper.impl;

import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;
import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 *
 * This implementation accesses registered {@link GenericRequestHandlerChain} instances in the order they were provided.
 * Suitable chains are identified by calling canHandle on the attached {@link GenericRequestHandler}.
 * @param <Input> handler input type.
 * @param <Output> handler output type.
 */
public class BaseRequestMapper<Input, Output> implements GenericRequestMapper<Input, Output> {

    /**
     * List of handler chains.
     */
    protected final List<BaseRequestHandlerChain<Input, Output>> handlerChains;

    /**
     * Constructor for BaseRequestMapper.
     * @param handlerChains list of handler chains.
     */
    protected BaseRequestMapper(final List<BaseRequestHandlerChain<Input, Output>> handlerChains) {
        this.handlerChains = ValidationUtils.assertNotEmpty(handlerChains, "handlerChains");
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Optional<GenericRequestHandlerChain<Input, Output>> getRequestHandlerChain(final Input input) {
        for (BaseRequestHandlerChain<Input, Output> handlerChain : handlerChains) {
            if (handlerChain.getRequestHandler().canHandle(input)) {
                return Optional.of(handlerChain);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an instance of Builder.
     * @param input class of type Input
     * @param output class of type Output
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
     * Base Request Mapper Builder.
     * @param <Input> handler input type.
     * @param <Output> handler output type.
     * @param <Self> of type Builder.
     */
    @SuppressWarnings("unchecked")
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        /**
         * List of handler chains.
         */
        protected List<BaseRequestHandlerChain<Input, Output>> handlerChains;

        /**
         * Constructor for Builder.
         */
        protected Builder() { }

        /**
         * Add multiple request handler chains to BaseRequestMapper.
         * @param handlerChains list of handler chains.
         * @return {@link Builder}.
         */
        public Self withRequestHandlerChains(final List<BaseRequestHandlerChain<Input, Output>> handlerChains) {
            this.handlerChains = handlerChains;
            return (Self) this;
        }

        /**
         * Add a request handler chain to BaseRequestMapper.
         * @param handlerChain handler chain.
         * @return {@link Builder}.
         */
        public Self addRequestHandlerChain(final BaseRequestHandlerChain<Input, Output> handlerChain) {
            if (handlerChains == null) {
                handlerChains = new ArrayList<>();
            }
            handlerChains.add(handlerChain);
            return (Self) this;
        }

        /**
         * Builder method to build an instance of BaseRequestMapper.
         * @return {@link GenericRequestMapper}.
         */
        public GenericRequestMapper<Input, Output> build() {
            return new BaseRequestMapper<>(handlerChains);
        }
    }

}
