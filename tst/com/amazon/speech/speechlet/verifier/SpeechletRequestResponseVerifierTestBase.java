package com.amazon.speech.speechlet.verifier;

import java.util.Locale;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.speechlet.interfaces.system.Error;
import com.amazon.speech.speechlet.interfaces.system.ErrorCause;
import com.amazon.speech.speechlet.interfaces.system.ErrorType;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;

public class SpeechletRequestResponseVerifierTestBase {
    protected SpeechletRequestEnvelope build(final String version, final SpeechletRequest request,
            final Session session) {
        return SpeechletRequestEnvelope
                .builder()
                .withVersion(version)
                .withSession(session)
                .withRequest(request)
                .build();
    }

    protected SpeechletResponseEnvelope buildResponseEnvelope(final String version,
            final SpeechletResponse response) {

        SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
        envelope.setVersion(version);
        envelope.setResponse(response);
        return envelope;
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

    protected LaunchRequest buildLaunchRequest() {
        return LaunchRequest
                .builder()
                .withRequestId("rId")
                .withLocale(Locale.forLanguageTag("en-US"))
                .build();
    }

    protected SpeechletRequest buildNonSessionRequest() {
        return ExceptionEncounteredRequest
                .builder()
                .withRequestId("rId")
                .withLocale(Locale.forLanguageTag("en-US"))
                .withError(
                        Error.builder()
                                .withType(ErrorType.INTERNAL_SERVICE_ERROR)
                                .withMessage("arbitraryMessage")
                                .build())
                .withCause(ErrorCause.builder().withRequestId("arbitraryRequestId").build())
                .build();
    }
}
