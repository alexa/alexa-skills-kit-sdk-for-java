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
 * Interface for sending directives to the Alexa directive service.
 *
 */
public interface DirectiveService {
    /**
     * Send the specified {@code DirectiveEnvelope} to Alexa directive service.
     *
     * @param directiveEnvelope
     *                  directive to send to service
     * @param apiEndpoint
     *                  API endpoint from Alexa request
     * @param token
     *                  Bearer token for directive service
     * @throws ServiceException
     *                  if service call fails
     */
    void enqueue(DirectiveEnvelope directiveEnvelope, String apiEndpoint, String token) throws ServiceException;
}
