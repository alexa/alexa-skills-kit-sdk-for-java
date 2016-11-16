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

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object used to notify a {@code SpeechletV2} that a new session has launched.
 *
 * @see SpeechletV2#onLaunch(SpeechletRequestEnvelope)
 */
@JsonTypeName("SessionStartedRequest")
public class SessionStartedRequest extends CoreSpeechletRequest {
    /**
     * Returns a new builder instance used to construct a new {@code SessionStartedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SessionStartedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SessionStartedRequest}
     */
    private SessionStartedRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Private constructor a new {@code SessionStartedRequest}.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     */
    private SessionStartedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    /**
     * Builder used to construct a new {@code SessionStartedRequest}.
     */
    public static final class Builder extends
            SpeechletRequestBuilder<Builder, SessionStartedRequest> {
        private Builder() {
        }

        @Override
        public SessionStartedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new SessionStartedRequest(this);
        }
    }
}
