/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util;

import org.slf4j.Logger;

import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class UserAgentUtils {

    private static Logger logger = getLogger(UserAgentUtils.class);

    /** Prevent instantiation */
    private UserAgentUtils() {}

    /**
     * Returns the user agent string.  Format is "(SDK name)/(SDK version) (Language)/(Language version)"
     *
     * @return user agent string
     */
    public static String getUserAgent() {
        return getUserAgent(null);
    }

    /**
     * Returns the user agent string.  Format is "(SDK name)/(SDK version) (Language)/(Language version)"
     * Custom user agent will be appended to the end of the standard user agent.
     *
     * @param customUserAgent custom user agent to append to end of standard user agent
     * @return user agent string
     */
    public static String getUserAgent(String customUserAgent) {
        Properties systemProperties;
        try {
            systemProperties = System.getProperties();
        } catch (SecurityException e) {
            logger.debug("Unable to determine JVM version due to security restrictions.");
            systemProperties = null;
        }
        return internalGetUserAgent(systemProperties, customUserAgent);
    }

    static String internalGetUserAgent(Properties systemProperties, String customUserAgent) {
        String coreUserAgent = String.format("ask-java/%s Java/%s", SdkConstants.SDK_VERSION, getJavaVersion(systemProperties));
        return coreUserAgent + (customUserAgent != null ? " " + customUserAgent : "");
    }

    private static String getJavaVersion(Properties systemProperties) {
        if (systemProperties != null && systemProperties.getProperty("java.version") != null) {
            return systemProperties.getProperty("java.version");
        }
        return "UNKNOWN";
    }

}
