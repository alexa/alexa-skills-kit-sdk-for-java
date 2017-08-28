package com.amazon.speech.slu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResolutionStatus {

    public enum ResolutionStatusCode {
        ER_SUCCESS_MATCH
    }

    private final ResolutionStatusCode code;

    /**
     * Returns a new builder instance used to construct a new {@code Slot}.
     *
     * @return the builder
     */
    public static ResolutionStatus.Builder builder() {
        return new ResolutionStatus.Builder();
    }

    /**
     * Private constructor to return a new {@code ResolutionsPerAuthority} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code ResolutionsPerAuthority}.
     */

    private ResolutionStatus(final Builder builder) {
        this.code = builder.code;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param code
     *            the status code
     */
    private ResolutionStatus(@JsonProperty("code") final ResolutionStatusCode code) {
        this.code = code;
    }

    /**
     * Returns the status code for this {@code ResolutionStatus}.
     *
     * @return the status code
     */
    public ResolutionStatusCode getCode() {
        return code;
    }

    /**
     * Builder used to construct a new {@code ResolutionStatus}.
     */
    public static final class Builder {
        private ResolutionStatusCode code;

        public Builder withCode(final ResolutionStatusCode code) {
            this.code = code;
            return this;
        }

        public ResolutionStatus build() {
            return new ResolutionStatus(this);
        }
    }
}
