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

/**
 * <p>
 * A {@code Speechlet} is a speech-enabled web service that runs in the cloud. A {@code Speechlet}
 * receives and responds to speech initiated requests.
 * </p>
 *
 * <p>
 * The methods in the {@code Speechlet} interface define:
 * </p>
 * <ul>
 * <li>life-cycle events for the skill as experienced by the user</li>
 * <li>a way to service speech requests from the user</li>
 * <li>call-backs from events happening on the device the user is interacting with</li>
 * </ul>
 *
 * <p>
 * Because a {@code Speechlet} is a cloud-based service, the life-cycle of the actual object
 * implementing the {@code Speechlet} interface is unrelated to the life-cycle of the Alexa skill as
 * experienced by the user interacting with the speech enabled device. A single {@code Speechlet}
 * object typically handles requests from many users, from many devices, for many sessions, using
 * many concurrent threads. Also, multiple requests that are part of a single session coming from a
 * single user and a single device may actually be serviced by different {@code Speechlet} objects
 * if your service is deployed on multiple machines and load-balanced. The {@link Session} object is
 * what defines a single run of a skill as experienced by a user.
 * </p>
 *
 * @see Session
 * @see SpeechletException
 */
public interface Speechlet {
    /**
     * Used to notify that a new session started as a result of a user interacting with the device.
     * This method enables {@code Speechlet}s to perform initialization logic and allows for session
     * attributes to be stored for subsequent requests.
     *
     * @param request
     *            the session started request
     * @param session
     *            the session associated with the user starting a {@code Speechlet}
     * @throws SpeechletException
     *             for any errors encountered in the processing of the request
     */
    void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException;

    /**
     * Entry point for {@code Speechlet}s for handling a speech initiated request to start the skill
     * without providing an {@code Intent}.<br>
     *
     * This method is only invoked when {@link Session#isNew()} is {@code true}.
     *
     * @param request
     *            the launch request
     * @param session
     *            the session associated with the request
     * @return the response, spoken and visual, to the request
     * @throws SpeechletException
     *             for any errors encountered in the processing of the request
     */
    SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException;

    /**
     * Entry point for handling speech initiated requests.<br>
     *
     * This is where the bulk of the {@code Speechlet} logic lives. Intent requests are handled by
     * this method and return responses to render to the user.<br>
     *
     * If this is the initial request of a new {@code Speechlet} session, {@link Session#isNew()}
     * returns {@code true}. Otherwise, this is a subsequent request within an existing session.
     *
     * @param request
     *            the intent request to handle
     * @param session
     *            the session associated with the request
     * @return the response, spoken and visual, to the request
     * @throws SpeechletException
     *             for any errors encountered in the processing of the request
     */
    SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException;

    /**
     * Callback used to notify that the session ended as a result of the user interacting, or not
     * interacting with the device. This method is not invoked if the {@code Speechlet} itself ended
     * the session using {@link SpeechletResponse#setShouldEndSession(boolean)}.
     *
     * @param request
     *            the end of session request
     * @param session
     *            the session associated with the request
     * @throws SpeechletException
     *             for any errors encountered in the processing of the request
     */
    void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException;
}
