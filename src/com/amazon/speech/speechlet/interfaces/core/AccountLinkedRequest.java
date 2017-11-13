package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillAccountLinked")
public class AccountLinkedRequest extends AlexaSkillEventRequest {
    private final AccountLinkedBody body;
    private final Date eventCreationTime;
    private final Date eventPublishingTime;

    public static Builder builder() {
        return new Builder();
    }

    private AccountLinkedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
        this.eventCreationTime = builder.eventCreationTime;
        this.eventPublishingTime = builder.eventPublishingTime;
    }

    private AccountLinkedRequest(@JsonProperty("requestId") final String requestId,
                                 @JsonProperty("timestamp") final Date timestamp,
                                 @JsonProperty("locale") final Locale locale,
                                 @JsonProperty("body") final AccountLinkedBody body,
                                 @JsonProperty("eventCreationTime") final Date eventCreationTime,
                                 @JsonProperty("eventPublishingTime") final Date eventPublishingTime) {
        super(requestId, timestamp, locale);
        this.body = body;
        this.eventCreationTime = eventCreationTime;
        this.eventPublishingTime = eventPublishingTime;
    }

    public String getAccessToken() {
        return this.body.getAccessToken();
    }

    public Date getEventCreationTime() {
        return new Date(eventCreationTime.getTime());
    }

    public Date getEventPublishingTime() {
        return new Date(eventPublishingTime.getTime());
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, AccountLinkedRequest> {
        private AccountLinkedBody body;
        private Date eventCreationTime;
        private Date eventPublishingTime;

        private  Builder() {

        }

        public Builder withAccountLinkedBody(final AccountLinkedBody body) {
            this.body = body;
            return this;
        }

        public Builder withEventCreationTime(final Date eventCreationTime) {
            this.eventCreationTime = new Date(eventCreationTime.getTime());
            return this;
        }

        public Builder withEventPublishingTime(final Date eventPublishingTime) {
            this.eventPublishingTime = new Date(eventPublishingTime.getTime());
            return this;
        }

        @Override
        public AccountLinkedRequest build() {
            return new AccountLinkedRequest(this);
        }
    }
}
