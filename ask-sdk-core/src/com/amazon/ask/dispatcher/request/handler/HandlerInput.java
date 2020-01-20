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

import com.amazon.ask.exception.template.TemplateFactoryException;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.exception.handler.impl.AbstractHandlerInput;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.response.ResponseBuilder;
import com.amazon.ask.util.ValidationUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.amazon.ask.response.template.TemplateFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Input that is passed to all {@link RequestHandler}, {@link com.amazon.ask.dispatcher.exception.ExceptionHandler},
 * {@link com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor},
 * and {@link com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor}.
 *
 * Contains the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
 * {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
 * and other useful utilities.
 */
public class HandlerInput extends AbstractHandlerInput<Request> {

    /**
     * Request Envelope.
     */
    protected final RequestEnvelope requestEnvelope;

    /**
     * Manager for all skill attributes.
     */
    protected final AttributesManager attributesManager;

    /**
     * Factory class to vend out various service clients.
     */
    protected final ServiceClientFactory serviceClientFactory;

    /**
     * Response Builder.
     */
    protected final ResponseBuilder responseBuilder;

    /**
     * Request envelope in JSON format.
     */
    protected final JsonNode requestEnvelopeJson;

    /**
     * Interface to process template and data to generate skill response.
     */
    protected final TemplateFactory<HandlerInput, Response> templateFactory;

    /**
     * Constructor for HandlerInput.
     * @param requestEnvelope Request Envelope.
     * @param persistenceAdapter Store skill attributes to a persistence layer.
     * @param context object passed in when using AWS Lambda to host Skill backend code.
     * @param serviceClientFactory Factory class to vend out various service clients.
     * @param requestEnvelopeJson Request envelope in JSON format.
     * @param templateFactory Interface to process template and data to generate skill response.
     */
    protected HandlerInput(final RequestEnvelope requestEnvelope, final PersistenceAdapter persistenceAdapter,
                           final Object context, final ServiceClientFactory serviceClientFactory,
                           final JsonNode requestEnvelopeJson, final TemplateFactory<HandlerInput, Response> templateFactory) {
        super(ValidationUtils.assertNotNull(requestEnvelope, "request envelope").getRequest(), context);
        this.requestEnvelope = requestEnvelope;
        this.serviceClientFactory = serviceClientFactory;
        this.attributesManager = AttributesManager.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .build();
        this.responseBuilder = new ResponseBuilder();
        this.requestEnvelopeJson = requestEnvelopeJson;
        this.templateFactory = templateFactory;
    }

    /**
     * Static method to build an instance of Builder.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Generate {@link Response} using skill response template and injecting data.
     * Response template contains response components including but not limited to
     * {@link com.amazon.ask.model.ui.OutputSpeech}, {@link com.amazon.ask.model.ui.Card}, {@link com.amazon.ask.model.Directive}
     * and {@link com.amazon.ask.model.canfulfill.CanFulfillIntent} and placeholders for injecting data.
     * Injecting data provides component values to be injected into template.
     *
     * @param responseTemplateName name of response template
     * @param dataMap a map that contains injecting data
     * @return skill response
     * @throws TemplateFactoryException if fail to load or render template using provided
     * {@link com.amazon.ask.response.template.loader.TemplateLoader}
     * or {@link com.amazon.ask.response.template.renderer.TemplateRenderer}
     */
    public Optional<Response> generateTemplateResponse(final String responseTemplateName, final Map<String, Object> dataMap)
            throws TemplateFactoryException {
        return Optional.of(templateFactory.processTemplate(responseTemplateName, dataMap, this));
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
     * Returns a {@link JsonNode} representation of the incoming Request Envelope.
     *
     * @return JSON request envelope representation
     */
    public JsonNode getRequestEnvelopeJson() {
        return requestEnvelopeJson;
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
    public boolean matches(final Predicate<HandlerInput> predicate) {
        return predicate.test(this);
    }

    /**
     * Returns a {@link ResponseBuilder} that can be used to construct a complete skill response containing speech,
     * directives, etc.
     *
     * @return response builder
     */
    public ResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }

    /**
     * HandlerInput Builder.
     */
    public static final class Builder extends AbstractHandlerInput.Builder<Request, Builder> {

        /**
         * Request Envelope.
         */
        private RequestEnvelope requestEnvelope;

        /**
         * Store skill attributes to a persistence layer.
         */
        private PersistenceAdapter persistenceAdapter;

        /**
         * Factory class to vend out various service clients.
         */
        private ServiceClientFactory serviceClientFactory;

        /**
         * Request envelope in JSON format.
         */
        private JsonNode requestEnvelopeJson;

        /**
         * Interface to process template and data to generate skill response.
         */
        private TemplateFactory templateFactory;

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Adds Request Envelope to HandlerInput.
         * @param requestEnvelope Request Envelope.
         * @return {@link Builder}.
         */
        public Builder withRequestEnvelope(final RequestEnvelope requestEnvelope) {
            this.requestEnvelope = requestEnvelope;
            return this;
        }

        /**
         * Adds Persistence Adapter to HandlerInput.
         * @param persistenceAdapter Persistence Adapter.
         * @return {@link Builder}.
         */
        public Builder withPersistenceAdapter(final PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        /**
         * Adds Service Client Factory to HandlerInput.
         * @param serviceClientFactory Service Client Factory.
         * @return {@link Builder}.
         */
        public Builder withServiceClientFactory(final ServiceClientFactory serviceClientFactory) {
            this.serviceClientFactory = serviceClientFactory;
            return this;
        }

        /**
         * Adds Request Envelope Json to HandlerInput.
         * @param requestEnvelopeJson Request Envelope Json.
         * @return {@link Builder}.
         */
        public Builder withRequestEnvelopeJson(final JsonNode requestEnvelopeJson) {
            this.requestEnvelopeJson = requestEnvelopeJson;
            return this;
        }

        /**
         * Adds Template factory to HandlerInput.
         * @param templateFactory Template factory.
         * @return {@link Builder}.
         */
        public Builder withTemplateFactory(final TemplateFactory templateFactory) {
            this.templateFactory = templateFactory;
            return this;
        }

        /**
         * Builder method to build an instance of HandlerInput with the provided data.
         * @return {@link HandlerInput}.
         */
        public HandlerInput build() {
            return new HandlerInput(requestEnvelope, persistenceAdapter, context, serviceClientFactory, requestEnvelopeJson, templateFactory);
        }
    }

}
