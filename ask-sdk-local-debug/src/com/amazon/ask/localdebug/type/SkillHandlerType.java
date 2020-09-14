/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for skill handler types.
 */
public enum SkillHandlerType {
    /**
     * Enum for Http Servlet handler type.
     */
    SKILL_SERVLET_TYPE("SkillServlet"),
    /**
     * Enum for lambda handler type.
     */
    SKILL_STREAM_HANDLER_TYPE("SkillStreamHandler");
    /**
     * Enum value.
     */
    private final String enumValue;

    /**
     * Constructor to set the enum value.
     * @param enumValue value of the enum.
     */
    SkillHandlerType(final String enumValue) {
        this.enumValue = enumValue;
    }

    /**
     * Gets the enum value.
     * @return enumValue returns value of the enum.
     */
    public String getEnumValue() {
        return this.enumValue;
    }

    /**
     * Key value map for all skill handler types.
     */
    private static final Map<String, SkillHandlerType> SKILL_HANDLER_TYPE_MAP = new HashMap<>();

    /**
     * Creates key value map by iterating over each skill handler type.
     */
    static {
        for (SkillHandlerType skillHandlerType: values()) {
            SKILL_HANDLER_TYPE_MAP.put(skillHandlerType.getEnumValue(), skillHandlerType);
        }
    }

    /**
     * Gets the handler type name from SKILL_HANDLER_TYPE_MAP collection.
     * @param typeName - typename, i.e., "SkillStreamHandler".
     * @return {@link SkillHandlerType}.
     */
    public static SkillHandlerType getHandlerType(final String typeName) {
        return SKILL_HANDLER_TYPE_MAP.get(typeName);
    }
}
