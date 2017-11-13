package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillPermissionAccepted")
public class PermissionAcceptedRequest extends AlexaSkillEventRequest {
    private final PermissionBody body;
    private final Date eventCreationTime;
    private final Date eventPublishingTime;

    public static Builder builder() {
        return new Builder();
    }

    private PermissionAcceptedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
        this.eventCreationTime = builder.eventCreationTime;
        this.eventPublishingTime = builder.eventPublishingTime;
    }

    private PermissionAcceptedRequest(@JsonProperty("requestId") final String requestId,
                                      @JsonProperty("timestamp") final Date timestamp,
                                      @JsonProperty("locale") final Locale locale,
                                      @JsonProperty("body") final PermissionBody body,
                                      @JsonProperty("eventCreationTime") final Date eventCreationTime,
                                      @JsonProperty("eventPublishingTime") final Date eventPublishingTime) {
        super(requestId, timestamp, locale);
        this.body = body;
        this.eventCreationTime = eventCreationTime;
        this.eventPublishingTime = eventPublishingTime;
    }

    public List<Permission> getAcceptedPermissionList() {
        List<Permission> permissions = this.body.getAcceptedPermissions();
        return permissions;
    }

    public Date getEventCreationTime() {
        return new Date(eventCreationTime.getTime());
    }

    public Date getEventPublishingTime() { return new Date(eventPublishingTime.getTime()); }

    public static final class Builder extends SpeechletRequestBuilder<Builder, PermissionAcceptedRequest> {
        private PermissionBody body;
        private Date eventCreationTime;
        private Date eventPublishingTime;

        private  Builder() {

        }

        public Builder withPermissionBody (final PermissionBody body) {
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
        public  PermissionAcceptedRequest build() {
            return new PermissionAcceptedRequest(this);
        }
    }
}

