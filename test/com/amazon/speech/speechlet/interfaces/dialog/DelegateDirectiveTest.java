package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateDirectiveTest {

    @Test
    @DisplayName("Should build a delegate directive and serialize to json")
    void builder() throws JsonProcessingException {
        DelegateDirective delegateDirective = DelegateDirective.builder()
                .withUpdatedIntent(Intent.builder()
                    .withName("IntentName1").build())
                .build();
        final String json = new ObjectMapper().writeValueAsString(delegateDirective);

        assertEquals("{" +
                "\"type\":\"Dialog.Delegate\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"IntentName1\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}", json);
    }

    @Test
    @DisplayName("Should return updated intent field from built delegate directive")
    void getUpdatedIntent() {
        DelegateDirective delegateDirective = DelegateDirective.builder()
                .withUpdatedIntent(Intent.builder()
                        .withName("IntentName1").build())
                .build();
        final Intent updatedIntent = delegateDirective.getUpdatedIntent();
        assertEquals("IntentName1", updatedIntent.getName());
    }
}