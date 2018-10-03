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

import com.amazon.ask.Skill;
import com.amazon.ask.builder.impl.AbstractSkillBuilder;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.module.SdkModule;
import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillBuilder<T extends SkillBuilder<T>> extends AbstractSkillBuilder<HandlerInput, Optional<Response>, T> {

    protected final List<SdkModule> sdkModules;
    protected PersistenceAdapter persistenceAdapter;
    protected ApiClient apiClient;
    protected String skillId;

    public SkillBuilder() {
        this.sdkModules = new ArrayList<>();
    }

    public T withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        return getThis();
    }

    public T withApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        return getThis();
    }

    public T registerSdkModule(SdkModule sdkModule) {
        sdkModules.add(sdkModule);
        return getThis();
    }

    public T withSkillId(String skillId) {
        this.skillId = skillId;
        return getThis();
    }

    @SuppressWarnings("unchecked")
    private T getThis() {
        return (T) this;
    }

    protected SkillConfiguration.Builder getConfigBuilder() {
        SkillConfiguration.Builder skillConfigBuilder = SkillConfiguration.builder();

        super.populateConfig(skillConfigBuilder);

        if (!requestHandlers.isEmpty()) {
            skillConfigBuilder.addHandlerAdapter(new BaseHandlerAdapter<>(RequestHandler.class));
        }

        skillConfigBuilder.withPersistenceAdapter(persistenceAdapter)
                .withApiClient(apiClient)
                .withSkillId(skillId);

        SdkModuleContext sdkModuleContext = new SdkModuleContext(skillConfigBuilder);
        for (SdkModule sdkModule : sdkModules) {
            sdkModule.setupModule(sdkModuleContext);
        }

        return skillConfigBuilder;
    }

    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}
