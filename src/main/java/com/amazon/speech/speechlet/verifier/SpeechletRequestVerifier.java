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

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;

/**
 * Verifier for validating the received {@link SpeechletRequest}s from the devices.
 * 
 * @deprecated Please use {@link SpeechletRequestEnvelopeVerifier} instead. This version cannot deal with
 *             additional fields in the {@link com.amazon.speech.json.SpeechletRequestEnvelope}.
 */
@Deprecated
public interface SpeechletRequestVerifier {
    /**
     * Verifies a {@link SpeechletRequest} within the context of the {@link Session} in which it was
     * received. Returns true if the verify succeeded, false otherwise.
     * 
     * @param request
     *            {@link SpeechletRequest} to verify
     * @param session
     *            {@link Session} context within which to verify the call
     * @return true if the verify succeeded, false otherwise
     */
    boolean verify(SpeechletRequest request, Session session);
}
