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
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.builder.impl.AbstractSkillBuilder;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.module.SdkModule;
import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;
import com.amazon.ask.response.template.TemplateFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Exposes a builder interface to add configuration to a Skill.
 * @param <T> of type SkillBuilder.
 */
public class SkillBuilder<T extends SkillBuilder<T>> extends AbstractSkillBuilder<HandlerInput, Optional<Response>, T> {

    /**
     * An interface for SDK extensions that can be registered on the {@link SkillBuilder} when setting up a Skill instance.
     */
    protected final List<SdkModule> sdkModules;

    /**
     *  Persistence adapters allow an {@link com.amazon.ask.attributes.AttributesManager} to store skill attributes to a
     *  persistence layer.
     */
    protected PersistenceAdapter persistenceAdapter;

    /**
     * Api client to make external API calls.
     */
    protected ApiClient apiClient;

    /**
     * Unique ID associated with a Skill.
     */
    protected String skillId;

    /**
     * Template Factory interface to process template and data to generate skill response.
     */
    protected TemplateFactory<HandlerInput, Response> templateFactory;

    /**
     * Constructor for SkillBuilder.
     */
    public SkillBuilder() {
        this.sdkModules = new ArrayList<>();
    }

    /**
     * Adds PersistenceAdapter to a Skill.
     * @param persistenceAdapter store attributes to a persistence layer.
     * @return {@link T}.
     */
    public T withPersistenceAdapter(final PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        return getThis();
    }

    /**
     * Adds ApiClient to a Skill.
     * @param apiClient Api client to make external API calls.
     * @return {@link T}.
     */
    public T withApiClient(final ApiClient apiClient) {
        this.apiClient = apiClient;
        return getThis();
    }

    /**
     * Adds SDK modules to a Skill.
     * @param sdkModule interface for SDK extensions.
     * @return {@link T}.
     */
    public T registerSdkModule(final SdkModule sdkModule) {
        sdkModules.add(sdkModule);
        return getThis();
    }

    /**
     * Adds SkillId.
     * @param skillId Unique ID associated with a Skill.
     * @return {@link T}.
     */
    public T withSkillId(final String skillId) {
        this.skillId = skillId;
        return getThis();
    }

    /**
     * Adds Template Factory to a Skill.
     * @param templateFactory Interface to process template and data to generate skill response.
     * @return {@link T}.
     */
    public T withTemplateFactory(final TemplateFactory templateFactory) {
        this.templateFactory = templateFactory;
        return getThis();
    }

    /**
     * Typecast class to type T.
     * @return {@link T}.
     */
    @SuppressWarnings("unchecked")
    private T getThis() {
        return (T) this;
    }

    /**
     * Get configuration builder.
     * @return {@link com.amazon.ask.builder.SkillConfiguration.Builder}.
     */
    protected SkillConfiguration.Builder getConfigBuilder() {
        SkillConfiguration.Builder skillConfigBuilder = SkillConfiguration.builder();

        super.populateConfig(skillConfigBuilder);

        if (!requestHandlers.isEmpty()) {
            skillConfigBuilder.addHandlerAdapter(new BaseHandlerAdapter<>(RequestHandler.class));
        }

        skillConfigBuilder.withPersistenceAdapter(persistenceAdapter)
                .withApiClient(apiClient)
                .withSkillId(skillId);

        if (templateFactory != null) {
            skillConfigBuilder.withTemplateFactory(templateFactory);
        }


        SdkModuleContext sdkModuleContext = new SdkModuleContext(skillConfigBuilder);
        for (SdkModule sdkModule : sdkModules) {
            sdkModule.setupModule(sdkModuleContext);
        }

        return skillConfigBuilder;
    }

    /**
     * Build an instance of Skill with the given configuration.
     * @return {@link Skill}.
     */
    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}
