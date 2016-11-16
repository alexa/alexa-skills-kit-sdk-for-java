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
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioItem;
import com.amazon.speech.speechlet.interfaces.audioplayer.PlayBehavior;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * This class represents the directive sent to play an audio item.
 */
@JsonTypeName("AudioPlayer.Play")
public class PlayDirective extends Directive {
    private PlayBehavior playBehavior;
    private AudioItem audioItem;

    /**
     * Returns the play behavior of this directive.
     * 
     * @return the play behavior of this directive
     */
    public PlayBehavior getPlayBehavior() {
        return playBehavior;
    }

    /**
     * Sets the play behavior of this directive.
     * 
     * @param playBehavior
     *            the play behavior of this directive
     */
    public void setPlayBehavior(PlayBehavior playBehavior) {
        this.playBehavior = playBehavior;
    }

    /**
     * Returns the audio item of the directive.
     * 
     * @return the audio item of the directive
     */
    public AudioItem getAudioItem() {
        return audioItem;
    }

    /**
     * Sets the audio item of the directive.
     * 
     * @param audioItem
     *            the audio item of the directive
     */
    public void setAudioItem(AudioItem audioItem) {
        this.audioItem = audioItem;
    }
}
