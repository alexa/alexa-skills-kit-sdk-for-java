package com.amazon.speech.speechlet.interfaces.core;

import java.util.Date;
import java.util.Locale;

import com.amazon.speech.speechlet.SpeechletRequest;

public abstract class AlexaSkillEventRequest extends SpeechletRequest {
    protected AlexaSkillEventRequest(SpeechletRequestBuilder builder) {
        super(builder);
    }

    protected AlexaSkillEventRequest(String requestId, Date timestamp, Locale locale) {
        super(requestId, timestamp, locale);
    }
}
