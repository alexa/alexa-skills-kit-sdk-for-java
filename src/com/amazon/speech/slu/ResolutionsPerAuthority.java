package com.amazon.speech.slu;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public final class ResolutionsPerAuthority {

    private final String authority;
    private final ResolutionStatus status;
    private final List<ValueWrapper> values;

    /**
     * Returns a new builder instance used to construct a new {@code ResolutionsPerAuthority}.
     *
     * @return the builder
     */
    public static ResolutionsPerAuthority.Builder builder() {
        return new ResolutionsPerAuthority.Builder();
    }

    /**
     * Private constructor to return a new {@code ResolutionsPerAuthority} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code ResolutionsPerAuthority}.
     */

    private ResolutionsPerAuthority(final Builder builder) {
        this.authority = builder.authority;
        this.status = builder.status;
        this.values = builder.values;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param authority
     *            the authority name
     * @param status
     *            the status
     * @param values
     *            the values
     */
    private ResolutionsPerAuthority(@JsonProperty("authority") final String authority,
                 @JsonProperty("status") final ResolutionStatus status,
                 @JsonProperty("values") final List<ValueWrapper> values) {
        this.authority = authority;
        this.status = status;
        this.values = values;
    }

    /**
     * Returns the authority for this {@code ResolutionsPerAuthority}.
     *
     * @return the confirmation status
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Returns the resolution status for this {@code ResolutionsPerAuthority}.
     *
     * @return the resolution status
     */
    public ResolutionStatus getStatus() {
        return status;
    }

    /**
     * Returns the values for this {@code ResolutionsPerAuthority}.
     *
     * @return the values
     */
    public List<Value> getValues() {
        List<Value> returnList = new ArrayList<>();

        for (ValueWrapper valueWrapper : values) {
            returnList.add(valueWrapper.getValue());
        }

        return returnList;
    }

    /**
     * Builder used to construct a new {@code ResolutionsPerAuthority}.
     */
    public static final class Builder {
        private String authority;
        private ResolutionStatus status;
        private List<ValueWrapper> values;

        public Builder withAuthority(final String authority) {
            this.authority = authority;
            return this;
        }

        public Builder withStatus(final ResolutionStatus status) {
            this.status = status;
            return this;
        }

        public Builder withValues(final List<ValueWrapper> values) {
            this.values = values;
            return this;
        }

        public ResolutionsPerAuthority build() {
            Validate.notBlank(authority, "Authority name must be defined");
            return new ResolutionsPerAuthority(this);
        }
    }
}
