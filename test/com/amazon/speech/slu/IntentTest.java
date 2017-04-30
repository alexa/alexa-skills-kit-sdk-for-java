package com.amazon.speech.slu;

import com.amazon.speech.speechlet.interfaces.dialog.ConfirmationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IntentTest {
    @Test
    @DisplayName("Should map object from json with missing confim status")
    void fromJson() throws IOException {
        final String json = "{\n" +
                "    \"name\": \"GoodIntent\",\n" +
                "    \"slots\": {\n" +
                "      \"SlotOne\": {\n" +
                "        \"name\": \"SlotOne\",\n" +
                "        \"value\": \"------\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        final Intent intent = new ObjectMapper().readValue(json, Intent.class);
        assertEquals("GoodIntent", intent.getName());
        assertEquals(1, intent.getSlots().size());
        assertEquals("SlotOne", intent.getSlots().get("SlotOne").getName());
        assertEquals("------", intent.getSlot("SlotOne").getValue());
        assertEquals(ConfirmationStatus.NONE, intent.getConfirmationStatus());
    }

    @Test
    @DisplayName("Should map object from json")
    void fromJson2() throws IOException {
        final String json = "{\n" +
                "    \"name\": \"GoodIntent\",\n" +
                "    \"confirmationStatus\":\"DENIED\",\n" +
                "    \"slots\": {\n" +
                "      \"SlotOne\": {\n" +
                "        \"name\": \"SlotOne\",\n" +
                "        \"value\": \"------\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        final Intent intent = new ObjectMapper().readValue(json, Intent.class);
        assertEquals(ConfirmationStatus.DENIED, intent.getConfirmationStatus());
    }

    @Test
    @DisplayName("Should build intent with missing confirm status and serialize to json")
    void builder() throws JsonProcessingException {
        final Intent intent = Intent.builder()
                .withName("NewIntent1")
                .withSlots(new HashMap<String, Slot>())
                .build();

        final String json = "{" +
                "\"name\":\"NewIntent1\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}";
        assertEquals(json, new ObjectMapper().writeValueAsString(intent));
    }

    @Test
    @DisplayName("Should build intent and serialize to json")
    void builder2() throws JsonProcessingException {
        final Intent intent = Intent.builder()
                .withName("NewIntent1")
                .withConfirmationStatus(ConfirmationStatus.CONFIRMED)
                .withSlots(new HashMap<String, Slot>())
                .build();

        final String json = "{" +
                "\"name\":\"NewIntent1\"," +
                "\"confirmationStatus\":\"CONFIRMED\"" +
                "}";
        assertEquals(json, new ObjectMapper().writeValueAsString(intent));
    }

    @Test
    @DisplayName("Should update existing slot")
    void setSlot() throws IOException {
        final String json = "{\n" +
                "    \"name\": \"GoodIntent\",\n" +
                "    \"slots\": {\n" +
                "      \"SlotOne\": {\n" +
                "        \"name\": \"SlotOne\",\n" +
                "        \"value\": \"------\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        final Intent updatedIntent = new ObjectMapper().readValue(json, Intent.class);
        updatedIntent.setSlot(Slot.builder().withName("SlotOne").withValue("^^^^^^").build());

        assertEquals("SlotOne", updatedIntent.getSlot("SlotOne").getName());
        assertEquals("^^^^^^", updatedIntent.getSlot("SlotOne").getValue());
    }

    @Test
    @DisplayName("Should add new slot")
    void setSlot2() throws IOException {
        final String json = "{\n" +
                "    \"name\": \"GoodIntent\",\n" +
                "    \"slots\": {\n" +
                "      \"SlotOne\": {\n" +
                "        \"name\": \"SlotOne\",\n" +
                "        \"value\": \"------\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        final Intent updatedIntent = new ObjectMapper().readValue(json, Intent.class);
        updatedIntent.setSlot(Slot.builder().withName("SlotTwo").withValue("*****").build());

        assertEquals("SlotOne", updatedIntent.getSlot("SlotOne").getName());
        assertEquals("------", updatedIntent.getSlot("SlotOne").getValue());
        assertEquals("SlotTwo", updatedIntent.getSlot("SlotTwo").getName());
        assertEquals("*****", updatedIntent.getSlot("SlotTwo").getValue());
    }
}