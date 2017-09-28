package com.amazon.speech.speechlet.verifier;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;

@SuppressWarnings("deprecation")
public class SpeechletRequestVerifierV1WrapperTest {
    @Test
    public void verify_whenV1ReturnsFalse_returnsFalse() {
        SpeechletRequestVerifier mockVerifier = Mockito.mock(SpeechletRequestVerifier.class);
        Mockito.when(mockVerifier.verify(Mockito.<SpeechletRequest>any(), Mockito.<Session>any()))
                .thenReturn(false);
        SpeechletRequestVerifierWrapper wrapper =
                new SpeechletRequestVerifierWrapper(mockVerifier);

        Assert.assertFalse(wrapper.verify(Mockito.mock(SpeechletRequestEnvelope.class)));
    }

    @Test
    public void verify_whenV1ReturnsFalse_returnsTrue() {
        SpeechletRequestVerifier mockVerifier = Mockito.mock(SpeechletRequestVerifier.class);
        Mockito.when(mockVerifier.verify(Mockito.<SpeechletRequest>any(), Mockito.<Session>any()))
                .thenReturn(true);
        SpeechletRequestVerifierWrapper wrapper =
                new SpeechletRequestVerifierWrapper(mockVerifier);

        Assert.assertTrue(wrapper.verify(Mockito.mock(SpeechletRequestEnvelope.class)));
    }
}
