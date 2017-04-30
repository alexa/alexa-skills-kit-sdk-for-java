package com.amazon.speech.speechlet;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpeechletResponseTest {
    @Test
    @DisplayName("Should create new speechlet response with delegate dialog directive")
    void newDelegateResponse() throws JsonProcessingException {
        final SpeechletResponse response = SpeechletResponse.newDelegateResponse(Intent.builder().withName("UpdatedIntent").build());
        assertEquals(1, response.getDirectives().size());

        final String json = "{" +
                "\"outputSpeech\":null," +
                "\"card\":null," +
                "\"directives\":[" +
                "{" +
                "\"type\":\"Dialog.Delegate\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"UpdatedIntent\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}" +
                "]," +
                "\"reprompt\":null," +
                "\"shouldEndSession\":false" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(response));
    }

    @Test
    @DisplayName("Should create new speechlet response with elicit slot dialog directive")
    void newElicitSlotResponse() throws JsonProcessingException {
        final SpeechletResponse response = SpeechletResponse.newElicitSlotResponse("MyNeededSlot",
                Intent.builder().withName("UpdatedIntent").build(), PlainTextOutputSpeech.builder().withText("What?").build(),
                Reprompt.builder().withOutputSpeech(PlainTextOutputSpeech.builder().withText("You there?").build()).build());
        assertEquals(1, response.getDirectives().size());

        final String json = "{" +
                "\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"What?\"}," +
                "\"card\":null," +
                "\"directives\":[" +
                "{" +
                "\"type\":\"Dialog.ElicitSlot\"," +
                "\"slotToElicit\":\"MyNeededSlot\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"UpdatedIntent\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}" +
                "]," +
                "\"reprompt\":{\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"You there?\"}}," +
                "\"shouldEndSession\":false" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(response));
    }

    @Test
    @DisplayName("Should create new speechlet response with confirm slot dialog directive")
    void newConfirmSlotResponse() throws JsonProcessingException {
        final SpeechletResponse response = SpeechletResponse.newConfirmSlotResponse("SlotToConfirmA",
                Intent.builder().withName("UpdatedIntent").build(), PlainTextOutputSpeech.builder().withText("What?").build(),
                Reprompt.builder().withOutputSpeech(PlainTextOutputSpeech.builder().withText("You there?").build()).build());
        assertEquals(1, response.getDirectives().size());

        final String json = "{" +
                "\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"What?\"}," +
                "\"card\":null," +
                "\"directives\":[" +
                "{" +
                "\"type\":\"Dialog.ConfirmSlot\"," +
                "\"slotToConfirm\":\"SlotToConfirmA\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"UpdatedIntent\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}" +
                "]," +
                "\"reprompt\":{\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"You there?\"}}," +
                "\"shouldEndSession\":false" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(response));
    }

    @Test
    @DisplayName("Should create new speechlet response with confirm intent dialog directive")
    void newConfirmIntentResponse() throws JsonProcessingException {
        final SpeechletResponse response = SpeechletResponse.newConfirmIntentResponse(Intent.builder().withName("UpdatedIntent").build(), PlainTextOutputSpeech.builder().withText("What?").build(),
                Reprompt.builder().withOutputSpeech(PlainTextOutputSpeech.builder().withText("You there?").build()).build());
        assertEquals(1, response.getDirectives().size());

        final String json = "{" +
                "\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"What?\"}," +
                "\"card\":null," +
                "\"directives\":[" +
                "{" +
                "\"type\":\"Dialog.ConfirmIntent\"," +
                "\"updatedIntent\":{" +
                "\"name\":\"UpdatedIntent\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}" +
                "]," +
                "\"reprompt\":{\"outputSpeech\":{\"type\":\"PlainText\",\"id\":null,\"text\":\"You there?\"}}," +
                "\"shouldEndSession\":false" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(response));
    }
}
