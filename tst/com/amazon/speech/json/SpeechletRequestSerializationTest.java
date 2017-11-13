package com.amazon.speech.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Device;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionEndedRequest.Reason;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SupportedInterfaces;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayerInterface;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayerState;
import com.amazon.speech.speechlet.interfaces.audioplayer.CurrentPlaybackState;
import com.amazon.speech.speechlet.interfaces.audioplayer.Error;
import com.amazon.speech.speechlet.interfaces.audioplayer.ErrorType;
import com.amazon.speech.speechlet.interfaces.audioplayer.PlayerActivity;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStartedRequest;
import com.amazon.speech.speechlet.interfaces.system.ErrorCause;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;

/**
 * Serialization tests for {@link SpeechletRequestEnvelope}.
 */
public class SpeechletRequestSerializationTest extends SpeechletSerializationTestBase {
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String API_ACCESS_TOKEN = "apiAccessToken";
    private static final Locale LOCALE = Locale.forLanguageTag("en-US");
    private static final long OFFSET = 100l;
    private static final String TOKEN = "audioPlayerToken";
    private static final String TOKEN_2 = "audioPlayerToken_2";
    private static final String ERROR_MESSAGE = "Error message";

    @Before
    public void setup() {
        // Configure logging to output to the console with default level of
        // INFO.
        BasicConfigurator.configure();
    }

    /**
     * Test the serialization of an envelope for an "onRequest" request.
     */
    @Test
    public void onIntentRequest() throws Exception {
        SpeechletRequestEnvelope<IntentRequest> envelope =
                SpeechletRequestEnvelope
                        .<IntentRequest>builder()
                        .withSession(buildSession(true))
                        .withRequest(buildIntentRequest())
                        .build();
        testSerialization(envelope);
    }

    /**
     * Test the serialization of an envelope for an "onSessionEnded" request.
     */
    @Test
    public void onSessionEndedRequest() throws Exception {
        SpeechletRequestEnvelope<SessionEndedRequest> envelope =
                SpeechletRequestEnvelope
                        .<SessionEndedRequest>builder()
                        .withSession(buildSession(false))
                        .withRequest(buildOnSessionEndedRequest())
                        .build();
        testSerialization(envelope);
    }

    /**
     * Test that new fields are ignored when deserializing a request for forward compatibility.
     */
    @Test
    public void forwardCompatibilityNewField() throws IOException {
        /**
         * A hypothetical new version of the SpeechletRequest.
         */
        class SpeechletRequestV2 extends IntentRequest {
            public SpeechletRequestV2(final String requestId, final Date timestamp,
                    final Locale locale, final Intent intent,
                    final DialogState dialogState, final String foo) {
                super(requestId, timestamp, locale, intent, dialogState);
                mFoo = foo;
            }

            /**
             * The new field.
             */
            @SuppressWarnings("unused")
            public String getFoo() {
                return mFoo;
            }

            private final String mFoo;
        }

        IntentRequest request =
                new SpeechletRequestV2("r_id", new Date(), LOCALE, buildIntent(),
                        null, "bar");
        SpeechletRequestEnvelope<IntentRequest> envelope1 =
                SpeechletRequestEnvelope
                        .<IntentRequest>builder()
                        .withSession(buildSession(true))
                        .withRequest(request)
                        .build();

        log.info("Original Request    : {}", envelope1);

        String json1 = OBJECT_WRITER.writeValueAsString(envelope1);
        log.info("Serialized JSON     : {}", json1);

        SpeechletRequestEnvelope<? extends SpeechletRequest> envelope2 = SpeechletRequestEnvelope.fromJson(json1);
        log.info("Deserialized Request: {}", envelope2);

        /*
         * The success criteria is that SpeechletRequestEnvelope.fromJson() did not throw an
         * exception and the "foo" member was simply ignored.
         */
    }

