/*
 * Copyright 2016-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 * this file except in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package plantripdialog;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a sample skill that illustrates how to use the Skill Builder (Beta) to Define Intents, Slots, and Dialogs.
 */
public class PlanTripDialogSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(PlanTripDialogSpeechlet.class);

    private static final String WELCOME_TEXT = "Welcome to the Sample Plan Trip Skill! What do you want to ask?";
    private static final String HELP_TEXT = "You can use this skill by asking something like: I want to go on a trip.";
    private static final String UNHANDLED_TEXT = "This is unsupported. Please ask something else.";
    private static final String ERROR_TEXT = "There was an error with the skill. Please try again.";

    private final IntentHandler intentHandler;

    @Inject
    public PlanTripDialogSpeechlet(@Named("PlanMyTripIntent") IIntentHandler planMyTripIntentHandler) {
        intentHandler = IntentHandler.builder()
                .with("PlanMyTripIntent", planMyTripIntentHandler)
                .with("AMAZON.HelpIntent", request -> {
                    final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                    outputSpeech.setText(HELP_TEXT);
                    final Reprompt reprompt = new Reprompt();
                    reprompt.setOutputSpeech(outputSpeech);
                    return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                })
                .withDefault(request -> {
                    log.info("Unhandled Intent {}", request.getRequest().getIntent().getName());

                    // Unhandled Intent
                    final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                    outputSpeech.setText(UNHANDLED_TEXT);
                    final Reprompt reprompt = new Reprompt();
                    reprompt.setOutputSpeech(outputSpeech);
                    return SpeechletResponse.newTellResponse(outputSpeech);
                })
                .build();
    }

    /**
     * This is fired when a session is started. Here we could potentially initialize a session in
     * our own service for the user, or update a record in a table, etc.
     * @param speechletRequestEnvelope container for the speechlet request.
     */
    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {
        SessionStartedRequest sessionStartedRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onSessionStarted requestId={}, sessionId={}", sessionStartedRequest.getRequestId(),
            session.getSessionId());
    }

    /**
     * When our skill session is started, a launch event will be triggered. In the case of this
     * sample skill, we will return a welcome message, however the sky is the limit.
     * @param speechletRequestEnvelope container for the speechlet request.
     * @return SpeechletResponse our welcome message
     */
    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        LaunchRequest launchRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onLaunch requestId={}, sessionId={}", launchRequest.getRequestId(),
            session.getSessionId());

        final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(WELCOME_TEXT);
        final Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

    /**
     * When we receive an intent, this will be triggered. This function will handle the processing
     * of that intent based on the intentName.
     * @param speechletRequestEnvelope container for the speechlet request.
     * @return SpeechletResponse a message of our address or an error message
     */
    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest intentRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onIntent requestId={}, sessionId={}", intentRequest.getRequestId(),
            session.getSessionId());

        try {
            return intentHandler.handle(speechletRequestEnvelope);
        } catch (SpeechletRequestHandlerException e) {
            log.error("Failed to handle intent", e);

            final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText(ERROR_TEXT);
            final Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(outputSpeech);
            return SpeechletResponse.newTellResponse(outputSpeech);
        }
    }

    /**
     * Similar to onSessionStarted, this method will be fired when the skill session has been closed.
     * @param speechletRequestEnvelope container for the speechlet request.
     */
    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        SessionEndedRequest sessionEndedRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onSessionEnded requestId={}, sessionId={}", sessionEndedRequest.getRequestId(),
            session.getSessionId());
    }
}
