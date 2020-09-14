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

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.response.template.TemplateFactory;

import java.util.Optional;

/**
 * Custom implementation of {@link SkillConfiguration}.
 */
public interface CustomSkillConfiguration extends GenericSkillConfiguration<HandlerInput, Optional<Response>> {

    /**
     * Getter method for Custom UserAgent.
     * @return user agent.
     */
    String getCustomUserAgent();

    /**
     * Getter method for SkillId.
     * @return skill id.
     */
    String getSkillId();

    /**
     * Getter method for Persistence Adapter.
     * @return {@link PersistenceAdapter}.
     */
    PersistenceAdapter getPersistenceAdapter();

    /**
     * Getter method for Api Client.
     * @return {@link ApiClient}.
     */
    ApiClient getApiClient();

    /**
     * Getter method for Template Factory.
     * @return {@link TemplateFactory}.
     */
    TemplateFactory<HandlerInput, Response> getTemplateFactory();

}
