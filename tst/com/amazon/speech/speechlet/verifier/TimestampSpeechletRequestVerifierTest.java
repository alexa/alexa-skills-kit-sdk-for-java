package com.amazon.speech.speechlet.verifier;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

public class TimestampSpeechletRequestVerifierTest extends SpeechletRequestResponseVerifierTestBase {
    private static final long TOLERANCE_IN_MILLIS = 2000;
    private static final TimestampSpeechletRequestVerifier verifier =
            new TimestampSpeechletRequestVerifier(TOLERANCE_IN_MILLIS, TimeUnit.MILLISECONDS);

    @Test
    public void construct_withNegativeTolerance_throwsIllegalArgumentException() {
        try {
            new TimestampSpeechletRequestVerifier(-1, TimeUnit.SECONDS);
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void verify_currentDate_returnsTrue() {
        assertTrue(verifier.verify(getRequest(new Date()), buildSession()));
    }

    @Test
    public void verify_recentOldDate_returnsTrue() {
        assertTrue(verifier.verify(getRequest(new Date(System.currentTimeMillis()
                - TOLERANCE_IN_MILLIS / 2)), buildSession()));
    }

    @Test
    public void verify_upcomingNewDate_returnsTrue() {
        assertTrue(verifier.verify(getRequest(new Date(System.currentTimeMillis()
                + TOLERANCE_IN_MILLIS / 2)), buildSession()));
    }

    @Test
    public void verify_tooOldDate_returnsFalse() {
        assertFalse(verifier.verify(getRequest(new Date(System.currentTimeMillis()
                - TOLERANCE_IN_MILLIS * 2)), buildSession()));
    }

    @Test
    public void verify_tooNewDate_returnsFalse() {
        assertFalse(verifier.verify(getRequest(new Date(System.currentTimeMillis()
                + TOLERANCE_IN_MILLIS * 2)), buildSession()));
    }

    @Test
    public void verify_nullDate_returnsFalse() {
        assertFalse(verifier.verify(getRequest(null), buildSession()));
    }

    @Test
    public void verify_nullRequest_returnsTrue() {
        assertFalse(verifier.verify(null, buildSession()));
    }

    private SpeechletRequest getRequest(Date timestamp) {
        return LaunchRequest
                .builder()
                .withRequestId("rId")
                .withTimestamp(timestamp)
                .withLocale(Locale.forLanguageTag("en-US"))
                .build();
    }
}
