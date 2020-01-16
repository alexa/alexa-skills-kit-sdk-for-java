/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.smapi.builder.refreshtoken;

import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiConfiguration;
import com.amazon.ask.model.services.AuthenticationConfiguration;
import com.amazon.ask.model.services.DefaultApiConfiguration;
import com.amazon.ask.model.services.DefaultAuthenticationConfiguration;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.model.services.skillManagement.SkillManagementService;
import com.amazon.ask.model.services.skillManagement.SkillManagementServiceClient;
import com.amazon.ask.model.services.util.JacksonSerializer;
import com.amazon.ask.services.ApacheHttpApiClient;

/**
 * Builder class used to build an instance of {@link SkillManagementService}.
 */
public final class SmapiClientBuilder {
    /**
     * Default api endpoint.
     */
    private static final String DEFAULT_API_ENDPOINT = "https://api.amazonalexa.com";

    /**
     * Public identifier for the client.
     */
    private String clientId;

    /**
     * Secret known only to the application and the authorization server.
     */
    private String clientSecret;

    /**
     * Allows an application to obtain a new access token.
     */
    private String refreshToken;

    /**
     * Custom api client.
     */
    private ApiClient apiClient;

    /**
     * Custom api endpoint.
     */
    private String apiEndpoint;

    /**
     * Custom serializer.
     */
    private Serializer serializer;

    /**
     * Prevent instantiation.
     */
    private SmapiClientBuilder() { }

    /**
     * Constructs an instance of SmapiClientBuilder.
     * @return {@link SmapiClientBuilder}.
     */
    public static SmapiClientBuilder create() {
        return new SmapiClientBuilder();
    }

    /**
     * Adds client id information to the builder.
     * @param clientId client id.
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Adds client secret information to the builder.
     * @param clientSecret client secret
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * Adds refresh token information to the builder.
     * @param refreshToken refresh token.
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    /**
     * Adds api client information to the builder.
     * @param apiClient custom api client.
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withApiClient(final ApiClient apiClient) {
        this.apiClient = apiClient;
        return this;
    }

    /**
     * Adds api endpoint information to the builder.
     * @param apiEndpoint custom api endpoint.
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withApiEndpoint(final String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

    /**
     * Adds serializer information to the builder.
     * @param serializer custom serializer.
     * @return {@link SmapiClientBuilder}.
     */
    public SmapiClientBuilder withSerializer(final Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    /**
     * Builder method to build an instance of SkillManagementService with the provided configuration.
     * @return instance of SkillManagementService.
     */
    public SkillManagementService build() {
        final ApiConfiguration apiConfiguration = DefaultApiConfiguration.builder()
                .withSerializer(serializer == null ? new JacksonSerializer() : serializer)
                .withApiClient(apiClient == null ? ApacheHttpApiClient.standard() : apiClient)
                .withApiEndpoint((apiEndpoint == null || apiEndpoint.isEmpty()) ? DEFAULT_API_ENDPOINT : apiEndpoint)
                .build();
        final AuthenticationConfiguration authenticationConfiguration = DefaultAuthenticationConfiguration.builder()
                .withClientSecret(clientSecret)
                .withClientId(clientId)
                .withRefreshToken(refreshToken)
                .build();
        return new SkillManagementServiceClient(apiConfiguration, authenticationConfiguration);
    }
}
