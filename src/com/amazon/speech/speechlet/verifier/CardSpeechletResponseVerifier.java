/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.verifier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.SimpleCard;

/**
 * A {@link SpeechletResponseVerifier} to verify the size of the {@link Card} returned in
 * {@link SpeechletResponseEnvelope}. In the current implementation, the
 * {@link #verify(SpeechletResponseEnvelope, Session)} method logs a warning if the card size in
 * bytes exceeds {@value #MAX_CARD_SIZE}.
 * <p>
 * Note: This verifier currently does not not enforce response checks and always returns true. The
 * primary purpose of this verifier is to log a warning in the app developer's runtime.
 */
public class CardSpeechletResponseVerifier implements SpeechletResponseVerifier {
    private static final Logger log = LoggerFactory.getLogger(CardSpeechletResponseVerifier.class);

    private static final int MAX_CARD_SIZE = 8000;

    @Override
    public boolean verify(SpeechletResponseEnvelope responseEnvelope, Session session) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null) {
            return true;
        }

        Card card = responseEnvelope.getResponse().getCard();
        if (card instanceof SimpleCard) {
            SimpleCard simpleCard = (SimpleCard) card;
            int cardSize =
                    StringUtils.length(simpleCard.getContent())
                            + StringUtils.length(simpleCard.getTitle());
            if (cardSize > MAX_CARD_SIZE) {
                log.warn("Card with size {} exceeds the maximum allowed size of {} and will be "
                        + "rejected by the Alexa service", cardSize, MAX_CARD_SIZE);
            }
        }

        // We are currently not enforcing response checks. Always return true.
        return true;
    }
}
