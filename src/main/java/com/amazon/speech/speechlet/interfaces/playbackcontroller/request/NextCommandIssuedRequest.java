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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object indicating that a user wishes to skip to the next media item in their playback
 * queue using an on-client button press or GUI affordance.
 */
@JsonTypeName("PlaybackController.NextCommandIssued")
public class NextCommandIssuedRequest extends PlaybackControllerRequest {
    /**
     * Returns a new builder instance used to construct a new {@code NextCommandIssuedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code NextCommandIssuedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code NextCommandIssuedRequest}
     */
    private NextCommandIssuedRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Private constructor a new {@code NextCommandIssuedRequest}.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     */
    private NextCommandIssuedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    /**
     * Builder used to construct a new {@code NextCommandIssuedRequest}.
     */
    public static final class Builder extends
            SpeechletRequestBuilder<Builder, NextCommandIssuedRequest> {
        private Builder() {
        }

        @Override
        public NextCommandIssuedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new NextCommandIssuedRequest(this);
        }
    }
}
