package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfirmSlotDirectiveTest {
    @Test
    @DisplayName("Should build a confirm slot directive and serialize to json")
    void builder() throws JsonProcessingException {
        ConfirmSlotDirective directive = (ConfirmSlotDirective) ConfirmSlotDirective.builder()
                .withSlotToConfirm("slot1")
                .withUpdatedIntent(Intent.builder().withName("IntentName1").build())
                .build();
        final String json = new ObjectMapper().writeValueAsString(directive);

        assertEquals("{" +
                "\"type\":\"Dialog.ConfirmSlot\"," +
                "\"slotToConfirm\":\"slot1\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"IntentName1\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}", json);
    }

    @Test
    @DisplayName("Should get slot to confirm value")
    void getSlotToConfirm() {
        ConfirmSlotDirective directive = (ConfirmSlotDirective) ConfirmSlotDirective.builder()
                .withSlotToConfirm("slot1")
                .withUpdatedIntent(Intent.builder().withName("IntentName1").build())
                .build();
        assertEquals("slot1", directive.getSlotToConfirm());
    }
}