/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiConfiguration;
import com.amazon.ask.model.services.DefaultApiConfiguration;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.builder.SkillConfiguration;
import com.amazon.ask.dispatcher.impl.DefaultRequestDispatcher;
import com.amazon.ask.dispatcher.RequestDispatcher;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.util.JacksonSerializer;
import com.amazon.ask.util.SdkConstants;
import com.amazon.ask.util.UserAgentUtils;

import java.util.Optional;

/**
 * Top level container for request dispatcher which provides multiple builders for easy configuration
 */
public class Skill {

    protected final RequestDispatcher requestDispatcher;
    protected final PersistenceAdapter persistenceAdapter;
    protected final ApiClient apiClient;
    protected final Serializer serializer;
    protected final String customUserAgent;
    protected final String skillId;

    public Skill(SkillConfiguration config) {
        this.persistenceAdapter = config.getPersistenceAdapter();
        this.requestDispatcher = DefaultRequestDispatcher.builder()
                .withRequestMappers(config.getRequestMappers())
                .withExceptionMapper(config.getExceptionMapper())
                .withHandlerAdapters(config.getHandlerAdapters())
                .withRequestInterceptors(config.getRequestInterceptors())
                .withResponseInterceptors(config.getResponseInterceptors())
                .build();
        this.apiClient = config.getApiClient();
        this.serializer = new JacksonSerializer();
        this.customUserAgent = config.getCustomUserAgent();
        this.skillId = config.getSkillId();
    }

    public ResponseEnvelope invoke(RequestEnvelope requestEnvelope) {
        return invoke(requestEnvelope, null);
    }

    /**
     * Invokes the dispatcher to handler the request envelope and construct the handler input
     * @param requestEnvelope request envelope
     * @param context context
     * @return optional request envelope
     */
    public ResponseEnvelope invoke(RequestEnvelope requestEnvelope, Object context) {
        if (skillId != null && !requestEnvelope.getContext().getSystem().getApplication().getApplicationId().equals(skillId)) {
            throw new AskSdkException("Skill ID verification failed.");
        }

        ServiceClientFactory factory = apiClient != null ? ServiceClientFactory.builder()
                .withDefaultApiConfiguration(getApiConfiguration(requestEnvelope))
                .build() : null;

        HandlerInput handlerInput = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .withContext(context)
                .withServiceClientFactory(factory)
                .build();

        Optional<Response> response = requestDispatcher.dispatch(handlerInput);
        return ResponseEnvelope.builder()
                .withResponse(response.orElse(null))
                .withSessionAttributes
                        (requestEnvelope.getSession() != null ? handlerInput.getAttributesManager().getSessionAttributes() : null)
                .withVersion(SdkConstants.FORMAT_VERSION)
                .withUserAgent(UserAgentUtils.getUserAgent(customUserAgent))
                .build();
    }

    protected ApiConfiguration getApiConfiguration(RequestEnvelope requestEnvelope) {
        String apiEndpoint = requestEnvelope.getContext().getSystem().getApiEndpoint();
        String apiToken = requestEnvelope.getContext().getSystem().getApiAccessToken();
        return DefaultApiConfiguration.builder()
                .withApiClient(apiClient)
                .withApiEndpoint(apiEndpoint)
                .withAuthorizationValue(apiToken)
                .withSerializer(serializer)
                .build();
        }

}
