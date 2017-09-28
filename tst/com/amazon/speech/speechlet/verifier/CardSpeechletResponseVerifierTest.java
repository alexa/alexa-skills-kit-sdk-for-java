package com.amazon.speech.speechlet.verifier;

import static com.amazon.speech.speechlet.verifier.CardSpeechletResponseVerifier.MAX_URL_LENGTH;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.StandardCard;

public class CardSpeechletResponseVerifierTest extends SpeechletRequestResponseVerifierTestBase {
    private CardSpeechletResponseVerifier verifier;

    @Before
    public void setup() {
        verifier = new CardSpeechletResponseVerifier();
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
    public void testVerify_cardOfAnonType() {
        Card card = new Card() {
        };
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), card)),
                buildSession()));
    }

    @Test
    public void testVerify_simpleCard() {
        SimpleCard simpleCard = new SimpleCard();
        simpleCard.setTitle("123456");
        simpleCard.setContent("123456");
        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), simpleCard)),
                buildSession()));
    }

    @Test
    public void testVerify_standardCardValidContent() {
        StandardCard standardCard = new StandardCard();
        standardCard.setTitle("Some title");
        standardCard.setText("Some text");

        Image image = new Image();
        image.setLargeImageUrl("https://example.com/largeImgUrl.jpg");
        image.setSmallImageUrl("https://example.com/smallImgUrl.jpg");
        standardCard.setImage(image);

        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), standardCard)),
                buildSession()));
    }

    @Test
    public void testVerify_standardCardNullImage() {
        StandardCard standardCard = new StandardCard();
        standardCard.setTitle("Some title");
        standardCard.setText("Some text");
        standardCard.setImage(null);

        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), standardCard)),
                buildSession()));
    }

    @Test
    public void testVerify_standardCardUnsupportedProtocol() {
        StandardCard standardCard = new StandardCard();
        standardCard.setTitle("Some title");
        standardCard.setText("Some text");

        Image image = new Image();
        image.setLargeImageUrl("http://example.com/largeImgUrl.jpg");
        image.setSmallImageUrl("http://example.com/smallImgUrl.jpg");
        standardCard.setImage(image);

        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), standardCard)),
                buildSession()));
    }

    @Test
    public void testVerify_standardCardMaxImageUrlLengthExceeded() {
        StandardCard standardCard = new StandardCard();
        standardCard.setTitle("Some title");
        standardCard.setText("Some text");

        Image image = new Image();
        String url = "https://example.com/image.jpg?";
        int urlLengthToBeGenerated = MAX_URL_LENGTH - url.length();
        image.setLargeImageUrl(url + RandomStringUtils.randomAlphanumeric(urlLengthToBeGenerated));
        image.setSmallImageUrl(url + RandomStringUtils.randomAlphanumeric(urlLengthToBeGenerated));
        standardCard.setImage(image);

        assertTrue(verifier.verify(
                buildResponseEnvelope(Sdk.VERSION,
                        SpeechletResponse.newTellResponse(mock(OutputSpeech.class), standardCard)),
                buildSession()));
    }
}
