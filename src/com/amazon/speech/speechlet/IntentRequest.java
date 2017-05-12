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
 * @see SpeechletV2#onIntent
 */
@JsonTypeName("IntentRequest")
public class IntentRequest extends CoreSpeechletRequest {

    /**
     * When a skill has a managed dialog configured, this field indicates the current dialog state
     * for the intent request.
     */
    public enum DialogState {
        /**
         * Indicates this is the first turn in a multi-turn dialog. Skills can use this state to
         * trigger behavior that only needs to be executed in the first turn. For example, a skill
         * may wish to provide missing slot values that it can determine based on the current user,
         * or other such session information, but doesn't wish to perform that action for every turn
         * in a dialog.
         */
        STARTED,

        /**
         * Indicates that a multi-turn dialog is in process and it is not the first turn. Skills may
         * assume that all of the required slot values and confirmations have not yet been provided,
         * and react accordingly (for instance by immediately returning a response containing a
         * {@link com.amazon.speech.speechlet.dialog.directives.DelegateDirective}).
         */
        IN_PROGRESS,

        /**
         * Indicates that all required slot values and confirmations have been provided, the dialog
         * is considered complete, and the skill can proceed to fulfilling the intent. Nevertheless,
         * the skill may manually continue the dialog if it determines at runtime that it requires
         * more input in order to fulfill the intent, in which case it may return an appropriate
         * {@link com.amazon.speech.speechlet.dialog.directives.DialogDirective} and update slot
         * values/confirmations as required.
         */
        COMPLETED;
    }

    private final Intent intent;
    private final DialogState dialogState;

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
        this.dialogState = builder.dialogState;
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
     * @param dialogState
     *            the dialog state
     */
    protected IntentRequest(@JsonProperty("requestId") final String requestId,
            @JsonProperty("timestamp") final Date timestamp,
            @JsonProperty("locale") final Locale locale,
            @JsonProperty("intent") final Intent intent,
            @JsonProperty("dialogState") final DialogState dialogState) {
        super(requestId, timestamp, locale);
        this.intent = intent;
        this.dialogState = dialogState;
    }

    /**
     * Returns the intent associated with this request. For a new session, the {@code Intent} passed
     * as a parameter is the one that caused the Alexa skill to be started. It can be an
     * {@code Intent} that is relevant to the skill and provides information on what to do, or it
     * can simply be the {@code Intent} resulting from the user saying "Alexa, start &lt;Invocation
     * Name&gt;".
     *
     * @return the intent to handle
     */
    public Intent getIntent() {
        return intent;
    }

    /**
     * Returns the isInDialog value associated with this request.
     *
     * @return the isInDialog value
     */
    public DialogState getDialogState() {
        return dialogState;
    }

    /**
     * Builder used to construct a new {@code IntentRequest}.
     */
    public static final class Builder extends SpeechletRequestBuilder<Builder, IntentRequest> {
        private Intent intent;
        private DialogState dialogState;

        private Builder() {
        }

        public Builder withIntent(final Intent intent) {
            this.intent = intent;
            return this;
        }

        public Builder withDialogState(final DialogState dialogState) {
            this.dialogState = dialogState;
            return this;
        }

        @Override
        public IntentRequest build() {
            Validate.notBlank(getRequestId(), "RequestId must be defined");
            return new IntentRequest(this);
        }
    }
}
