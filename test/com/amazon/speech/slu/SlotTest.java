package com.amazon.speech.slu;

import com.amazon.speech.speechlet.interfaces.dialog.ConfirmationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlotTest {
    @Test
    @DisplayName("Should map object from json missing confirm status")
    void fromJson() throws IOException {
        final String json = "{\"name\":\"Slot1\",\"value\":\"**Value**\"}";
        final Slot slot = new ObjectMapper().readValue(json, Slot.class);

        assertEquals("Slot1", slot.getName());
        assertEquals("**Value**", slot.getValue());
    }

    @Test
    @DisplayName("Should map object from json")
    void fromJson2() throws IOException {
        final String json = "{\"name\":\"Slot1\",\"value\":\"**Value**\",\"confirmationStatus\":\"CONFIRMED\"}";
        final Slot slot = new ObjectMapper().readValue(json, Slot.class);

        assertEquals("Slot1", slot.getName());
        assertEquals("**Value**", slot.getValue());
        assertEquals(ConfirmationStatus.CONFIRMED, slot.getConfirmationStatus());
    }

    @Test
    @DisplayName("Should build slot missing confirmation status and serialize to json")
    void builder() throws JsonProcessingException {
        final Slot slot = Slot.builder()
                .withName("Slot1")
                .withValue("**Value**")
                .build();

        final String json = "{\"name\":\"Slot1\",\"value\":\"**Value**\",\"confirmationStatus\":\"NONE\"}";
        assertEquals(json, new ObjectMapper().writeValueAsString(slot));
    }

    @Test
    @DisplayName("Should build slot and serialize to json")
    void builder2() throws JsonProcessingException {
        final Slot slot = Slot.builder()
                .withName("Slot1")
                .withValue("**Value**")
                .withConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();

        final String json = "{\"name\":\"Slot1\",\"value\":\"**Value**\",\"confirmationStatus\":\"CONFIRMED\"}";
        assertEquals(json, new ObjectMapper().writeValueAsString(slot));
    }

    @Test
    @DisplayName("Should update slot value")
    void setValue() {
        final Slot slot = Slot.builder()
                .withName("Slot1")
                .withValue("**Value**")
                .withConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .build();
        slot.setValue("__NEW_VALUE__");
        assertEquals("__NEW_VALUE__", slot.getValue());
    }
}
