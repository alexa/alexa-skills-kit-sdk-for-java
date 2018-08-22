/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;

import java.util.List;

/**
 * A handler chain contains a request handler and any associated request/response interceptors and exception handlers.
 */
public interface RequestHandlerChain {

    /**
     * Returns the request handler
     *
     * @return request handler
     */
    Object getRequestHandler();

    /**
     * @return list of chain-level request interceptors
     */
    List<RequestInterceptor> getRequestInterceptors();

    /**
     * @return list of chain-level response interceptors
     */
    List<ResponseInterceptor> getResponseInterceptors();

    /**
     * @return list of chain-level exception handlers
     */
    List<ExceptionHandler> getExceptionHandlers();

}
