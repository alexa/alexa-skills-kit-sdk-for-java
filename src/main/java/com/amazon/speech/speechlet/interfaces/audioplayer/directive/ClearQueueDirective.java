/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.audioplayer.directive;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.interfaces.audioplayer.ClearBehavior;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * This class represents the directive sent to clear the playback queue.
 */
@JsonTypeName("AudioPlayer.ClearQueue")
public class ClearQueueDirective extends Directive {
    private ClearBehavior clearBehavior;

    /**
     * Returns how the queue should be cleared.
     * 
     * @return how the queue should be cleared
     */
    public ClearBehavior getClearBehavior() {
        return clearBehavior;
    }

    /**
     * Sets how the queue should be cleared.
     * 
     * @param clearBehavior
     *            how the queue should be cleared
     */
    public void setClearBehavior(ClearBehavior clearBehavior) {
        this.clearBehavior = clearBehavior;
    }
}
