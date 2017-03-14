/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.authentication;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;

/**
 * Verifies whether or not timestamps are valid within a certain tolerance.
 * 
 * @deprecated Use {@link TimestampSpeechletRequestVerifier} instead of this class.
 */
@Deprecated
public class TimestampVerifier {
    private final long toleranceInMilliseconds;

    /**
     * Constructs a new timestamp verifier with the provided tolerance (in the provided units).
     * 
     * @param tolerance
     *            the tolerance of this verifier
     * @param unit
     *            the time unit of the {@code tolerance} parameter
     */
    public TimestampVerifier(long tolerance, TimeUnit unit) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("A negative tolerance is not supported");
        }
        toleranceInMilliseconds = unit.toMillis(tolerance);
    }

    /**
     * Returns true if the provided date is inclusively within the verifier tolerance, either in the
     * past or future, of the current system time. This method will return false if provided a
     * {@code null} date.
     * 
     * @param timestamp
     *            the timestamp to verify
     * @return True if the provided date is within the verifier tolerance
     */
    public boolean isValid(Date timestamp) {
        if (timestamp != null) {
            long delta = Math.abs(System.currentTimeMillis() - timestamp.getTime());
            return delta <= toleranceInMilliseconds;
        } else {
            return false;
        }
    }
}
