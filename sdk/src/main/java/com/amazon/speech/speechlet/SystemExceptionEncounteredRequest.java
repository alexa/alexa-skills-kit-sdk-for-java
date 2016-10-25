package com.amazon.speech.speechlet;

import com.amazon.speech.ui.Cause;
import com.amazon.speech.ui.Error;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.commons.lang3.Validate;

import java.util.Date;

@JsonTypeName("System.ExceptionEncountered")
public final class SystemExceptionEncounteredRequest extends SpeechletRequest {
    private final Error error;
    private final Cause cause;
    private final String locale;

    public static Builder builder() {
        return new Builder();
    }

    private SystemExceptionEncounteredRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        this.cause = builder.cause;
        this.error = builder.error;
        this.locale = builder.locale;
    }

    private SystemExceptionEncounteredRequest(
            @JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("cause") final Cause cause,
            @JsonProperty("error") final Error error,
            @JsonProperty("locale") final String locale
    ) {
        super(requestId, timestamp);
        this.error = error;
        this.cause = cause;
        this.locale = locale;
    }

    public Error getError() {
        return error;
    }

    public Cause getCause() {
        return cause;
    }

    public String getLocale() {
        return this.locale;
    }

    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();
        private Cause cause;
        private Error error;
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

        public Builder withError(final Error error) {
            this.error = error;
            return this;
        }

        public Builder withCause(final Cause cause) {
            this.cause = cause;
            return this;
        }

        public SystemExceptionEncounteredRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new SystemExceptionEncounteredRequest(this);
        }
    }
}

