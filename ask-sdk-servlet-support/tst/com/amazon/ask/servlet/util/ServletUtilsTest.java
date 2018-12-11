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

import com.amazon.ask.servlet.ServletConstants;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ServletUtilsTest {

    @After
    public void cleanUp() {
        System.clearProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY);
        System.clearProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY);
    }

    @Test
    public void null_timestamp_tolerance_system_property_returns_null() {
        assertNull(ServletUtils.getTimeStampToleranceSystemProperty());
    }

    @Test
    public void empty_timestamp_tolerance_system_property_returns_null() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "");
        assertNull(ServletUtils.getTimeStampToleranceSystemProperty());
    }

    @Test
    public void whitespace_only_timestamp_tolerance_system_property_returns_null() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "    ");
        assertNull(ServletUtils.getTimeStampToleranceSystemProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_numeric_value_throws_exception_timestamp_tolerance_system_property() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "bar");
        assertNull(ServletUtils.getTimeStampToleranceSystemProperty());
    }

    @Test
    public void numeric_value_parsed_timestamp_tolerance_system_property() {
        System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, "1234");
        assertEquals(ServletUtils.getTimeStampToleranceSystemProperty(), new Long(1234));
    }

    @Test
    public void null_request_signature_check_system_property_returns_false() {
        assertFalse(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }

    @Test
    public void empty_request_signature_check_system_property_returns_false() {
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "");
        assertFalse(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }

    @Test
    public void whitespace_only_request_signature_check_system_property_returns_false() {
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "    ");
        assertFalse(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }

    @Test
    public void string_value_request_signature_check_system_property_returns_false() {
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "bar");
        assertFalse(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }

    @Test
    public void false_value_request_signature_check_system_property_returns_false() {
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "false");
        assertFalse(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }

    @Test
    public void true_value_request_signature_check_system_property_returns_true() {
        System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "true");
        assertTrue(ServletUtils.isRequestSignatureCheckSystemPropertyDisabled());
    }
}
