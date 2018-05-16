/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util;

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
        assertEquals(UserAgentUtils.internalGetUserAgent(props, null), String.format("ask-java/%s Java/%s", SdkConstants.SDK_VERSION, version));
    }

    @Test
    public void customUserAgentAppendedToEnd() {
        String version = "1.8.0_151";
        String customUserAgent = "foo/bar/baz";
        Properties props = mock(Properties.class);
        when(props.getProperty("java.version")).thenReturn(version);
        assertEquals(UserAgentUtils.internalGetUserAgent(props, customUserAgent), String.format("ask-java/%s Java/%s foo/bar/baz", SdkConstants.SDK_VERSION, version));
    }

    @Test
    public void nullJvmPropertiesReturnsUnknownJvmVersion() {
        assertEquals(UserAgentUtils.internalGetUserAgent(null, null), String.format("ask-java/%s Java/UNKNOWN", SdkConstants.SDK_VERSION));
    }

}