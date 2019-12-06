/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.smapi;

import com.amazon.ask.model.services.skillManagement.SkillManagementService;
import com.amazon.ask.smapi.builder.refreshtoken.SmapiClientBuilder;

/**
 * Factory methods for  {@link SkillManagementService} instances.
 */
public final class SmapiClients {

    /** Prevent instantiation */
    private SmapiClients() {}

    /**
     * Returns an instance of {@link SkillManagementService} with default values of {@link com.amazon.ask.model.services.Serializer},
     * {@link com.amazon.ask.model.services.ApiClient} and apiEndpoint.
     */
    public static SkillManagementService createDefault(String clientId, String clientSecret, String refreshToken) {
        return SmapiClientBuilder.create()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRefreshToken(refreshToken)
                .build();
    }

    /**
     * Returns an instance of {@link SmapiClientBuilder} used to build an instance of {@link SkillManagementService}.
     */
    public static SmapiClientBuilder custom() {
        return SmapiClientBuilder.create();
    }
}
