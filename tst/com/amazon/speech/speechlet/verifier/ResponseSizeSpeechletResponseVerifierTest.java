package com.amazon.speech.speechlet.verifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.StandardCard;

public class ResponseSizeSpeechletResponseVerifierTest extends
        SpeechletRequestResponseVerifierTestBase {
    private ResponseSizeSpeechletResponseVerifier verifier;

    @Before
    public void setup() {
        verifier = new ResponseSizeSpeechletResponseVerifier();
    }

    @Test
    public void testVerify_nullRequestEnvelope() {
        assertFalse(verifier.verify(null, buildSession()));
    }

    @Test
    public void testVerify_nullResponse() {
        assertTrue(verifier.verify(buildResponseEnvelope(Sdk.VERSION, null), buildSession()));
    }

    @Test
    public void verify_receviesValidTextContentAndSimpleCard_passes() {
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION, SpeechletResponse.newTellResponse(
                        new PlainTextOutputSpeech(), new SimpleCard())), buildSession()));
    }

    @Test
    public void verify_receivesValidSsmlContentAndSimpleCard_passes() {
        assertTrue(verifier
                .verify(buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(new SsmlOutputSpeech(), new SimpleCard())),
                        buildSession()));
    }

    @Test
    public void verify_receivesValidTextContentAndStandardCard_passes() {
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION, SpeechletResponse.newTellResponse(
                        new PlainTextOutputSpeech(), new StandardCard())), buildSession()));
    }

    @Test
    public void verify_receivesValidSsmlContentAndStandardCard_passes() {
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION, SpeechletResponse.newTellResponse(
                        new SsmlOutputSpeech(), new StandardCard())), buildSession()));
    }
}
