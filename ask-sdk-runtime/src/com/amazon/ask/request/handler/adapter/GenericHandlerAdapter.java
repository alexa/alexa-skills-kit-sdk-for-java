/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.handler.adapter;

/**
 * An adapter to execute a type of request handler.
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public interface GenericHandlerAdapter<Input, Output> {

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
     * @param input input to the request handler.
     *
     * @param handler request handler
     * @return result of executing the request handler
     */
    Output execute(Input input, Object handler);


}
