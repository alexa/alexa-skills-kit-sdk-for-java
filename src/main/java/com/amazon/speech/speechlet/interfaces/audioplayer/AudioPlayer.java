/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.audioplayer;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackNearlyFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStartedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStoppedRequest;

/**
 * The interface for interacting with the Audio Player on Alexa-enabled devices. The Audio Player
 * allows long form audio, such as music and podcasts, to be played.
 */
public interface AudioPlayer {
    /**
     * Entry point for handling an AudioPlayer failure event. This occurs when an audio item fails
     * to play on device.
     *
     * @param requestEnvelope
     *            the request envelope containing the playback failed request to handle
     * @return the response to the request.  The response can only include AudioPlayer directives.
     */
    SpeechletResponse onPlaybackFailed(
            SpeechletRequestEnvelope<PlaybackFailedRequest> requestEnvelope);

    /**
     * Entry point for handling an AudioPlayer playback finished event. This occurs when an audio
     * item has finished playing on device.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the playback finished request to handle
     * @return the response to the request. The response can only include the following AudioPlayer
     *         directives: ClearQueueDirective and StopDirective
     */
    SpeechletResponse onPlaybackFinished(
            SpeechletRequestEnvelope<PlaybackFinishedRequest> requestEnvelope);

    /**
     * Entry point for handling an AudioPlayer playback nearly finished event. This occurs when an
     * audio item has nearly finished playing on device. This is usually an occasion to enqueue the
     * next audio item.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the playback nearly failed request to handle
     * @return the response to the request. The response can only include AudioPlayer directives.
     */
    SpeechletResponse onPlaybackNearlyFinished(
            SpeechletRequestEnvelope<PlaybackNearlyFinishedRequest> requestEnvelope);

    /**
     * Entry point for handling an AudioPlayer playback started event. This occurs when an audio
     * item has begun playing on device.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the playback nearly failed request to handle
     * @return the response to the request. The response can only include the following AudioPlayer
     *         directives: ClearQueueDirective and StopDirective
     */
    SpeechletResponse onPlaybackStarted(
            SpeechletRequestEnvelope<PlaybackStartedRequest> requestEnvelope);

    /**
     * Entry point for handling an AudioPlayer playback stopped event. This occurs when the
     * AudioPlayer has gone from a playing state to a stopped state.<br>
     *
     * @param requestEnvelope
     *            the request envelope containing the playback nearly failed request to handle
     * @return the response to the request. The contents of the response is currently ignored, but
     *         remains here for forward compatibility reasons.
     */
    SpeechletResponse onPlaybackStopped(
            SpeechletRequestEnvelope<PlaybackStoppedRequest> requestEnvelope);
}
