package com.amazon.speech.ui;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A card that will ask user for consent on the specified permissions. Return this card if the
 * request requires a consent token, but the token included in the request is null or does not
 * contain enough permissions.
 *
 * @see Card
 */
@JsonTypeName("AskForPermissionsConsent")
public class AskForPermissionsConsentCard extends Card {
    private Set<String> permissions;

    /**
     * Returns the permissions that require the customer's consent
     *
     * @return the permissions
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions that require the customer's consent
     *
     * @param permissions
     *            the permissions to set
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
