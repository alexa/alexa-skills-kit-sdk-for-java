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

import com.amazon.ask.localdebug.client.WebSocketClientImpl;
import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.config.WebSocketClientConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.net.URI;
import java.util.Map;

/**
 * Configures client dependencies for injection.
 */
public class ClientModule extends AbstractModule {
    /**
     * Abstract module implementation.
     */
    @Override
    protected void configure() {
    }

    /**
     * Creates and returns an instance of {@link WebSocketClientImpl}.
     * @param connectCustomDebugEndpointUri - Uri for debug connection.
     * @param skillInvokerConfiguration - Reflection configuration for invoking skill code.
     * @param headers - Headers for the web socket connection.
     * @return instance of {@link WebSocketClientImpl}.
     */
    @Provides
    @Singleton
    public WebSocketClientImpl webSocketClient(@Named("ConnectCustomDebugEndpointUri")
                                                   final URI connectCustomDebugEndpointUri,
                                               final SkillInvokerConfiguration skillInvokerConfiguration,
                                               @Named("ConnectCustomDebugEndpointHeaders")
                                                   final Map<String, String> headers) {
        return new WebSocketClientImpl(WebSocketClientConfig.builder()
                .withWebSocketServerUri(connectCustomDebugEndpointUri)
                .withHeaders(headers)
                .build(), skillInvokerConfiguration);
    }
}
