package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillPermissionAccepted")
public class PermissionAcceptedRequest extends AlexaSkillEventRequest {
    private final PermissionBody body;

    public static Builder builder() {
        return new Builder();
    }

    private PermissionAcceptedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
    }

    private PermissionAcceptedRequest(@JsonProperty("requestId") final String requestId,
                                      @JsonProperty("timestamp") final Date timestamp,
                                      @JsonProperty("locale") final Locale locale,
                                      @JsonProperty("body") final PermissionBody body) {
        super(requestId, timestamp, locale);
        this.body = body;
    }

    public List<Permission> getAcceptedPermissionList() {
        List<Permission> permissions = this.body.getAcceptedPermissions();
        return permissions;
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, PermissionAcceptedRequest> {
        private PermissionBody body;

        private  Builder() {

        }

        public Builder withPermissionBody (final PermissionBody body) {
            this.body = body;
            return this;
        }

        @Override
        public  PermissionAcceptedRequest build() {
            return new PermissionAcceptedRequest(this);
        }
    }
}

