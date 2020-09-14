/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.client;

import com.amazon.ask.localdebug.util.RequestResponseUtils;
import com.amazon.ask.model.dynamicEndpoints.FailureResponse;
import com.amazon.ask.model.dynamicEndpoints.SuccessResponse;
import com.amazon.ask.model.services.util.JacksonSerializer;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        RequestResponseUtils.class
})
@PowerMockIgnore("jdk.internal.reflect.*")
public class WebSocketClientImplTest {
    private final String validSampleRequestData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillRequestMessage\",\n" + "    \"requestId\": \"foorequestid\",\n" + "    \"requestPayload\": \"foorequestpayload\"\n" + "}";
    private final String validSampleSuccessResponseData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillResponseSuccessMessage\",\n" + "    \"originalRequestId\": \"foorequestid\",\n" + "    \"responsePayload\": \"TestPayload\"\n" + "}";
    private final String validSampleFailureResponseData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillResponseFailureMessage\",\n" + "    \"originalRequestId\": \"foorequestid\",\n" + "    \"errorCode\": \"500\",\n" + "    \"errorMessage\": \"mock error\"\n" +"}";

    @Mock
    WebSocketClientImpl mockWebSocketClient;

    @Captor
    ArgumentCaptor<String> responseCaptor;

    JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @After
    public void resetMock() {
        reset(mockWebSocketClient);
    }

    @Test
    public void skillResponse_successTest() {
        PowerMockito.mockStatic(RequestResponseUtils.class);
        when(RequestResponseUtils.getSkillResponse(any(), any())).thenReturn(validSampleSuccessResponseData);

        doCallRealMethod().when(mockWebSocketClient).onMessage(anyString());
        doCallRealMethod().when(mockWebSocketClient).sendSkillResponse(anyString());

        mockWebSocketClient.onMessage(validSampleRequestData);
        verify(mockWebSocketClient).sendSkillResponse(responseCaptor.capture());
        SuccessResponse response = jacksonSerializer.deserialize(responseCaptor.getValue(), SuccessResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getResponsePayload(), "TestPayload");
        Assert.assertEquals(response.getOriginalRequestId(), "foorequestid");
        Assert.assertEquals(response.getVersion(), "fooversion");
        Assert.assertEquals(response.getType(), "SkillResponseSuccessMessage");
    }

    @Test
    public void skillResponse_failureTest() {
        PowerMockito.mockStatic(RequestResponseUtils.class);
        when(RequestResponseUtils.getSkillResponse(any(), any())).thenReturn(validSampleFailureResponseData);

        doCallRealMethod().when(mockWebSocketClient).onMessage(anyString());
        doCallRealMethod().when(mockWebSocketClient).sendSkillResponse(anyString());

        mockWebSocketClient.onMessage(validSampleRequestData);
        verify(mockWebSocketClient).sendSkillResponse(responseCaptor.capture());
        FailureResponse response = jacksonSerializer.deserialize(responseCaptor.getValue(), FailureResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorCode(), String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        Assert.assertEquals(response.getOriginalRequestId(), "foorequestid");
        Assert.assertEquals(response.getVersion(), "fooversion");
        Assert.assertEquals(response.getType(), "SkillResponseFailureMessage");
        Assert.assertEquals(response.getErrorMessage(), "mock error");
    }
}
