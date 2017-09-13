package com.amazon.speech.speechlet.interfaces.core;

import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.Locale;

@JsonTypeName("AlexaSkillEvent.SkillDisabled")
public class SkillDisabledEventRequest extends AlexaSkillEventRequest {
    protected SkillDisabledEventRequest(SpeechletRequest.SpeechletRequestBuilder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private SkillDisabledEventRequest(@JsonProperty("requestId") final String requestId,
                                      @JsonProperty("timestamp") final Date timestamp,
                                      @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, SkillDisabledEventRequest> {
        private Builder() {

        }

        @Override
        public SkillDisabledEventRequest build() {
            return new SkillDisabledEventRequest(this);
        }
    }
}
