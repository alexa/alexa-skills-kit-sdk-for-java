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
 * {@code ValueWrapper} is part of an {@link Resolution}.
 * It performs as a Wrapper class for {@link Value} used for JSON serialization.
 * </p>
 */
public final class ValueWrapper {

    private final Value value;

    /**
     * Returns a new builder instance used to construct a new {@code ValueWrapper}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code ValueWrapper} from a {@code Builder}.
     *
     * @param builder
     *              the builder used to construct the {@code ValueWrapper}
     */
    private ValueWrapper(final Builder builder) {
        value = builder.value;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param value
     *              The value object for entity resolution
     */
    private ValueWrapper(@JsonProperty("value") final Value value) {
        this.value = value;
    }

    /**
     * Returns the {@code Value} for this {@code ValueWrapper}.
     *
     * @return the {@code Value}
     */
    public Value getValue() {
        return value;
    }

    /**
     * Builder used to construct a new {@code ValueWrapper}.
     */
    public static final class Builder {
        private Value value;

        public Builder withValue(final Value value) {
            this.value = value;
            return this;
        }

        public ValueWrapper build() {
            return new ValueWrapper(this);
        }
    }
}
