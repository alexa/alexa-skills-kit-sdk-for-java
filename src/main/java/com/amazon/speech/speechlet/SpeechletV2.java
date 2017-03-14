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

import com.amazon.speech.json.SpeechletRequestEnvelope;

/**
 * <p>
 * A {@code SpeechletV2} is a speech-enabled web service that runs in the cloud. A
 * {@code SpeechletV2} receives and responds to speech initiated requests.
 * </p>
 *
 * <p>
 * This is the second version, which takes in {@link SpeechletRequestEnvelope}s instead of a request
 * and session.
 * </p>
 *
 * <p>
 * The methods in the {@code SpeechletV2} interface define:
 * </p>
 * <ul>
 * <li>life-cycle events for the skill as experienced by the user</li>
 * <li>a way to service speech requests from the user</li>
 * <li>call-backs from events happening on the device the user is interacting with</li>
 * </ul>
 *
 * <p>
 * Because a {@code SpeechletV2} is a cloud-based service, the life-cycle of the actual object
 * implementing the {@code SpeechletV2} interface is unrelated to the life-cycle of the Alexa skill
 * as experienced by the user interacting with the speech enabled device. A single
 * {@code SpeechletV2} object typically handles requests from many users, from many devices, for
 * many sessions, using many concurrent threads. Also, multiple requests that are part of a single
 * session coming from a single user and a single device may actually be serviced by different
 * {@code SpeechletV2} objects if your service is deployed on multiple machines and load-balanced.
 * The {@link Session} object is what defines a single run of a skill as experienced by a user.
 * </p>
 *
 * @see Session
 */
public interface SpeechletV2 {
    /**
     * Used to notify that a new session started as a result of a user interacting with the device.
     * This method enables {@code SpeechletV2}s to perform initialization logic and allows for session
     * attributes to be stored for subsequent requests.
     *
     * @param requestEnvelope
     *            the session started request envelope
     */
    void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope);

    /**
     * Entry point for {@code SpeechletV2}s for handling a speech initiated request to start the skill
     * without providing an {@code Intent}.<br>
     *
     * This method is only invoked when {@link Session#isNew()} is {@code true}.
     *
     * @param requestEnvelope
     *            the launch request envelope
     * @return the response, spoken and visual, to the request
     */
    SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope);

    /**
     * Entry point for handling speech initiated requests.<br>
     *
     * This is where the bulk of the {@code SpeechletV2} logic lives. Intent requests are handled by
     * this method and return responses to render to the user.<br>
     *
     * If this is the initial request of a new {@code SpeechletV2} session, {@link Session#isNew()}
     * returns {@code true}. Otherwise, this is a subsequent request within an existing session.
     *
     * @param requestEnvelope
     *            the intent request envelope to handle
     * @return the response, spoken and visual, to the request
     */
    SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);

    /**
     * Callback used to notify that the session ended as a result of the user interacting, or not
     * interacting with the device. This method is not invoked if the {@code SpeechletV2} itself
     * ended the session using {@link SpeechletResponse#setShouldEndSession(boolean)}.
     *
     * @param requestEnvelope
     *            the end of session request envelope
     */
    void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope);
}
