/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.services;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.amazon.speech.speechlet.Directive;

/**
 * The speakDirective which will be packed with {@code DirectiveEnvelopeHeader} as {@code DirectiveEnvelope}
 * and send to the {@code DirectiveService}. It includes the speech.
 *
 */
@JsonTypeName("VoicePlayer.Speak")
public class SpeakDirective extends Directive {
    private String speech;

    public SpeakDirective() {

    }

    /**
     * Returns a new builder instance used to construct a new {@code SpeakDirective}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SpeakDirective} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SpeakDirective}
     */
    public SpeakDirective(final Builder builder) {
        speech = builder.speech;
    }

    /**
     * Gets the speech content in SpeakDirective.
     *
     * @return speech
     *
     */
    public String getSpeech() {
        return speech;
    }

    /**
     * Builder used to construct a new {@code SpeakDirective}.
     */
    public static final class Builder {
        private String speech;

        public Builder withSpeech(final String speech) {
            this.speech = speech;
            return this;
        }

        public SpeakDirective build() {
            return new SpeakDirective(this);
        }
    }

}
