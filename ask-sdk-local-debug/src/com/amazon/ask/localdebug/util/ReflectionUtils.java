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

import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazonaws.services.lambda.runtime.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Reflection utilities.
 */
public final class ReflectionUtils {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * Private constructor.
     */
    private ReflectionUtils() {
    }

    /**
     * Returns Class reference for a full qualified class name. Used to construct class reference for a skill's
     * stream handler class.
     * @param fullyQualifiedClassName - fully qualified class name. Current use cases
     *                                1. Class name will be the name of SkillStreamHandlerClass.
     * @return Class reference
     */
    public static Class<?> getClassReference(final String fullyQualifiedClassName) {
        try {
            return Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException e) {
            LOG.error(String.format("Class not found exception for class - %s", fullyQualifiedClassName), e.toString());
            throw new LocalDebugSdkException(e.getMessage(), e);
        }
    }

    /**
     * Gets the name of the super class for the class object.
     * The super class name will be used to classify the type as either a lambda
     * or a servlet.
     * @param classObject Object for which the extended class needs to be determined.
     * @return Name of the super class.
     */
    public static String getSuperClassName(final Class<?> classObject) {
        return classObject.getSuperclass().getSimpleName();
    }

    /**
     * Creates instance from classObject.
     * @param classObject - class object for which an instance will be created.
     * @return instance of the class.
     */
    public static Object createClassInstance(final Class<?> classObject) {
        try {
            return classObject.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            LOG.error(ex.getMessage());
            throw new LocalDebugSdkException(ex.getMessage(), ex);
        }
    }

    /**
     * Creates {@link Method} reference.
     * @param classObject - Class object in which the method resides.
     * @param methodName - Name of the method.
     * @param paramTypes - Method's paramter types.
     * @return {@link Method}
     */
    public static Method getMethod(final Class<?> classObject, final String methodName,
                                   final Class<?>[] paramTypes) {
        try {
            return classObject.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            LOG.error(ex.getMessage());
            throw new LocalDebugSdkException(ex.getMessage(), ex);
        }

    }

    /**
     * To be used for invoking skill code of type SkillServlet.
     * @param method - handleRequest method.
     * @param instance - instance of skill entry class.
     * @param inputStream - input stream of the request envelope.
     * @param outputStream - output stream of the response envelope.
     */
    public static void callMethodAndGetResult(final Method method, final Object instance,
                                              final InputStream inputStream, final OutputStream outputStream) {
        try {
            method.invoke(instance, inputStream, outputStream);
        } catch (ReflectiveOperationException ex) {
            LOG.error("Encountered runtime exception when invoking skill code", ex);
            throw new LocalDebugSdkException(ex.getMessage(), ex);
        }
    }

    /**
     * To be used to invoke handleRequest method for skill code extending SkillStreamHandler.
     * @param method - handleRequest method.
     * @param instance - instance of skill entry class.
     * @param inputStream - input stream of the request envelope.
     * @param outputStream - output stream of the response envelope.
     * @param context - lambda context class.
     */
    public static void callMethodAndGetResult(final Method method, final Object instance,
                                                  final InputStream inputStream, final OutputStream outputStream,
                                                  final Context context) {
        try {
            method.invoke(instance, inputStream, outputStream, context);
        } catch (ReflectiveOperationException ex) {
            LOG.error("Encountered runtime exception when invoking skill code", ex);
            throw new LocalDebugSdkException(ex.getMessage(), ex);
        }
    }
}
