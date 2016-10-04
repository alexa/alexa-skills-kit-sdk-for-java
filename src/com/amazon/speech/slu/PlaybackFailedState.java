package com.amazon.speech.slu;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PlaybackFailedState {
    private final String token;
    private final int offsetInMilliseconds;
    private final String playerActivity;

    public static Builder builder() {
        return new Builder();
    }

    private PlaybackFailedState(final Builder builder) {
        token = builder.token;
        offsetInMilliseconds = builder.offsetInMilliseconds;
        playerActivity = builder.playerActivity;
    }

    private PlaybackFailedState(
            @JsonProperty("token") final String token,
            @JsonProperty("offsetInMilliseconds") final int offsetInMilliseconds,
            @JsonProperty("playerActivity") final String playerActivity
    ) {
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.playerActivity = playerActivity;
    }

    public String getToken() {
        return token;
    }

    public int getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    public String getPlayerActivity() {
        return playerActivity;
    }

    public static final class Builder {
        private String token;
        private int offsetInMilliseconds;
        private String playerActivity;

        private Builder() {
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public Builder withOffsetInMilliseconds(final int offsetInMilliseconds) {
            this.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        public Builder withPlayerActivity(final String playerActivity) {
            this.playerActivity = playerActivity;
            return this;
        }

        public PlaybackFailedState build() {
            Validate.notBlank(token, "PlaybackFailedState token must be defined");
            return new PlaybackFailedState(this);
        }
    }
}
