/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code User} tied to a {@code Speechlet} {@code Session} is the user registered to the device
 * initiating the {@code Speechlet} {@code Session} and contains a unique identifier.
 *
 * @see Speechlet
 * @see Session
 * @see SpeechletException
 */
public class AudioPlayer {
    private final String token;
    private final String playerActivity;
    private final long offsetInMilliseconds;

    /**
     * Returns a new builder instance used to construct a new {@code IntentRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Constructs a AudioPlayer.
     *
     * @param token
     *            the token identifier
     * @deprecated use {@link AudioPlayer#builder()} to construct new instances of this class
     */
    @Deprecated
    public AudioPlayer(final String token, String playerActivity, long offsetInMilliseconds) {

        //this(token, playerActivity, offsetInMilliseconds);
        this.token = token;
        this.playerActivity = playerActivity;
        this.offsetInMilliseconds = offsetInMilliseconds;
    }

    @SuppressWarnings("unused")
    private AudioPlayer() {
        this(null, null,0);
    }

    /**
     * Private constructor to return a new {@code User} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code User}
     */
    private AudioPlayer(final Builder builder) {
        this.playerActivity = builder.playerActivity;
        this.token = builder.token;
        this.offsetInMilliseconds = builder.offsetInMilliseconds;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param offsetInMilliseconds
     *            the current playback offsetInMilliseconds
     * @param token
     *            the access token
     * @param playerActivity
     *            the playerActivity state
     */
    private AudioPlayer(@JsonProperty("offsetInMilliseconds") final long offsetInMilliseconds,
						@JsonProperty("token") final String token,
                        @JsonProperty("playerActivity") final String playerActivity) {
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.token = token;
        this.playerActivity = playerActivity;
    }

    /**
     * Returns the user identifier.
     *
     * @return the user identifier
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getPlayerActivity() {
        return playerActivity;
    }

    /**
     * Returns the access token.
     *
     * @return the access token. Returns null if the user has not yet linked their account, or if
     * the skill is not configured for account linking.
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getToken() {
        return token;
    }

    /**
     * Returns the access token.
     *
     * @return the access token. Returns null if the user has not yet linked their account, or if
     * the skill is not configured for account linking.
     */
    @JsonInclude(Include.NON_EMPTY)
    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    /**
     * Builder used to construct a new {@code User}.
     */
    public static final class Builder {
        private String playerActivity;
        private String token;
        private long offsetInMilliseconds;

        private Builder() {
        }

        public Builder withPlayerActivity(final String playerActivity) {
            this.playerActivity = playerActivity;
            return this;
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public Builder withOffsetInMilliseconds(final long offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public AudioPlayer build() {
            return new AudioPlayer(this);
        }
    }
}
