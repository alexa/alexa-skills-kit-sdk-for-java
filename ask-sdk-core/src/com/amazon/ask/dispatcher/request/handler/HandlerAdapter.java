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
 * An adapter that allows a {@link com.amazon.ask.dispatcher.RequestDispatcher} to invoke a type of request handler.
 */
public interface HandlerAdapter {

    /**
     * Returns true if the adapter supports the type of handler.
     *
     * @param handler request handler
     * @return true if the adapter supports the type of handler
     */
    boolean supports(Object handler);

    /**
     * Executes the request handler with the supplied input.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @param handler request handler
     * @return result of executing the request handler, optionally containing a {@link Response}
     */
    Optional<Response> execute(HandlerInput input, Object handler);

}
