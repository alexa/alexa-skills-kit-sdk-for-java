/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system.request;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.interfaces.system.Error;
import com.amazon.speech.speechlet.interfaces.system.ErrorCause;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing an {@link Intent} for {@code SpeechletV2} invocation.
 */
@JsonTypeName("System.ExceptionEncountered")
public class ExceptionEncounteredRequest extends SystemRequest {
    private final Error error;
    private final ErrorCause cause;

    /**
     * Returns a new builder instance used to construct a new {@code ErrorOccurredRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code ExceptionEncounteredRequest} from a
     * {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code ExceptionEncounteredRequest}
     */
    private ExceptionEncounteredRequest(final Builder builder) {
        super(builder);
        this.error = builder.error;
        this.cause = builder.cause;
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
     * @param error
     *            the error the request is communicating
     * @param cause
     *            the cause of the request error
     */
    protected ExceptionEncounteredRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale, @JsonProperty("error") final Error error,
            @JsonProperty("cause") final ErrorCause cause) {
        super(requestId, timestamp, locale);
        this.error = error;
        this.cause = cause;
    }

    /**
     * @return the error for logging, debugging, and metric purposes
     */
    public Error getError() {
        return error;
    }

    /**
     * @return information about the cause of the error
     */
    public ErrorCause getCause() {
        return cause;
    }

    /**
     * Builder used to construct a new {@code ErrorOccurredRequest}.
     */
    public static final class Builder extends
            SpeechletRequestBuilder<Builder, ExceptionEncounteredRequest> {
        private Error error;
        private ErrorCause cause;

        private Builder() {
        }

        public Builder withError(final Error error) {
            this.error = error;
            return this;
        }

        public Builder withCause(final ErrorCause cause) {
            this.cause = cause;
            return this;
        }

        @Override
        public ExceptionEncounteredRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            Validate.notNull(error, "Error must be defined");
            Validate.notNull(cause, "Cause must be defined");
            return new ExceptionEncounteredRequest(this);
        }
    }
}
