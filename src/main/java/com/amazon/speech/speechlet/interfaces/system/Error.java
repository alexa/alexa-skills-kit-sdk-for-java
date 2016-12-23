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
 * Represents information about an error in the System.ErrorOccurredRequest.
 */
public final class Error {
    private final ErrorType type;
    private final String message;

    /**
     * Returns a new builder instance used to construct a new {@code ErrorCause}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public Error(Builder builder) {
        type = builder.type;
        message = builder.message;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param type
     *            the type of the error
     * @param message
     *            the error message
     */
    @SuppressWarnings("unused")
    private Error(@JsonProperty("type") ErrorType type,
            @JsonProperty("message") final String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Returns the human-readable message describing the error.
     * 
     * @return the human-readable message describing the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the type of the error.
     * 
     * @return the type of the error
     */
    public ErrorType getType() {
        return type;
    }

    /**
     * Builder used to construct a new {@code Error}.
     */
    public static final class Builder {
        private ErrorType type;
        private String message;

        private Builder() {
        }

        public Builder withType(ErrorType type) {
            this.type = type;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Error build() {
            Validate.notNull(type, "Type must be defined");
            Validate.notBlank(message, "Message must be non-blank");
            return new Error(this);
        }
    }
}
