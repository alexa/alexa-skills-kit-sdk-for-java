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
 * Routes the request to appropriate controller
 */
public interface RequestMapper {

    /**
     * Routes the request to appropriate controller and retrieves request handler chains
     *
     * @param input input containing request envelope and other context
     * @return optional request handler chain
     */
    Optional<RequestHandlerChain> getRequestHandlerChain(HandlerInput input);

}
