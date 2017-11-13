package com.amazon.speech.speechlet.services;

import com.amazon.speech.json.SpeechletSerializationTestBase;

import org.junit.Test;

/**
 * Serialization tests for {@link DirectiveEnvelope}.
 */
public class DirectiveEnvelopeSerializationTest extends SpeechletSerializationTestBase {
    private DirectiveEnvelope testdirectiveEnvelope;

    @Test
    public void serialization_DirectiveEnvelopeWithHeaderAndDirectiveCorrectly() throws Exception {
        DirectiveEnvelopeHeader header = DirectiveEnvelopeHeader.builder().withRequestId("requestId").build();
        SpeakDirective directive = SpeakDirective.builder().withSpeech("loading").build();
        testdirectiveEnvelope = DirectiveEnvelope.builder().withHeader(header).withDirective(directive).build();

        testSerialization(testdirectiveEnvelope);
    }
}
