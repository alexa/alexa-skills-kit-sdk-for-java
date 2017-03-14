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

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the current playback state of the device in a {@code PlaybackFailedRequest}, which may
 * involve a different {@code AudioItem} than the one that generated the failure.
 */
public class CurrentPlaybackState {
    private final Long offsetInMilliseconds;
    private final PlayerActivity playerActivity;
    private final String token;

    /**
     * Returns a new builder instance used to construct a new {@code CurrentPlaybackState}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code CurrentPlaybackState} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code CurrentPlaybackState}
     */
    private CurrentPlaybackState(final Builder builder) {
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
    private CurrentPlaybackState(
            @JsonProperty("offsetInMilliseconds") final Long offsetInMilliseconds,
            @JsonProperty("token") final String token,
            @JsonProperty("playerActivity") final PlayerActivity playerActivity) {
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.token = token;
        this.playerActivity = playerActivity;
    }

    /**
     * Returns the offset in milliseconds of the {@code AudioItem} from the start of its stream, or
     * null if there is either no item or the item belongs to a different skill.
     * 
     * @return the offset in milliseconds of the current audio, if available, or null if not
     */
    public Long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    /**
     * Returns the player activity
     * 
     * @return the player activity
     */
    public PlayerActivity getPlayerActivity() {
        return playerActivity;
    }

    /**
     * Returns the token that was passed down with the {@code Play} directive for the current
     * {@code AudioItem}, or null if there is either no item or if the item belongs to a different
     * skill. The token is an arbitrary value set by the skill that can be used for identification.
     * 
     * @return the token, if available, or null if not
     */
    public String getToken() {
        return token;
    }

    /**
     * Builder used to construct a new {@code CurrentPlaybackState}.
     */
    public static final class Builder {
        private Long offsetInMilliseconds;
        private PlayerActivity playerActivity;
        private String token;

        private Builder() {
        }

        public Builder withOffsetInMilliseconds(final long offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public Builder withPlayerActivity(final PlayerActivity playerActivity) {
            this.playerActivity = playerActivity;
            return this;
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public CurrentPlaybackState build() {
            Validate.notNull(playerActivity, "PlayerActivity must be defined");
            return new CurrentPlaybackState(this);
        }
    }
}
