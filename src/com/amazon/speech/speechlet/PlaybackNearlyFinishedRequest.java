package com.amazon.speech.speechlet;

import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AudioPlayer.PlaybackNearlyFinished")
public final class PlaybackNearlyFinishedRequest extends SpeechletRequest {
    private final String token;
    private final int offsetInMilliseconds;
    private final String locale;

    public static Builder builder() {
        return new Builder();
    }

    private PlaybackNearlyFinishedRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        this.token = builder.token;
        this.offsetInMilliseconds = builder.offsetInMilliseconds;
        this.locale = builder.locale;
    }

    private PlaybackNearlyFinishedRequest(
            @JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("token") final String token,
            @JsonProperty("offsetInMilliseconds") final int offsetInMilliseconds,
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

    public int getOffsetInMilliseconds() {
        return this.offsetInMilliseconds;
    }

    public String getLocale() {
        return this.locale;
    }

    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();
        private String token;
        private int offsetInMilliseconds;
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

        public Builder withOffsetInMilliseconds(final int offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public PlaybackNearlyFinishedRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new PlaybackNearlyFinishedRequest(this);
        }
    }
}

