/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.exception.handler;

/**
 * A handler for handling one or more types of exceptions thrown during the request processing lifecycle.
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public interface GenericExceptionHandler<Input, Output> {

    /**
     * Returns true if this handler is capable of handling the current exception and/or state.
     *
     * @param input handler input
     * @param throwable exception
     * @return boolean
     */
    boolean canHandle(Input input, Throwable throwable);

    /**
     * Handles the exception.
     *
     * @param input handler input
     * @param throwable the exception that was originally thrown
     * @return handler output
     */
    Output handle(Input input, Throwable throwable);

}
