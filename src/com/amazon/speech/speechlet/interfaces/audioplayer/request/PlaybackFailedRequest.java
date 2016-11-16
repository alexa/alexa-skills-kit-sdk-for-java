/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.audioplayer.request;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.speechlet.interfaces.audioplayer.CurrentPlaybackState;
import com.amazon.speech.speechlet.interfaces.audioplayer.Error;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing the {@link Error} that occurred during audio player playback for
 * {@code AudioPlayer} invocation.
 * 
 * @see com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayer
 */
@JsonTypeName("AudioPlayer.PlaybackFailed")
public class PlaybackFailedRequest extends AudioPlayerRequest {
    private final CurrentPlaybackState currentPlaybackState;
    private final Error error;
    private final String token;

    /**
     * Returns a new builder instance used to construct a new {@code PlaybackFailedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code PlaybackFailedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code PlaybackFailedRequest}
     */
    private PlaybackFailedRequest(final Builder builder) {
        super(builder);
        this.currentPlaybackState = builder.currentPlaybackState;
        this.error = builder.error;
        this.token = builder.token;
    }

    /**
     * Protected constructor used for JSON serialization and for extending this class.
     *
     * @param requestId
     *            the request identifier
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     * @param currentPlaybackState
     *            the current playback state
     * @param error
     *            the error that has occurred
     * @param token
     *            the token representing the stream that failed to playback
     */
    private PlaybackFailedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("currentPlaybackState") final CurrentPlaybackState currentPlaybackState,
            @JsonProperty("error") final Error error, @JsonProperty("token") final String token) {
        super(requestId, timestamp, locale);
        this.currentPlaybackState = currentPlaybackState;
        this.error = error;
        this.token = token;
    }

    /**
     * Returns the playback state for the item currently playing, which may be different than the
     * item that encountered a failure.
     * 
     * @return the playback state for the item currently playing
     */
    public CurrentPlaybackState getCurrentPlaybackState() {
        return currentPlaybackState;
    }

    /**
     * Returns the error causing the failure
     * 
     * @return the error causing the failure
     */
    public Error getError() {
        return error;
    }

    /**
     * Returns the token of the stream encountering the failure
     * 
     * @return the token of the stream encountering the failure
     */
    public String getToken() {
        return token;
    }

    /**
     * Builder used to construct a new {@code PlaybackFailedRequest}.
     */
    public static final class Builder extends
            SpeechletRequestBuilder<Builder, PlaybackFailedRequest> {
        private CurrentPlaybackState currentPlaybackState;
        private Error error;
        private String token;

        private Builder() {
        }

        public Builder withCurrentPlaybackState(CurrentPlaybackState currentPlaybackState) {
            this.currentPlaybackState = currentPlaybackState;
            return this;
        }

        public Builder withError(Error error) {
            this.error = error;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        @Override
        public PlaybackFailedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new PlaybackFailedRequest(this);
        }
    }
}
