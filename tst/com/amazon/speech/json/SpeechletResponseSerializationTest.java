package com.amazon.speech.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.Sdk;
import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.ConfirmIntentDirective;
import com.amazon.speech.speechlet.dialog.directives.ConfirmSlotDirective;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioItem;
import com.amazon.speech.speechlet.interfaces.audioplayer.PlayBehavior;
import com.amazon.speech.speechlet.interfaces.audioplayer.Stream;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.PlayDirective;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.StopDirective;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.LinkAccountCard;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.StandardCard;
import com.google.common.collect.ImmutableMap;

/**
 * Serialization tests for {@link SpeechletResponseEnvelope}.
 */
public class SpeechletResponseSerializationTest extends SpeechletSerializationTestBase {
    private static final String TOKEN = "audioPlayerToken";
    private SpeechletResponseEnvelope envelope;

    @Before
    public void setup() {
        // Configure logging to output to the console with default level of INFO.
        BasicConfigurator.configure();
        envelope = new SpeechletResponseEnvelope();
        envelope.setVersion(Sdk.VERSION);
    }

    /**
     * Tests that the "shouldEndSession" flag is present in the serialized JSON when set to true.
     */
    @Test
    public void serialization_withShouldEndSessionTrue_flagPresentInSerializedJson()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(true);

        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertTrue("shouldEndSession flag should be true in the response",
                json.contains("\"shouldEndSession\" : true"));
    }

    /**
     * Tests that the "shouldEndSession" flag is present in the serialized JSON when set to false.
     */
    @Test
    public void serialization_withShouldEndSessionFalse_flagPresentInSerializedJson()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(false);

        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertTrue("shouldEndSession flag should be false in the response",
                json.contains("\"shouldEndSession\" : false"));
    }

    /**
     * Tests that the "shouldEndSession" flag is present in the serialized JSON by default.
     */
    @Test
    public void serialization_nullableShouldEndSession_default()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertTrue("shouldEndSession flag should be true in the response",
                json.contains("\"shouldEndSession\" : true"));
        assertTrue(response.getNullableShouldEndSession());
        assertTrue(response.getShouldEndSession());
    }


    /**
     * Tests that the "shouldEndSession" flag is not present in the serialized JSON when set to null
     * by nullableShouldEndSession.
     */
    @Test
    public void serialization_nullableShouldEndSession_null()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setNullableShouldEndSession(null);
        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertFalse("shouldEndSession flag should not be present in the response",
                json.contains("\"shouldEndSession\" : null"));
        assertNull(response.getNullableShouldEndSession());
        assertFalse(response.getShouldEndSession());
    }


    /**
     * Tests that the "shouldEndSession" flag is present in the serialized JSON when set to true by
     * nullableShouldEndSession.
     */
    @Test
    public void serialization_nullableShouldEndSession_true()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setNullableShouldEndSession(true);
        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertTrue("shouldEndSession flag should be true in the response",
                json.contains("\"shouldEndSession\" : true"));
        assertTrue(response.getNullableShouldEndSession());
        assertTrue(response.getShouldEndSession());
    }


    /**
     * Tests that the "shouldEndSession" flag is present in the serialized JSON when set to false by
     * nullableShouldSendSession.
     */
    @Test
    public void serialization_nullableShouldEndSession_false()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setNullableShouldEndSession(false);
        envelope.setResponse(response);

        testSerialization(envelope);
        String json = OBJECT_WRITER.writeValueAsString(envelope);
        assertTrue("shouldEndSession flag should be false in the response",
                json.contains("\"shouldEndSession\" : false"));
        assertFalse(response.getNullableShouldEndSession());
        assertFalse(response.getShouldEndSession());
    }

    /**
     * Tests that the session attributes serialized properly.
     */
    @Test
    public void serialization_withSessionAttributes_responseSerializedCorrectly() throws Exception {
        envelope.setSessionAttributes(buildSessionAttributes());
        testSerialization(envelope);
    }

    /**
     * Tests that a plain text output speech response is serialized properly.
     */
    @Test
    public void plainTextOutputSpeechResponse() throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildPlainTextOutputSpeech());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that a plain text output speech response with a simple UI card is serialized properly.
     */
    @Test
    public void serialization_plainTextOutputSpeechWithSimpleCardResponse_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildPlainTextOutputSpeech());
        response.setCard(buildSimpleCard());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that a plain text output speech response for a card containing text and image
     * information is serialized properly.
     */
    @Test
    public void serialization_plainTextOutputSpeechWithStandardCardResponse_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildPlainTextOutputSpeech());
        response.setCard(buildStandardCard());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that a reprompt response containing plaintext is serialized correctly.
     */
    @Test
    public void serialization_responseContainsPlainTextReprompt_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setReprompt(buildReprompt(buildPlainTextOutputSpeech()));

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that an SSML output speech response is serialized properly.
     */
    @Test
    public void serialization_ssmlSpeechResponse_responseSerializedCorrectly() throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildSsmlOutputSpeech());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that an SSML output speech response with a simple UI card is serialized properly.
     */
    @Test
    public void serialization_ssmlOutputSpeechWithSimpleCardResponse_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildSsmlOutputSpeech());
        response.setCard(buildSimpleCard());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that an SSML output speech response for a card containing text and image information is
     * serialized properly.
     */
    @Test
    public void serialization_ssmlOutputSpeechWithStandardCardResponse_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(buildSsmlOutputSpeech());
        response.setCard(buildStandardCard());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    /**
     * Tests that a reprompt response containing SSML is serialized correctly.
     */
    @Test
    public void serialization_responseContainsSsmlReprompt_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setReprompt(buildReprompt(buildSsmlOutputSpeech()));

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    @Test
    public void serialization_responseContainsLinkAccountCard_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        LinkAccountCard card = new LinkAccountCard();
        response.setCard(card);

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    @Test
    public void serialization_responseContainsAskForPermissionsConsentCard_responseSerializedCorrectly()
            throws Exception {
        final SpeechletResponse response = new SpeechletResponse();
        response.setCard(buildAskForPermissionsConsentCard());

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    @Test
    public void serialization_responseContainsDirective_responseSerializedCorrectly()
            throws Exception {
        Stream stream = new Stream();
        stream.setToken(TOKEN);
        AudioItem audioItem = new AudioItem();
        audioItem.setStream(stream);
        PlayDirective playDirective = new PlayDirective();
        playDirective.setPlayBehavior(PlayBehavior.ENQUEUE);
        playDirective.setAudioItem(audioItem);

        StopDirective stopDirective = new StopDirective();

        Map<String, DialogSlot> dialogSlots =
            ImmutableMap.of("toCity", new DialogSlot("toCity", "Mumbai", ConfirmationStatus.CONFIRMED),
                             "fromCity", new DialogSlot("fromCity", "Hyderabad", ConfirmationStatus.NONE));

        DialogIntent dialogIntent = new DialogIntent("DialogIntent", ConfirmationStatus.NONE, dialogSlots);

        DelegateDirective delegateDirective = new DelegateDirective();
        delegateDirective.setUpdatedIntent(dialogIntent);

        ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
        elicitSlotDirective.setSlotToElicit("toCity");

        ConfirmSlotDirective confirmSlotDirective = new ConfirmSlotDirective();
        confirmSlotDirective.setSlotToConfirm("fromCity");

        ConfirmIntentDirective confirmIntentDirective = new ConfirmIntentDirective();

        final SpeechletResponse response = new SpeechletResponse();
        response.setDirectives(Arrays.<Directive>asList(playDirective, stopDirective,
                delegateDirective, elicitSlotDirective,
                confirmIntentDirective, confirmSlotDirective));

        envelope.setResponse(response);

        testSerialization(envelope);
    }

    // -----------------------
    // Helper factory methods

    /**
     * @return a test plain text output speech.
     */
    private PlainTextOutputSpeech buildPlainTextOutputSpeech() {
        final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("Hello World!");
        speech.setId("speech_id");

        return speech;
    }

    /**
     * @return a test SSML text output speech.
     */
    private SsmlOutputSpeech buildSsmlOutputSpeech() {
        final SsmlOutputSpeech sssmlSpeech = new SsmlOutputSpeech();
        sssmlSpeech.setSsml("<speak>Hello world!</speak>");
        sssmlSpeech.setId("ssml_speech_id");

        return sssmlSpeech;
    }

    /**
     * @return a test UI card.
     */
    private Card buildSimpleCard() {
        final SimpleCard card = new SimpleCard();
        card.setTitle("Hello World Title");
        card.setContent("Hello World Content");

        return card;
    }

    private Card buildStandardCard() {
        final StandardCard card = new StandardCard();
        card.setTitle("Hello World Title");
        card.setText("Hello World Content");

        Image image = new Image();
        image.setSmallImageUrl("https://example.com/smallImgUrl.jpg");
        image.setLargeImageUrl("https://example.com/largeImgUrl.jpg");
        card.setImage(image);

        return card;
    }

    private Card buildAskForPermissionsConsentCard() {
        final AskForPermissionsConsentCard card = new AskForPermissionsConsentCard();
        Set<String> permissions = new HashSet<>();
        permissions.add("TEST_SCOPE");
        card.setPermissions(permissions);
        return card;
    }

    private Reprompt buildReprompt(OutputSpeech outputSpeech) {
        final Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return reprompt;
    }
 }
