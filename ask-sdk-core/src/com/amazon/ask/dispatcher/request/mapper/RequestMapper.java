/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.mapper;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;

import java.util.Optional;

/**
 * The request mapper is responsible for mapping an incoming request to a {@link RequestHandlerChain} containing
 * a handler suitable for processing the request.
 */
public interface RequestMapper {

    /**
     * Finds a {@link RequestHandlerChain} containing a request handler that is suitable for the incoming request.
     * An {@link Optional} empty is returned if no such handler can be located.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @return an {@link Optional} containing the request handler chain if one is located, or empty if not.
     */
    Optional<RequestHandlerChain> getRequestHandlerChain(HandlerInput input);

}
