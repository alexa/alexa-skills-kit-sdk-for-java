/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.speechlet.interfaces.system.Error;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing all the parameters passed to notify a {@code SpeechletV2} that a
 * session ended.
 *
 * @see SpeechletV2#onSessionEnded(SpeechletRequestEnvelope)
 */
@JsonTypeName("SessionEndedRequest")
public final class SessionEndedRequest extends CoreSpeechletRequest {
    private final Reason reason;
    private final Error error;

    /**
     * Returns a new builder instance used to construct a new {@code SessionEndedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SessionEndedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SessionEndedRequest}
     */
    private SessionEndedRequest(final Builder builder) {
        super(builder);
        reason = builder.reason;
        error = builder.error;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param requestId
     *            the request identifier
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     * @param reason
     *            the reason for the session to have ended
     */
    private SessionEndedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("reason") final Reason reason, @JsonProperty("error") final Error error) {
        super(requestId, timestamp, locale);
        this.reason = reason;
        this.error = error;
    }

    /**
     * Returns the reason for the session to have ended.
     *
     * @return the reason. Will be {@code null} if the value for the reason is not supported by the
     *         version of the SDK used.
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * @return the error that caused this SessionEndedRquest, if {@code Reason} was
     *         {@code Reason.ERROR}
     */
    public Error getError() {
        return error;
    }

    /**
     * This enum lists the reasons why a session ended when not initiated by the {@code SpeechletV2}
     * itself.
     *
     * @see SessionEndedRequest#getReason()
     */
    public static enum Reason {
        /**
         * The user explicitly exited the skill.
         */
        USER_INITIATED,

        /**
         * An error occurred and the session had to be ended.
         */
        ERROR,

        /**
         * The Alexa skill has not received a valid response within the maximum allowed number of
         * re-prompts.
         */
        EXCEEDED_MAX_REPROMPTS;
    }

    /**
     * Builder used to construct a new {@code SessionEndedRequest}.
     */
    public static final class Builder extends SpeechletRequestBuilder<Builder, SessionEndedRequest> {
        private Reason reason;
        private Error error;

        private Builder() {
        }

        public Builder withReason(final Reason reason) {
            this.reason = reason;
            return this;
        }

        public Builder withError(final Error error) {
            this.error = error;
            return this;
        }

        @Override
        public SessionEndedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new SessionEndedRequest(this);
        }
    }
}
