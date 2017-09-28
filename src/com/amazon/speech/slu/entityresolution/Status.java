/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu.entityresolution;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * {@code Status} is part of an {@link Resolution}.
 * A Status object represents the status of entity resolution for the slot.
 * </p>
 */
public final class Status {

    private final StatusCode code;

    /**
     * Returns a new builder instance used to construct a new {@code Status}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Status} from a {@code Builder}.
     *
     * @param builder
     *              the builder used to construct the {@code Status}
     */
    private Status(final Builder builder) {
        code = builder.code;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param code
     *            the code indicating the results of attempting to resolve the user utterance
     *            against the defined slot types.
     */
    private Status(@JsonProperty("code") final StatusCode code) {
        this.code = code;
    }

    /**
     * Returns the {@code StatusCode} for this {@code Resolutions}.
     *
     * @return the value of {@code StatusCode}
     */
    public StatusCode getCode() {
        return code;
    }

    /**
     * Builder used to construct a new {@code Status}.
     */
    public static final class Builder {
        private StatusCode code;

        public Builder withCode(final StatusCode code) {
            this.code = code;
            return this;
        }

        public Status build() {
            return new Status(this);
        }
    }
}
