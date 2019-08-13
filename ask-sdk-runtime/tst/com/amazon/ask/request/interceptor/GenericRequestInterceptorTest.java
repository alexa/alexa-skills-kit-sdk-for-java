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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericRequestInterceptorTest {

    @Test
    public void original_request_returned_by_default() {
        GenericRequestInterceptor<TestHandlerInput> testInterceptor
                = new GenericRequestInterceptor<TestHandlerInput>() {
            @Override
            public void process(TestHandlerInput testHandlerInput) {
            }
        };
        TestHandlerInput testHandlerInput = new TestHandlerInput();
        assertEquals(testInterceptor.processRequest(testHandlerInput), testHandlerInput);
    }

}
