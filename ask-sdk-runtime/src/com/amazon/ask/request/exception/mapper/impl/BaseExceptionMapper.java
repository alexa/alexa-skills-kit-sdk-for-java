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
 * {@inheritDoc}.
 *
 * @param <Input> handler input type.
 * @param <Output> handler output type.
 */
public class BaseExceptionMapper<Input, Output> implements GenericExceptionMapper<Input, Output> {

    /**
     * List of exception handlers.
     */
    protected final List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

    /**
     * Constructor of BaseExceptionMapper.
     * @param exceptionHandlers list of exception handlers.
     */
    protected BaseExceptionMapper(final List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : new ArrayList<>();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Optional<GenericExceptionHandler<Input, Output>> getHandler(final Input input, final Throwable ex) {
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

    /**
     * Returns an instance of Builder.
     * @param input class of type Input.
     * @param output class of type Output.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @param <Self> of type Builder.
     * @return {@link Builder}.
     */
    public static <Input, Output, Self extends Builder<Input, Output, Self>> Builder<Input, Output, Self> forTypes(
            final Class<Input> input, final Class<Output> output) {
        return new Builder<>();
    }

    /**
     * Returns an instance of Builder.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @return {@link Builder}.
     */
    public static <Input, Output> Builder<Input, Output, ?> builder() {
        return new Builder<>();
    }

    /**
     * Base Exception Mapper Builder.
     * @param <Input> Skill input type.
     * @param <Output> Skill output type.
     * @param <Self> of type Builder.
     */
    @SuppressWarnings("unchecked")
    public static class Builder<Input, Output, Self extends Builder<Input, Output, Self>> {
        /**
         * List of exception handlers.
         */
        protected List<GenericExceptionHandler<Input, Output>> exceptionHandlers;

        /**
         * Constructor for Builder class.
         */
        protected Builder() { }

        /**
         * List of exception handlers to use in the handler chain. Handlers will accessed in the order
         * determined by the list.
         *
         * @param exceptionHandlers list of exception handlers
         * @return {@link Builder}.
         */
        public Self withExceptionHandlers(final List<GenericExceptionHandler<Input, Output>> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return (Self) this;
        }

        /**
         * Adds an exception handler to the end of the handler chain.
         *
         * @param handler handler to add.
         * @return {@link Builder}.
         */
        public Self addExceptionHandler(final GenericExceptionHandler<Input, Output> handler) {
            if (exceptionHandlers == null) {
                this.exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(handler);
            return (Self) this;
        }

        /**
         * Builder method to build an instance of BaseExceptionMapper.
         * @return {@link BaseExceptionMapper}.
         */
        public GenericExceptionMapper<Input, Output> build() {
            return new BaseExceptionMapper<>(exceptionHandlers);
        }
    }

}
