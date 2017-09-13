package com.amazon.speech.slu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {
    private final String name;
    private final String id;

    /**
     * Returns a new builder instance used to construct a new {@code Value}.
     *
     * @return the builder
     */
    public static Value.Builder builder() {
        return new Value.Builder();
    }

    /**
     * Private constructor to return a new {@code Value} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Value}.
     */

    private Value(final Value.Builder builder) {
        this.name = builder.name;
        this.id = builder.id;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param name
     *            the name of the value
     * @param id
     *            the id of the value
     */
    private Value(@JsonProperty("name") final String name,
                  @JsonProperty("id") final String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the name for this {@code Value}.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id for this {@code Value}.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Builder used to construct a new {@code Value}.
     */
    public static final class Builder {
        private String name;
        private String id;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Value build() {
            return new Value(this);
        }
    }

}
