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

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.response.ResponseBuilder;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.util.ValidationUtils;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Input to request handler.
 */
public class HandlerInput {

    protected final RequestEnvelope requestEnvelope;
    protected final Object context;
    protected final AttributesManager attributesManager;
    protected final ServiceClientFactory serviceClientFactory;
    protected final ResponseBuilder responseBuilder;

    protected HandlerInput(RequestEnvelope requestEnvelope, PersistenceAdapter persistenceAdapter,
                           Object context, ServiceClientFactory serviceClientFactory) {
        this.requestEnvelope = ValidationUtils.assertNotNull(requestEnvelope, "requestEnvelope");
        this.serviceClientFactory = serviceClientFactory;
        this.context = context;

        this.attributesManager = AttributesManager.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .build();
        this.responseBuilder = new ResponseBuilder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public RequestEnvelope getRequestEnvelope() {
        return requestEnvelope;
    }

    public AttributesManager getAttributesManager() {
        return attributesManager;
    }

    public Optional<Object> getContext() {
        return Optional.ofNullable(context);
    }

    public ServiceClientFactory getServiceClientFactory() {
        if (serviceClientFactory == null) {
            throw new IllegalStateException("Attempting to use service client factory with no configured API client");
        }
        return serviceClientFactory;
    }

    public boolean matches(Predicate<HandlerInput> predicate) { return predicate.test(this); }

    public ResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }

    public static final class Builder {
        private RequestEnvelope requestEnvelope;
        private PersistenceAdapter persistenceAdapter;
        private Object context;
        private ServiceClientFactory serviceClientFactory;

        private Builder() {
        }

        public Builder withRequestEnvelope(RequestEnvelope requestEnvelope) {
            this.requestEnvelope = requestEnvelope;
            return this;
        }

        public Builder withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        public Builder withContext(Object context) {
            this.context = context;
            return this;
        }

        public Builder withServiceClientFactory(ServiceClientFactory serviceClientFactory) {
            this.serviceClientFactory = serviceClientFactory;
            return this;
        }

        public HandlerInput build() {
            return new HandlerInput(requestEnvelope, persistenceAdapter, context, serviceClientFactory);
        }
    }

}