    /**
     * Test that new enum values are replaced by null when deserializing a request for forward
     * compatibility.
     */
    @Test
    public void forwardCompatibilityNewEnumValue() throws IOException {
        Date consistentDateToPassUnitTest = new Date();
        SessionEndedRequest requestWithReason =
                SessionEndedRequest
                        .builder()
                        .withRequestId("r_id")
                        .withLocale(LOCALE)
                        .withReason(Reason.ERROR)
                        .withTimestamp(consistentDateToPassUnitTest)
                        .build();
        SpeechletRequestEnvelope<SessionEndedRequest> envelopeWithReason =
                SpeechletRequestEnvelope
                        .<SessionEndedRequest>builder()
                        .withSession(buildSession(true))
                        .withRequest(requestWithReason)
                        .build();

        log.info("Original Request    : {}", envelopeWithReason);

        String jsonWithReasonV1 = OBJECT_WRITER.writeValueAsString(envelopeWithReason);
        log.info("Serialized JSON     : {}", jsonWithReasonV1);

        String jsonWithReasonV2 = jsonWithReasonV1.replaceAll("ERROR", "SOME_NEW_REASON");
        // Make sure we did do a replacement
        assertNotEquals(jsonWithReasonV1, jsonWithReasonV2);

        SpeechletRequestEnvelope<? extends SpeechletRequest> deserializedEnvelope =
                SpeechletRequestEnvelope.fromJson(jsonWithReasonV2);
        log.info("Deserialized Request: {}", deserializedEnvelope);
        SessionEndedRequest deserializedRequest =
                (SessionEndedRequest) deserializedEnvelope.getRequest();
        assertNull(deserializedRequest.getReason());

        String reserializedJson = OBJECT_WRITER.writeValueAsString(deserializedEnvelope);
        log.info("Re-serialized JSON  : {}", reserializedJson);

        SessionEndedRequest requestWithoutReason =
                SessionEndedRequest
                        .builder()
                        .withRequestId("r_id")
                        .withLocale(LOCALE)
                        .withTimestamp(consistentDateToPassUnitTest)
                        .build();

        SpeechletRequestEnvelope<SessionEndedRequest> envelopeWithoutReason =
                SpeechletRequestEnvelope
                        .<SessionEndedRequest>builder()
                        .withSession(buildSession(true))
                        .withRequest(requestWithoutReason)
                        .build();

        String jsonWithoutReason = OBJECT_WRITER.writeValueAsString(envelopeWithoutReason);
        log.info("Re-serialized JSON   : {}", jsonWithoutReason);

        assertEquals(reserializedJson, jsonWithoutReason);
    }

    @Test
    public void serializesSessionEndedRequest_withError() throws JSONException,
            JsonProcessingException {

        com.amazon.speech.speechlet.interfaces.system.Error arbitraryError =
                constructArbitraryError();

        String arbitraryNewRequestId = "arbitraryNewRequestId";
        Locale arbitraryLocale = Locale.CANADA_FRENCH;
        SessionEndedRequest request =
                SessionEndedRequest
                        .builder()
                        .withReason(Reason.ERROR)
                        .withError(arbitraryError)
                        .withLocale(arbitraryLocale)
                        .withRequestId(arbitraryNewRequestId)
                        .build();

        String actual = OBJECT_WRITER.writeValueAsString(request);

        String expected =
                readFileAsString("tst/com/amazon/speech/json/SessionEndedRequest.withError.json");
        JSONAssert.assertEquals(expected, actual, false /* strict */);
    }

    private com.amazon.speech.speechlet.interfaces.system.Error constructArbitraryError() {
        com.amazon.speech.speechlet.interfaces.system.ErrorType arbitraryType =
                com.amazon.speech.speechlet.interfaces.system.ErrorType.INTERNAL_SERVICE_ERROR;
        String arbitraryMessage = "arbitraryMessage";
        com.amazon.speech.speechlet.interfaces.system.Error arbitraryError =
                com.amazon.speech.speechlet.interfaces.system.Error
                        .builder()
                        .withType(arbitraryType)
                        .withMessage(arbitraryMessage)
                        .build();
        return arbitraryError;
    }

    @Test
    public void serializeSessionObject_existingSession_newFlagPresentInJson() throws Exception {
        Session session = buildSession(false);
        String json = OBJECT_WRITER.writeValueAsString(session);
        assertTrue("New flag should be present for existing sessions",
                json.contains("\"new\" : false"));
    }

    @Test
    public void serializeSpeechletRequestObject_nullLocale_localeNotPresentInJson()
            throws Exception {
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("request").withLocale(null).build();
        String json = OBJECT_WRITER.writeValueAsString(request);
        assertFalse("Null locale should not be present in JSON", json.contains("\"locale\""));
    }

    @Test
    public void serializePlaybackStartedRequestObject_regularParameters_serializes()
            throws Exception {
        PlaybackStartedRequest request =
                PlaybackStartedRequest
                        .builder()
                        .withRequestId("request")
                        .withOffsetInMilliseconds(OFFSET)
                        .withToken(TOKEN)
                        .build();
        String json = OBJECT_WRITER.writeValueAsString(request);
        assertTrue("Offset should be present in JSON", json.contains(Long.toString(OFFSET)));
        assertTrue("Token should be present in JSON", json.contains(TOKEN));
    }

