/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.exception;

import com.amazon.ask.dispatcher.impl.DefaultRequestDispatcher;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;

import java.util.Optional;

/**
 * Used by the {@link DefaultRequestDispatcher} to act on unhandled exceptions encountered during request processing.
 * The exception mapper contains one or more exception handlers. Handlers are accessed through the mapper to attempt to
 * find a handler that is compatible with the current exception.
 */
@Deprecated
public interface ExceptionMapper extends GenericExceptionMapper<HandlerInput, Optional<Response>> { }
