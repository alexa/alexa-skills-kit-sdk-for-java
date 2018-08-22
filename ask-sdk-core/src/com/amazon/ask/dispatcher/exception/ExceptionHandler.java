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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import java.util.Optional;

/**
 * A handler for handling one or more types of exceptions thrown during the request processing lifecycle.
 */
public interface ExceptionHandler {

    /**
     * Returns true if this handler is capable of handling the current exception and/or state.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @param throwable exception
     * @return true if this handler can handle the current exception and/or state
     */
    boolean canHandle(HandlerInput input, Throwable throwable);

    /**
     * Handles the exception. A {@link Response} can optionally be returned to give the user a graceful error response.
     * If no response is provided, the SDK will throw the original exception back to the container, resulting in an
     * unhandled exception response to the end user.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @param throwable the exception that was originally thrown
     * @return an {@link Optional} that may contain an error {@link Response} to be sent back by the skill
     */
    Optional<Response> handle(HandlerInput input, Throwable throwable);

}
