/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.interceptor;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;

/**
 * Request interceptors are invoked immediately prior to execution of the request handler for an incoming request.
 * Request interceptors can pass data and entities on to request handlers via the use of request attributes.
 */
public interface RequestInterceptor {

    /**
     * Intercept the incoming request before the request handler is executed.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     */
    void process(HandlerInput input);

}
