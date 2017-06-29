/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
