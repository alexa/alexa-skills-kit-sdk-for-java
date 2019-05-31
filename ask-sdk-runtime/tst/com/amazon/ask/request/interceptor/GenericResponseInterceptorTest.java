/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.interceptor;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericResponseInterceptorTest {

    @Test
    public void original_response_returned_by_default() {
        GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput> testInterceptor
                = new GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput>() {
            @Override
            public void process(TestHandlerInput testHandlerInput, TestHandlerOutput response) { }
        };
        TestHandlerInput testHandlerInput = new TestHandlerInput();
        TestHandlerOutput testHandlerOutput = new TestHandlerOutput();
        assertEquals(testInterceptor.processResponse(testHandlerInput, testHandlerOutput), testHandlerOutput);
    }

}
