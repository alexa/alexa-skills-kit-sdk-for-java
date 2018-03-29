/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.exception.impl;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class DefaultExceptionMapper implements ExceptionMapper {

    protected final List<ExceptionHandler> exceptionHandlers;

    protected DefaultExceptionMapper(List<ExceptionHandler> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Optional<ExceptionHandler> getHandler(HandlerInput handlerInput, Throwable ex) {
        //if it is a reflection exception, unwrap it to get the real cause
        Throwable exception = ex instanceof ReflectiveOperationException ? ex.getCause() : ex;

        //give the exception handlers a chance to dispatch this error
        for (ExceptionHandler exceptionHandler : exceptionHandlers) {
            if (exceptionHandler.canHandle(handlerInput, exception)) {
                return Optional.of(exceptionHandler);
            }
        }
        return Optional.empty();
    }

    public static final class Builder {
        private List<ExceptionHandler> exceptionHandlers;

        private Builder() {
        }

        /**
         * List of exception handlers to use in the handler chain. Handlers will accessed in the order
         * determined by the list.
         *
         * @param exceptionHandlers list of exception handlers
         * @return builder
         */
        public Builder withExceptionHandlers(List<ExceptionHandler> exceptionHandlers) {
            this.exceptionHandlers = exceptionHandlers;
            return this;
        }

        /**
         * Adds an exception handler to the end of the handler chain.
         *
         * @param handler handler to add
         * @return builder
         */
        public Builder addExceptionHandler(ExceptionHandler handler) {
            if (exceptionHandlers == null) {
                this.exceptionHandlers = new ArrayList<>();
            }
            exceptionHandlers.add(handler);
            return this;
        }

        public DefaultExceptionMapper build() {
            exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : Collections.emptyList();
            return new DefaultExceptionMapper(exceptionHandlers);
        }
    }

}
