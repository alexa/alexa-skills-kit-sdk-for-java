/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.mapper;

import com.amazon.ask.request.handler.chain.GenericRequestHandlerChain;

import java.util.Optional;

/**
 * The request mapper is responsible for mapping an incoming request to a {@link GenericRequestHandlerChain} containing
 * a handler suitable for processing the request.
 *
 * @param <Input> handler input type
 * @param <Output> handler output type
 */
public interface GenericRequestMapper<Input, Output> {

    /**
     * Finds a {@link GenericRequestHandlerChain} containing a request handler that is suitable for the incoming request.
     *
     * @param input handler input
     * @return an {@link Optional} containing the request handler chain if one is located, or empty if not.
     */
    Optional<GenericRequestHandlerChain<Input, Output>> getRequestHandlerChain(Input input);

}
