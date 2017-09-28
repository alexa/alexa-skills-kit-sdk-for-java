package com.amazon.speech.speechlet;

import static com.amazon.speech.Sdk.VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SessionEndedRequest.Reason;
import com.amazon.speech.speechlet.authentication.TimestampVerifier;
import com.amazon.speech.speechlet.interfaces.system.Error;
import com.amazon.speech.speechlet.interfaces.system.ErrorCause;
import com.amazon.speech.speechlet.interfaces.system.ErrorType;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.CardSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.OutputSpeechSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.ResponseSizeSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;

public class SpeechletRequestHandlerTest extends SpeechletRequestHandlerTestBase {
    private Speechlet speechlet;
    private SpeechletRequestHandler speechletRequestHandler;

    private static final long TIMESTAMP_TOLERANCE_IN_SECONDS = 150;
    private static final TimestampVerifier timestampVerifier = new TimestampVerifier(
            TIMESTAMP_TOLERANCE_IN_SECONDS, TimeUnit.SECONDS);

    private static final Locale LOCALE = Locale.forLanguageTag("en-US");
    private static final String ONLY_VALID_APPLICATION_ID = "ONLY_VALID_APPLICATION_ID";
    private static final Set<String> supportedApplicationIds = Sets
            .newSet(ONLY_VALID_APPLICATION_ID);

    @Before
    public void setup() throws SpeechletException {
        speechlet = Mockito.mock(Speechlet.class);
        speechletRequestHandler =
                new SpeechletRequestHandler(Arrays.asList(
                        new ApplicationIdSpeechletRequestVerifier(supportedApplicationIds),
                        new TimestampSpeechletRequestVerifier(TIMESTAMP_TOLERANCE_IN_SECONDS,
                                TimeUnit.SECONDS)), Arrays.asList(
                        new ResponseSizeSpeechletResponseVerifier(),
                        new OutputSpeechSpeechletResponseVerifier(),
                        new CardSpeechletResponseVerifier()));
    }

    // -----------
    // New Session

    @Test
    public void onLaunch() throws Exception {
        onLaunch(buildNewSession());
    }

    @Test
    public void onLaunchWithNoUser() throws Exception {
        onLaunch(buildNewSessionWithNoUser());
    }

    @Test
    public void onIntentWithNewSession() throws Exception {
        onIntent(buildNewSession());
    }

    @Test
    public void onIntentWithNewSessionAndNoUser() throws Exception {
        onIntent(buildNewSessionWithNoUser());
    }

    @Test
    public void onSessionStartedSpeechletException() throws Exception {
        onSessionStartedWithException(new SpeechletException("Exception"));
    }

    @Test
    public void onSessionStartedUnknownException() throws Exception {
        onSessionStartedWithException(new NullPointerException("Exception"));
    }

    // ----------------
    // Existing Session

    @Test
    public void onIntentWithExistingSession() throws Exception {
        onIntent(buildExistingSession());
    }

    @Test
    public void onIntentWithExistingSessionAndNoUser() throws Exception {
        onIntent(buildExistingSessionWithNoUser());
    }

    @Test
    public void onSessionEnded() throws Exception {
        onSessionEnded(buildExistingSession());
    }

    @Test
    public void onSessionEndedWithNoUser() throws Exception {
        onSessionEnded(buildExistingSessionWithNoUser());
    }

    // -------------
    // Unhappy Cases

    @Test
    public void doPost_nullRequest_rejectedAsBadRequestWithoutInvokingLifeCycleMethods()
            throws Exception {
        SpeechletServlet servlet = new SpeechletServlet();
        servlet.setSpeechlet(speechlet);

        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, null /* request */, null /* session */, null /* context */);
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (SpeechletRequestHandlerException ex) {
            assertTrue(
                    "Exception must mention invalid application ID",
                    ex.getMessage().contains(
                            ApplicationIdSpeechletRequestVerifier.class.getSimpleName()));
        }

