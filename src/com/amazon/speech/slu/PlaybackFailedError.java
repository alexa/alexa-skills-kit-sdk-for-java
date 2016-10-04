package com.amazon.speech.slu;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PlaybackFailedError {
    private final String type;
    private final String message;

    public static Builder builder() {
        return new Builder();
    }

    private PlaybackFailedError(final Builder builder) {
        type = builder.type;
        message = builder.message;
    }

    private PlaybackFailedError(@JsonProperty("type") final String type, @JsonProperty("message") final String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public static final class Builder {
        private String type;
        private String message;

        private Builder() {
        }

        public Builder withType(final String type) {
            this.type = type;
            return this;
        }

        public Builder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public PlaybackFailedError build() {
            Validate.notBlank(type, "PlaybackFailedError type must be defined");
            return new PlaybackFailedError(this);
        }
    }
}
