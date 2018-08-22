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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultRequestHandlerChain;
import com.amazon.ask.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 *
 * This implementation accesses registered {@link DefaultRequestHandlerChain} instances in the order they were provided.
 * Suitable chains are identified by calling canHandle on the attached {@link com.amazon.ask.dispatcher.request.handler.RequestHandler}.
 */
public class DefaultRequestMapper implements RequestMapper {

    protected final List<DefaultRequestHandlerChain> handlerChains;

    protected DefaultRequestMapper(List<DefaultRequestHandlerChain> handlerChains) {
        this.handlerChains = ValidationUtils.assertNotEmpty(handlerChains, "handlerChains");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Optional<RequestHandlerChain> getRequestHandlerChain(HandlerInput input) {
        return handlerChains.stream()
                .filter(handlerChain -> handlerChain.getRequestHandler().canHandle(input))
                .map(handlerChain -> (RequestHandlerChain) handlerChain)
                .findFirst();
    }

    public static class Builder {
        private List<DefaultRequestHandlerChain> handlerChains;

        private Builder() {
        }

        public Builder withRequestHandlerChains(List<DefaultRequestHandlerChain> handlerChains) {
            this.handlerChains = handlerChains;
            return this;
        }

        public Builder addRequestHandlerChain(DefaultRequestHandlerChain handlerChain) {
            if (handlerChains == null) {
                handlerChains = new ArrayList<>();
            }
            handlerChains.add(handlerChain);
            return this;
        }

        public DefaultRequestMapper build() {
            return new DefaultRequestMapper(handlerChains);
        }
    }

}
