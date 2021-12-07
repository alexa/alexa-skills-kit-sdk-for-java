/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.utils;

import com.amazon.ask.localdebug.config.ClientConfiguration;
import com.amazon.ask.localdebug.util.ArgumentParserUtils;
import com.amazon.ask.localdebug.util.ClientConfigurationBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ArgumentParserUtils.class
})
public class ClientConfigurationBuilderTest {
    @Test
    public void buildClientConfiguration_validConfigurations_Test() {
        PowerMockito.mockStatic(ArgumentParserUtils.class);
        when(ArgumentParserUtils.getArgumentValue(eq("skillStreamHandlerClass"), any())).thenReturn("fooSkillStreamHandlerClass");
        when(ArgumentParserUtils.getArgumentValue(eq("skillId"), any())).thenReturn("fooSkillId");
        when(ArgumentParserUtils.getArgumentValue(eq("accessToken"), any())).thenReturn("fooAccessToken");
        when(ArgumentParserUtils.getArgumentValue(eq("threadPoolSize"), any(), eq(false), eq(0))).thenReturn(0);
        when(ArgumentParserUtils.getArgumentValue(eq("region"), any(), eq(false), eq("NA"))).thenReturn("NA");

        final ClientConfiguration clientConfiguration = ClientConfigurationBuilder.buildClientConfiguration(new String[]{"foo"});

        Assert.assertEquals("fooSkillId", clientConfiguration.getSkillId());
        Assert.assertEquals("fooSkillStreamHandlerClass", clientConfiguration.getSkillInvokerClassName());
        Assert.assertEquals("fooAccessToken", clientConfiguration.getAccessToken());
        Assert.assertEquals(0, clientConfiguration.getThreadPoolSize());
        Assert.assertEquals("NA", clientConfiguration.getRegion());
    }
}
