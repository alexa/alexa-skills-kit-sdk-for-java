package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfirmIntentDirectiveTest {
    @Test
    @DisplayName("Should build a confirm intent directive and serialize to json")
    void builder() throws JsonProcessingException {
        ConfirmIntentDirective delegateDirective = (ConfirmIntentDirective) ConfirmIntentDirective.builder()
                .withUpdatedIntent(Intent.builder()
                        .withName("IntentName1").build())
                .build();
        final String json = new ObjectMapper().writeValueAsString(delegateDirective);

        assertEquals("{" +
                "\"type\":\"Dialog.ConfirmIntent\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"IntentName1\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}", json);
    }
}