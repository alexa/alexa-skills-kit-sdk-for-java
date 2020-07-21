/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.utils;

import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.localdebug.util.ArgumentParserUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArgumentParserUtilsTest {
    private final String[] TEST_ARGUMENT = {"--skillStreamHandlerClass", "mockStreamHandlerValue", "--skillId", "mockSkillId", "--invalidTestArg"};

    @Test
    public void parseArgument_IsRequired_NoDefaultVal_ValueExists_ValueIsReturned() {
        Assert.assertEquals("mockStreamHandlerValue" ,ArgumentParserUtils.getArgumentValue("skillStreamHandlerClass", Arrays.asList(TEST_ARGUMENT)));
    }

    @Test(expected = LocalDebugSdkException.class)
    public void parseArgument_IsRequired_NoDefaultVal_ValueNotExists_ExceptionReturned() {
        ArgumentParserUtils.getArgumentValue("foobar", Arrays.asList(TEST_ARGUMENT));
    }

    @Test
    public void parseArgument_IsNotRequired_NoDefaultVal_ValueNotExists_NullValueIsReturned() {
        Assert.assertNull(ArgumentParserUtils.getArgumentValue("foobar", Arrays.asList(TEST_ARGUMENT), false));
    }

    @Test
    public void parseArgument_IsNotRequired_DefaultVal_ValueNotExists_DefaultValueIsReturned() {
        Assert.assertEquals("foo" ,ArgumentParserUtils.getArgumentValue("bar", Arrays.asList(TEST_ARGUMENT), false, "foo"));
    }

    @Test(expected = LocalDebugSdkException.class)
    public void parseArgument_IndexOutOfBounds_ExceptionReturned() {
        try{
            ArgumentParserUtils.getArgumentValue("invalidTestArg", Arrays.asList(TEST_ARGUMENT));
        } catch (LocalDebugSdkException ex) {
            Assert.assertEquals(ArrayIndexOutOfBoundsException.class, ex.getCause().getClass());
            Assert.assertTrue(ex.toString().contains("java.lang.ArrayIndexOutOfBoundsException"));
            throw ex;
        }
    }
}
