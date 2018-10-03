/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.exception.mapper.impl.BaseExceptionMapper;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SkillConfigurationTest {
    private GenericRequestMapper mockMapper;

    @Before
    public void setup() {
        mockMapper = mock(GenericRequestMapper.class);
    }

    @Test
    public void get_request_mapper() {
        GenericSkillConfiguration config = TestSkillConfiguration.builder().withRequestMappers(Collections.singletonList(mockMapper)).build();
        assertEquals(Collections.singletonList(mockMapper), config.getRequestMappers());
    }

    @Test
    public void get_request_interceptors() {
        List<GenericRequestInterceptor<TestHandlerInput>> requestInterceptors = Collections.singletonList(mock(GenericRequestInterceptor.class));
        GenericSkillConfiguration config = TestSkillConfiguration.builder().withRequestInterceptors(requestInterceptors).build();
        assertEquals(requestInterceptors, config.getRequestInterceptors());
    }

    @Test
    public void get_response_interceptors() {
        List<GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput>> responseInterceptors = Collections.singletonList(mock(GenericResponseInterceptor.class));
        GenericSkillConfiguration config = TestSkillConfiguration.builder().withResponseInterceptors(responseInterceptors).build();
        assertEquals(responseInterceptors, config.getResponseInterceptors());
    }

    @Test
    public void get_exception_mapper() {
        BaseExceptionMapper exceptionMapper = mock(BaseExceptionMapper.class);
        GenericSkillConfiguration config = TestSkillConfiguration.builder().withExceptionMapper(exceptionMapper).build();
        assertEquals(exceptionMapper, config.getExceptionMapper());
    }

}