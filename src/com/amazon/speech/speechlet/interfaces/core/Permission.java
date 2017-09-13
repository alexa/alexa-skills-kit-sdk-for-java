package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.Validate;

public class Permission {
    private final String scope;
    /**
     * Returns a new builder instance used to construct a new {@code AcceptedPermission}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code AcceptedPermission} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code AcceptedPermission}.
     */
    private Permission(final Builder builder) {
        scope = builder.scope;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param scope
     *            the AcceptedPermission scope
     */
    private Permission(@JsonProperty("scope") final String  scope) {
        this.scope = scope;
    }

    /**
     * Returns the name of this {@code AcceptedPermission}.
     *
     * @return the AcceptedPermission scope
     */
    public String getScope() {
        return scope;
    }


    /**
     * Builder used to construct a new {@code AcceptedPermission}.
     */
    public static final class Builder {
        private String scope;

        public Builder withScope (final String scope) {
            this.scope = scope;
            return this;
        }

        public Permission build() {
            Validate.notBlank(scope, "AcceptedPermission scope must be defined");
            return new Permission(this);
        }
    }
}

