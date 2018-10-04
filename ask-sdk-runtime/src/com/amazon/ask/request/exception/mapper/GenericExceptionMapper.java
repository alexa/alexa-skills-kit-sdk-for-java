/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.exception.mapper;

import com.amazon.ask.request.dispatcher.impl.BaseRequestDispatcher;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;

import java.util.Optional;

/**
 * Used by the {@link BaseRequestDispatcher} to act on unhandled exceptions encountered during request processing.
 * The exception mapper contains one or more exception handlers. Handlers are accessed through the mapper to attempt to
 * find a handler that is compatible with the current exception.
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public interface GenericExceptionMapper<Input, Output> {

    /**
     * Returns a suitable exception handler to dispatch the specified exception, if one exists.
     *
     * @param input handler input
     * @param throwable the exception that was originally thrown
     * @return optional exception handler if found, else an empty result
     */
    Optional<GenericExceptionHandler<Input, Output>> getHandler(Input input, Throwable throwable);

}
