/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.guice;

import com.amazon.ask.localdebug.config.ClientConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ConfigModuleTest {
    static final String TEST_URI = "wss://bob-dispatch-prod-na.amazon.com/v1/skills/fooSkillId/stages/development/connectCustomDebugEndpoint";
    private ClientConfiguration clientConfiguration;

    @Before
    public void setMock() {
        clientConfiguration = ClientConfiguration.builder()
                .withSkillId("fooSkillId")
                .withSkillInvokerClassName("Foo")
                .withAccessToken("foo")
                .build();
    }

    @Test
    public void webSocketUri_ValidURITest() throws URISyntaxException {
        ConfigModule configModule = new ConfigModule(clientConfiguration);
        Assert.assertEquals(new URI(TEST_URI), configModule.connectCustomDebugEndpointUri());
    }

    @Test
    public void webSocketHeaders_SetTest() {
        ConfigModule configModule = new ConfigModule(clientConfiguration);
        final Map<String, String> actualHeaders = configModule.connectCustomDebugEndpointHeaders("fooAuthToken");
        Assert.assertEquals("fooAuthToken", actualHeaders.get("authorization"));
        Assert.assertEquals("websocket", actualHeaders.get("upgrade"));
        Assert.assertEquals("upgrade", actualHeaders.get("connection"));
        Assert.assertEquals(3, actualHeaders.size());
    }
}
