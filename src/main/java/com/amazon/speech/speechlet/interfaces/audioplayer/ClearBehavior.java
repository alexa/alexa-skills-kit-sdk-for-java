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
 * This enum lists behaviors possible when issuing the {@code ClearQueue} directive.
 * 
 * <dl>
 * <dt>{@code CLEAR_ENQUEUED}</dt>
 * <dd>clears the queue and continues to play the currently playing stream</dd>
 * <dt>{@code CLEAR_ALL}</dt>
 * <dd>clears the entire playback queue and stops the currently playing stream (if applicable).</dd>
 * </dl>
 * 
 * @see com.amazon.speech.speechlet.interfaces.audioplayer.directive.ClearQueueDirective
 */
public enum ClearBehavior {
    CLEAR_ALL,
    CLEAR_ENQUEUED;
}
