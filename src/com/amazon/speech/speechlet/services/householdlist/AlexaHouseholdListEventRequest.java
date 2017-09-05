package com.amazon.speech.speechlet.services.householdlist;

import com.amazon.speech.speechlet.SpeechletRequest;

import java.util.Date;
import java.util.Locale;

public class AlexaHouseholdListEventRequest extends SpeechletRequest {
    protected AlexaHouseholdListEventRequest(SpeechletRequest.SpeechletRequestBuilder builder) {
        super(builder);
    }

    protected AlexaHouseholdListEventRequest(String requestId, Date timestamp, Locale locale) {
        super(requestId, timestamp, locale);
    }

}

