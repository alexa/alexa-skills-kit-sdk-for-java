/*
    Copyright 2014-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.util;

import com.amazon.speech.Sdk;

import java.util.Properties;

/**
 * Utilities for generating a user agent.
 */
public final class UserAgentUtils {

    /** Prevent instantiation */
    private UserAgentUtils() {}

    /**
     * Returns the user agent string.  Format is "(SDK name)/(SDK version) (Language)/(Language version)"
     *
     * @return user agent string
     */
    public static String getUserAgent() {
        return getUserAgent(System.getProperties());
    }

    static String getUserAgent(Properties systemProperties) {
        return String.format("ask-java/%s Java/%s", Sdk.SDK_VERSION, getJavaVersion(systemProperties));
    }

    private static String getJavaVersion(Properties systemProperties) {
        try {
            return systemProperties.getProperty("java.version");
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

}
