/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.config;

import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.localdebug.type.SkillHandlerType;
import com.amazon.ask.localdebug.util.ReflectionUtils;
import com.amazonaws.services.lambda.runtime.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handles reflection configurations for skill invocation.
 */
public class SkillInvokerConfiguration {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SkillInvokerConfiguration.class);
    /**
     * Request handler method name.
     */
    private static final String HANDLE_REQUEST = "handleRequest";
    /**
     * Skill entry class object.
     */
    private Class<?> skillInvokerClass;
    /**
     * Input param types to handle request method.
     */
    private Class<?>[] paramTypes;
    /**
     * Skill invocation method name. Instance of request handler
     */
    private Method skillInvokerMethod;
    /**
     * Type of skill invoker class - Lambda or Servlet.
     */
    private SkillHandlerType type;
    /**
     * New instance of skillInvokerClass.
     */
    private Object skillInvokerInstance;

    /**
     * SkillInvokerConfiguration constructor.
     * @param builder builder instance {@link SkillInvokerConfiguration.Builder}.
     */
    public SkillInvokerConfiguration(final Builder builder) {
        skillInvokerClass = builder.skillInvokerClass;
        setSkillInvokerAndParamTypes();
        skillInvokerInstance = ReflectionUtils.createClassInstance(skillInvokerClass);
        skillInvokerMethod = ReflectionUtils.getMethod(skillInvokerClass, HANDLE_REQUEST, paramTypes);
    }

    /**
     * Creates builder instance.
     * @return new Builder instance {@link SkillInvokerConfiguration.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets skill invoker request handler reflected method.
     * @return reflected method {@link Method}.
     */
    public Method getSkillInvokerMethod() {
        return skillInvokerMethod;
    }

    /**
     * Gets skill handler type.
     * @return {@link SkillHandlerType}.
     */
    public SkillHandlerType getType() {
        return type;
    }

    /**
     * Gets skill invoker class instance.
     * @return object of skill invoker class instance.
     */
    public Object getSkillInvokerInstance() {
        return skillInvokerInstance;
    }

    /**
     * Gets param types as an unmodifiableList.
     * @return Param type list.
     */
    public List<Class<?>> getParamTypes() {
        return Collections.unmodifiableList(Arrays.asList(paramTypes));
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * Reflected class of the skill entry class.
         */
        private Class<?> skillInvokerClass;

        /**
         * Initializes class reference of the skill stream handler.
         * @param skillInvokerClassName fully qualified class name.
         * @return Builder instance {@link SkillInvokerConfiguration.Builder}
         */
        public Builder withSkillInvokerClass(final String skillInvokerClassName) {
            this.skillInvokerClass = ReflectionUtils.getClassReference(skillInvokerClassName);
            return this;
        }

        /**
         * Builds instance of SkillInvokerConfiguration.
         * @return {@link SkillInvokerConfiguration}.
         */
        public SkillInvokerConfiguration build() {
            return new SkillInvokerConfiguration(this);
        }
    }

    /**
     * Sets skill invoker type {@link SkillHandlerType} and param types for request handler method.
     * The type is determined using reflection.
     * The super class name will be used to classify the type as either a lambda
     * or a servlet.
     */
    private void setSkillInvokerAndParamTypes() {
        SkillHandlerType extendedClassType = SkillHandlerType
                .getHandlerType(ReflectionUtils.getSuperClassName(skillInvokerClass));
        if (extendedClassType == null) {
            final String errorMessage = String.format("Extension type cannot be null");
            LOG.error(errorMessage);
            throw new LocalDebugSdkException(errorMessage);
        }
        switch (extendedClassType) {
            case SKILL_SERVLET_TYPE:
                type = SkillHandlerType.SKILL_SERVLET_TYPE;
                paramTypes = new Class[]{InputStream.class, OutputStream.class};
                break;
            case SKILL_STREAM_HANDLER_TYPE:
                type = SkillHandlerType.SKILL_STREAM_HANDLER_TYPE;
                paramTypes = new Class[]{InputStream.class, OutputStream.class, Context.class};
                break;
            default:
                final String errorMessage = String.format("Unknown extension type - %s", extendedClassType);
                LOG.error(errorMessage);
                throw new LocalDebugSdkException(errorMessage);
        }
    }
}
