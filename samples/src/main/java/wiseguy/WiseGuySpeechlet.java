/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package wiseguy;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
 * This sample shows how to create a Lambda function for handling Alexa Skill requests that:
 * 
 * <ul>
 * <li><b>Session State</b>: Handles a multi-turn dialog model.</li>
 * <li><b>Custom slot type</b>: demonstrates using custom slot types to handle a finite set of known values</li>
 * <li><b>SSML</b>: Using SSML tags to control how Alexa renders the text-to-speech</li>
 * </ul>
 * <p>
 * <h2>Examples</h2>
 * <p>
 * <b>Dialog model</b>
 * <p>
 * User: "Alexa, ask Wise Guy to tell me a knock knock joke."
 * <p>
 * Alexa: "Knock knock"
 * <p>
 * User: "Who's there?"
 * <p>
 * Alexa: "<phrase>"
 * <p>
 * User: "<phrase> who"
 * <p>
 * Alexa: "<Punchline>"
 */
public class WiseGuySpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(WiseGuySpeechlet.class);

    /**
     * Session attribute to store the stage the joke is at.
     */
    private static final String SESSION_STAGE = "stage";

    /**
     * Session attribute to store the current Joke ID.
     */
    private static final String SESSION_JOKE_ID = "jokeid";

    /**
     * Stage 1 indicates we've already said 'knock knock' and will set up the joke next.
     */
    private static final int KNOCK_KNOCK_STAGE = 1;
    /**
     * Stage 2 indicates we've set up the joke and will deliver the punchline next.
     */
    private static final int SETUP_STAGE = 2;

    /**
     * ArrayList containing knock knock jokes.
     */
    private static final ArrayList<Joke> JOKE_LIST = new ArrayList<Joke>();

    static {
        JOKE_LIST.add(new Joke("To", "Correct grammar is <break time=\"0.2s\" /> to whom.", "Correct grammar is 'to whom'."));
        JOKE_LIST.add(new Joke("Beets!", "Beats me!", "Beats me!"));
        JOKE_LIST.add(new Joke("Little old lady", "I didn't know you could yodel!", "I didn't know you could yodel!"));
        JOKE_LIST.add(new Joke("A broken pencil", "Never mind. It's pointless.", "Never mind. It's pointless."));
        JOKE_LIST.add(new Joke("Snow", "Snow use. I forgot.", "Snow use. I forgot."));
        JOKE_LIST.add(new Joke("Boo", "Aww <break time=\"0.3s\" /> it's okay <break time=\"0.3s\" /> don't cry.", "Aww, it's okay, don't cry."));
        JOKE_LIST.add(new Joke("Woo", "Don't get so excited, it's just a joke.", "Don't get so excited, it's just a joke."));
        JOKE_LIST.add(new Joke("Spell", "<say-as interpret-as=\"characters\">who</say-as>", "w.h.o"));
        JOKE_LIST.add(new Joke("Atch", "I didn't know you had a cold!", "I didn't know you had a cold!"));
        JOKE_LIST.add(new Joke("Owls", "Yes, they do.", "Yes, they do."));
        JOKE_LIST.add(new Joke("Berry", "Berry nice to meet you.", "Berry nice to meet you."));
    }

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return handleTellMeAJokeIntent(session);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("TellMeAJokeIntent".equals(intentName)) {
            return handleTellMeAJokeIntent(session);
        } else if ("WhosThereIntent".equals(intentName)) {
            return handleWhosThereIntent(session);
        } else if ("SetupNameWhoIntent".equals(intentName)) {
            return handleSetupNameWhoIntent(session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            String speechOutput = "";
            int stage = -1;
            if (session.getAttributes().containsKey(SESSION_STAGE)) {
                stage = (Integer) session.getAttribute(SESSION_STAGE);
            }
            switch (stage) {
                case 0:
                    speechOutput =
                            "Knock knock jokes are a fun call and response type of joke. "
                                    + "To start the joke, just ask by saying tell me a"
                                    + " joke, or you can say exit.";
                    break;
                case 1:
                    speechOutput = "You can ask, who's there, or you can say exit.";
                    break;
                case 2:
                    speechOutput = "You can ask, who, or you can say exit.";
                    break;
                default:
                    speechOutput =
                            "Knock knock jokes are a fun call and response type of joke. "
                                    + "To start the joke, just ask by saying tell me a "
                                    + "joke, or you can say exit.";
            }

            String repromptText = speechOutput;
            return newAskResponse(speechOutput, false, repromptText, false);
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any session cleanup logic would go here
    }

    /**
     * Selects a joke randomly and starts it off by saying "Knock knock".
     *
     * @param session
     *            the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleTellMeAJokeIntent(final Session session) {
        String speechOutput = "";

        // Reprompt speech will be triggered if the user doesn't respond.
        String repromptText = "You can ask, who's there";

        // / Select a random joke and store it in the session variables
        int jokeID = (int) Math.floor(Math.random() * JOKE_LIST.size());

        // The stage variable tracks the phase of the dialogue.
        // When this function completes, it will be on stage 1.
        session.setAttribute(SESSION_STAGE, KNOCK_KNOCK_STAGE);
        session.setAttribute(SESSION_JOKE_ID, jokeID);
        speechOutput = "Knock knock!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Wise Guy");
        card.setContent(speechOutput);

        SpeechletResponse response = newAskResponse(speechOutput, false,
                repromptText, false);
        response.setCard(card);
        return response;
    }

    /**
     * Responds to the user saying "Who's there".
     *
     * @param session
     *            the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleWhosThereIntent(final Session session) {
        String speechOutput = "", repromptText = "";
        if (session.getAttributes().containsKey(SESSION_STAGE)) {
            if ((Integer) session.getAttribute(SESSION_STAGE) == KNOCK_KNOCK_STAGE) {
                // Retrieve the joke's setup text.
                int jokeID = (Integer) session.getAttribute(SESSION_JOKE_ID);
                speechOutput = JOKE_LIST.get(jokeID).setup;

                // Advance the stage of the dialogue.
                session.setAttribute(SESSION_STAGE, SETUP_STAGE);

                repromptText = "You can ask, " + speechOutput + " who?";

            } else {
                session.setAttribute(SESSION_STAGE, KNOCK_KNOCK_STAGE);
                speechOutput = "That's not how knock knock jokes work! <break time=\"0.3s\" /> Knock knock";
                repromptText = "You can ask who's there.";
            }
        } else {
            // If the session attributes are not found, the joke must restart.
            speechOutput =
                    "Sorry, I couldn't correctly retrieve the joke. You can say, tell me a joke.";
            repromptText = "You can say, tell me a joke.";
        }

        return newAskResponse("<speak>" + speechOutput + "</speak>", true, repromptText, false);
    }

    /**
     * Delivers the punchline of the joke after the user responds to the setup.
     *
     * @param session
     *            the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleSetupNameWhoIntent(final Session session) {
        String speechOutput = "", repromptText = "";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Wise Guy");

        if (session.getAttributes().containsKey(SESSION_STAGE)) {
            if ((Integer) session.getAttribute(SESSION_STAGE) == SETUP_STAGE) {
                int jokeID = (Integer) session.getAttribute(SESSION_JOKE_ID);
                speechOutput = JOKE_LIST.get(jokeID).speechPunchline;
                card.setContent(JOKE_LIST.get(jokeID).cardPunchline);

                // Create the ssml text output
                SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
                outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");

                // If the joke completes successfully, this function will end the active session
                return SpeechletResponse.newTellResponse(outputSpeech, card);
            } else {
                session.setAttribute(SESSION_STAGE, KNOCK_KNOCK_STAGE);
                speechOutput = "That's not how knock knock jokes work! <break time=\"0.3s\" /> Knock knock";
                repromptText = "You can ask who's there.";

                card.setContent("That's not how knock knock jokes work! Knock knock");

                // Create the ssml text output
                SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
                outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");
                PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
                repromptOutputSpeech.setText(repromptText);
                Reprompt repromptSpeech = new Reprompt();
                repromptSpeech.setOutputSpeech(repromptOutputSpeech);

                // If the joke has to be restarted, then keep the session alive
                return SpeechletResponse.newAskResponse(outputSpeech, repromptSpeech, card);
            }
        } else {
            speechOutput =
                    "Sorry, I couldn't correctly retrieve the joke. You can say, tell me a joke";
            repromptText = "You can say, tell me a joke";
            card.setContent(speechOutput);
            SpeechletResponse response = newAskResponse(speechOutput, false,
                    repromptText, false);
            response.setCard(card);
            return response;
        }
    }

    /**
     * Wrapper for creating the Ask response from the input strings.
     * 
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
            String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

    private static class Joke {

        private final String setup;
        private final String speechPunchline;
        private final String cardPunchline;

        Joke(String setup, String speechPunchline, String cardPunchline) {
            this.setup = setup;
            this.speechPunchline = speechPunchline;
            this.cardPunchline = cardPunchline;
        }
    }
}
