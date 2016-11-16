/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.verifier;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.StandardCard;

/**
 * A {@link SpeechletResponseVerifier} to verify the {@link Card} returned by
 * {@link SpeechletResponseEnvelope}. In the current implementation, the
 * {@link #verify(SpeechletResponseEnvelope, Session)} method logs warnings if the validation of the
 * card fails.
 * <p>
 * Note: This verifier currently does not not enforce response checks and always returns true. The
 * primary purpose of this verifier is to log a warning in the app developer's runtime.
 */
public class CardSpeechletResponseVerifier implements SpeechletResponseVerifier {
    private static final Logger log = LoggerFactory.getLogger(CardSpeechletResponseVerifier.class);

    private static final int MAX_CARD_SIZE = 8000;
    protected static final int MAX_URL_LENGTH = 2000;
    private static final String VALID_IMAGE_PROTOCOL = "https";

    @Override
    public boolean verify(SpeechletResponseEnvelope responseEnvelope, Session session) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null) {
            return true;
        }

        Card card = responseEnvelope.getResponse().getCard();
        if (card instanceof SimpleCard) {
            SimpleCard simpleCard = (SimpleCard) card;
            verifyCardSize(simpleCard.getTitle(), simpleCard.getContent());
        } else if (card instanceof StandardCard) {
            StandardCard standardCard = (StandardCard) card;
            Image image = standardCard.getImage();
            if (image != null) {
                verifyCardSize(standardCard.getTitle(), standardCard.getText(),
                        image.getSmallImageUrl(), image.getLargeImageUrl());
            } else {
                verifyCardSize(standardCard.getTitle(), standardCard.getText());
            }
            verifyImageAttributes(standardCard);
        }

        // We are currently not enforcing response checks. Always return true.
        return true;
    }

    /**
     * Checks the length of the card elements and it logs a warning if the card size exceeds
     * {@value #MAX_CARD_SIZE} characters.
     *
     * @param cardElements
     *            the elements of the card
     */
    private void verifyCardSize(String... cardElements) {
        int cardSize = 0;
        for (String element : cardElements) {
            cardSize += StringUtils.length(element);
        }
        if (cardSize > MAX_CARD_SIZE) {
            log.warn("Card with size {} exceeds the maximum allowed size of {} and will be "
                    + "rejected by the Alexa service", cardSize, MAX_CARD_SIZE);
        }
    }

    /**
     * Verifies the attributes of an image bundle that is used for displaying and image on a card. A
     * warning message is logged if the provided card does not contain an image bundle.
     *
     * @param card
     *            the card containing the image bundle to be checked
     */
    private void verifyImageAttributes(StandardCard card) {
        Image image = card.getImage();

        if (image != null) {
            verifyImageUrl("smallImageUrl", image.getSmallImageUrl());
            verifyImageUrl("largeImageUrl", image.getLargeImageUrl());
        }
    }

    /**
     * Verifies the correctness of an image URL.
     *
     * @param imageUrlType
     *            the type of the image URL that is being verified
     * @param imageUrl
     *            the image URL to be checked
     */
    private void verifyImageUrl(String imageUrlType, String imageUrl) {
        if (imageUrl != null) {
            verifyLengthImageUrl(imageUrlType, imageUrl);
            verifyProtocolImageUrl(imageUrlType, imageUrl);
        }
    }

    /**
     * Verifies the length of the provided URL and it logs a warning if it exceeds
     * {@value #MAX_URL_LENGTH} characters.
     *
     * @param imageUrlType
     *            the type of the image URL that is being verified
     * @param imageUrl
     *            the URL to be checked
     */
    private void verifyLengthImageUrl(String imageUrlType, String imageUrl) {
        int length = imageUrl.length();
        if (MAX_URL_LENGTH < length) {
            log.warn("The length of {} exceeds the maximum allowed of "
                    + "{} for the image of the card of type StandardCard.", imageUrlType, length);
        }
    }

    /**
     * Verifies the protocol of the provided URL and it logs a warning if the protocol is different
     * than {@value #VALID_IMAGE_PROTOCOL}.
     *
     * @param imageUrlType
     *            the type of the image URL that is being verified
     * @param imageUrl
     *            the URL to be checked
     */
    private void verifyProtocolImageUrl(String imageType, String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String protocol = url.getProtocol();
            if (!VALID_IMAGE_PROTOCOL.equals(protocol)) {
                log.warn("{} with value {} is invalid for the image of the card of type "
                        + "StandardCard since HTTPS is required", imageType, imageUrl);
            }
        } catch (MalformedURLException e) {
            log.warn("{} with value {} is malformed for the image of the card of type "
                    + "StandardCard", imageType, imageUrl);
        }
    }
}
