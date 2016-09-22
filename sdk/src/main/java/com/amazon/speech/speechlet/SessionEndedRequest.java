/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing all the parameters passed to notify a {@code Speechlet} that a
 * session ended.
 *
 * @see Speechlet#onSessionEnded(SessionEndedRequest, Session)
 */
@JsonTypeName("SessionEndedRequest")
public final class SessionEndedRequest extends SpeechletRequest {
    private final Reason reason;

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
     *            the builder used to construct the {@code SessionEndedRequest}.
     */
    private SessionEndedRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        reason = builder.reason;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param requestId
     *            the request identifier
     * @param timestamp
     *            the request timestamp
     * @param reason
     *            the reason for the session to have ended
     */
    private SessionEndedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("reason") final Reason reason) {
        super(requestId, timestamp);
        this.reason = reason;
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
     * This enum lists the reasons why a {@code Speechlet} session ended when not initiated by the
     * {@code Speechlet} itself.
     *
     * @see SessionEndedRequest#getReason()
     */
    public static enum Reason {
        /**
         * The user explicitly exited the {@code Speechlet}.
         */
        USER_INITIATED,

        /**
         * An error occurred and the {@code Speechlet} session had to be ended.
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
    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();
        private Reason reason;

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

        public Builder withReason(final Reason reason) {
            this.reason = reason;
            return this;
        }

        public SessionEndedRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new SessionEndedRequest(this);
        }
    }
}
