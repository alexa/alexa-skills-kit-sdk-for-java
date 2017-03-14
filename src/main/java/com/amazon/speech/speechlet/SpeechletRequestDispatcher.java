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

import java.io.IOException;
import java.util.Locale;

import com.amazon.speech.Sdk;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayer;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.AudioPlayerRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackNearlyFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStartedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStoppedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.PlaybackController;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.NextCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PauseCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PlayCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PlaybackControllerRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PreviousCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.system.System;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;
import com.amazon.speech.speechlet.interfaces.system.request.SystemRequest;

/**
 * This class takes an incoming request from the Alexa service, executes that call on the
 * {@link SpeechletV2} and returns the resulting response.
 */
public class SpeechletRequestDispatcher {
    private final SpeechletV2 speechlet;

    public SpeechletRequestDispatcher(SpeechletV2 speechlet) {
        this.speechlet = speechlet;
    }

    public SpeechletRequestDispatcher(Speechlet speechlet) {
        this(new SpeechletToSpeechletV2Adapter(speechlet));
    }

    /**
     * Processes the provided {@link SpeechletRequestEnvelope} and generates an appropriate response
     * after dispatching the appropriate method calls on the {@link SpeechletV2} provided at
     * construction time.
     *
     * @param requestEnvelope
     *            the current request
     * @param session
     *            the session for the request
     * @return {@link SpeechletResponseEnvelope} generated after invoking the {@link SpeechletV2}
     * @throws IOException
     *             may occur during request or response serialization
     * @throws SpeechletRequestHandlerException
     *             indicates a problem with the request or response
     * @throws SpeechletException
     *             indicates a problem from within the included Speechlet
     */
    public SpeechletResponseEnvelope dispatchSpeechletCall(
            SpeechletRequestEnvelope<?> requestEnvelope, Session session)
            throws IOException, SpeechletRequestHandlerException, SpeechletException {
        SpeechletRequest speechletRequest = requestEnvelope.getRequest();

        // Prepare a response envelope
        final SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
        responseEnvelope.setVersion(Sdk.VERSION);

        // If this is a new session, invoke the speechlet's onSessionStarted life-cycle method.
        if ((session != null) && session.isNew()) {
            final String requestId =
                    (speechletRequest != null) ? speechletRequest.getRequestId() : null;
            final Locale locale = (speechletRequest != null) ? speechletRequest.getLocale() : null;

            SessionStartedRequest sessionStartedRequest =
                    SessionStartedRequest
                            .builder()
                            .withRequestId(requestId)
                            .withLocale(locale)
                            .build();

            SpeechletRequestEnvelope<SessionStartedRequest> sessionStartedRequestEnvelope =
                    SpeechletRequestEnvelope
                            .<SessionStartedRequest>builder()
                            .withContext(requestEnvelope.getContext())
                            .withRequest(sessionStartedRequest)
                            .withSession(session)
                            .withVersion(requestEnvelope.getVersion())
                            .build();
            try {
                speechlet.onSessionStarted(sessionStartedRequestEnvelope);
            } catch (RuntimeException e) {
                // Doing this to preserve backwards compatibility if a Speechlet instead of a
                // SpeechletV2 is used
                if (e.getCause() instanceof SpeechletException) {
                    throw (SpeechletException) e.getCause();
                }

                throw e;
            }
        }

        Object speechletWithInterfaces = speechlet instanceof SpeechletToSpeechletV2Adapter ?
                ((SpeechletToSpeechletV2Adapter) speechlet).getSpeechlet() : speechlet;

        boolean saveSessionAttributes = false;
        SpeechletResponse speechletResponse = null;

        /** AudioPlayer **/
        if (speechletRequest instanceof AudioPlayerRequest) {
            if (speechletWithInterfaces instanceof AudioPlayer) {
                AudioPlayer audioPlayerSpeechlet = (AudioPlayer) speechletWithInterfaces;
                if (speechletRequest instanceof PlaybackFailedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlaybackFailedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlaybackFailedRequest>) requestEnvelope;
                    speechletResponse = audioPlayerSpeechlet.onPlaybackFailed(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PlaybackFinishedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlaybackFinishedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlaybackFinishedRequest>) requestEnvelope;
                    speechletResponse =
                            audioPlayerSpeechlet
                                    .onPlaybackFinished(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PlaybackNearlyFinishedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlaybackNearlyFinishedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlaybackNearlyFinishedRequest>) requestEnvelope;
                    speechletResponse =
                            audioPlayerSpeechlet
                                    .onPlaybackNearlyFinished(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PlaybackStartedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlaybackStartedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlaybackStartedRequest>) requestEnvelope;
                    speechletResponse =
                            audioPlayerSpeechlet
                                    .onPlaybackStarted(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PlaybackStoppedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlaybackStoppedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlaybackStoppedRequest>) requestEnvelope;
                    speechletResponse =
                            audioPlayerSpeechlet
                                    .onPlaybackStopped(typeSpecificRequestEnvelope);
                }
            }
            /** PlaybackController **/
        } else if (speechletRequest instanceof PlaybackControllerRequest) {
            if (speechletWithInterfaces instanceof PlaybackController) {
                PlaybackController playbackControllerSpeechlet = (PlaybackController) speechletWithInterfaces;
                if (speechletRequest instanceof NextCommandIssuedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<NextCommandIssuedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<NextCommandIssuedRequest>) requestEnvelope;
                    speechletResponse =
                            playbackControllerSpeechlet
                                    .onNextCommandIssued(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PreviousCommandIssuedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PreviousCommandIssuedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PreviousCommandIssuedRequest>) requestEnvelope;
                    speechletResponse =
                            playbackControllerSpeechlet
                                    .onPreviousCommandIssued(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PauseCommandIssuedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PauseCommandIssuedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PauseCommandIssuedRequest>) requestEnvelope;
                    speechletResponse =
                            playbackControllerSpeechlet
                                    .onPauseCommandIssued(typeSpecificRequestEnvelope);
                } else if (speechletRequest instanceof PlayCommandIssuedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<PlayCommandIssuedRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<PlayCommandIssuedRequest>) requestEnvelope;
                    speechletResponse =
                            playbackControllerSpeechlet
                                    .onPlayCommandIssued(typeSpecificRequestEnvelope);
                }
            }
            /** System **/
        } else if (speechletRequest instanceof SystemRequest) {
            if (speechletWithInterfaces instanceof System) {
                System systemSpeechlet = (System) speechletWithInterfaces;
                if (speechletRequest instanceof ExceptionEncounteredRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<ExceptionEncounteredRequest> typeSpecificRequestEnvelope =
                            (SpeechletRequestEnvelope<ExceptionEncounteredRequest>) requestEnvelope;
                    systemSpeechlet
                            .onExceptionEncountered(typeSpecificRequestEnvelope);
                }
            }
            /** SpeechletV2 **/
        } else if (speechletRequest instanceof CoreSpeechletRequest) {
            try {
                if (speechletRequest instanceof SessionEndedRequest) {
                    @SuppressWarnings("unchecked")
                    SpeechletRequestEnvelope<SessionEndedRequest> parameterizedRequestEnvelope =
                            (SpeechletRequestEnvelope<SessionEndedRequest>) requestEnvelope;
                    speechlet.onSessionEnded(parameterizedRequestEnvelope);
                } else {
                    if (speechletRequest instanceof IntentRequest) {
                        @SuppressWarnings("unchecked")
                        SpeechletRequestEnvelope<IntentRequest> parameterizedRequestEnvelope =
                                (SpeechletRequestEnvelope<IntentRequest>) requestEnvelope;
                        speechletResponse = speechlet.onIntent(parameterizedRequestEnvelope);
                    } else if (speechletRequest instanceof LaunchRequest) {
                        @SuppressWarnings("unchecked")
                        SpeechletRequestEnvelope<LaunchRequest> parameterizedRequestEnvelope =
                                (SpeechletRequestEnvelope<LaunchRequest>) requestEnvelope;
                        speechletResponse = speechlet.onLaunch(parameterizedRequestEnvelope);
                    }

                    if (speechletResponse != null) {
                        saveSessionAttributes = !speechletResponse.getShouldEndSession();
                    } else {
                        saveSessionAttributes = true;
                    }
                }
            } catch (RuntimeException e) {
                // Doing this to preserve backwards compatibility if a Speechlet instead of a
                // SpeechletV2 is used
                if (e.getCause() instanceof SpeechletException) {
                    throw (SpeechletException) e.getCause();
                }

                throw e;
            }
            /** Exception **/
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

        responseEnvelope.setResponse(speechletResponse);

        if (saveSessionAttributes && session != null) {
            responseEnvelope.setSessionAttributes(session.getAttributes());
        }

        return responseEnvelope;
    }
}
