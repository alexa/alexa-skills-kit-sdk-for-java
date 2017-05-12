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

package address;

import java.util.HashSet;
import java.util.Set;
import address.exceptions.DeviceAddressClientException;
import address.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
 * This is a sample skill that illustrates how to query the Alexa Device Address API.
 */
public class DeviceAddressSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(DeviceAddressSpeechlet.class);

    /**
     * This is the default title that this skill will be using for cards.
     */
    private static final String ADDRESS_CARD_TITLE = "Sample Device Address Skill";

    /**
     * The permissions that this skill relies on for retrieving addresses. If the consent token isn't
     * available or invalid, we will request the user to grant us the following permission
     * via a permission card.
     *
     * Another Possible value if you only want permissions for the country and postal code is:
     * read::alexa:device:all:address:country_and_postal_code
     * Be sure to check your permissions settings for your skill on https://developer.amazon.com/
     */
    private static final String ALL_ADDRESS_PERMISSION = "read::alexa:device:all:address";

    private static final String WELCOME_TEXT = "Welcome to the Sample Device Address API Skill! What do you want to ask?";
    private static final String HELP_TEXT = "You can use this skill by asking something like: whats my address";
    private static final String UNHANDLED_TEXT = "This is unsupported. Please ask something else.";
    private static final String ERROR_TEXT = "There was an error with the skill. Please try again.";

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

        return getAskResponse(ADDRESS_CARD_TITLE, WELCOME_TEXT);
    }

    /**
     * When we receive an intent, this will be triggered. This function will handle the processing
     * of that intent based on the intentName. In the case of a GetAddress intent, it will
     * query the address API.
     * @param speechletRequestEnvelope container for the speechlet request.
     * @return SpeechletResponse a message of our address or an error message
     */
    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest intentRequest = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();

        log.info("onIntent requestId={}, sessionId={}", intentRequest.getRequestId(),
            session.getSessionId());

        Intent intent = intentRequest.getIntent();
        String intentName = getIntentName(intent);

        log.info("Intent received: {}", intentName);

        // We want to handle each intent differently here, so that we can give each a unique response.
        // Refer to the Interaction Model for more information:
        // https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/alexa-skills-kit-interaction-model-reference
        switch(intentName) {
            // This is the custom intent that delivers the main functionality of the sample skill.
            // Refer to speechAssets/SampleUtterances for examples that would trigger this.
            case "GetAddress":
                String consentToken = session.getUser().getPermissions().getConsentToken();

                if (consentToken == null) {
                    log.info("The user hasn't authorized the skill. Sending a permissions card.");
                    return getPermissionsResponse();
                }

                try {
                    SystemState systemState = getSystemState(speechletRequestEnvelope.getContext());

                    String deviceId = systemState.getDevice().getDeviceId();
                    String apiEndpoint = systemState.getApiEndpoint();

                    AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(
                        deviceId, consentToken, apiEndpoint);

                    Address addressObject = alexaDeviceAddressClient.getFullAddress();

                    if (addressObject == null) {
                        return getAskResponse(ADDRESS_CARD_TITLE, ERROR_TEXT);
                    }

                    return getAddressResponse(
                        addressObject.getAddressLine1(),
                        addressObject.getStateOrRegion(),
                        addressObject.getPostalCode());
                } catch (UnauthorizedException e) {
                    return getPermissionsResponse();
                } catch (DeviceAddressClientException e) {
                    log.error("Device Address Client failed to successfully return the address.", e);
                    return getAskResponse(ADDRESS_CARD_TITLE, ERROR_TEXT);
                }
            // This is one of the many Amazon built in intents.
            // Refer to the following for a list of all available built in intents:
            // https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/built-in-intent-ref/standard-intents
            case "AMAZON.HelpIntent":
                return getAskResponse(ADDRESS_CARD_TITLE, HELP_TEXT);
            default:
                return getAskResponse(ADDRESS_CARD_TITLE, UNHANDLED_TEXT);
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

    /**
     * Creates a {@code SpeechletResponse} for the GetAddress intent.
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getAddressResponse(String streetName, String state, String zipCode) {
        String speechText = "Your address is " + streetName + " " + state + ", " + zipCode;

        SimpleCard card = getSimpleCard(ADDRESS_CARD_TITLE, speechText);

        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for permission requests.
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getPermissionsResponse() {
        String speechText = "You have not given this skill permissions to access your address. " +
            "Please give this skill permissions to access your address.";

        // Create the permission card content.
        // The differences between a permissions card and a simple card is that the
        // permissions card includes additional indicators for a user to enable permissions if needed.
        AskForPermissionsConsentCard card = new AskForPermissionsConsentCard();
        card.setTitle(ADDRESS_CARD_TITLE);

        Set<String> permissions = new HashSet<>();
        permissions.add(ALL_ADDRESS_PERMISSION);
        card.setPermissions(permissions);

        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Helper method that retrieves the system state from the request context.
     * @param context request context.
     * @return SystemState the systemState
     */
    private SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }

    /**
     * Helper method that creates a card object.
     * @param title title of the card
     * @param content body of the card
     * @return SimpleCard the display card to be sent along with the voice response.
     */
    private SimpleCard getSimpleCard(String title, String content) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);

        return card;
    }

    /**
     * Helper method that will get the intent name from a provided Intent object. If a name does not
     * exist then this method will return null.
     * @param intent intent object provided from a skill request.
     * @return intent name or null.
     */
    private String getIntentName(Intent intent) {
        return (intent != null) ? intent.getName() : null;
    }

    /**
     * Helper method for retrieving an OutputSpeech object when given a string of TTS.
     * @param speechText the text that should be spoken out to the user.
     * @return an instance of SpeechOutput.
     */
    private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return speech;
    }

    /**
     * Helper method that returns a reprompt object. This is used in Ask responses where you want
     * the user to be able to respond to your speech.
     * @param outputSpeech The OutputSpeech object that will be said once and repeated if necessary.
     * @return Reprompt instance.
     */
    private Reprompt getReprompt(OutputSpeech outputSpeech) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return reprompt;
    }

    /**
     * Helper method for retrieving an Ask response with a simple card and reprompt included.
     * @param cardTitle Title of the card that you want displayed.
     * @param speechText speech text that will be spoken to the user.
     * @return the resulting card and speech text.
     */
    private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        Reprompt reprompt = getReprompt(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
