/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Base class for requests used for {@code Speechlet} invocation.
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        // The sub types that extend the SpeechletRequest class.
        @Type(value = LaunchRequest.class),
        @Type(value = IntentRequest.class),
        @Type(value = SessionStartedRequest.class),
        @Type(value = SessionEndedRequest.class)
})
public abstract class SpeechletRequest {
    private final String requestId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_8601_FORMAT, timezone = "UTC")
    private final Date timestamp;

    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Constructs a new {@code SpeechletRequest} with a request identifier and timestamp.
     *
     * @param requestId
     *            the unique identifier for the request
     * @param timestamp
     *            the timestamp for the request
     */
    protected SpeechletRequest(final String requestId, final Date timestamp) {
        this.requestId = requestId;
        this.timestamp = (timestamp != null) ? new Date(timestamp.getTime()) : null;
    }

    /**
     * Returns a unique request identifier. This can be used for logging and correlating events
     * linked to a request.
     *
     * @return the request identifier.
     */
    public final String getRequestId() {
        return requestId;
    }

    /**
     * Returns the request timestamp.
     *
     * @return the request timestamp.
     */
    public final Date getTimestamp() {
        return (timestamp != null) ? new Date(timestamp.getTime()) : null;
    }
}
