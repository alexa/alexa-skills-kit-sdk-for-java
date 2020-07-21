/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.certificate;

import com.amazon.ask.localdebug.client.WebSocketClientImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AllTrustCertificateProviderTest {

    @Mock
    WebSocketClientImpl mockWebSocketClient;

    @Test
    public void invoke_Test() throws InterruptedException {
        CertificateProvider allTrustCert = new AllTrustCertificateProvider();
        doCallRealMethod().when(mockWebSocketClient).invoke();
        when(mockWebSocketClient.getCertificateProvider()).thenReturn(allTrustCert);
        when(mockWebSocketClient.connectBlocking()).thenReturn(true);
        doCallRealMethod().when(mockWebSocketClient).setSocketFactory(any());

        mockWebSocketClient.invoke();
    }
}
