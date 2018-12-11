/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.util;

import com.amazon.ask.servlet.ServletConstants;

public class ServletUtils {

    /** Prevent instantiation */
    private ServletUtils() {}

    /**
     * Returns the value of the {@link ServletConstants#TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY} JVM system property as a {@link Long},
     * or returns null if the property is empty.
     * @return value of the {@link ServletConstants#TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY} system property as a {@link Long}, or null if the property is empty.
     * @throws IllegalArgumentException if the system property is present but not parseable as a Long.
     */
    public static Long getTimeStampToleranceSystemProperty() {
        String timestampToleranceAsString = System.getProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY);
        if (timestampToleranceAsString != null && !timestampToleranceAsString.trim().equals("")) {
            try {
                return Long.parseLong(timestampToleranceAsString);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Could not parse provided value as long: " + timestampToleranceAsString);
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the value of the {@link ServletConstants#DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY} JVM system property as a {@link Boolean}.
     * @return value of the {@link ServletConstants#DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY} system property as a {@link Boolean}.
     */
    public static Boolean isRequestSignatureCheckSystemPropertyDisabled(){
        String isRequestSignatureCheckDisabled = System.getProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY);
        return Boolean.valueOf(isRequestSignatureCheckDisabled);
    }

}
