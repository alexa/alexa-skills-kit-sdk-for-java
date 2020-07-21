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
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures configuration dependencies for injection.
 */
public class ConfigModule extends AbstractModule {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigModule.class);
    /**
     * Web socket connection uri skeleton.
     */
    private static final String CONNECT_CUSTOM_DEBUG_URI_SKELETON =
            "wss://bob-dispatch-prod-na.amazon.com/v1/skills/%1$s/stages/development/connectCustomDebugEndpoint";

    // Header constants for web socket connection request
    /**
     * Upgrade header value.
     */
    private static final String UPGRADE_HEADER_VALUE = "websocket";
    /**
     * Upgrade header key.
     */
    private static final String UPGRADE_HEADER_NAME = "upgrade";
    /**
     * Connection header value.
     */
    private static final String CONNECTION_HEADER_VALUE = "upgrade";
    /**
     * Authorization header key.
     */
    private static final String AUTHORIZATION_HEADER_NAME = "authorization";
    /**
     * Connection header key.
     */
    private static final String CONNECTION_HEADER_NAME = "connection";

    /**
     * Client configuration.
     */
    private final ClientConfiguration clientConfiguration;

    /**
     * Public Constructor.
     * @param clientConfiguration - {@link ClientConfiguration}
     */
    public ConfigModule(final ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    /**
     * Abstract module implementation.
     */
    @Override
    protected void configure() {
    }

    /**
     * Creates authentication token from obtained client id, client secret and refresh token.
     * @return authentication token.
     */
    @Provides
    @Singleton
    @Named("AccessToken")
    public String accessToken() {
        return clientConfiguration.getAccessToken();
    }

    /**
     * Creates and returns an instance of {@link SkillInvokerConfiguration}.
     * @return {@link SkillInvokerConfiguration}.
     */
    @Provides
    @Singleton
    public SkillInvokerConfiguration skillInvokerConfiguration() {
        return SkillInvokerConfiguration.builder()
                .withSkillInvokerClass(clientConfiguration.getSkillInvokerClassName())
                .build();
    }

    /**
     * Creates debug connection URI.
     * @return {@link URI}.
     */
    @Provides
    @Singleton
    @Named("ConnectCustomDebugEndpointUri")
    public URI connectCustomDebugEndpointUri() {
        try {
            return new URI(String.format(
                    CONNECT_CUSTOM_DEBUG_URI_SKELETON, clientConfiguration.getSkillId()));
        } catch (URISyntaxException e) {
            LOG.error("Encountered error when constructing Uri", e);
            throw new LocalDebugSdkException(e.getMessage(), e);
        }
    }

    @Provides
    @Singleton
    @Named("ConnectCustomDebugEndpointHeaders")
    public Map<String, String> connectCustomDebugEndpointHeaders(@Named("AccessToken")
                                                                     final String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION_HEADER_NAME, accessToken);
        headers.put(UPGRADE_HEADER_NAME, UPGRADE_HEADER_VALUE);
        headers.put(CONNECTION_HEADER_NAME, CONNECTION_HEADER_VALUE);
        return headers;
    }
}
