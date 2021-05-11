package com.amazon.ask;

import com.amazon.ask.impl.AbstractSkill;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.model.utils.SubTypesManifest;
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
import com.amazon.ask.response.template.TemplateFactory;
import com.amazon.ask.util.SdkConstants;
import com.amazon.ask.util.UserAgentUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Custom type Alexa Skill.
 */
public class CustomSkill extends AbstractSkill<RequestEnvelope, ResponseEnvelope> implements AlexaSkill<RequestEnvelope, ResponseEnvelope> {

    /**
     * Logger instance to log information for debugging purposes.
     */
    private static final Logger LOGGER = getLogger(CustomSkill.class);

    /**
     * Request Dispatcher.
     */
    protected final GenericRequestDispatcher<HandlerInput, Optional<Response>> requestDispatcher;

    /**
     * store skill attributes to a persistence layer.
     */
    protected final PersistenceAdapter persistenceAdapter;

    /**
     * Client to make external API calls.
     */
    protected final ApiClient apiClient;

    /**
     * Serializer for marshalling and un-marshalling purposes.
     */
    protected final Serializer serializer;

    /**
     * Custom user agent.
     */
    protected final String customUserAgent;

    /**
     * Unique ID associated with a Skill.
     */
    protected final String skillId;

    /**
     * Interface to process template and data to generate skill response.
     */
    protected final TemplateFactory<HandlerInput, Response> templateFactory;

    /**
     * Constructor for CustomSkill.
     * @param configuration custom skill configuration.
     */
    public CustomSkill(final CustomSkillConfiguration configuration) {
        super(JacksonJsonUnmarshaller.withTypeBinding(RequestEnvelope.class, Arrays.asList("request", "type"),
                SubTypesManifest.getSubType(Request.class)),
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
        this.templateFactory = configuration.getTemplateFactory();
    }

    /**
     * {@inheritDoc}.
     */
    public ResponseEnvelope invoke(final RequestEnvelope requestEnvelope) {
        return invoke(requestEnvelope, null);
    }

    /**
     * {@inheritDoc}.
     */
    public ResponseEnvelope invoke(final RequestEnvelope requestEnvelope, final Object context) {
        return invoke(new BaseUnmarshalledRequest<>(requestEnvelope, null), context);
    }

    /**
     * Invokes the dispatcher to handler the request envelope and construct the handler input.
     * @param unmarshalledRequest unmarshalled output from {@link JacksonJsonUnmarshaller}, containing a
     *                            {@link RequestEnvelope} and a JSON representation of the request.
     * @param context context
     * @return optional request envelope
     */
    protected ResponseEnvelope invoke(final UnmarshalledRequest<RequestEnvelope> unmarshalledRequest, final Object context) {
        RequestEnvelope requestEnvelope = unmarshalledRequest.getUnmarshalledRequest();
        JsonNode requestEnvelopeJson = unmarshalledRequest.getRequestJson();

        if (skillId != null && !requestEnvelope.getContext().getSystem().getApplication().getApplicationId().equals(skillId)) {
            LOGGER.debug("AlexaSkill ID verification failed.");
            return null;
        }

        ServiceClientFactory serviceClientFactory = apiClient != null ? ServiceClientFactory.builder()
                .withDefaultApiConfiguration(getApiConfiguration(requestEnvelope))
                .build() : null;

        HandlerInput handlerInput = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .withPersistenceAdapter(persistenceAdapter)
                .withContext(context)
                .withRequestEnvelopeJson(requestEnvelopeJson)
                .withServiceClientFactory(serviceClientFactory)
                .withTemplateFactory(templateFactory)
                .build();

        Optional<Response> response = requestDispatcher.dispatch(handlerInput);
        return ResponseEnvelope.builder()
                .withResponse(response.orElse(null))
                .withSessionAttributes(requestEnvelope.getSession() != null ? handlerInput.getAttributesManager().getSessionAttributes() : null)
                .withVersion(SdkConstants.FORMAT_VERSION)
                .withUserAgent(UserAgentUtils.getUserAgent(customUserAgent))
                .build();
    }

    /**
     * Getter for Api configuration.
     * @param requestEnvelope request envelope.
     * @return {@link ApiConfiguration}.
     */
    protected ApiConfiguration getApiConfiguration(final RequestEnvelope requestEnvelope) {
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
