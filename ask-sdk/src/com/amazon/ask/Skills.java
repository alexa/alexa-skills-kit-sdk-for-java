/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazon.ask.builder.CustomSkillBuilder;
import com.amazon.ask.builder.StandardSkillBuilder;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.services.ApacheHttpApiClient;

/**
 * Factory methods for different {@link Skill} builders.
 */
public final class Skills {

    /** Prevent instantiation */
    private Skills() {}

    /**
     * Creates a builder used to construct a new {@link Skill} using the default {@link DynamoDbPersistenceAdapter} and {@link ApacheHttpApiClient}.
     *
     * @return standard skill builder
     */
    public static StandardSkillBuilder standard() {
        return new StandardSkillBuilder();
    }

    /**
     * Creates a builder used to construct a new {@link Skill} with a custom {@link ApiClient} and {@link PersistenceAdapter}.
     *
     * @return custom skill builder
     */
    public static CustomSkillBuilder custom() {
        return new CustomSkillBuilder();
    }

}
