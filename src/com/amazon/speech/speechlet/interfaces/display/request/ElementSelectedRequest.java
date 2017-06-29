/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.request;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object indicating that an element has been touched or selected on the display.
 */
@JsonTypeName("Display.ElementSelected")
public class ElementSelectedRequest extends DisplayRequest {
    private final String token;

    /**
     * Returns a new builder instance used to construct a new {@code ElementSelectedRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code ElementSelectedRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code ElementSelectedRequest}
     */
    private ElementSelectedRequest(final Builder builder) {
        super(builder);
        this.token = builder.token;
    }

    /**
     * Protected constructor used for JSON serialization and for extending this class.
     *
     * @param requestId
     *            the request identifier
     * @param timestamp
     *            the request timestamp
     * @param locale
     *            the locale of the request
     * @param token
     *            the token that represents the selected item
     */
    private ElementSelectedRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("token") final String token) {
        super(requestId, timestamp, locale);
        this.token = token;
    }

    /**
     * Returns the token of the selected element
     *
     * @return the selected element's token that was attached when the list was rendered
     */
    public String getToken() {
        return token;
    }

    /**
     * Builder used to construct a new {@code ElementSelectedRequest}.
     */
    public static final class Builder extends
            SpeechletRequest.SpeechletRequestBuilder<Builder, ElementSelectedRequest> {
        private String token;

        private Builder() {
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        @Override
        public ElementSelectedRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new ElementSelectedRequest(this);
        }
    }
}
