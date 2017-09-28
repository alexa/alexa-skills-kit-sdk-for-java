package com.amazon.speech.speechlet.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

@Deprecated
public class TimestampVerifierTest {
    private static final long TOLERANCE_IN_MILLIS = 2000;
    private static final TimestampVerifier VERIFIER = new TimestampVerifier(TOLERANCE_IN_MILLIS,
            TimeUnit.MILLISECONDS);

    @Test
    public void construct_withNegativeTolerance_throwsIllegalArgumentException() {
        try {
            new TimestampVerifier(-1, TimeUnit.SECONDS);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    @Test
    public void isValid_currentDate_returnsTrue() {
        assertTrue(VERIFIER.isValid(new Date()));
    }

    @Test
    public void isValid_recentOldDate_returnsTrue() {
        assertTrue(VERIFIER.isValid(new Date(System.currentTimeMillis() - TOLERANCE_IN_MILLIS / 2)));
    }

    @Test
    public void isValid_upcomingNewDate_returnsTrue() {
        assertTrue(VERIFIER.isValid(new Date(System.currentTimeMillis() + TOLERANCE_IN_MILLIS / 2)));
    }

    @Test
    public void isValid_tooOldDate_returnsFalse() {
        assertFalse(VERIFIER
                .isValid(new Date(System.currentTimeMillis() - TOLERANCE_IN_MILLIS * 2)));
    }

    @Test
    public void isValid_tooNewDate_returnsFalse() {
        assertFalse(VERIFIER
                .isValid(new Date(System.currentTimeMillis() + TOLERANCE_IN_MILLIS * 2)));
    }

    @Test
    public void isValid_nullDate_returnsFalse() {
        assertFalse(VERIFIER.isValid(null));
    }
}
