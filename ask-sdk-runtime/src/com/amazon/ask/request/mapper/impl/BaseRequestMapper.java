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
 */
public class BaseRequestMapper<Input, Output> implements GenericRequestMapper<Input, Output> {

    protected final List<BaseRequestHandlerChain<Input, Output>> handlerChains;

    protected BaseRequestMapper(List<BaseRequestHandlerChain<Input, Output>> handlerChains) {
        this.handlerChains = ValidationUtils.assertNotEmpty(handlerChains, "handlerChains");
    }

    @Override
    public Optional<GenericRequestHandlerChain<Input, Output>> getRequestHandlerChain(Input input) {
        for (BaseRequestHandlerChain<Input, Output> handlerChain : handlerChains) {
            if (handlerChain.getRequestHandler().canHandle(input)) {
                return Optional.of(handlerChain);
            }
        }
        return Optional.empty();
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
        protected List<BaseRequestHandlerChain<Input, Output>> handlerChains;

        protected Builder() {
        }

        public Self withRequestHandlerChains(List<BaseRequestHandlerChain<Input, Output>> handlerChains) {
            this.handlerChains = handlerChains;
            return (Self) this;
        }

        public Self addRequestHandlerChain(BaseRequestHandlerChain<Input, Output> handlerChain) {
            if (handlerChains == null) {
                handlerChains = new ArrayList<>();
            }
            handlerChains.add(handlerChain);
            return (Self) this;
        }

        public GenericRequestMapper<Input, Output> build() {
            return new BaseRequestMapper<>(handlerChains);
        }
    }

}