        // Verify that no life-cycle methods were invoked.
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
        verify(speechlet, never()).onIntent(any(IntentRequest.class), any(Session.class));
        verify(speechlet, never()).onSessionEnded(any(SessionEndedRequest.class),
                any(Session.class));
    }

    @Test
    public void doPost_launchRequestForUnsupportedApplicationId_rejectedAsBadRequestWithoutInvokingLifeCycleMethods()
            throws Exception {
        // Build launch request whose session does not include the "ONLY_SUPPORTED_ID" applicationId
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        Session session = buildExistingSessionWithWrongApplicationId();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, session, null /*context*/);
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (SpeechletRequestHandlerException ex) {
            assertTrue(
                    "Exception must mention invalid application ID",
                    ex.getMessage().contains(
                            ApplicationIdSpeechletRequestVerifier.class.getSimpleName()));
        }

        // Verify that no relevant life-cycle methods were invoked
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
    }

    /**
     * The ExceptionEncounteredRequests usually are in envelopes that do not have a Session. This
     * tests that the proper exception is thrown if the application ID in the Context section is not
     * correct, since an ApplicationIdSpeechVerifier is specified. ApplicationIdSpeechVerifier does
     * not actually look in the Context, but the presence of an instance of this class will cause
     * ApplicationIdSpeechVerifierV2 to be instantiated as well, which does look at Context.
     */
    @Test
    public void doPost_exceptionEncounteredRequestForUnsupportedApplicationId_rejectedAsBadRequestWithoutInvokingLifeCycleMethods()
            throws Exception {
        // Build launch request whose session does not include the "ONLY_SUPPORTED_ID" applicationId
        ExceptionEncounteredRequest request =
                ExceptionEncounteredRequest
                        .builder()
                        .withRequestId("rId")
                        .withLocale(LOCALE)
                        .withCause(ErrorCause.builder().withRequestId("causeRequestId").build())
                        .withError(
                                Error.builder()
                                        .withMessage("msg")
                                        .withType(ErrorType.INVALID_RESPONSE)
                                        .build())
                        .build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, null /* session */, buildContext(ONLY_VALID_APPLICATION_ID
                        + "unsupport"));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (SpeechletRequestHandlerException ex) {
            assertTrue(
                    "Exception must mention invalid application ID, but was instead: " + ex.getMessage(),
                    ex.getMessage().contains(
                            ApplicationIdSpeechletRequestEnvelopeVerifier.class.getSimpleName()));
        }

        // Verify that no relevant life-cycle methods were invoked
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
    }

    /**
     * This is just the positive example of
     * doPost_exceptionEncounteredRequestForUnsupportedApplicationId_rejectedAsBadRequestWithoutInvokingLifeCycleMethods
     */
    @Test
    public void doPost_exceptionEncounteredRequestForSupportedApplicationId_noExceptionThrown()
            throws Exception {
        ExceptionEncounteredRequest request =
                ExceptionEncounteredRequest
                        .builder()
                        .withRequestId("rId")
                        .withLocale(LOCALE)
                        .withCause(ErrorCause.builder().withRequestId("causeRequestId").build())
                        .withError(
                                Error.builder()
                                        .withMessage("msg")
                                        .withType(ErrorType.INVALID_RESPONSE)
                                        .build())
                        .build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, null /* session */, buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (SpeechletRequestHandlerException ex) {
            assertTrue(
                    "Exception must mention invalid application ID, but only has: " + ex.getMessage(),
                    ex.getMessage().contains(
                            ApplicationIdSpeechletRequestEnvelopeVerifier.class.getSimpleName()));
        }

        // Verify that no relevant life-cycle methods were invoked
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
    }

    @Test
    public void doPost_launchRequestForInvalidTimestamp_rejectedAsBadRequestWithoutInvokingLifeCycleMethods()
            throws Exception {
        // Build launch request with old timestamp
        Date oldTimestamp =
                new Date(System.currentTimeMillis() - (TIMESTAMP_TOLERANCE_IN_SECONDS * 1000 * 2));
        LaunchRequest request =
                LaunchRequest
                        .builder()
                        .withRequestId("rId")
                        .withTimestamp(oldTimestamp)
                        .withLocale(LOCALE)
                        .build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, buildNewSession(), buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (SpeechletRequestHandlerException ex) {
            assertTrue(
                    "Exception must mention invalid timestamp",
                    ex.getMessage().contains(
                            TimestampSpeechletRequestVerifier.class.getSimpleName()));
        }

        // Verify that no relevant life-cycle methods were invoked
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
    }

    // -------
    // Helpers

    private Session buildNewSession() {
        return Session
                .builder()
                .withIsNew(true)
                .withSessionId("sId")
                .withApplication(new Application(ONLY_VALID_APPLICATION_ID))
                .withUser(new User("uId"))
                .build();
    }

    private Session buildNewSessionWithNoUser() {
        return Session
                .builder()
                .withIsNew(true)
                .withSessionId("sId")
                .withApplication(new Application(ONLY_VALID_APPLICATION_ID))
                .build();
    }

    private Session buildExistingSession() {
        return Session
                .builder()
                .withSessionId("sId")
                .withApplication(new Application(ONLY_VALID_APPLICATION_ID))
                .withUser(new User("uId"))
                .build();
    }

    private Session buildExistingSessionWithNoUser() {
        return Session
                .builder()
                .withSessionId("sId")
                .withApplication(new Application(ONLY_VALID_APPLICATION_ID))
                .build();
    }

    private Session buildExistingSessionWithWrongApplicationId() {
        return Session
                .builder()
                .withSessionId("sId")
                .withApplication(new Application("INVALID_APPLICATION_ID"))
                .build();
    }

    // -----------------------------
    // Life-cycle Invocation Helpers

    private void onLaunch(final Session session) throws Exception {
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, session, buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);

        // Verify that model life-cycle methods were invoked.
        if ((session != null) && session.isNew()) {
            verify(speechlet)
                    .onSessionStarted(any(SessionStartedRequest.class), any(Session.class));
        } else {
            verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                    any(Session.class));
        }
        verify(speechlet).onLaunch(any(LaunchRequest.class), any(Session.class));
        verify(speechlet, never()).onIntent(any(IntentRequest.class), any(Session.class));
        verify(speechlet, never()).onSessionEnded(any(SessionEndedRequest.class),
                any(Session.class));
    }

    private void onIntent(final Session session) throws Exception {
        IntentRequest request =
                IntentRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, session, buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);

        // Verify that one-shot life-cycle methods were invoked.
        if ((session != null) && session.isNew()) {
            verify(speechlet)
                    .onSessionStarted(any(SessionStartedRequest.class), any(Session.class));
        } else {
            verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                    any(Session.class));
        }
        verify(speechlet).onIntent(any(IntentRequest.class), any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
        verify(speechlet, never()).onSessionEnded(any(SessionEndedRequest.class),
                any(Session.class));
    }

    private void onSessionEnded(final Session session) throws Exception {
        SessionEndedRequest request =
                SessionEndedRequest
                        .builder()
                        .withRequestId("rId")
                        .withLocale(LOCALE)
                        .withReason(Reason.USER_INITIATED)
                        .build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, session, buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);

        // Verify that only onSessionEnded was invoked.
        verify(speechlet).onSessionEnded(any(SessionEndedRequest.class), any(Session.class));
        verify(speechlet, never()).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        verify(speechlet, never()).onLaunch(any(LaunchRequest.class), any(Session.class));
        verify(speechlet, never()).onIntent(any(IntentRequest.class), any(Session.class));
    }

    // -----------------
    // Exception Helpers

    private void onSessionStartedWithException(final Exception exception) throws Exception {
        doThrow(exception).when(speechlet).onSessionStarted(any(SessionStartedRequest.class),
                any(Session.class));
        Session session = buildNewSession();
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        SpeechletRequestEnvelope<?> envelope =
                build(VERSION, request, session, buildContext(ONLY_VALID_APPLICATION_ID));
        byte[] requestBytes = OBJECT_MAPPER.writeValueAsBytes(envelope);
        try {
            speechletRequestHandler.handleSpeechletCall(speechlet, requestBytes);
        } catch (Exception ex) {
            assertEquals("Exception types must match", exception.getClass(), ex.getClass());
        }
    }
}
