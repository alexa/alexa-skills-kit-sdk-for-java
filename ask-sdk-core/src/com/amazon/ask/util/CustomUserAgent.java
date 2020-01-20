/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util;

/**
 * Enum holds the values of all the custom user agents that need to be appended to the base user agent.
 */
public enum CustomUserAgent {

    /**
     * Template resolver's user agent.
     */
    TEMPLATE_RESOLVER("templateResolver");

    /**
     * User agent string.
     */
    private final String userAgent;

    /**
     * Constructs a custom user agent instance with the given string.
     * @param userAgent custom user agent string.
     */
    CustomUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Returns the user agent.
     * @return userAgent.
     */
    public String getUserAgent() {
        return this.userAgent;
    }
}
