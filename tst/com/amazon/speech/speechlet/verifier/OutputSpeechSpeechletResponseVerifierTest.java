package com.amazon.speech.speechlet.verifier;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;

public class OutputSpeechSpeechletResponseVerifierTest extends
        SpeechletRequestResponseVerifierTestBase {
    private OutputSpeechSpeechletResponseVerifier verifier;

    @Before
    public void setup() {
        verifier = new OutputSpeechSpeechletResponseVerifier();
    }

    @Test
    public void testVerify_nullRequestEnvelope() {
        assertTrue(verifier.verify(null, buildSession()));
    }

    @Test
    public void testVerify_nullResponse() {
        assertTrue(verifier.verify(buildResponseEnvelope(Sdk.VERSION, null), buildSession()));
    }

    @Test
    public void testVerify_outputSpeechOfAnonType() {
        OutputSpeech outputSpeech = new OutputSpeech() {
        };

        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(outputSpeech, mock(Card.class))),
                buildSession()));
    }

    @Test
    public void verify_receivesValidTextContent_passes() {
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setId("test");
        outputSpeech.setText("123456");
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(outputSpeech, mock(Card.class))),
                buildSession()));
    }

    @Test
    public void verify_receivesValidSsmlContent_passes() {
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setId("test");
        outputSpeech.setSsml("<speak>123456</speak>");
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(outputSpeech, mock(Card.class))),
                buildSession()));
    }
}
