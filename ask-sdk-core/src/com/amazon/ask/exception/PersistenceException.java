/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.exception;

/**
 * Exception thrown when an exception is encountered while a {@link com.amazon.ask.attributes.persistence.PersistenceAdapter} is
 * handling persistent attributes.
 */
public class PersistenceException extends AskSdkException {

    /**
     * Constructor to build an instance of {@link PersistenceException} with a message.
     * @param message description of the response.
     */
    public PersistenceException(final String message) {
        super(message);
    }

    /**
     * Constructor to build an instance of {@link PersistenceException} with a message and cause.
     * @param message description of the response.
     * @param cause instance of type {@link Throwable}.
     */
    public PersistenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
