/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.exception.handler.impl;

import com.amazon.ask.util.ValidationUtils;

/**
 * Abstraction over HandlerInput. HandlerInput exposes incoming request object and context (for lambda backend).
 * @param <Request> Incoming request type.
 */
public abstract class AbstractHandlerInput<Request> {

    /**
     * Incoming request object.
     */
    protected final Request request;

    /**
     * Exposed by AWS Lambda.
     */
    protected final Object context;

    /**
     * Constructor for AbstractHandlerInput.
     * @param request incoming request object.
     * @param context context.
     */
    protected AbstractHandlerInput(final Request request, final Object context) {
        this.request = ValidationUtils.assertNotNull(request, "request");
        this.context = context;
    }

    /**
     * Getter for Request.
     * @return {@link Request}.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Getter for context.
     * @return context.
     */
    public Object getContext() {
        return context;
    }

    /**
     * Static builder class for AbstractHandlerInput.
     * @param <Request> incoming request object type.
     * @param <Self> of type Builder.
     */
    @SuppressWarnings("unchecked")
    protected abstract static class Builder<Request, Self extends Builder<Request, Self>> {
        /**
         * Incoming request object.
         */
        protected Request request;

        /**
         * Exposed by AWS Lambda.
         */
        protected Object context;

        /**
         * Add request object to HandlerInput.
         * @param request incoming request object.
         * @return {@link Builder}.
         */
        public Self withRequest(final Request request) {
            this.request = request;
            return (Self) this;
        }

        /**
         * Add context object to HandlerInput.
         * @param context context.
         * @return {@link Builder}.
         */
        public Self withContext(final Object context) {
            this.context = context;
            return (Self) this;
        }

        /**
         * Abstract method to build an instance of AbstractHandlerInput.
         * @return {@link AbstractHandlerInput}.
         */
        public abstract AbstractHandlerInput<Request> build();
    }

}
