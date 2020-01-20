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

import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 *
 * This implementation accesses registered {@link com.amazon.ask.dispatcher.exception.ExceptionHandler} instances in the order they were provided.
 */
@Deprecated
public class DefaultExceptionMapper extends BaseExceptionMapper<HandlerInput, Optional<Response>> implements ExceptionMapper {

    /**
     * Constructor for DefaultExceptionMapper.
     * @param exceptionHandlers list of exception handlers.
     */
    protected DefaultExceptionMapper(final List<GenericExceptionHandler<HandlerInput, Optional<Response>>> exceptionHandlers) {
        super(exceptionHandlers);
    }

    /**
     * Static method to build an instance of Builder.
     * @return {@link com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * DefaultExceptionMapper Builder.
     */
    public static final class Builder extends BaseExceptionMapper.Builder<HandlerInput, Optional<Response>, Builder> {
        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Builder method to construct an instance of DefaultExceptionMapper.
         * @return {@link DefaultExceptionMapper}.
         */
        public DefaultExceptionMapper build() {
            exceptionHandlers = exceptionHandlers != null ? exceptionHandlers : Collections.emptyList();
            return new DefaultExceptionMapper(exceptionHandlers);
        }
    }

}
