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

import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object indicating the client playing the audio player stream is ready to
 * buffer/download the next stream in playback queue. Used for {@code AudioPlayer} invocation.
 * 
 * @see com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayer
 */
@JsonTypeName("AudioPlayer.PlaybackNearlyFinished")
public class PlaybackNearlyFinishedRequest extends AudioPlayerRequest {
    private final long offsetInMilliseconds;
    private final String token;

    /**
     * Returns a new builder instance used to construct a new {@code PlaybackNearlyFinishedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code PlaybackNearlyFinishedRequest} from a
     * {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code PlaybackNearlyFinishedRequest}
     */
    private PlaybackNearlyFinishedRequest(final Builder builder) {
        super(builder);
        this.offsetInMilliseconds = builder.offsetInMilliseconds;
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
     * @param offsetInMilliseconds
     *            the track's current offset in milliseconds
     * @param token
     *            the token that represents the current stream
     */
    private PlaybackNearlyFinishedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("intent") final long offsetInMilliseconds,
            @JsonProperty("token") final String token) {
        super(requestId, timestamp, locale);
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.token = token;
    }

    /**
     * Returns the offset in milliseconds from the beginning of the stream.
     * 
     * @return the offset in milliseconds from the beginning of the stream
     */
    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    /**
     * Returns the token of the stream
     * 
     * @return the token of the stream
     */
    public String getToken() {
        return token;
    }

    /**
     * Builder used to construct a new {@code PlaybackNearlyFinishedRequest}.
     */
    public static final class Builder extends
            SpeechletRequest.SpeechletRequestBuilder<Builder, PlaybackNearlyFinishedRequest> {
        private long offsetInMilliseconds;
        private String token;

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

        @Override
        public PlaybackNearlyFinishedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new PlaybackNearlyFinishedRequest(this);
        }
    }
}
