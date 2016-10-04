package com.amazon.speech.speechlet;

import com.amazon.speech.slu.PlaybackFailedError;
import com.amazon.speech.slu.PlaybackFailedState;

import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AudioPlayer.PlaybackFailed")
public final class PlaybackFailedRequest extends SpeechletRequest {
    private final String token;
    private final int offsetInMilliseconds;
    private final String locale;
    private final PlaybackFailedError error;
    private final PlaybackFailedState currentPlaybackState;

    public static Builder builder() {
        return new Builder();
    }

    private PlaybackFailedRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        this.token = builder.token;
        this.offsetInMilliseconds = builder.offsetInMilliseconds;
        this.locale = builder.locale;
        this.error = builder.error;
        this.currentPlaybackState = builder.currentPlaybackState;
    }

    private PlaybackFailedRequest(
            @JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("token") final String token,
            @JsonProperty("offsetInMilliseconds") final int offsetInMilliseconds,
            @JsonProperty("locale") final String locale,
            @JsonProperty("error") final PlaybackFailedError error,
            @JsonProperty("currentPlaybackState") final PlaybackFailedState currentPlaybackState
    ) {
        super(requestId, timestamp);
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.locale = locale;
        this.error = error;
        this.currentPlaybackState = currentPlaybackState;
    }

    public String getToken() {
        return this.token;
    }

    public int getOffsetInMilliseconds() {
        return this.offsetInMilliseconds;
    }

    public String getLocale() {
        return this.locale;
    }

    public PlaybackFailedError getError() {
        return this.error;
    }

    public PlaybackFailedState getCurrentPlaybackState() {
        return this.currentPlaybackState;
    }

    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();
        private String token;
        private int offsetInMilliseconds;
        private String locale;
        private PlaybackFailedError error;
        private PlaybackFailedState currentPlaybackState;

        private Builder() {
        }

        public Builder withRequestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder withTimestamp(final Date timestamp) {
            this.timestamp = (timestamp != null) ? new Date(timestamp.getTime()) : null;
            return this;
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public Builder withOffsetInMilliseconds(final int offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public Builder withError(final PlaybackFailedError error) {
            this.error = error;
            return this;
        }

        public Builder withCurrentPlaybackState(final PlaybackFailedState currentPlaybackState) {
            this.currentPlaybackState = currentPlaybackState;
            return this;
        }

        public PlaybackFailedRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new PlaybackFailedRequest(this);
        }
    }
}

