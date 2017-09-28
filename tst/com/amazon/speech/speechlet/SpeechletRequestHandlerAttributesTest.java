package com.amazon.speech.speechlet;

import static com.amazon.speech.Sdk.VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SessionEndedRequest.Reason;
import com.amazon.speech.speechlet.verifier.SpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletResponseVerifier;

@RunWith(PowerMockRunner.class)
public class SpeechletRequestHandlerAttributesTest extends SpeechletRequestHandlerTestBase {
    // ----------------
    // Test attributes
    private static final String ON_SESSION_STARTED_ATTRIBUTE_NAME = "SessionStartedAttributeName";
    private static final String ON_SESSION_STARTED_ATTRIBUTE_VALUE = "SessionStartedAttributeValue";

    private static final String ON_LAUNCH_ATTRIBUTE_NAME = "LaunchAttributeName";
    private static final String ON_LAUNCH_ATTRIBUTE_VALUE = "LaunchAttributeValue";

    private static final String ON_INTENT_ATTRIBUTE_NAME = "IntentAttributeName";
    private static final String ON_INTENT_ATTRIBUTE_VALUE = "IntentAttributeValue";

    private static final String REQUEST_ID = "rId";
    private static final Locale LOCALE = Locale.forLanguageTag("en-US");

    private static final Map<String, String> ONE_SHOT_LAUNCH_ATTRIBUTE_MAP = new HashMap<>();
    static {
        ONE_SHOT_LAUNCH_ATTRIBUTE_MAP.put(ON_SESSION_STARTED_ATTRIBUTE_NAME,
                ON_SESSION_STARTED_ATTRIBUTE_VALUE);
        ONE_SHOT_LAUNCH_ATTRIBUTE_MAP.put(ON_INTENT_ATTRIBUTE_NAME, ON_INTENT_ATTRIBUTE_VALUE);
    }

    private static final Map<String, String> MODAL_LAUNCH_ATTRIBUTE_MAP = new HashMap<>();
    static {
        MODAL_LAUNCH_ATTRIBUTE_MAP.put(ON_SESSION_STARTED_ATTRIBUTE_NAME,
                ON_SESSION_STARTED_ATTRIBUTE_VALUE);
        MODAL_LAUNCH_ATTRIBUTE_MAP.put(ON_LAUNCH_ATTRIBUTE_NAME, ON_LAUNCH_ATTRIBUTE_VALUE);
    }

    private static final Map<String, String> INTENT_REQUEST_ONLY_ATTRIBUTE_MAP = Collections
            .singletonMap(ON_INTENT_ATTRIBUTE_NAME, ON_INTENT_ATTRIBUTE_VALUE);

    // -----------
    // Attributes

    private final TestSpeechlet speechlet = new TestSpeechlet();
    private SpeechletRequestHandler speechletRequestHandler;

    /**
     * Test Speechlet implementation to trigger "shouldEndSession" or throw an exception.
     */
    private static class TestSpeechlet implements Speechlet {
        private boolean shouldEndSession;
        private SpeechletException exceptionToThrow;

        @Override
        public void onSessionStarted(final SessionStartedRequest request, final Session session)
                throws SpeechletException {
            session.setAttribute(ON_SESSION_STARTED_ATTRIBUTE_NAME,
                    ON_SESSION_STARTED_ATTRIBUTE_VALUE);
        }

        @Override
        public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
                throws SpeechletException {
            session.setAttribute(ON_LAUNCH_ATTRIBUTE_NAME, ON_LAUNCH_ATTRIBUTE_VALUE);
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            final SpeechletResponse response = new SpeechletResponse();
            response.setShouldEndSession(shouldEndSession);
            return response;
        }

        @Override
        public SpeechletResponse onIntent(final IntentRequest request, final Session session)
                throws SpeechletException {
            session.setAttribute(ON_INTENT_ATTRIBUTE_NAME, ON_INTENT_ATTRIBUTE_VALUE);
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            final SpeechletResponse response = new SpeechletResponse();
            response.setShouldEndSession(shouldEndSession);

            return response;
        }

        @Override
        public void onSessionEnded(final SessionEndedRequest request, final Session session)
                throws SpeechletException {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
        }

        public void setShouldEndSession(final boolean shouldEndSession) {
            this.shouldEndSession = shouldEndSession;
        }
    }

    @Before
    public void setup() throws SpeechletException {
        speechletRequestHandler =
                new SpeechletRequestHandler(Collections.<SpeechletRequestVerifier>emptyList(),
                        Collections.<SpeechletResponseVerifier>emptyList());
    }

    @Test
    public void launchRequest_newSession_sessionAttributesUpdated() throws Exception {
        Session session = buildSession(true);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId(REQUEST_ID).withLocale(LOCALE).build();
        Map<String, Object> attributes = doPost(request, session);

        // Verify that the session attributes were updated from onSessionStarted and onLaunch.
        assertEquals(MODAL_LAUNCH_ATTRIBUTE_MAP, attributes);
    }

    @Test
    public void intentRequest_existingSession_sessionAttributesUpdated() throws Exception {
        Session session = buildSession(true);
        IntentRequest request =
                IntentRequest.builder().withRequestId(REQUEST_ID).withLocale(LOCALE).build();
        Map<String, Object> attributes = doPost(request, session);

        // Verify that session attributes were updated from onSessionStarted and onIntent.
        assertEquals(ONE_SHOT_LAUNCH_ATTRIBUTE_MAP, attributes);
    }

    @Test
    public void secondIntentRequest_existingSession_sessionAttributesUpdated() throws Exception {
        IntentRequest request =
                IntentRequest.builder().withRequestId(REQUEST_ID).withLocale(LOCALE).build();
        Map<String, Object> attributes = doPost(request);

        // Verify that session attributes were only updated from onIntent.
        assertEquals(INTENT_REQUEST_ONLY_ATTRIBUTE_MAP, attributes);
    }

    @Test
    public void intentRequest_shouldEndSessionIsTrue_sessionShouldEnd() throws Exception {
        speechlet.setShouldEndSession(true);
        IntentRequest request =
                IntentRequest.builder().withRequestId(REQUEST_ID).withLocale(LOCALE).build();
        Map<String, Object> attributes = doPost(request);

        // Verify that session attributes were discarded upon ending the session from onIntent.
        assertNull(attributes);
    }

    @Test
    public void sessionEndedRequest_sessionShouldEnd() throws Exception {
        SessionEndedRequest request = buildSessionEndedRequest();
        Map<String, Object> attributes = doPost(request);

        /*
         * Verify that session attributes were discarded upon ending the session from
         * onSessionEnded.
         */
        assertNull(attributes);
    }

    // --------------
    // Helper methods

    private Map<String, Object> doPost(final SpeechletRequest request) throws ServletException,
            IOException, SpeechletRequestHandlerException, SpeechletException {
        return doPost(request, buildSession());
    }

    private Map<String, Object> doPost(final SpeechletRequest request, final Session session)
            throws ServletException, IOException, SpeechletRequestHandlerException,
            SpeechletException {
        SpeechletRequestEnvelope envelope = build(VERSION, request, session, buildContext("someApplicationId"));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        byte[] responseBytes = speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        SpeechletResponseEnvelope responseEnvelope =
                OBJECT_MAPPER.readValue(responseBytes, SpeechletResponseEnvelope.class);
        return responseEnvelope.getSessionAttributes();
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

    private SessionEndedRequest buildSessionEndedRequest() {
        return SessionEndedRequest
                .builder()
                .withRequestId(REQUEST_ID)
                .withLocale(LOCALE)
                .withReason(Reason.USER_INITIATED)
                .build();
    }
}