    @Test
    public void serializePlaybackFailedRequestObject_regularParameters_serializes()
            throws Exception {
        Error error = new Error();
        error.setMessage(ERROR_MESSAGE);
        error.setType(ErrorType.MEDIA_ERROR_INTERNAL_DEVICE_ERROR);
        CurrentPlaybackState state =
                CurrentPlaybackState
                        .builder()
                        .withOffsetInMilliseconds(OFFSET)
                        .withPlayerActivity(PlayerActivity.PLAYING)
                        .withToken(TOKEN_2)
                        .build();

        PlaybackFailedRequest request =
                PlaybackFailedRequest
                        .builder()
                        .withRequestId("request")
                        .withToken(TOKEN)
                        .withCurrentPlaybackState(state)
                        .withError(error)
                        .build();

        String json = OBJECT_WRITER.writeValueAsString(request);
        assertTrue("Offset should be present in JSON", json.contains(Long.toString(OFFSET)));
        assertTrue("Token should be present in JSON", json.contains(TOKEN));
        assertTrue("Token should be present in JSON", json.contains(TOKEN_2));
        assertTrue("PlayerActivity should be present in JSON",
                json.contains(PlayerActivity.PLAYING.name()));
        assertTrue("Error message should be present in JSON", json.contains(ERROR_MESSAGE));
        assertTrue("Error type should be present in JSON",
                json.contains(ErrorType.MEDIA_ERROR_INTERNAL_DEVICE_ERROR.name()));
    }

    @Test
    public void serializePlaybackFailedRequestObject_withoutOffset_serializesWithoutOffsetInJson()
            throws Exception {
        Error error = new Error();
        error.setMessage(ERROR_MESSAGE);
        error.setType(ErrorType.MEDIA_ERROR_INTERNAL_DEVICE_ERROR);
        CurrentPlaybackState state = CurrentPlaybackState.builder()
        // No offset
                .withPlayerActivity(PlayerActivity.PLAYING)
                .withToken(TOKEN_2)
                .build();

        PlaybackFailedRequest request =
                PlaybackFailedRequest
                        .builder()
                        .withRequestId("request")
                        .withToken(TOKEN)
                        .withCurrentPlaybackState(state)
                        .withError(error)
                        .build();

        String json = OBJECT_WRITER.writeValueAsString(request);
        assertFalse("Offset should not be present in JSON",
                json.contains("\"offsetInMilliseconds\""));
        assertTrue("Token should be present in JSON", json.contains(TOKEN));
        assertTrue("Token should be present in JSON", json.contains(TOKEN_2));
        assertTrue("Error message should be present in JSON", json.contains(ERROR_MESSAGE));
        assertTrue("Error type should be present in JSON",
                json.contains(ErrorType.MEDIA_ERROR_INTERNAL_DEVICE_ERROR.name()));
    }

    @Test
    public void serializesExceptionEncounteredRequest_regularParameters_serializes()
            throws JsonProcessingException, JSONException {
        com.amazon.speech.speechlet.interfaces.system.Error arbitraryError =
                constructArbitraryError();

        String arbitraryCauseRequestId = "arbitraryCauseRequestId";
        ErrorCause arbitraryCause =
                ErrorCause.builder().withRequestId(arbitraryCauseRequestId).build();

        String arbitraryNewRequestId = "arbitraryNewRequestId";
        Locale arbitraryLocale = Locale.CANADA_FRENCH;
        ExceptionEncounteredRequest request =
                ExceptionEncounteredRequest
                        .builder()
                        .withCause(arbitraryCause)
                        .withError(arbitraryError)
                        .withLocale(arbitraryLocale)
                        .withRequestId(arbitraryNewRequestId)
                        .build();

        String actual = OBJECT_WRITER.writeValueAsString(request);

        String expected =
                readFileAsString("tst/com/amazon/speech/json/ExceptionEncounteredRequest.normal.json");
        JSONAssert.assertEquals(expected, actual, false /* strict */);
    }

    @Test
    public void serializeRequestEnvelope_withContext_serializes() throws Exception {
        SpeechletRequestEnvelope<? extends SpeechletRequest> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withContext(
                                Context.builder()
                                        .addState(
                                                AudioPlayerState
                                                        .builder()
                                                        .withOffsetInMilliseconds(OFFSET)
                                                        .withPlayerActivity(PlayerActivity.PLAYING)
                                                        .withToken(TOKEN)
                                                        .build())
                                        .addState(
                                                SystemState
                                                        .builder()
                                                        .withApplication(
                                                                new Application("applicationId"))
                                                        .withUser(
                                                                User.builder()
                                                                        .withUserId(USER_ID)
                                                                        .withAccessToken(
                                                                                ACCESS_TOKEN)
                                                                        .build())
                                                        .withDevice(
                                                                Device.builder()
                                                                        .withSupportedInterfaces(
                                                                                SupportedInterfaces
                                                                                        .builder()
                                                                                        .addSupportedInterface(
                                                                                                AudioPlayerInterface
                                                                                                        .builder()
                                                                                                        .build())
                                                                                        .build())
                                                                        .build())
                                                        .withApiAccessToken(API_ACCESS_TOKEN)
                                                        .build())
                                        .build())
                        .build();

        String json = OBJECT_WRITER.writeValueAsString(envelope);
        log.info("Serialized JSON   : {}", json);
        testSerialization(envelope);
    }

