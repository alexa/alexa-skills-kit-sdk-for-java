/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code Application} included as part of the {@code Session} encapsulates metadata about the
 * Alexa skill being invoked by a customer. This metadata can be used to confirm that customer
 * requests are indeed being routed to the correct skill. For example, if you've built a service to
 * handle requests for the skill with ID {@code amzn1.echo-sdk-ams.app.1234} but begin to receive
 * requests with the unexpected ID {amzn1.echo-sdk-ams.app.5678}, it is recommended that you reject
 * these requests.
 *
 * @see SpeechletV2
 * @see Session
 */
public class Application {
    private final String applicationId;

    /**
     * Constructs an application.
     *
     * @param applicationId
     *            the application identifier
     */
    public Application(@JsonProperty("applicationId") final String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * This constructor will be used by Jackson to determine the default values for deserialized
     * objects that are missing properties.
     */
    @SuppressWarnings("unused")
    private Application() {
        applicationId = null;
    }

    /**
     * Returns the application identifier.
     *
     * @return the application identifier
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getApplicationId() {
        return applicationId;
    }
}