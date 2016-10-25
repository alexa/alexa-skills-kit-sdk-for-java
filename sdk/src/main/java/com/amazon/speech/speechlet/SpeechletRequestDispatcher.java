/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.io.IOException;
import java.util.ArrayList;

import com.amazon.speech.Sdk;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.ui.AudioDirective;

/**
 * This class takes an incoming request from the Alexa service, executes that call on the
 * {@link Speechlet} and returns the resulting response.
 */
public class SpeechletRequestDispatcher {
    private final Speechlet speechlet;

    public SpeechletRequestDispatcher(Speechlet speechlet) {
        this.speechlet = speechlet;
    }

    /**
     * Processes the provided {@link SpeechletRequestEnvelope} and generates an appropriate response
     * after dispatching the appropriate method calls on the {@link Speechlet} provided at
     * construction time.
     * 
     * @param requestEnvelope
     *            the current request
     * @param session
     *            the session for the request
     * @return {@link SpeechletResponseEnvelope} generated after invoking the {@link Speechlet}
     * @throws IOException
     *             may occur during request or response serialization
     * @throws SpeechletRequestHandlerException
     *             indicates a problem with the request or response
     * @throws SpeechletException
     *             indicates a problem from within the included Speechlet
     */
    public SpeechletResponseEnvelope dispatchSpeechletCall(
            SpeechletRequestEnvelope requestEnvelope, Session session) throws IOException,
            SpeechletRequestHandlerException, SpeechletException {
        /*
         * Set to true until we know that the session was ended by the Speechlet or this is an end
         * of session callback. This way the session attributes are saved if an exception occurs.
         */
        boolean saveSessionAttributes = true;

        SpeechletRequest speechletRequest = requestEnvelope.getRequest();

        // Prepare a response envelope
        final SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
        responseEnvelope.setVersion(Sdk.VERSION);

        final String requestId =
                (speechletRequest != null) ? speechletRequest.getRequestId() : null;

        // If this is a new session, invoke the speechlet's onSessionStarted life-cycle method.
        if ((session != null) && session.isNew()) {
            SessionStartedRequest sessionStartedRequest =
                    SessionStartedRequest.builder().withRequestId(requestId).build();
            speechlet.onSessionStarted(sessionStartedRequest, session);
        }

        if (speechletRequest instanceof IntentRequest) {
            final SpeechletResponse speechletResponse =
                    speechlet.onIntent((IntentRequest) speechletRequest, session);
            responseEnvelope.setResponse(speechletResponse);

            // Don't save session attributes if the session just ended
            if (speechletResponse != null) {
                saveSessionAttributes = !speechletResponse.getShouldEndSession();
            }
        } else if (speechletRequest instanceof LaunchRequest) {
            final SpeechletResponse speechletResponse =
                    speechlet.onLaunch((LaunchRequest) speechletRequest, session);
            responseEnvelope.setResponse(speechletResponse);

            // Don't save session attributes if the session just ended
            if (speechletResponse != null) {
                saveSessionAttributes = !speechletResponse.getShouldEndSession();
            }
        } else if (speechletRequest instanceof SessionEndedRequest) {
            // Don't save session attributes as the session already ended
            saveSessionAttributes = false;
            speechlet.onSessionEnded((SessionEndedRequest) speechletRequest, session);
        } else if (speechletRequest instanceof PlaybackStartedRequest) {

            final SpeechletResponse speechletResponse =
                    speechlet.onPlaybackStarted((PlaybackStartedRequest)speechletRequest);
            responseEnvelope.setResponse(speechletResponse);
        } else if (speechletRequest instanceof PlaybackStoppedRequest) {
           // cannot return anything on this one but must have an empty response
            speechlet.onPlaybackStopped((PlaybackStoppedRequest)speechletRequest);
			responseEnvelope.setResponse(SpeechletResponse.newTellResponse(new ArrayList<AudioDirective>()));
        } else if (speechletRequest instanceof PlaybackFinishedRequest) {
            final SpeechletResponse speechletResponse =
                    speechlet.onPlaybackFinished((PlaybackFinishedRequest)speechletRequest);
            responseEnvelope.setResponse(speechletResponse);
        } else if (speechletRequest instanceof PlaybackNearlyFinishedRequest) {
            final SpeechletResponse speechletResponse =
                    speechlet.onPlaybackNearlyFinished((PlaybackNearlyFinishedRequest)speechletRequest);
            responseEnvelope.setResponse(speechletResponse);
        } else if (speechletRequest instanceof PlaybackFailedRequest) {
            final SpeechletResponse speechletResponse =
                    speechlet.onPlaybackFailed((PlaybackFailedRequest)speechletRequest);
            responseEnvelope.setResponse(speechletResponse);
        } else if (speechletRequest instanceof SystemExceptionEncounteredRequest) {
			// can return nothing here but must have some sort of response
			speechlet.onSystemException((SystemExceptionEncounteredRequest)speechletRequest);
			responseEnvelope.setResponse(SpeechletResponse.newTellResponse(new ArrayList<AudioDirective>()));
		} else {
            String requestType =
                    (speechletRequest != null) ? speechletRequest.getClass().getName() : null;
            String message =
                    String.format(
                            "Unsupported request type %s. Consider updating your SDK version. "
                                    + "Request envelope version %s, SDK version %s", requestType,
                            requestEnvelope.getVersion(), Sdk.VERSION);
            throw new SpeechletRequestHandlerException(message);
        }

        if (session != null) {
            // Save mutable data in session when applicable
            if (saveSessionAttributes) {
                responseEnvelope.setSessionAttributes(session.getAttributes());
            }
        }

        return responseEnvelope;
    }
}
