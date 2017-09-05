package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillEnabled")
public class SkillEnabledEventRequest extends AlexaSkillEventRequest {
    protected SkillEnabledEventRequest(SpeechletRequestBuilder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private SkillEnabledEventRequest(@JsonProperty("requestId") final String requestId,
                                     @JsonProperty("timestamp") final Date timestamp,
                                     @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, SkillEnabledEventRequest> {
        private Builder() {

        }

        @Override
        public SkillEnabledEventRequest build() {
            return new SkillEnabledEventRequest(this);
        }
    }

}
