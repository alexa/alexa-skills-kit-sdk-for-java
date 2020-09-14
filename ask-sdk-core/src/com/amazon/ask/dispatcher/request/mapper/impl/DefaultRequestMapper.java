/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.mapper.impl;

import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.mapper.impl.BaseRequestMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.model.Response;

import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 *
 * This implementation accesses registered {@link com.amazon.ask.dispatcher.request.handler.impl.DefaultRequestHandlerChain} instances
 * in the order they were provided. Suitable chains are identified by calling canHandle on the attached
 * {@link com.amazon.ask.dispatcher.request.handler.RequestHandler}.
 */
@Deprecated
public class DefaultRequestMapper extends BaseRequestMapper<HandlerInput, Optional<Response>> implements RequestMapper {

    /**
     * Constructor for DefaultRequestMapper.
     * @param handlerChains list of request handler chains.
     */
    protected DefaultRequestMapper(final List<BaseRequestHandlerChain<HandlerInput, Optional<Response>>> handlerChains) {
        super(handlerChains);
    }

    /**
     * Static method which returns an instance of Builder.
     * @return {@link com.amazon.ask.request.mapper.impl.BaseRequestMapper.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * DefaultRequestMapper Builder.
     */
    public static final class Builder extends BaseRequestMapper.Builder<HandlerInput, Optional<Response>, Builder> {

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Builder method to construct an instance of DefaultRequestMapper.
         * @return {@link DefaultRequestMapper}.
         */
        public DefaultRequestMapper build() {
            return new DefaultRequestMapper(handlerChains);
        }
    }

}
