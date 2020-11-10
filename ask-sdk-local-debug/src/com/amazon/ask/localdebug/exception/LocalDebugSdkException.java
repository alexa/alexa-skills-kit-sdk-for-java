/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.exception;

/**
 * Base exception class for all exceptions encountered during local debug session.
 */
public class LocalDebugSdkException extends RuntimeException {
    /**
     * Constructs an instance of LocalDebugSdkException with the given message.
     * @param message String error message.
     */
    public LocalDebugSdkException(final String message) {
        super(message);
    }

    /**
     * Constructs an instance of LocalDebugSdkException with the given message and innerException.
     * @param message message exception message.
     * @param innerException instance of {@link Exception}.
     */
    public LocalDebugSdkException(final String message, final Exception innerException) {
        super(message, innerException);
    }

    @Override
    public String toString() {
        if (getCause() != null) {
            return getCause().toString();
        }
        return super.toString();
    }
}
