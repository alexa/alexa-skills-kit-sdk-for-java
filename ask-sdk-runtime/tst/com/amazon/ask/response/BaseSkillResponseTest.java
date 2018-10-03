/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.response.impl.BaseSkillResponse;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.util.JsonMarshaller;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseSkillResponseTest {

    private TestHandlerOutput response;
    private JsonMarshaller<TestHandlerOutput> marshaller;

    @Before
    public void setup() {
        response = new TestHandlerOutput();
        marshaller = mock(JsonMarshaller.class);
    }

    @Test
    public void is_present_returns_true_if_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, response);
        assertTrue(skillResponse.isPresent());
    }

    @Test
    public void is_present_returns_false_if_not_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, null);
        assertFalse(skillResponse.isPresent());
    }

    @Test
    public void get_response_returns_response_if_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, response);
        assertEquals(skillResponse.getResponse(), response);
    }

    @Test
    public void get_response_returns_null_if_not_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, null);
        assertEquals(skillResponse.getResponse(), null);
    }

    @Test
    public void get_raw_response_calls_unmarshaller_if_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, response);
        byte[] byteResp = new byte[]{};
        when(marshaller.marshall(response)).thenReturn(byteResp);
        assertEquals(skillResponse.getRawResponse(), byteResp);
        verify(marshaller).marshall(response);
    }

    @Test(expected = AskSdkException.class)
    public void get_raw_response_throws_exception_if_not_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, null);
        skillResponse.getRawResponse();
    }

    @Test
    public void write_to_calls_unmarshaller_if_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, response);
        OutputStream outputStream = mock(OutputStream.class);
        skillResponse.writeTo(outputStream);
        verify(marshaller).marshall(response, outputStream);
    }

    @Test(expected = AskSdkException.class)
    public void write_to_throws_exception_if_not_present() {
        SkillResponse<TestHandlerOutput> skillResponse = new BaseSkillResponse<>(marshaller, null);
        OutputStream outputStream = mock(OutputStream.class);
        skillResponse.writeTo(outputStream);
    }

}
