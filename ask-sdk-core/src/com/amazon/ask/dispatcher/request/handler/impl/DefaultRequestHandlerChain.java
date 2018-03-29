/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler.impl;

import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;

import java.util.List;

/**
 * Default implementation of {@link RequestHandlerChain}
 */
public class DefaultRequestHandlerChain extends GenericRequestHandlerChain {

    protected DefaultRequestHandlerChain(RequestHandler handler, List<RequestInterceptor> requestInterceptors,
                                         List<ResponseInterceptor> responseInterceptors) {
        super(handler, requestInterceptors, responseInterceptors);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public RequestHandler getRequestHandler() {
        return (RequestHandler)handler;
    }

    public static final class Builder extends GenericRequestHandlerChain.Builder<Builder> {
        private RequestHandler handler;

        private Builder() {
        }

        public Builder withRequestHandler(RequestHandler handler) {
            this.handler = handler;
            return this;
        }

        public DefaultRequestHandlerChain build() {
            return new DefaultRequestHandlerChain(handler, requestInterceptors, responseInterceptors);
        }
    }
}
