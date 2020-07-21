/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.util;

import com.amazon.ask.localdebug.config.ClientConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class to build client configuration instance {@link ClientConfiguration}
 * from user provided arguments.
 */
public final class ClientConfigurationBuilder {
    /**
     * Private constructor.
     */
    private ClientConfigurationBuilder() {

    }
    // Configuration names
    /**
     * Skill entry class argument name.
     */
    private static final String SKILL_ENTRY_CLASS_ARG_NAME = "skillStreamHandlerClass";
    /**
     * Skill id argument name.
     */
    private static final String SKILL_ID_ARG_NAME = "skillId";
    /**
     * LWA client id argument name.
     */
    private static final String ACCESS_TOKEN = "accessToken";

    /**
     * Builds {@link ClientConfiguration} instance.
     * @param arguments - User provided arguments.
     * @return {@link ClientConfiguration}.
     */
    public static ClientConfiguration buildClientConfiguration(final String[] arguments) {
        List<String> argumentList = Arrays.asList(arguments);
        return ClientConfiguration.builder()
                .withSkillId(ArgumentParserUtils.getArgumentValue(SKILL_ID_ARG_NAME, argumentList).toString())
                .withSkillInvokerClassName(ArgumentParserUtils
                        .getArgumentValue(SKILL_ENTRY_CLASS_ARG_NAME, argumentList).toString())
                .withAccessToken(ArgumentParserUtils.getArgumentValue(ACCESS_TOKEN, argumentList).toString())
                .build();
    }
}
