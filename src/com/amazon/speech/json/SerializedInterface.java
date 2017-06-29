/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.json;

import com.amazon.speech.speechlet.Interface;
import com.amazon.speech.speechlet.State;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayerInterface;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayerState;
import com.amazon.speech.speechlet.interfaces.display.DisplayInterface;
import com.amazon.speech.speechlet.interfaces.display.DisplayState;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.interfaces.videoapp.VideoAppInterface;

enum SerializedInterface {
    AudioPlayer(AudioPlayerInterface.class, AudioPlayerState.class),
    Display(DisplayInterface.class, DisplayState.class),
    VideoApp(VideoAppInterface.class, null /* no state */),
    System(SystemInterface.class, SystemState.class);

    private final Class<? extends Interface> interfaceClass;
    private final Class<? extends State<?>> stateClass;

    SerializedInterface(final Class<? extends Interface> interfaceClass,
                        final Class<? extends State<?>> stateClass) {
        this.interfaceClass = interfaceClass;
        this.stateClass = stateClass;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Interface> Class<T> getInterfaceClass() {
        return (Class<T>) interfaceClass;
    }

    @SuppressWarnings("unchecked")
    protected <T extends State<?>> Class<T> getStateClass() {
        return (Class<T>) stateClass;
    }
}
