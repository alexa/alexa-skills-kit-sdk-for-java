/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.template;

/**
 * This class represents an item on a list. If this list item is selected by the user, your skill
 * will receive an
 * {@link com.amazon.speech.speechlet.interfaces.display.request.ElementSelectedRequest} with the
 * supplied token.
 */
public class ListItem {
    private String token;

    /**
     * Returns the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
