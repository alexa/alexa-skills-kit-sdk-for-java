/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler;

import com.amazon.ask.model.Response;

import java.util.Optional;

/**
 * Request handlers are responsible for handling one or more types of incoming requests.
 */
public interface RequestHandler {

    /**
     * Returns true if the handler is capable of handling the current request and/or state
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @return true if the handler can handle the current request and/or state
     */
    boolean canHandle(HandlerInput input);

    /**
     * Handles the request. A {@link Response} should be returned by this method when processing an in-session request,
     * but may be omitted for certain out of session requests.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @return an {@link Optional} that may contain a {@link Response} to be sent back by the skill
     */
    Optional<Response> handle(HandlerInput input);

}
