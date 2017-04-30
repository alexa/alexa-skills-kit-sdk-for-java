package com.amazon.speech.speechlet;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.interfaces.dialog.DialogState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntentRequestTest {
    @Test
    @DisplayName("Should map intent request object from json missing dialogState")
    void fromJson() throws IOException {
        final String json = "{\n" +
                "  \"type\": \"IntentRequest\",\n" +
                "  \"requestId\": \"b787f2b4-c2da-43df-a75f-4c2bdd217c71\",\n" +
                "  \"timestamp\": \"2017-05-07T03:21:37Z\",\n" +
                "  \"locale\": \"en-us\",\n" +
                "  \"intent\": {\n" +
                "    \"name\": \"Intent1\"\n" +
                "  }\n" +
                "}\n";

        final IntentRequest intentRequest = new ObjectMapper().readValue(json, IntentRequest.class);

        assertEquals("b787f2b4-c2da-43df-a75f-4c2bdd217c71", intentRequest.getRequestId());
        assertEquals("eng", intentRequest.getLocale().getISO3Language());
        assertEquals("Sat May 06 20:21:37 PDT 2017", intentRequest.getTimestamp().toString());
        assertEquals("Intent1", intentRequest.getIntent().getName());
    }

    @Test
    @DisplayName("Should map intent request object from json")
    void fromJson2() throws IOException {
        final String json = "{\n" +
                "  \"type\": \"IntentRequest\",\n" +
                "  \"dialogState\":\"IN_PROGRESS\"," +
                "  \"requestId\": \"b787f2b4-c2da-43df-a75f-4c2bdd217c71\",\n" +
                "  \"timestamp\": \"2017-05-07T03:21:37Z\",\n" +
                "  \"locale\": \"en-us\",\n" +
                "  \"intent\": {\n" +
                "    \"name\": \"Intent1\"\n" +
                "  }\n" +
                "}\n";

        final IntentRequest intentRequest = new ObjectMapper().readValue(json, IntentRequest.class);

        assertEquals(DialogState.IN_PROGRESS, intentRequest.getDialogState());
        assertEquals("b787f2b4-c2da-43df-a75f-4c2bdd217c71", intentRequest.getRequestId());
        assertEquals("eng", intentRequest.getLocale().getISO3Language());
        assertEquals("Sat May 06 20:21:37 PDT 2017", intentRequest.getTimestamp().toString());
        assertEquals("Intent1", intentRequest.getIntent().getName());
    }

    @Test
    @DisplayName("Should build intent request without dialogState and serialize to json")
    void builder() throws JsonProcessingException, ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2017-05-07T03:21:37Z");

        final IntentRequest intentRequest = IntentRequest.builder()
                .withRequestId("b787f2b4-c2da-43df-a75f-4c2bdd217c71")
                .withTimestamp(date)
                .withIntent(Intent.builder().withName("NewIntentZed").build())
                .build();

        final String json = "{" +
                "\"type\":\"IntentRequest\"," +
                "\"requestId\":\"b787f2b4-c2da-43df-a75f-4c2bdd217c71\"," +
                "\"timestamp\":\"2017-05-07T03:21:37Z\"," +
                "\"intent\":{" +
                "\"name\":\"NewIntentZed\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(intentRequest));
    }

    @Test
    @DisplayName("Should build intent request and serialize to json")
    void builder2() throws JsonProcessingException, ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2017-05-07T03:21:37Z");

        final IntentRequest intentRequest = IntentRequest.builder()
                .withRequestId("b787f2b4-c2da-43df-a75f-4c2bdd217c71")
                .withTimestamp(date)
                .withDialogState(DialogState.COMPLETED)
                .withIntent(Intent.builder().withName("NewIntentZed").build())
                .build();

        final String json = "{" +
                "\"type\":\"IntentRequest\"," +
                "\"requestId\":\"b787f2b4-c2da-43df-a75f-4c2bdd217c71\"," +
                "\"timestamp\":\"2017-05-07T03:21:37Z\"," +
                "\"intent\":{" +
                "\"name\":\"NewIntentZed\"," +
                "\"confirmationStatus\":\"NONE\"" +
                "}," +
                "\"dialogState\":\"COMPLETED\"" +
                "}";

        assertEquals(json, new ObjectMapper().writeValueAsString(intentRequest));
    }

    @Test
    @DisplayName("Should get intent from built intent request")
    void getIntent() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2017-05-07T03:21:37Z");

        final IntentRequest intentRequest = IntentRequest.builder()
                .withRequestId("b787f2b4-c2da-43df-a75f-4c2bdd217c71")
                .withTimestamp(date)
                .withIntent(Intent.builder().withName("NewIntentZed").build())
                .build();

        assertEquals("NewIntentZed", intentRequest.getIntent().getName());
    }

    @Test
    @DisplayName("Should get dialog state from built intent request")
    void getDialogState() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2017-05-07T03:21:37Z");

        final IntentRequest intentRequest = IntentRequest.builder()
                .withRequestId("b787f2b4-c2da-43df-a75f-4c2bdd217c71")
                .withTimestamp(date)
                .withDialogState(DialogState.IN_PROGRESS)
                .withIntent(Intent.builder().withName("NewIntentZed").build())
                .build();

        assertEquals(DialogState.IN_PROGRESS, intentRequest.getDialogState());
    }
}
