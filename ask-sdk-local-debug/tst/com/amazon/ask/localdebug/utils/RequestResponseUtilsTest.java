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

import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.localdebug.type.SkillHandlerType;
import com.amazon.ask.localdebug.util.ReflectionUtils;
import com.amazon.ask.localdebug.util.RequestResponseUtils;
import com.amazon.ask.model.dynamicEndpoints.FailureResponse;
import com.amazon.ask.model.dynamicEndpoints.Request;
import com.amazon.ask.model.dynamicEndpoints.SuccessResponse;
import com.amazon.ask.model.services.util.JacksonSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ReflectionUtils.class
})
@PowerMockIgnore("jdk.internal.reflect.*")
public class RequestResponseUtilsTest {
    Request mockLocalDebugRequest = Request.builder()
            .withRequestId("fooRequestId")
            .withRequestPayload("TestPayload")
            .withVersion("fooVersionId")
            .build();


    @Test
    public void localDebugSuccessResponseBuilder_Test() {
        final SuccessResponse testSuccessResponse = RequestResponseUtils.getLocalDebugSuccessResponse(mockLocalDebugRequest, "TestPayload");
        Assert.assertEquals("TestPayload", testSuccessResponse.getResponsePayload());
        Assert.assertEquals("fooVersionId", testSuccessResponse.getVersion());
        Assert.assertEquals("fooRequestId", testSuccessResponse.getOriginalRequestId());
        Assert.assertEquals("SkillResponseSuccessMessage", testSuccessResponse.getType());
    }

    @Test
    public void localDebugFailureResponseBuilder_Test() {
        final FailureResponse testSuccessResponse = RequestResponseUtils.getLocalDebugFailureResponse(mockLocalDebugRequest, new Exception("Test Exception"));
        Assert.assertEquals("java.lang.Exception: Test Exception", testSuccessResponse.getErrorMessage());
        Assert.assertEquals("fooVersionId", testSuccessResponse.getVersion());
        Assert.assertEquals("fooRequestId", testSuccessResponse.getOriginalRequestId());
        Assert.assertEquals("SkillResponseFailureMessage", testSuccessResponse.getType());
    }

    @Test
    public void getSkillResponse_StreamHandlerType_SuccessResponse() {
        PowerMockito.mockStatic(ReflectionUtils.class);
        final SuccessResponse sampleResponse = SuccessResponse.builder()
                .withVersion("fooVersionId")
                .withResponsePayload("")
                .withOriginalRequestId("fooRequestId")
                .build();
        SkillInvokerConfiguration mockSkillInvokerConfiguration = mock(SkillInvokerConfiguration.class);
        when(mockSkillInvokerConfiguration.getType()).thenReturn(SkillHandlerType.SKILL_STREAM_HANDLER_TYPE);
        Assert.assertEquals(new JacksonSerializer().serialize(sampleResponse),
                RequestResponseUtils.getSkillResponse(mockLocalDebugRequest, mockSkillInvokerConfiguration));
    }

    @Test
    public void getSkillResponse_ServletHandlerType_SuccessResponse() {
        PowerMockito.mockStatic(ReflectionUtils.class);
        final SuccessResponse sampleResponse = SuccessResponse.builder()
                .withVersion("fooVersionId")
                .withResponsePayload("")
                .withOriginalRequestId("fooRequestId")
                .build();
        SkillInvokerConfiguration mockSkillInvokerConfiguration = mock(SkillInvokerConfiguration.class);
        when(mockSkillInvokerConfiguration.getType()).thenReturn(SkillHandlerType.SKILL_SERVLET_TYPE);
        Assert.assertEquals(new JacksonSerializer().serialize(sampleResponse),
                RequestResponseUtils.getSkillResponse(mockLocalDebugRequest, mockSkillInvokerConfiguration));
    }

    @Test
    public void getSkillResponse_ServletHandlerType_FailureResponse() throws Exception {
        PowerMockito.mockStatic(ReflectionUtils.class);
        PowerMockito.doThrow(new LocalDebugSdkException("Test Exception")).when(ReflectionUtils.class, "callMethodAndGetResult", any(), any(), any(), any());
        final FailureResponse sampleResponse = FailureResponse.builder()
                .withVersion("fooVersionId")
                .withErrorMessage("com.amazon.ask.localdebug.exception.LocalDebugSdkException: Test Exception")
                .withOriginalRequestId("fooRequestId")
                .withErrorCode("500")
                .build();
        SkillInvokerConfiguration mockSkillInvokerConfiguration = mock(SkillInvokerConfiguration.class);
        when(mockSkillInvokerConfiguration.getType()).thenReturn(SkillHandlerType.SKILL_SERVLET_TYPE);
        Assert.assertEquals(new JacksonSerializer().serialize(sampleResponse),
                RequestResponseUtils.getSkillResponse(mockLocalDebugRequest, mockSkillInvokerConfiguration));
    }
}

