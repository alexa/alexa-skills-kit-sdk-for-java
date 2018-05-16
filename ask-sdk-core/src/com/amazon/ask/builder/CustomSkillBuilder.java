/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder;

import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.Skill;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;

/**
 * Builder used to construct a new {@link Skill} with a custom {@link ApiClient} and {@link PersistenceAdapter}.
 */
public class CustomSkillBuilder extends SkillBuilder<CustomSkillBuilder> {

    protected PersistenceAdapter persistenceAdapter;
    protected ApiClient apiClient;

    public CustomSkillBuilder withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        return this;
    }

    public CustomSkillBuilder withApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        return this;
    }

    protected SkillConfiguration.Builder getConfigBuilder() {
        return super.getConfigBuilder()
                .withPersistenceAdapter(persistenceAdapter)
                .withApiClient(apiClient);
    }

    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}