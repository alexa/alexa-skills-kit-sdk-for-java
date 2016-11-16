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

import com.amazon.speech.slu.Intent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The request object containing an {@link Intent} for {@code SpeechletV2} invocation.
 *
 * @see SpeechletV2#onIntent(SpeechletRequestEnvelope)
 */
@JsonTypeName("IntentRequest")
public class IntentRequest extends CoreSpeechletRequest {
    private final Intent intent;

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
     *            the builder used to construct the {@code IntentRequest}
     */
    private IntentRequest(final Builder builder) {
        super(builder);
        this.intent = builder.intent;
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
     * @param intent
     *            the intent to handle
     */
    protected IntentRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale, @JsonProperty("intent") final Intent intent) {
        super(requestId, timestamp, locale);
        this.intent = intent;
    }

    /**
     * Returns the intent associated with this request. For a new session, the {@code Intent} passed
     * as a parameter is the one that caused the Alexa skill to be started. It can be an
     * {@code Intent} that is relevant to the skill and provides information on what to do, or it
     * can simply be the {@code Intent} resulting from the user saying
     * "Alexa, start &lt;Invocation Name&gt;".
     *
     * @return the intent to handle
     */
    public Intent getIntent() {
        return intent;
    }

    /**
     * Builder used to construct a new {@code IntentRequest}.
     */
    public static final class Builder extends SpeechletRequestBuilder<Builder, IntentRequest> {
        private Intent intent;

        private Builder() {
        }

        public Builder withIntent(final Intent intent) {
            this.intent = intent;
            return this;
        }

        @Override
        public IntentRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new IntentRequest(this);
        }
    }
}
