package com.amazon.speech.speechlet.lambda;

import static com.amazon.speech.Sdk.VERSION;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpeechletRequestStreamHandlerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private Speechlet speechlet;

    @Before
    public void setup() {
        speechlet = Mockito.mock(Speechlet.class);
    }

    @Test
    public void handlelRequest_withValidRequest_responseNotNull() throws IOException {
        LaunchRequest request =
                LaunchRequest
                        .builder()
                        .withRequestId("rId")
                        .withLocale(Locale.forLanguageTag("en-US"))
                        .build();
        Session session = buildSession(true);

        // Creates a request envelope
        SpeechletRequestEnvelope envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withVersion(VERSION)
                        .withSession(session)
                        .withRequest(request)
                        .build();

        // Serialize it
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(envelope);
        InputStream is = new ByteArrayInputStream(json);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        SpeechletRequestStreamHandler streamHandler = new TestSpeechletRequestStreamHandler();
        streamHandler.handleRequest(is, os, null);

        String output = new String(os.toByteArray(), Charset.defaultCharset());
        assertNotNull(output);
    }

    private Session buildSession(boolean isNew) {
        return Session
                .builder()
                .withIsNew(isNew)
                .withSessionId("sId")
                .withApplication(new Application("applicationId"))
                .withUser(new User("uId"))
                .build();
    }

    private final class TestSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
        public TestSpeechletRequestStreamHandler() {
            super(speechlet, Collections.<String>emptySet());
        }
    }
}