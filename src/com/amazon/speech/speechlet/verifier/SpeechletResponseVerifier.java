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

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Session;

/**
 * Verifier for validating the {@link SpeechletResponseEnvelope} received from the Speechlets.
 */
public interface SpeechletResponseVerifier {
    /**
     * Verifies a {@link SpeechletResponseEnvelope} within the context of the {@link Session} in
     * which it was received. Returns true if the verify succeeded, false otherwise.
     * 
     * @param responseEnvelope
     *            {@link SpeechletResponseEnvelope} to verify
     * @param session
     *            {@link Session} context within which to verify the call
     * @return true if the verify succeeded, false otherwise
     */
    boolean verify(SpeechletResponseEnvelope responseEnvelope, Session session);
}
