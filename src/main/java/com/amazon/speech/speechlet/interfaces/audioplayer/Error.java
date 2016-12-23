/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.audioplayer;

/**
 * Represents an error associated with {@code PlaybackFailedRequest}.
 * 
 * @see com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest
 */
public class Error {
    private String message;
    private ErrorType type;

    /**
     * Returns a human-readable description of the error, or {@code null} if not present.
     * 
     * @return a human-readable description of the error, or {@code null} if not present.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the human-readable description of the error.
     * 
     * @param message
     *            the human-readable description of the error
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the type of the {@code Error}
     * 
     * @return the type of the {@code Error}
     */
    public ErrorType getType() {
        return type;
    }

    /**
     * Sets the type of the {@code Error}.
     * 
     * @param type
     *            the type of the {@code Error}
     */
    public void setType(ErrorType type) {
        this.type = type;
    }
}
