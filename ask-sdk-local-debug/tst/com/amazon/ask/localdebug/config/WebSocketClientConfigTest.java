/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.config;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClientConfigTest {
    @Test
    public void validateConfigBuilder() throws URISyntaxException {
        URI testURI = new URI("foo");
        Map<String, String> testHeaders = new HashMap<>();
        testHeaders.put("foo", "bar");
        WebSocketClientConfig webSocketClientConfig = WebSocketClientConfig.builder()
                .withWebSocketServerUri(testURI)
                .withHeaders(testHeaders)
                .build();

        Assert.assertEquals(testURI ,webSocketClientConfig.getWebSocketServerUri());
        Assert.assertEquals(testHeaders ,webSocketClientConfig.getHeaders());
    }
}
