/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.List;

import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.Reprompt;

/**
 * The response to a {@code SpeechletV2} invocation. Defines text to speak to the user, content to
 * display in the companion application, and whether or not the current {@code SpeechletV2} session
 * should end.
 *
 * @see SpeechletV2#onLaunch(SpeechletRequestEnvelope)
 * @see SpeechletV2#onIntent(SpeechletRequestEnvelope)
 */
public class SpeechletResponse {
    private OutputSpeech outputSpeech = null;
    private Card card = null;
    private List<Directive> directives = null;
    private Reprompt reprompt = null;
    private boolean shouldEndSession = true;

    /**
     * Returns the speech associated with this response.
     *
     * @return the speech
     */
    public OutputSpeech getOutputSpeech() {
        return outputSpeech;
    }

    /**
     * Sets the speech associated with this response.
     *
     * @param outputSpeech
     *            the speech to set
     */
    public void setOutputSpeech(final OutputSpeech outputSpeech) {
        this.outputSpeech = outputSpeech;
    }

    /**
     * Returns whether or not the session should end with this response.
     *
     * @return whether the session should end
     */
    public boolean getShouldEndSession() {
        return shouldEndSession;
    }

    /**
     * Sets whether or not the session should end with this response.
     *
     * @param shouldEndSession
     *            {@code true} if the session should end with this response
     */
    public void setShouldEndSession(final boolean shouldEndSession) {
        this.shouldEndSession = shouldEndSession;
    }

    /**
     * Returns the UI card associated with this response.
     *
     * @return the UI card to set
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the UI card associated with this response.
     *
     * @param card
     *            the UI card to set
     */
    public void setCard(final Card card) {
        this.card = card;
    }

    /**
     * Returns the directives associated with this response.
     *
     * @return the directives
     */
    public List<Directive> getDirectives() {
        return directives;
    }

    /**
     * Sets the directives for this response.
     *
     * @param directives
     *            the directives
     */
    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    /**
     * Returns the reprompt associated with this response.
     *
     * @return the reprompt
     */
    public Reprompt getReprompt() {
        return reprompt;
    }

    /**
     * Sets the reprompt associated with this response.
     *
     * @param reprompt
     *            the reprompt
     */
    public void setReprompt(final Reprompt reprompt) {
        this.reprompt = reprompt;
    }

    /**
     * Creates and returns a response intended to tell the user something. After the specified
     * output is read to the user, the session ends. Note that the response created with this method
     * does not include a graphical card for the companion app.
     * <p>
     * All arguments in this method are required and cannot be null.
     *
     * @param outputSpeech
     *            output speech content for the tell voice response
     * @return SpeechletResponse spoken response for the given input
     */
    public static SpeechletResponse newTellResponse(final OutputSpeech outputSpeech) {
        if (outputSpeech == null) {
            throw new IllegalArgumentException("OutputSpeech cannot be null");
        }

        SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(true);
        response.setOutputSpeech(outputSpeech);
        return response;
    }

    /**
     * Creates and returns a response intended to tell the user something, both in speech and with a
     * graphical card in the companion app. After the tell output is read to the user, the session
     * ends.
     * <p>
     * All arguments in this method are required and cannot be null.
     *
     * @param outputSpeech
     *            output speech content for the tell voice response
     * @param card
     *            card to display in the companion application
     * @return SpeechletResponse spoken and visual response for the given input
     */
    public static SpeechletResponse newTellResponse(final OutputSpeech outputSpeech, final Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        SpeechletResponse response = newTellResponse(outputSpeech);
        response.setCard(card);
        return response;
    }

    /**
     * Creates and returns a response intended to ask the user a question. After the ask response is
     * read to the user, the user can respond and continue to interact with the skill. The session
     * does not immediately end after the ask response. Note that the response created with this
     * method does not include a graphical card for the companion app.
     * <p>
     * All arguments in this method are required and cannot be null.
     *
     * @param outputSpeech
     *            output speech content for the ask voice response
     * @param reprompt
     *            reprompt speech for the ask voice response. This speech is played if the user does
     *            not reply to the question or replies with something that is not understood.
     * @return SpeechletResponse spoken response for the given input
     */
    public static SpeechletResponse newAskResponse(final OutputSpeech outputSpeech,
            final Reprompt reprompt) {
        if (outputSpeech == null) {
            throw new IllegalArgumentException("OutputSpeech cannot be null");
        }

        if (reprompt == null) {
            throw new IllegalArgumentException("Reprompt cannot be null");
        }

        SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(false);
        response.setOutputSpeech(outputSpeech);
        response.setReprompt(reprompt);
        return response;
    }

    /**
     * Creates and returns a response intended to ask the user a question, both in speech and with a
     * graphical card displayed in the companion app. After the ask response is read to the user,
     * the user can respond and continue to interact with the skill. The session does not
     * immediately end after the ask response.
     * <p>
     * All arguments in this method are required and cannot be null.
     *
     * @param outputSpeech
     *            output speech content for the ask voice response
     * @param reprompt
     *            reprompt speech for the ask voice response. This speech is played if the user does
     *            not reply to the question or replies with something that is not understood.
     * @param card
     *            card to display in the companion application
     * @return SpeechletResponse spoken and visual response for the given input
     */
    public static SpeechletResponse newAskResponse(final OutputSpeech outputSpeech,
            final Reprompt reprompt, final Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        SpeechletResponse response = newAskResponse(outputSpeech, reprompt);
        response.setCard(card);
        return response;
    }
}
