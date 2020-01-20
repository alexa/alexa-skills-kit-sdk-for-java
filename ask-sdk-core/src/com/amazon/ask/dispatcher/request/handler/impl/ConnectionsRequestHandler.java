/*
* Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
* except in compliance with the License. A copy of the License is located at
*
* http://aws.amazon.com/apache2.0/
*
* or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
* the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.dispatcher.request.handler.impl;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.interfaces.connections.ConnectionsRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

/**
 * Request handler for ConnectionsRequest requests.
 */
public interface ConnectionsRequestHandler extends RequestHandler {

    /**
     * Returns true if the handler can dispatch the current request.
     *
     * @param input input to the request handler
     * @param connectionsRequest ConnectionsRequest request
     * @return true if the handler is capable of handling the current request and/or state
     */
    boolean canHandle(HandlerInput input, ConnectionsRequest connectionsRequest);

    /**
     * Handles the request.
     *
     * @param input input to the request handler
     * @param connectionsRequest ConnectionsRequest request
     * @return output from the handler.
     */
    Optional<Response> handle(HandlerInput input, ConnectionsRequest connectionsRequest);

    @Override
    default boolean canHandle(HandlerInput handlerInput) {
        if (handlerInput.getRequest() instanceof ConnectionsRequest) {
            return canHandle(handlerInput, (ConnectionsRequest) handlerInput.getRequest());
        }
        return false;
    }

    @Override
    default Optional<Response> handle(HandlerInput handlerInput) {
        return handle(handlerInput, (ConnectionsRequest) handlerInput.getRequest());
    }

}
