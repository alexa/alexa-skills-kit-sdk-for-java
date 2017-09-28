package com.amazon.speech.speechlet.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

@Deprecated
public class ApplicationIdVerifierTest {
    private static final String SUPPORTED_ID = "supportedId";
    private static final String UNSUPPORTED_ID = "unsupportedId";
    private static final Set<String> SUPPORTED_IDS = new HashSet<>(Arrays.asList(SUPPORTED_ID));

    @Test
    public void isSupported_emptySet_nullIsSupported() {
        assertTrue(new ApplicationIdVerifier(Collections.<String>emptySet()).isSupported(null));
    }

    @Test
    public void isSupported_emptySet_nonNullIsSupported() {
        assertTrue(new ApplicationIdVerifier(Collections.<String>emptySet())
                .isSupported(SUPPORTED_ID));
    }

    @Test
    public void isSupported_supportedInSet_isSupported() {
        assertTrue(new ApplicationIdVerifier(SUPPORTED_IDS).isSupported(SUPPORTED_ID));
    }

    @Test
    public void isSupported_unsupportedNotInSet_notSupported() {
        assertFalse(new ApplicationIdVerifier(SUPPORTED_IDS).isSupported(UNSUPPORTED_ID));
    }
}
