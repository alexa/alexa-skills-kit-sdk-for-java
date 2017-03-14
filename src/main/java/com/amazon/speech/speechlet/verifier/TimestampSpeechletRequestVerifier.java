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

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletRequestHandler;

/**
 * Verifies whether or not timestamps are valid within a certain tolerance.
 * <p>
 * Every request sent by Alexa includes a timestamp. This information is part of the signed portion
 * of the request, so it cannot be changed without also invalidating the request signature. Using
 * this timestamp to verify the freshness of the request before responding protects the client's
 * service from attackers attempting a "replay" attack in which they acquire a properly signed
 * request and then repeatedly resend it to disrupt your service.
 *
 * @see <a
 *      href="https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/developing-an-alexa-skill-as-a-web-service#Checking%20the%20Timestamp%20of%20the%20Request">
 *      Checking the Timestamp of the Request</a>
 */
public class TimestampSpeechletRequestVerifier implements SpeechletRequestVerifier {
    private static final Logger log = LoggerFactory.getLogger(TimestampSpeechletRequestVerifier.class);

    private final long toleranceInMilliseconds;

    /**
     * Constructs a new timestamp verifier with the provided tolerance (in the provided units).
     *
     * @param tolerance
     *            the tolerance of this verifier
     * @param unit
     *            the time unit of the {@code tolerance} parameter
     */
    public TimestampSpeechletRequestVerifier(long tolerance, TimeUnit unit) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("A negative tolerance is not supported");
        }
        toleranceInMilliseconds = unit.toMillis(tolerance);
    }

    /**
     * Returns true if the provided date is inclusively within the verifier tolerance, either in the
     * past or future, of the current system time. This method will return false if
     * {@link SpeechletRequest} or {@link SpeechletRequest#getTimestamp()} is {@code null}.
     *
     * @param request
     *            {@link SpeechletRequest} to validate
     * @param session
     *            {@link Session} context within which to validate the call
     * @return true if the provided date is within the verifier tolerance, false otherwise
     */
    @Override
    public boolean verify(SpeechletRequest request, Session session) {
        if (request == null || request.getTimestamp() == null) {
            return false;
        }

        long delta = Math.abs(System.currentTimeMillis() - request.getTimestamp().getTime());
        boolean withinTolerance = delta <= toleranceInMilliseconds;

        if (!withinTolerance) {
            log.warn("Request with id {} and timestamp {} failed timestamp validation with a "
                    + "delta {}", request.getRequestId(), request.getTimestamp().getTime(), delta);
        }

        return withinTolerance;
    }
}
