/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
