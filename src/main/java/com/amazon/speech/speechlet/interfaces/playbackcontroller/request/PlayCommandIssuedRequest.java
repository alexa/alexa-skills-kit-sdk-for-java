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
 * The request object indicating that a user wishes to start/resume playback of a media item using
 * an on-client button press or GUI affordance.
 */
@JsonTypeName("PlaybackController.PlayCommandIssued")
public class PlayCommandIssuedRequest extends PlaybackControllerRequest {
    /**
     * Returns a new builder instance used to construct a new {@code PlayCommandIssuedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code PlayCommandIssuedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code PlayCommandIssuedRequest}
     */
    private PlayCommandIssuedRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Private constructor a new {@code PlayCommandIssuedRequest}.
     *
     * @param requestId
     *            the unique identifier associated with the request
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     */
    private PlayCommandIssuedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale) {
        super(requestId, timestamp, locale);
    }

    /**
     * Builder used to construct a new {@code PlayCommandIssuedRequest}.
     */
    public static final class Builder extends
            SpeechletRequest.SpeechletRequestBuilder<Builder, PlayCommandIssuedRequest> {
        private Builder() {
        }

        @Override
        public PlayCommandIssuedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new PlayCommandIssuedRequest(this);
        }
    }
}
