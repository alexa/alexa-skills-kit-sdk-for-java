package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillDisabled")
public class SkillDisabledEventRequest extends AlexaSkillEventRequest {
    private final Date eventCreationTime;
    private final Date eventPublishingTime;

    protected SkillDisabledEventRequest(Builder builder) {
        super(builder);
        this.eventCreationTime = builder.eventCreationTime;
        this.eventPublishingTime = builder.eventPublishingTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    private SkillDisabledEventRequest(@JsonProperty("requestId") final String requestId,
                                      @JsonProperty("timestamp") final Date timestamp,
                                      @JsonProperty("locale") final Locale locale,
                                      @JsonProperty("eventCreationTime") final Date eventCreationTime,
                                      @JsonProperty("eventPublishingTime") final Date eventPublishingTime) {
        super(requestId, timestamp, locale);
        this.eventCreationTime = eventCreationTime;
        this.eventPublishingTime = eventPublishingTime;
    }

    public Date getEventCreationTime() {
        return new Date(eventCreationTime.getTime());
    }

    public Date getEventPublishingTime() {
        return new Date(eventPublishingTime.getTime());
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, SkillDisabledEventRequest> {
        private Date eventCreationTime;
        private Date eventPublishingTime;

        private Builder() {

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
        public SkillDisabledEventRequest build() {
            return new SkillDisabledEventRequest(this);
        }
    }
}
