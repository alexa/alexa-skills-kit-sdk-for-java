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
import com.amazon.ask.localdebug.config.Region;
import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.constants.Constants;
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
     * Returns user configured accessToken.
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
            final String region = clientConfiguration.getRegion();
            LOG.info("Region chosen: " + region);
            String endpointUrl;
            try {
                endpointUrl = Region.valueOf(region.toUpperCase()).getEndpoint();
            } catch (IllegalArgumentException ie) {
                final String errorMessage = String.format("Invalid region - %1$s."
                        + " Please ensure that the region value is one of "
                        + "NorthAmerica, Europe or FarEast", region);
                LOG.error(errorMessage);
                throw new LocalDebugSdkException(errorMessage);
            }
            return new URI(String.format(
                    Constants.CONNECT_CUSTOM_DEBUG_URI_SKELETON, endpointUrl,
                    clientConfiguration.getSkillId()));
        } catch (URISyntaxException e) {
            LOG.error("Encountered error when constructing Uri", e);
            throw new LocalDebugSdkException(e.getMessage(), e);
        }
    }

    /**
     * Creates headers for connectCustomDebugEndpoint call.
     * @param accessToken - LWA Access token.
     * @return {@link Map}.
     */
    @Provides
    @Singleton
    @Named("ConnectCustomDebugEndpointHeaders")
    public Map<String, String> connectCustomDebugEndpointHeaders(@Named("AccessToken")
                                                                     final String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION_HEADER_NAME, accessToken);
        headers.put(Constants.UPGRADE_HEADER_NAME, Constants.UPGRADE_HEADER_VALUE);
        headers.put(Constants.CONNECTION_HEADER_NAME, Constants.CONNECTION_HEADER_VALUE);
        return headers;
    }

    /**
     * Returns user provided thread pool size.
     * @return number of worker threads.
     */
    @Provides
    @Singleton
    @Named("ThreadPoolSize")
    public int threadPoolSize() {
        return clientConfiguration.getThreadPoolSize();
    }
}
