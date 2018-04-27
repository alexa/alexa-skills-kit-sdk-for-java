/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.util;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ServletUtilsTest {

    @After
    public void cleanUp() {
        System.clearProperty("foo");
    }

    @Test
    public void null_system_property_returns_null() {
        assertNull(ServletUtils.getSystemPropertyAsLong("foo"));
    }

    @Test
    public void empty_system_property_returns_null() {
        System.setProperty("foo", "");
        assertNull(ServletUtils.getSystemPropertyAsLong("foo"));
    }

    @Test
    public void whitespace_only_system_property_returns_null() {
        System.setProperty("foo", "    ");
        assertNull(ServletUtils.getSystemPropertyAsLong("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_numeric_value_throws_exception() {
        System.setProperty("foo", "bar");
        assertNull(ServletUtils.getSystemPropertyAsLong("foo"));
    }

    @Test
    public void numeric_value_parsed() {
        System.setProperty("foo", "1234");
        assertEquals(ServletUtils.getSystemPropertyAsLong("foo"), new Long(1234));
    }

}
