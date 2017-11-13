/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.services;

/**
 * Defines a general exception that a {@code DirectiveService} can throw when encountering difficulty while
 *  sending a service.
 *
 */
public class ServiceException extends Exception {

    /**
     * Constructs a new exception with null as its detail message. The cause is not initialized.
     */
    public ServiceException() {

    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized.
     *
     * @param message
     *            the detail message
     */
    public ServiceException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause. Note that the detail
     * message associated with cause is not automatically incorporated in this exception's detail
     * message.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause. A {@code null} value is permitted, and indicates that the cause is
     *            nonexistent or unknown
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
