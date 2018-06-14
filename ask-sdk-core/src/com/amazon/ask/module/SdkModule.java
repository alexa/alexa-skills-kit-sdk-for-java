/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.module;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.builder.SkillConfiguration;

/**
 * An interface for SDK extensions that can be registered on the {@link SkillBuilder} when setting up
 * a {@link Skill} instance.
 */
public interface SdkModule {

    /**
     * Method called by the {@link SkillBuilder} when .build() is called. Allows this module to configure
     * itself as part of {@link SkillConfiguration} building.
     */
    void setupModule(SdkModuleContext context);

}