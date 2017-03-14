/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.playbackcontroller.request;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object indicating that a user wishes to skip to the previous media item in their
 * playback queue using an on-client button press or GUI affordance.
 */
@JsonTypeName("PlaybackController.PreviousCommandIssued")
public class PreviousCommandIssuedRequest extends PlaybackControllerRequest {
    /**
     * Returns a new builder instance used to construct a new {@code PreviousCommandIssuedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code PreviousCommandIssuedRequest} from a
     * {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code PreviousCommandIssuedRequest}
     */
    private PreviousCommandIssuedRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Private constructor a new {@code PreviousCommandIssuedRequest}.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     */
    private PreviousCommandIssuedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    /**
     * Builder used to construct a new {@code PreviousCommandIssuedRequest}.
     */
    public static final class Builder extends
            SpeechletRequest.SpeechletRequestBuilder<Builder, PreviousCommandIssuedRequest> {
        private Builder() {
        }

        @Override
        public PreviousCommandIssuedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new PreviousCommandIssuedRequest(this);
        }
    }
}
