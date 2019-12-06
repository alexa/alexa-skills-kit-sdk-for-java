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

public class SmapiClientBuilder {
    private static final String DEFAULT_API_ENDPOINT = "https://api.amazonalexa.com";

    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private ApiClient apiClient;
    private String apiEndpoint;
    private Serializer serializer;

    private SmapiClientBuilder() {}

    public static SmapiClientBuilder create() { return new SmapiClientBuilder(); }

    public SmapiClientBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public SmapiClientBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public SmapiClientBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public SmapiClientBuilder withApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        return this;
    }

    public SmapiClientBuilder withApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

    public SmapiClientBuilder withSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

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
