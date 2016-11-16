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

import com.amazon.speech.speechlet.Interface;

/**
 * Represents the presence of AudioPlayer capabilities of the Alexa client.
 */
public final class AudioPlayerInterface extends Interface {
    /**
     * The type of state returned for this interface, to be passed in as the second argument for
     * {@code Context#getState(Interface, State<Interface>)}.
     */
    public static final Class<AudioPlayerState> STATE_TYPE = AudioPlayerState.class;

    /**
     * Returns a new builder instance used to construct a new {@code AudioPlayerInterface}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code AudioPlayerInterface} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code AudioPlayerInterface}
     */
    private AudioPlayerInterface(final Builder builder) {
    }

    /**
     * Private constructor a new {@code AudioPlayerInterface}.
     */
    private AudioPlayerInterface() {
    }

    /**
     * Builder used to construct a new {@code AudioPlayerInterface}.
     */
    public static final class Builder {
        private Builder() {
        }

        public AudioPlayerInterface build() {
            return new AudioPlayerInterface(this);
        }
    }
}
