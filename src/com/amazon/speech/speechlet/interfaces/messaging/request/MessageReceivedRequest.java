package com.amazon.speech.speechlet.interfaces.messaging.request;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Messaging.MessageReceived")
public class MessageReceivedRequest extends SpeechletRequest {
    private Map<String, Object> message;

    /**
     * Returns a new builder instance used to construct a new {@code MessageReceivedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code MessageReceivedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code MessageReceivedRequest}
     */
    private MessageReceivedRequest(final Builder builder) {
        super(builder);
        this.message = builder.message;
    }

    /**
     * Protected constructor used for JSON serialization and for extending this class.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     * @param message
     *            the message needs to be delivered
     */
    protected MessageReceivedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("message") final Map<String, Object> message) {
        super(requestId, timestamp, locale);
        this.message = message;
    }

    /**
     * Returns the message associated with this request.
     *
     * @return the message to be delivered
     */
    public Map<String, Object> getMessage() {
        return message;
    }

    /**
     * Builder used to construct a new {@code MessageReceivedRequest}.
     */
    public static final class Builder extends
            SpeechletRequest.SpeechletRequestBuilder<Builder, MessageReceivedRequest> {
        private Map<String, Object> message;

        private Builder() {
        }

        public Builder withMessage(final Map<String, Object> message) {
            this.message = message;
            return this;
        }

        @Override
        public MessageReceivedRequest build() {
            Validate.notNull(message, "Message must be defined");
            return new MessageReceivedRequest(this);
        }
    }
}
