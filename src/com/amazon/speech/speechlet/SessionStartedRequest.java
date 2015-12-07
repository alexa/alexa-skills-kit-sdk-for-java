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
 * The request object used to notify a {@code Speechlet} that a new session has launched.
 *
 * @see Speechlet#onLaunch(LaunchRequest, Session)
 */
@JsonTypeName("SessionStartedRequest")
public final class SessionStartedRequest extends SpeechletRequest {
    /**
     * Returns a new builder instance used to construct a new {@code SessionStartedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SessionStartedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SessionStartedRequest}
     */
    private SessionStartedRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
    }

    /**
     * Private constructor a new {@code SessionStartedRequest}.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     */
    private SessionStartedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp) {
        super(requestId, timestamp);
    }

    /**
     * Builder used to construct a new {@code SessionStartedRequest}.
     */
    public static final class Builder {
        private String requestId;
        private Date timestamp = new Date();

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

        public SessionStartedRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new SessionStartedRequest(this);
        }
    }
}
