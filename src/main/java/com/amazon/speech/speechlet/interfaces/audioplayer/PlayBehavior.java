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
 * This enum lists behaviors possible for a stream when issuing the {@code Play} directive.
 * 
 * <dl>
 * <dt>{@code REPLACE_ALL}</dt>
 * <dd>Causes the client to immediately begin playback of the stream returned with the Play
 * directive, and replace current and enqueued streams.
 * <dt>{@code ENQUEUE}</dt>
 * <dd>Adds a stream to the end of the current queue.</dd>
 * <dt>{@code REPLACE_ENQUEUED}</dt>
 * <dd>Replace all streams in the queue. This does not impact the currently playing stream.</dd>
 * </dl>
 */
public enum PlayBehavior {
    ENQUEUE,
    REPLACE_ALL,
    REPLACE_ENQUEUED;
}
