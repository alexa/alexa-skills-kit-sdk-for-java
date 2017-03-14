/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The cause of an error.
 */
public final class ErrorCause {
    private final String requestId;

    /**
     * Returns a new builder instance used to construct a new {@code ErrorCause}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public ErrorCause(Builder builder) {
        requestId = builder.requestId;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param requestId
     *            the request ID of the request with a response that caused this error
     */
    @SuppressWarnings("unused")
    private ErrorCause(@JsonProperty("requestId") final String requestId) {
        this.requestId = requestId;
    }

    // Getters

    /**
     * Returns the request ID of the cause of the error
     * 
     * @return the request ID of the cause of the error
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Builder used to construct a new {@code SessionStartedRequest}.
     */
    public static final class Builder {
        private String requestId;

        private Builder() {
        }

        public Builder withRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public ErrorCause build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new ErrorCause(this);
        }
    }
}
