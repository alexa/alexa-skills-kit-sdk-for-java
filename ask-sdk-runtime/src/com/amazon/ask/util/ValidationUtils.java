/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util;

import java.util.Collection;

/**
 * Useful utilities to validate dependencies.
 */
public final class ValidationUtils {

    /** Prevent instantiation. */
    private ValidationUtils() { }

    /**
     * Asserts that the given object is non-null and returns it.
     *
     * @param <T> type to evaluate
     * @param object
     *         Object to assert on
     * @param fieldName
     *         Field name to display in exception message if null
     * @return Object if non null
     * @throws IllegalArgumentException
     *         If object was null
     */
    public static <T> T assertNotNull(final T object, final String fieldName) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null", fieldName));
        }
        return object;
    }

    /**
     * Asserts that all of the objects are null.
     *
     * @param messageIfNull exception message to return if null
     * @param objects objects to evaluate
     * @throws IllegalArgumentException
     *         if any object provided was NOT null.
     */
    public static void assertAllAreNull(final String messageIfNull, final Object... objects) throws IllegalArgumentException {
        for (Object object : objects) {
            if (object != null) {
                throw new IllegalArgumentException(messageIfNull);
            }
        }
    }

    /**
     * Asserts that the given number is positive (non-negative and non-zero).
     *
     * @param num       Number to validate
     * @param fieldName Field name to display in exception message if not positive.
     * @return Number if positive.
     */
    public static int assertIsPositive(final int num, final String fieldName) {
        if (num <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive", fieldName));
        }
        return num;
    }

    /**
     * Asserts that the given collection is not empty (non-null and non-empty).
     * @param collection collection to validate.
     * @param fieldName field name to display in exception message if empty.
     * @param <T> type of elements in given collection.
     * @return collection if non-empty.
     * @throws IllegalArgumentException if the collection provided was empty.
     */
    public static <T extends Collection<?>> T assertNotEmpty(final T collection, final String fieldName) throws IllegalArgumentException {
        assertNotNull(collection, fieldName);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be empty", fieldName));
        }
        return collection;
    }

    /**
     * Asserts that the given array is not empty (non-null and non-empty).
     * @param array array to validate.
     * @param fieldName field name to display in exception message if empty.
     * @param <T> type of elements in given array.
     * @return array if non-empty.
     * @throws IllegalArgumentException if the array provided was empty.
     */
    public static <T> T[] assertNotEmpty(final T[] array, final String fieldName) throws IllegalArgumentException {
        assertNotNull(array, fieldName);
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format("%s cannot be empty", fieldName));
        }
        return array;
    }

    /**
     * Asserts that the given string is not empty (non-null and non-empty).
     * @param string string to validate.
     * @param fieldName field name to display in exception message if empty.
     * @return string if non-empty.
     * @throws IllegalArgumentException if the string provided was empty.
     */
    public static String assertStringNotEmpty(final String string, final String fieldName) throws IllegalArgumentException {
        assertNotNull(string, fieldName);
        if (string.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be empty", fieldName));
        }
        return string;
    }

}
