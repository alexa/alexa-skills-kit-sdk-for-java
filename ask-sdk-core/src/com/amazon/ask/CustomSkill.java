package com.amazon.ask;

import com.amazon.ask.impl.AbstractSkill;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.request.UnmarshalledRequest;
import com.amazon.ask.request.dispatcher.GenericRequestDispatcher;
import com.amazon.ask.request.dispatcher.impl.BaseRequestDispatcher;
import com.amazon.ask.request.impl.BaseUnmarshalledRequest;
import com.amazon.ask.util.JacksonSerializer;
import com.amazon.ask.util.impl.JacksonJsonMarshaller;
import com.amazon.ask.util.impl.JacksonJsonUnmarshaller;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.builder.CustomSkillConfiguration;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiConfiguration;
import com.amazon.ask.model.services.DefaultApiConfiguration;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.util.SdkConstants;
import com.amazon.ask.util.UserAgentUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class CustomSkill extends AbstractSkill<RequestEnvelope, ResponseEnvelope> implements AlexaSkill<RequestEnvelope, ResponseEnvelope> {

    protected final GenericRequestDispatcher<HandlerInput, Optional<Response>> requestDispatcher;
    protected final PersistenceAdapter persistenceAdapter;
    protected final ApiClient apiClient;
    protected final Serializer serializer;
    protected final String customUserAgent;
    protected final String skillId;

    public CustomSkill(CustomSkillConfiguration configuration) {
        super(JacksonJsonUnmarshaller.withTypeBinding(RequestEnvelope.class, "request"),
                JacksonJsonMarshaller.forType(ResponseEnvelope.class));
        this.persistenceAdapter = configuration.getPersistenceAdapter();
        this.requestDispatcher = BaseRequestDispatcher.<HandlerInput, Optional<Response>>builder()
                .withRequestMappers(configuration.getRequestMappers())
                .withHandlerAdapters(configuration.getHandlerAdapters())
                .withExceptionMapper(configuration.getExceptionMapper())
                .withRequestInterceptors(configuration.getRequestInterceptors())
                .withResponseInterceptors(configuration.getResponseInterceptors())
                .build();
        this.apiClient = configuration.getApiClient();
        this.serializer = new JacksonSerializer();
        this.customUserAgent = configuration.getCustomUserAgent();
        this.skillId = configuration.getSkillId();
    }

    /**
     * @deprecated
     */
    public ResponseEnvelope invoke(RequestEnvelope requestEnvelope) {
        return invoke(requestEnvelope, null);
    }

    /**
     * @deprecated
     */
    public ResponseEnvelope invoke(RequestEnvelope requestEnvelope, Object context) {
        return invoke(new BaseUnmarshalledRequest<>(requestEnvelope, null), context);
    }

    /**
     * Invokes the dispatcher to handler the request envelope and construct the handler input
     * @param unmarshalledRequest unmarshalled output from {@link JacksonJsonUnmarshaller}, containing a
     *                            {@link RequestEnvelope} and a JSON representation of the request.
     * @param context context
     * @return optional request envelope
     */
    protected ResponseEnvelope invoke(UnmarshalledRequest<RequestEnvelope> unmarshalledRequest, Object context) {
        RequestEnvelope requestEnvelope = unmarshalledRequest.getUnmarshalledRequest();
        JsonNode requestEnvelopeJson = unmarshalledRequest.getRequestJson();

        if (skillId != null && !requestEnvelope.getContext().getSystem().getApplication().getApplicationId().equals(skillId)) {
            throw new AskSdkException("AlexaSkill ID verification failed.");
        }

        ServiceClientFactory factory = apiClient != null ? ServiceClientFactory.builder()
                .withDefaultApiConfiguration(getApiConfiguration(requestEnvelope))
                .build() : null;

        HandlerInput handlerInput = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .withContext(context)
                .withServiceClientFactory(factory)
                .withRequestEnvelopeJson(requestEnvelopeJson)
                .build();

        Optional<Response> response = requestDispatcher.dispatch(handlerInput);
        return ResponseEnvelope.builder()
                .withResponse(response != null ? response.orElse(null) : null)
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