    @Test
    public void serializeRequestEnvelopet_withContextWithoutOffsetAndToken_serializesWithoutOffsetInJson()
            throws Exception {
        SpeechletRequestEnvelope<? extends SpeechletRequest> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withContext(
                                Context.builder()
                                        .addState(AudioPlayerState.builder()
                                        // No Offset
                                                .withPlayerActivity(PlayerActivity.PLAYING)
                                                // No Token
                                                .build())
                                        .addState(
                                                SystemState
                                                        .builder()
                                                        .withApplication(
                                                                new Application("applicationId"))
                                                        .withUser(
                                                                User.builder()
                                                                        .withUserId(USER_ID)
                                                                        .withAccessToken(
                                                                                ACCESS_TOKEN)
                                                                        .build())
                                                        .withDevice(
                                                                Device.builder()
                                                                        .withSupportedInterfaces(
                                                                                SupportedInterfaces
                                                                                        .builder()
                                                                                        .addSupportedInterface(
                                                                                                AudioPlayerInterface
                                                                                                        .builder()
                                                                                                        .build())
                                                                                        .build())
                                                                        .build())
                                                        .withApiAccessToken(API_ACCESS_TOKEN)
                                                        .build())
                                        .build())
                        .build();

        String json = OBJECT_WRITER.writeValueAsString(envelope);
        log.info("Serialized JSON   : {}", json);
        testSerialization(envelope);
        assertFalse(json.contains("\"offsetInMilliseconds\""));
        assertFalse(json.contains("\"token\""));
    }

    @Test
    public void serializeRequestEnvelopet_contextWithoutStates_serializes() throws Exception {
        SpeechletRequestEnvelope<? extends SpeechletRequest> envelope =
                SpeechletRequestEnvelope.builder().withContext(Context.builder().build()).build();

        String json = OBJECT_WRITER.writeValueAsString(envelope);
        log.info("Serialized JSON   : {}", json);
        testSerialization(envelope);
    }

    @Test
    public void serializeRequestEnvelopet_deviceWithoutSupportedInterfaces_serializes()
            throws Exception {
        SpeechletRequestEnvelope<? extends SpeechletRequest> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withContext(
                                Context.builder()
                                        .addState(
                                                AudioPlayerState
                                                        .builder()
                                                        .withOffsetInMilliseconds(OFFSET)
                                                        .withPlayerActivity(PlayerActivity.PLAYING)
                                                        .withToken(TOKEN)
                                                        .build())
                                        .addState(
                                                SystemState
                                                        .builder()
                                                        .withApplication(
                                                                new Application("applicationId"))
                                                        .withUser(
                                                                User.builder()
                                                                        .withUserId(USER_ID)
                                                                        .withAccessToken(
                                                                                ACCESS_TOKEN)
                                                                        .build())
                                                        .withDevice(Device.builder().build())
                                                        .withApiAccessToken(API_ACCESS_TOKEN)
                                                        .build())
                                        .build())
                        .build();
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        log.info("Serialized JSON   : {}", json);
        testSerialization(envelope);
    }

    // -----------------------
    // Helper factory methods

    /**
     * @return a test intent.
     */
    private Intent buildIntent() {
        Map<String, Slot> slots = new HashMap<>();
        Slot slot = Slot.builder().withName("city").withValue("Seattle, WA").build();
        slots.put(slot.getName(), slot);
        Intent intent = Intent.builder().withName("GetWeatherIntent").withSlots(slots).build();

        return intent;
    }

    /**
     * @return a test speechlet request.
     */
    private IntentRequest buildIntentRequest() {
        IntentRequest request =
                IntentRequest
                        .builder()
                        .withRequestId("my_request_id")
                        .withLocale(LOCALE)
                        .withIntent(buildIntent())
                        .build();
        return request;
    }

    /**
     * @return a test "on session ended" request.
     */
    private SessionEndedRequest buildOnSessionEndedRequest() {
        SessionEndedRequest request =
                SessionEndedRequest
                        .builder()
                        .withRequestId("my_end_request_id")
                        .withLocale(LOCALE)
                        .withReason(Reason.USER_INITIATED)
                        .build();
        return request;
    }

    /**
     * @param isNew
     *            should the session "isNew" flag be true.
     * @return a test session.
     */
    private Session buildSession(boolean isNew) {
        Application application = new Application("ApplicationId");
        User customer = User.builder().withUserId(USER_ID).withAccessToken(ACCESS_TOKEN).build();
        Map<String, Object> attributes = buildSessionAttributes();
        Session session =
                Session.builder()
                        .withIsNew(isNew)
                        .withSessionId("my_session_id")
                        .withApplication(application)
                        .withAttributes(attributes)
                        .withUser(customer)
                        .build();
        return session;
    }
}
