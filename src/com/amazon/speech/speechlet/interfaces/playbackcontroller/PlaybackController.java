/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.playbackcontroller;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.NextCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PauseCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PlayCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PreviousCommandIssuedRequest;

/**
 * The interface for interacting with remote controller devices that can control Alexa-enabled
 * devices.
 */
public interface PlaybackController {
    /**
     * Entry point for a user pressing the next button on a playback controller. <br>
     *
     * @param requestEnvelope
     *            the request envelope containing the next command issued request to handle
     * @return the response to the request. The response may only contain AudioPlayer directives.
     */
    SpeechletResponse onNextCommandIssued(
            SpeechletRequestEnvelope<NextCommandIssuedRequest> requestEnvelope);

    /**
     * Entry point for a user pressing the pause button on a playback controller.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the pause command issued request to handle
     * @return the response to the request. The response may only contain AudioPlayer directives.
     */
    SpeechletResponse onPauseCommandIssued(
            SpeechletRequestEnvelope<PauseCommandIssuedRequest> requestEnvelope);

    /**
     * Entry point for a user pressing the play button on a playback controller.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the play command issued request to handle
     * @return the response to the request. The response may only contain AudioPlayer directives.
     */
    SpeechletResponse onPlayCommandIssued(
            SpeechletRequestEnvelope<PlayCommandIssuedRequest> requestEnvelope);

    /**
     * Entry point for a user pressing the previous button on a playback controller.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the previous command issued request to handle
     * @return the response to the request. The response may only contain AudioPlayer directives.
     */
    SpeechletResponse onPreviousCommandIssued(
            SpeechletRequestEnvelope<PreviousCommandIssuedRequest> requestEnvelope);
}
