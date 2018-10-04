/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.handler;

/**
 * Request handlers are responsible for handling one or more types of incoming requests.
 */
public interface GenericRequestHandler<Input, Output> {

    /**
     * Returns true if the handler can dispatch the current request
     *
     * @param input input to the request handler
     * @return true if the handler is capable of handling the current request and/or state
     */
    boolean canHandle(Input input);

    /**
     * Handles the request.
     *
     * @param input input to the request handler
     * @return output from the handler.
     */
    Output handle(Input input);


}
