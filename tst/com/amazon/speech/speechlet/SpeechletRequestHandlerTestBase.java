package com.amazon.speech.speechlet;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SpeechletRequestHandlerTestBase {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // -----------------
    // Build Helpers

    protected SpeechletRequestEnvelope build(final String version, final SpeechletRequest request,
            final Session session, final Context context) {
        return SpeechletRequestEnvelope
                .builder()
                .withVersion(version)
                .withSession(session)
                .withRequest(request)
                .withContext(context)
                .build();
    }
    
    /**
     * @return a test context
     */
    protected Context buildContext(String applicationId) {
        SystemState state =
                SystemState.builder().withApplication(new Application(applicationId)).build();
        return Context.builder().addState(state).build();
    }

    /**
     * @return a test session.
     */
    protected Session buildSession() {
        return Session
                .builder()
                .withSessionId("sId")
                .withApplication(new Application("applicationId"))
                .withUser(new User("UserId"))
                .build();
    }
}
