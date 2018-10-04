/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.exception.mapper.impl;

import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class BaseExceptionMapper<Input, Output> implements GenericExceptionMapper<Input, Output> {

    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

    protected BaseExceptionMapper(List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : new ArrayList<>();
    }

    @Override
    public Optional<GenericExceptionHandler<Input, Output>> getHandler(Input input, Throwable ex) {
        //if it is a reflection exception, unwrap it to get the real cause
        Throwable exception = ex instanceof ReflectiveOperationException ? ex.getCause() : ex;

        //give the exception handlers a chance to dispatch this error
        for (GenericExceptionHandler<Input, Output> exceptionHandler : exceptionHandlers) {
            if (exceptionHandler.canHandle(input, exception)) {
                return Optional.of(exceptionHandler);
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
        protected List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

        protected Builder() {
        }

        /**
         * List of exception handlers to use in the handler chain. Handlers will accessed in the order
         * determined by the list.
         *
         * @param exceptionHandlers list of exception handlers
         * @return builder
         */
        public Self withExceptionHandlers(List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return (Self) this;
        }

        /**
         * Adds an exception handler to the end of the handler chain.
         *
         * @param handler handler to add
         * @return builder
         */
        public Self addExceptionHandler(GenericExceptionHandler<Input, Output> handler) {
            if (exceptionHandlers == null) {
                this.exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(handler);
            return (Self) this;
        }

        public GenericExceptionMapper<Input, Output> build() {
            return new BaseExceptionMapper<>(exceptionHandlers);
        }
    }

}
