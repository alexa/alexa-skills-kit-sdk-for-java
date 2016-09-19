/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.Date;

import org.apache.commons.lang3.Validate;

import com.amazon.speech.slu.Intent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing an {@link Intent} for {@code Speechlet} invocation.
 *
 * @see Speechlet#onIntent(IntentRequest, Session)
 */
@JsonTypeName("IntentRequest")
public class IntentRequest extends SpeechletRequest {
    private final Intent intent;
    private final String locale;

    /**
     * Returns a new builder instance used to construct a new {@code IntentRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code IntentRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code IntentRequest}.
     */
    private IntentRequest(final Builder builder) {
        super(builder.requestId, builder.timestamp);
        this.intent = builder.intent;
        this.locale = builder.locale;
    }

    /**
     * Protected constructor used for JSON serialization and for extending this class.
     *
     * @param requestId
     *            the request identifier.
     * @param timestamp
     *            the request timestamp.
     * @param locale
     *            the request locale.
     * @param intent
     *            the intent to handle.
     */
    protected IntentRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp, @JsonProperty("locale") final String locale,
            @JsonProperty("intent") final Intent intent) {
        super(requestId, timestamp);
        this.intent = intent;
        this.locale = locale;
    }

    /**
     * Returns the request locale.
     *
     * @return the request locale.
     */
    public final String getLocale() { return locale; }

    /**
     * Returns the intent associated with this request. For a new session, the {@code Intent} passed
     * as a parameter is the one that caused the Alexa skill to be started. It can be an
     * {@code Intent} that is relevant to the skill and provides information on what to do, or it
     * can simply be the {@code Intent} resulting from the user saying
     * "Alexa, start &lt;Invocation Name&gt;".
     *
     * @return the intent to handle.
     */
    public Intent getIntent() {
        return intent;
    }

    /**
     * Builder used to construct a new {@code IntentRequest}.
     */
    public static final class Builder {
        private String requestId;
        private String locale;
        private Date timestamp = new Date();
        private Intent intent;

        private Builder() {
        }

        public Builder withRequestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder withTimestamp(final Date timestamp) {
            this.timestamp = (timestamp != null) ? new Date(timestamp.getTime()) : null;
            return this;
        }

        public Builder withLocale(final String locale) {
            this.locale = locale;
            return this;
        }

        public Builder withIntent(final Intent intent) {
            this.intent = intent;
            return this;
        }

        public IntentRequest build() {
            Validate.notBlank(requestId, "RequestId must be defined");
            return new IntentRequest(this);
        }
    }
}
