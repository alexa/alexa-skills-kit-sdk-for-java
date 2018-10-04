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

public abstract class AbstractHandlerInput<Request> {

    protected final Request request;
    protected final Object context;

    protected AbstractHandlerInput(Request request, Object context) {
        this.request = ValidationUtils.assertNotNull(request, "request");
        this.context = context;
    }

    public Request getRequest() {
        return request;
    }

    public Object getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    protected abstract static class Builder<Request, Self extends Builder<Request, Self>> {
        protected Request request;
        protected Object context;

        public Self withRequest(Request request) {
            this.request = request;
            return (Self)this;
        }

        public Self withContext(Object context) {
            this.context = context;
            return (Self)this;
        }

        public abstract AbstractHandlerInput<Request> build();
    }

}
