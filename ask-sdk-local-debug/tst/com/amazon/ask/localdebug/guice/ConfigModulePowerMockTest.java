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
import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.localdebug.type.SkillHandlerType;
import com.amazon.ask.localdebug.util.ReflectionUtils;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ReflectionUtils.class
})
public class ConfigModulePowerMockTest {
    private ClientConfiguration mockClientConfiguration;

    @Before
    public void setMock() {
        mockClientConfiguration = ClientConfiguration.builder()
                .withSkillId("fooSkillId")
                .withSkillInvokerClassName("Foo")
                .withAccessToken("foo")
                .build();
    }

    @Test
    public void buildSkillInvokerConfiguration_SkillStreamHandlerType_Test() throws NoSuchMethodException {
        PowerMockito.mockStatic(ReflectionUtils.class);
        when(ReflectionUtils.getSuperClassName(any())).thenReturn("SkillStreamHandler");
        Class<?> userClass = Foo.class;
        Mockito.<Class<?>>when(ReflectionUtils.getClassReference(any())).thenReturn(userClass);
        when(ReflectionUtils.createClassInstance(any())).thenReturn(Foo.class);
        when(ReflectionUtils.getMethod(any(), any(), any())).thenReturn(Foo.class.getMethod("bar"));

        ConfigModule configModule = new ConfigModule(mockClientConfiguration);

        final SkillInvokerConfiguration skillInvokerConfiguration = configModule.skillInvokerConfiguration();

        Assert.assertEquals(SkillHandlerType.SKILL_STREAM_HANDLER_TYPE, skillInvokerConfiguration.getType());
        Assert.assertEquals(Arrays.asList(InputStream.class, OutputStream.class, Context.class), skillInvokerConfiguration.getParamTypes());
        Assert.assertEquals(Foo.class.getMethod("bar"), skillInvokerConfiguration.getSkillInvokerMethod());
        Assert.assertEquals(Foo.class, skillInvokerConfiguration.getSkillInvokerInstance());
    }

    @Test
    public void buildSkillInvokerConfiguration_SkillServletType_Test() throws NoSuchMethodException {
        PowerMockito.mockStatic(ReflectionUtils.class);
        when(ReflectionUtils.getSuperClassName(any())).thenReturn("SkillServlet");
        Class<?> userClass = Foo.class;
        Mockito.<Class<?>>when(ReflectionUtils.getClassReference(any())).thenReturn(userClass);
        when(ReflectionUtils.createClassInstance(any())).thenReturn(Foo.class);
        when(ReflectionUtils.getMethod(any(), any(), any())).thenReturn(Foo.class.getMethod("bar"));

        ConfigModule configModule = new ConfigModule(mockClientConfiguration);

        final SkillInvokerConfiguration skillInvokerConfiguration = configModule.skillInvokerConfiguration();

        Assert.assertEquals(SkillHandlerType.SKILL_SERVLET_TYPE, skillInvokerConfiguration.getType());
        Assert.assertEquals(Arrays.asList(InputStream.class, OutputStream.class), skillInvokerConfiguration.getParamTypes());
        Assert.assertEquals(Foo.class.getMethod("bar"), skillInvokerConfiguration.getSkillInvokerMethod());
        Assert.assertEquals(Foo.class, skillInvokerConfiguration.getSkillInvokerInstance());
    }

    @Test(expected = LocalDebugSdkException.class)
    public void buildSkillInvokerConfiguration_UnknownType_Test() throws NoSuchMethodException {
        PowerMockito.mockStatic(ReflectionUtils.class);
        when(ReflectionUtils.getSuperClassName(any())).thenReturn("UnknownType");
        Class<?> userClass = Foo.class;
        Mockito.<Class<?>>when(ReflectionUtils.getClassReference(any())).thenReturn(userClass);
        when(ReflectionUtils.createClassInstance(any())).thenReturn(Foo.class);
        when(ReflectionUtils.getMethod(any(), any(), any())).thenReturn(Foo.class.getMethod("bar"));

        ConfigModule configModule = new ConfigModule(mockClientConfiguration);
        try{
            configModule.skillInvokerConfiguration();
        } catch (LocalDebugSdkException ex) {
            Assert.assertEquals("com.amazon.ask.localdebug.exception.LocalDebugSdkException: Extension type cannot be null" , ex.toString());
            throw ex;
        }

    }

    private class Foo {
        public void bar() {

        }
    }
}
