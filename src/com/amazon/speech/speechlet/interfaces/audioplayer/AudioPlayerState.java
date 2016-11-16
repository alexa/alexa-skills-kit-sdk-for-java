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

import com.amazon.speech.speechlet.State;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the playback state of the AudioPlayer component on the device.
 */
public final class AudioPlayerState extends State<AudioPlayerInterface> {
    private final Long offsetInMilliseconds;
    private final String token;
    private final PlayerActivity playerActivity;

    /**
     * Returns a new builder instance used to construct a new {@code PlaybackState}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code PlaybackState} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code PlaybackState}
     */
    private AudioPlayerState(final Builder builder) {
        offsetInMilliseconds = builder.offsetInMilliseconds;
        token = builder.token;
        playerActivity = builder.playerActivity;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param offsetInMilliseconds
     *            the offset of current track in milliseconds
     * @param token
     *            the current token
     * @param playerActivity
     *            the current player activity
     */
    private AudioPlayerState(@JsonProperty("offsetInMilliseconds") final Long offsetInMilliseconds,
            @JsonProperty("token") final String token,
            @JsonProperty("playerActivity") final PlayerActivity playerActivity) {
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.token = token;
        this.playerActivity = playerActivity;
    }

    public Long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    public String getToken() {
        return token;
    }

    public PlayerActivity getPlayerActivity() {
        return playerActivity;
    }

    @Override
    public Class<AudioPlayerInterface> getInterfaceClass() {
        return AudioPlayerInterface.class;
    }

    /**
     * Builder used to construct a new {@code PlaybackState}.
     */
    public static final class Builder {
        private Long offsetInMilliseconds;
        private String token;
        private PlayerActivity playerActivity;

        private Builder() {
        }

        public Builder withOffsetInMilliseconds(final long offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public Builder withPlayerActivity(final PlayerActivity playerActivity) {
            this.playerActivity = playerActivity;
            return this;
        }

        public AudioPlayerState build() {
            return new AudioPlayerState(this);
        }
    }
}
