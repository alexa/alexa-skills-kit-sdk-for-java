/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.dispatcher;

import com.amazon.ask.exception.AskSdkException;

/**
 * Receives a request, dispatches to customer handling code, and returns a response
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public interface GenericRequestDispatcher<Input, Output> {

    /**
     * Dispatches an incoming request to the appropriate handling code and returns any output
     *
     * @param input input to the dispatcher
     * @return output optionally containing a response
     * @throws AskSdkException when an exception occurs during request processing
     */
    Output dispatch(Input input) throws AskSdkException;

}
