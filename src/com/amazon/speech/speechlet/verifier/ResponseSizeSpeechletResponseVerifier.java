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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Session;

/**
 * A {@link SpeechletResponseVerifier} to verify the size of the complete response returned in
 * {@link SpeechletResponseEnvelope}. In the current implementation, the
 * {@link #verify(SpeechletResponseEnvelope, Session)} method logs a warning if the response size in
 * bytes exceeds {@value #MAX_RESPONSE_SIZE}.
 * <p>
 * Note: This verifier currently does not not enforce response checks and always returns true. The
 * primary purpose of this verifier is to log a warning in the app developer's runtime.
 */
public class ResponseSizeSpeechletResponseVerifier implements SpeechletResponseVerifier {
    private static final Logger log = LoggerFactory
            .getLogger(ResponseSizeSpeechletResponseVerifier.class);

    private static final int MAX_RESPONSE_SIZE = 24 * 1024; // 24 KB

    @Override
    public boolean verify(SpeechletResponseEnvelope responseEnvelope, Session session) {
        if (responseEnvelope == null) {
            return false;
        }

        byte[] jsonBytes = null;
        try {
            jsonBytes = responseEnvelope.toJsonBytes();
        } catch (IOException e) {
            log.warn("Caught exception while trying to serialize ResponseEnvelope", e);
            return false;
        }

        int responseSize = jsonBytes.length;
        if (responseSize > MAX_RESPONSE_SIZE) {
            log.warn("Speechlet response with size of {} bytes exceeds the maximum allowed "
                    + "size of {} bytes and will be rejected by the Alexa service", responseSize,
                    MAX_RESPONSE_SIZE);
        }

        // We are currently not enforcing response checks. Always return true.
        return true;
    }
}
