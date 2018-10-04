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

import com.amazon.ask.request.exception.handler.impl.AbstractHandlerInput;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.response.ResponseBuilder;

import java.util.function.Predicate;

/**
 * Input that is passed to all {@link RequestHandler}, {@link com.amazon.ask.dispatcher.exception.ExceptionHandler},
 * {@link com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor}, and {@link com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor}.
 *
 * Contains the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
 * {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
 * and other useful utilities.
 */
public class HandlerInput extends AbstractHandlerInput<Request> {

    protected final RequestEnvelope requestEnvelope;
    protected final AttributesManager attributesManager;
    protected final ServiceClientFactory serviceClientFactory;
    protected final ResponseBuilder responseBuilder;

    protected HandlerInput(RequestEnvelope requestEnvelope, PersistenceAdapter persistenceAdapter,
                           Object context, ServiceClientFactory serviceClientFactory) {
        super(requestEnvelope.getRequest(), context);
        this.requestEnvelope = requestEnvelope;
        this.serviceClientFactory = serviceClientFactory;
        this.attributesManager = AttributesManager.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .build();
        this.responseBuilder = new ResponseBuilder();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the {@link RequestEnvelope} of the incoming request.
     *
     * @return request envelope
     */
    public RequestEnvelope getRequestEnvelope() {
        return requestEnvelope;
    }

    /**
     * Returns an {@link AttributesManager} which can be used to retrieve and store skill attributes.
     *
     * @return attributes manager
     */
    public AttributesManager getAttributesManager() {
        return attributesManager;
    }

    /**
     * Returns a {@link ServiceClientFactory} used to retrieve service client instances that can call Alexa APIs.
     *
     * @return service client factory
     * @throws IllegalStateException if this method is called when an {@link com.amazon.ask.model.services.ApiClient} is
     * not configured on this SDK instance.
     */
    public ServiceClientFactory getServiceClientFactory() {
        if (serviceClientFactory == null) {
            throw new IllegalStateException("Attempting to use service client factory with no configured API client");
        }
        return serviceClientFactory;
    }

    /**
     * Evaluates a {@link Predicate} against the current handler input state.
     *
     * @param predicate predicate to evaluate
     * @return true if the predicates matches this input, false if not
     */
    public boolean matches(Predicate<HandlerInput> predicate) { return predicate.test(this); }

    /**
     * Returns a {@link ResponseBuilder} that can be used to construct a complete skill response containing speech,
     * directives, etc.
     *
     * @return response builder
     */
    public ResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }

    public static final class Builder extends AbstractHandlerInput.Builder<Request, Builder> {
        private RequestEnvelope requestEnvelope;
        private PersistenceAdapter persistenceAdapter;
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

        public Builder withServiceClientFactory(ServiceClientFactory serviceClientFactory) {
            this.serviceClientFactory = serviceClientFactory;
            return this;
        }

        public HandlerInput build() {
            return new HandlerInput(requestEnvelope, persistenceAdapter, context, serviceClientFactory);
        }
    }

}
