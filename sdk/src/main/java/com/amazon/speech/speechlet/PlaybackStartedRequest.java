package com.amazon.speech.speechlet;

import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AudioPlayer.PlaybackStarted")
public final class PlaybackStartedRequest extends SpeechletRequest {
    private final String token;
    private final long offsetInMilliseconds;
    private final String locale;

    public static Builder builder() {
        return new Builder();
    }

    private PlaybackStartedRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        this.token = builder.token;
        this.offsetInMilliseconds = builder.offsetInMilliseconds;
        this.locale = builder.locale;
    }

    private PlaybackStartedRequest(
            @JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("token") final String token,
            @JsonProperty("offsetInMilliseconds") final long offsetInMilliseconds,
            @JsonProperty("locale") final String locale
    ) {
        super(requestId, timestamp);
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.locale = locale;
    }

    public String getToken() {
        return this.token;
    }

    public long getOffsetInMilliseconds() {
        return this.offsetInMilliseconds;
    }

    public String getLocale() {
        return this.locale;
    }

    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();
        private String token;
        private long offsetInMilliseconds;
        private String locale;

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

        public Builder withOffsetInMilliseconds(final long offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public PlaybackStartedRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new PlaybackStartedRequest(this);
        }
    }
}

