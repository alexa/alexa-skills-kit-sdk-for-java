/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.verifier;

import com.amazon.speech.json.SpeechletRequestEnvelope;

/**
 * A convenience wrapper class to convert a {@link SpeechletRequestVerifier} to fit the
 * {@link SpeechletRequestEnvelopeVerifier} interface.
 */
public class SpeechletRequestVerifierWrapper implements SpeechletRequestEnvelopeVerifier {
    @SuppressWarnings("deprecation")
    private final SpeechletRequestVerifier v1Verifier;

    @SuppressWarnings("deprecation")
    public SpeechletRequestVerifierWrapper(SpeechletRequestVerifier v1Verifier) {
        this.v1Verifier = v1Verifier;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean verify(SpeechletRequestEnvelope<?> speechletRequestEnvelope) {
        return v1Verifier.verify(speechletRequestEnvelope.getRequest(),
                speechletRequestEnvelope.getSession());
    }
}
