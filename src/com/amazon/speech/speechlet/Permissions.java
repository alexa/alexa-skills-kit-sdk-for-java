package com.amazon.speech.speechlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code Permissions} included as part of {@code User} represent the consent permissions for
 * the user.
 *
 * @see User
 */
public class Permissions {
    private String consentToken;

    /**
     * Constructs an Permissions.
     *
     * @param consentToken
     *            the application identifier
     */
    public Permissions(@JsonProperty("consentToken") final String consentToken) {
        this.consentToken = consentToken;
    }

    /**
     * This constructor will be used by Jackson to determine the default values for deserialized
     * objects that are missing properties.
     */
    @SuppressWarnings("unused")
    private Permissions() {
        consentToken = null;
    }

    /**
     * Returns the consentToken.
     *
     * @return the consentToken
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getConsentToken() {
        return consentToken;
    }
}
