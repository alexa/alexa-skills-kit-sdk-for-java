package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillAccountLinked")
public class AccountLinkedRequest extends AlexaSkillEventRequest {
    private final AccountLinkedBody body;

    public static Builder builder() {
        return new Builder();
    }

    private AccountLinkedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
    }

    private AccountLinkedRequest(@JsonProperty("requestId") final String requestId,
                                 @JsonProperty("timestamp") final Date timestamp,
                                 @JsonProperty("locale") final Locale locale,
                                 @JsonProperty("body") final AccountLinkedBody body) {
        super(requestId, timestamp, locale);
        this.body = body;
    }

    public String getAccessToken() {
        return this.body.getAccessToken();
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, AccountLinkedRequest> {
        private AccountLinkedBody body;

        private  Builder() {

        }

        public Builder withAccountLinkedBody(final AccountLinkedBody body) {
            this.body = body;
            return this;
        }

        @Override
        public AccountLinkedRequest build() {
            return new AccountLinkedRequest(this);
        }
    }
}
