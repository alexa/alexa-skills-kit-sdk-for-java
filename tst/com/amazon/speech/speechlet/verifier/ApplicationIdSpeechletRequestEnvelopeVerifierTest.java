package com.amazon.speech.speechlet.verifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.speechlet.interfaces.system.SystemState;

@SuppressWarnings("deprecation")
public class ApplicationIdSpeechletRequestEnvelopeVerifierTest extends
        SpeechletRequestResponseVerifierTestBase {
    private static final Set<String> supportedApplicationIds = new HashSet<String>(Arrays.asList(
            "appId1", "appId2", "appId3", "appId4"));

    private ApplicationIdSpeechletRequestEnvelopeVerifier verifier;

    @Before
    public void setup() {
        verifier = new ApplicationIdSpeechletRequestEnvelopeVerifier(supportedApplicationIds);
    }

    @Test
    public void verify_emptySet_returnsTrue() {
        ApplicationIdSpeechletRequestEnvelopeVerifier localVerifier =
                new ApplicationIdSpeechletRequestEnvelopeVerifier(Collections.<String>emptySet());

        
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(buildSession())
                        .build();
        
        // Unit under test
        assertTrue(localVerifier.verify(envelope));
    }

    /**
     * Verifies that if the request type is expected to have a session, but does not, that it returns false.
     */
    @Test
    public void verify_nullSessionForSessionRequest_returnsFalse() {
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(null)
                        .build();
        
        // Unit under test
        assertFalse(verifier.verify(envelope));
    }

    /**
     * Verify that if the request is null, we return false because determine whether or not it is a
     * session-less request.
     */
    @Test
    public void verify_nullRequest_returnsFalse() {
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(null)
                        .withSession(buildSession())
                        .build();
        assertFalse(verifier.verify(envelope));
    }

    @Test
    public void verify_nullApplicationInSession_returnsFalse() {
        Session session =
                Session.builder().withSessionId("sId").withUser(new User("UserId")).build();

        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(session)
                        .build();
        
        // Unit under test
        assertFalse(verifier.verify(envelope));
    }

    @Test
    public void verify_nullApplicationIdInSession_returnsFalse() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application(null))
                        .withUser(new User("UserId"))
                        .build();
        
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(session)
                        .build();

        // Unit under test
        assertFalse(verifier.verify(envelope));
    }

    @Test
    public void verify_unsupportedApplicationIdInSession_returnsFalse() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application("applicationId"))
                        .withUser(new User("UserId"))
                        .build();
        
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(session)
                        .build();

        // Unit under test
        assertFalse(verifier.verify(envelope));
    }

    @Test
    public void verify_supportedApplicationIdInSession_returnsTrue() {
        Session session =
                Session.builder()
                        .withSessionId("sId")
                        .withApplication(new Application("appId1"))
                        .withUser(new User("UserId"))
                        .build();
        
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildLaunchRequest())
                        .withSession(session)
                        .build();

        // Unit under test
        assertTrue(verifier.verify(envelope));
    }
    
    @Test
    public void verify_nullSessionAndContextForNonSessionRequest_returnsFalse() {
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildNonSessionRequest())
                        .build();
        
        assertFalse(verifier.verify(envelope));
    }
    
    @Test
    public void verify_nullSessionAndNoSystemContextForNonSessionRequest_returnsFalse() {
        Context context = Context.builder().build();
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildNonSessionRequest())
                        .withContext(context)
                        .build();
        
        assertFalse(verifier.verify(envelope));
    }
    
    @Test
    public void verify_nullSessionAndNoApplictionForNonSessionRequest_returnsFalse() {
        SystemState state = SystemState.builder().build();
        Context context = Context.builder().addState(state).build();
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildNonSessionRequest())
                        .withContext(context)
                        .build();
        
        assertFalse(verifier.verify(envelope));
    }
    
    @Test
    public void verify_nullSessionAndUnsupportedApplicationForNonSessionRequest_returnsFalse() {
        Application application = new Application("someNotSupportedApplicationId");
        SystemState state = SystemState.builder().withApplication(application).build();
        Context context = Context.builder().addState(state).build();
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildNonSessionRequest())
                        .withContext(context)
                        .build();
        
        assertFalse(verifier.verify(envelope));
    }
    
    @Test
    public void verify_nullSessionAndSupportedApplicationForNonSessionRequest_returnsTrue() {
        Application application = new Application(supportedApplicationIds.iterator().next());
        SystemState state = SystemState.builder().withApplication(application).build();
        Context context = Context.builder().addState(state).build();
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
                        .builder()
                        .withRequest(buildNonSessionRequest())
                        .withContext(context)
                        .build();
        
        assertTrue(verifier.verify(envelope));
    }
}

