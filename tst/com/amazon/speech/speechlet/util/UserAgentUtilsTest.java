package com.amazon.speech.speechlet.util;

import com.amazon.speech.Sdk;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAgentUtilsTest {

    @Test
    public void userAgentGeneratedWithExpectedSdkAndJvmVersion() {
        String version = "1.8.0_151";
        Properties props = mock(Properties.class);
        when(props.getProperty("java.version")).thenReturn(version);
        assertEquals(UserAgentUtils.getUserAgent(props), String.format("ask-java/%s Java/%s", Sdk.SDK_VERSION, version));
    }

    @Test
    public void exceptionThrownReturnsUnknownJvmVersion() {
        Properties props = mock(Properties.class);
        when(props.getProperty("java.version")).thenThrow(new SecurityException());
        assertEquals(UserAgentUtils.getUserAgent(props), String.format("ask-java/%s Java/UNKNOWN", Sdk.SDK_VERSION));
    }

}
