/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletRequestEnvelopeVerifier;

/**
 * Request handler for Lambda services
 */
public class LambdaSpeechletRequestHandler extends SpeechletRequestHandler {
    public LambdaSpeechletRequestHandler(Set<String> supportedApplicationIds) {
        super(requestVerifiers(supportedApplicationIds));
    }

    private static List<SpeechletRequestEnvelopeVerifier> requestVerifiers(
            Set<String> supportedApplicationIds) {
        return Arrays.<SpeechletRequestEnvelopeVerifier>asList(new ApplicationIdSpeechletRequestEnvelopeVerifier(
                supportedApplicationIds));
    }
}
