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

public class ServletUtils {

    /** Prevent instantiation */
    private ServletUtils() {}

    /**
     * Returns the value of a JVM system property as a {@link Long}, or returns null if the property
     * is empty.
     * @param property system property to read from
     * @return value of the system property as a {@link Long}, or null if the property is empty.
     * @throws IllegalArgumentException if the system property is present but not parseable as a Long.
     */
    public static Long getSystemPropertyAsLong(String property) {
        String timestampToleranceAsString = System.getProperty(property);
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

}
