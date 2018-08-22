/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.Response;

import java.util.Optional;

/**
 * Receives a request, dispatches to customer handling code, and optionally returns a {@link Response}.
 */
public interface RequestDispatcher {

    /**
     * Dispatches an incoming request to the appropriate handling code and returns any output
     *
     * @param input handler input containing the {@link com.amazon.ask.model.Request}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @return output from handlers, which may contain a {@link Response}
     * @throws AskSdkException when an exception occurs during request processing
     */
    Optional<Response> dispatch(HandlerInput input) throws AskSdkException;

}
