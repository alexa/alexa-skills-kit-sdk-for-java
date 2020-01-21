/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.verifiers;

import com.amazon.ask.model.Request;
import com.amazon.ask.servlet.ServletConstants;
import com.amazon.ask.util.ValidationUtils;

import java.util.concurrent.TimeUnit;

import static com.amazon.ask.servlet.ServletConstants.MAXIMUM_TOLERANCE_MILLIS;

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
 *      href="https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-a-web-service.html#timestamp">
 *      Checking the Timestamp of the Request</a>
 */
public class SkillRequestTimestampVerifier implements SkillServletVerifier {

    /**
     * Tolerance of this verifier, in milliseconds used to construct a new timestamp verifier.
     */
    private final long toleranceInMilliseconds;

    /**
     * Constructs a new timestamp verifier with the provided tolerance milliseconds.
     *
     * @param toleranceInMilliseconds
     *            the tolerance of this verifier, in milliseconds. must be non-negative and less than
     *            {@value ServletConstants#MAXIMUM_TOLERANCE_MILLIS}.
     */
    public SkillRequestTimestampVerifier(final long toleranceInMilliseconds) {
        if (toleranceInMilliseconds > MAXIMUM_TOLERANCE_MILLIS) {
            throw new IllegalArgumentException(String.format("Provided tolerance value %s exceeds the maximum"
                    + " allowed %s", toleranceInMilliseconds, MAXIMUM_TOLERANCE_MILLIS));
        } else if (toleranceInMilliseconds < 0) {
            throw new IllegalArgumentException("A negative tolerance is not supported");
        }
        this.toleranceInMilliseconds = toleranceInMilliseconds;
    }

    /**
     * Constructs a new timestamp verifier with the provided tolerance and timeUnit.
     *
     * @param tolerance
     *            the tolerance of this verifier must be non-negative and less than
     *            {@value ServletConstants#MAXIMUM_TOLERANCE_MILLIS} after converting to milliseconds.
     * @param timeUnit
     *            {@link TimeUnit} must be non-null.
     */
    public SkillRequestTimestampVerifier(final long tolerance, final TimeUnit timeUnit) {
        this(ValidationUtils.assertNotNull(timeUnit, "timeUnit").toMillis(tolerance));
    }

    /**
     * Validates if the provided date is inclusively within the verifier tolerance, either in the
     * past or future, of the current system time. This method will throw a {@link SecurityException} if the
     * tolerance is not in the expected range, or if the request is null or does not contain a timestamp value.
     *
     * {@inheritDoc}
     */
    public void verify(final AlexaHttpRequest alexaHttpRequest) {
        if (alexaHttpRequest.getDeserializedRequestEnvelope() == null) {
            throw new SecurityException("Incoming request did not contain a request envelope");
        }
        Request request = alexaHttpRequest.getDeserializedRequestEnvelope().getRequest();
        if (request == null || request.getTimestamp() == null) {
            throw new SecurityException("Incoming request was null or did not contain a timestamp to evaluate");
        }

        long requestTimestamp = request.getTimestamp().toInstant().toEpochMilli();
        long delta = Math.abs(System.currentTimeMillis() - requestTimestamp);
        boolean withinTolerance = delta <= toleranceInMilliseconds;

        if (!withinTolerance) {
            throw new SecurityException(String.format("Request with id %s and timestamp %s failed timestamp validation"
                    + " with a delta of %s", request.getRequestId(), requestTimestamp, delta));
        }
    }

}
