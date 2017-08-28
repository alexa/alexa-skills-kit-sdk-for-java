package com.amazon.speech.slu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValueWrapper {
    private final Value value;

    /**
     * Returns a new builder instance used to construct a new {@code ValueWrapper}.
     *
     * @return the builder
     */
    public static ValueWrapper.Builder builder() {
        return new ValueWrapper.Builder();
    }

    /**
     * Private constructor to return a new {@code ValueWrapper} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code ValueWrapper}.
     */

    private ValueWrapper(final Builder builder) {
        this.value = builder.value;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param value
     *            the value object
     */
    private ValueWrapper(@JsonProperty("value") final Value value) {
        this.value = value;
    }

    /**
     * Returns the value for this {@code ValueWrapper}.
     *
     * @return the value
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
