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

import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.config.WebSocketClientConfig;
import com.amazon.ask.localdebug.util.RequestResponseUtils;
import com.amazon.ask.model.dynamicEndpoints.FailureResponse;
import com.amazon.ask.model.dynamicEndpoints.SuccessResponse;
import com.amazon.ask.model.services.util.JacksonSerializer;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ForkJoinPool;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        RequestResponseUtils.class
})
public class WebSocketClientImplTest {
    private final String validSampleRequestData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillRequestMessage\",\n" + "    \"requestId\": \"foorequestid\",\n" + "    \"requestPayload\": \"foorequestpayload\"\n" + "}";
    private final String validSampleSuccessResponseData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillResponseSuccessMessage\",\n" + "    \"originalRequestId\": \"foorequestid\",\n" + "    \"responsePayload\": \"TestPayload\"\n" + "}";
    private final String validSampleFailureResponseData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillResponseFailureMessage\",\n" + "    \"originalRequestId\": \"foorequestid\",\n" + "    \"errorCode\": \"500\",\n" + "    \"errorMessage\": \"mock error\"\n" +"}";

    WebSocketClientImpl webSocketClient;

    @Captor
    ArgumentCaptor<String> responseCaptor;

    JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Before
    public void setup() throws URISyntaxException {
        WebSocketClientConfig mockWebSocketClientConfig = WebSocketClientConfig.builder()
                .withWebSocketServerUri(new URI("foo"))
                .build();
        webSocketClient = WebSocketClientImpl.builder()
                .withWebSocketClientConfig(mockWebSocketClientConfig)
                .withSkillInvokerConfiguration(mock(SkillInvokerConfiguration.class))
                .withExecutorService(ForkJoinPool.commonPool())
                .build();
    }

    @Test
    public void skillResponse_successTest() throws InterruptedException {
        WebSocketClientImpl spyWebSocketClient = spy(webSocketClient);
        PowerMockito.mockStatic(RequestResponseUtils.class);
        when(RequestResponseUtils.getSkillResponse(any(), any())).thenReturn(validSampleSuccessResponseData);

        spyWebSocketClient.onMessage(validSampleRequestData);
        Thread.sleep(1000);
        verify(spyWebSocketClient).sendSkillResponse(responseCaptor.capture());
        SuccessResponse response = jacksonSerializer.deserialize(responseCaptor.getValue(), SuccessResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getResponsePayload(), "TestPayload");
        Assert.assertEquals(response.getOriginalRequestId(), "foorequestid");
        Assert.assertEquals(response.getVersion(), "fooversion");
        Assert.assertEquals(response.getType(), "SkillResponseSuccessMessage");
    }

    @Test
    public void skillResponse_failureTest() throws InterruptedException {
        WebSocketClientImpl spyWebSocketClient = spy(webSocketClient);
        PowerMockito.mockStatic(RequestResponseUtils.class);
        when(RequestResponseUtils.getSkillResponse(any(), any())).thenReturn(validSampleFailureResponseData);

        spyWebSocketClient.onMessage(validSampleRequestData);
        Thread.sleep(1000);
        verify(spyWebSocketClient).sendSkillResponse(responseCaptor.capture());
        FailureResponse response = jacksonSerializer.deserialize(responseCaptor.getValue(), FailureResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getErrorCode(), String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        Assert.assertEquals(response.getOriginalRequestId(), "foorequestid");
        Assert.assertEquals(response.getVersion(), "fooversion");
        Assert.assertEquals(response.getType(), "SkillResponseFailureMessage");
        Assert.assertEquals(response.getErrorMessage(), "mock error");
    }
}
