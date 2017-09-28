package com.amazon.speech.speechlet.verifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.User;

@SuppressWarnings("deprecation")
public class ApplicationIdSpeechletRequestVerifierTest extends
        SpeechletRequestResponseVerifierTestBase {
    private static final Set<String> supportedApplicationIds = new HashSet<String>(Arrays.asList(
            "appId1", "appId2", "appId3", "appId4"));

    private ApplicationIdSpeechletRequestVerifier verifier;

    @Before
    public void setup() {
        verifier = new ApplicationIdSpeechletRequestVerifier(supportedApplicationIds);
    }

    @Test
    public void verify_emptySet_returnsTrue() {
        ApplicationIdSpeechletRequestVerifier localVerifier =
                new ApplicationIdSpeechletRequestVerifier(Collections.<String>emptySet());

        // Unit under test
        assertTrue(localVerifier.verify(buildLaunchRequest(), buildSession()));
    }

    /**
     * Verifies that if the request type is expected to have a session, but does not, that it returns false.
     */
    @Test
    public void verify_nullSessionForSessionRequest_returnsFalse() {
        // Unit under test
        assertFalse(verifier.verify(buildLaunchRequest(), null /*session*/));
    }
    
    /**
     * Some requests naturally do not have a session, and thus should not be filtered out on that
     * account. To prevent these requests with the incorrect applicationId from coming through,
     * ApplicationIdSpeechletRequestVerifierV2 should be used.
     */
    @Test
    public void verify_nullSessionForNonSessionRequest_returnsTrue() {
        assertTrue(verifier.verify(buildNonSessionRequest(), null /*session*/));
    }

    /**
     * Verify that if the request is null, we return false because determine whether or not it is a
     * session-less request.
     */
    @Test
    public void verify_nullRequest_returnsFalse() {
        assertFalse(verifier.verify(null /*SpeechletRequest*/, buildSession()));
    }

    @Test
    public void verify_nullApplication_returnsFalse() {
        Session session =
                Session.builder().withSessionId("sId").withUser(new User("UserId")).build();

        // Unit under test
        assertFalse(verifier.verify(buildLaunchRequest(), session));
    }

    @Test
    public void verify_nullApplicationId_returnsFalse() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application(null))
                        .withUser(new User("UserId"))
                        .build();

        // Unit under test
        assertFalse(verifier.verify(buildLaunchRequest(), session));
    }

    @Test
    public void verify_unsupportedApplicationId_returnsFalse() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application("applicationId"))
                        .withUser(new User("UserId"))
                        .build();

        // Unit under test
        assertFalse(verifier.verify(buildLaunchRequest(), session));
    }

    @Test
    public void verify_supportedApplicationId_returnsTrue() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application("appId1"))
                        .withUser(new User("UserId"))
                        .build();

        // Unit under test
        assertTrue(verifier.verify(buildLaunchRequest(), session));
    }
}
