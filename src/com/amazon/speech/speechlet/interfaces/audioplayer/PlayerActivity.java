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

/**
 * Enum representing the audio player state.
 * 
 * <dl>
 * <dt>PLAYING</dt>
 * <dd>the audio player is playing an audio stream</dd>
 * <dt>PAUSED</dt>
 * <dd>the audio player is paused to accomodate a higher priority input/output (such as user or
 * Alexa speech)</dd>
 * <dt>FINISHED</dt>
 * <dd>the audio player has finished playing a stream; may be IDLE instead</dd>
 * <dt>BUFFER_UNDERRUN</dt>
 * <dd>the audio player is being fed data slower than it is being read</dd>
 * <dt>IDLE</dt>
 * <dd>the device was powered on or rebooted and has not acted on a {@code Play} directive; may
 * occur when the audio player has finished playing a stream</dd>
 * <dt>STOPPED</dt>
 * <dd>the audio player has stopped due to an audio player directive or an issue with the stream</dd>
 * </dl>
 */
public enum PlayerActivity {
    PLAYING,
    PAUSED,
    FINISHED,
    BUFFER_UNDERRUN,
    IDLE,
    STOPPED
}
