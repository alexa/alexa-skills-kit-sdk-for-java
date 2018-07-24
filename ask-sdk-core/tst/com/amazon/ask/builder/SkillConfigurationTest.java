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

import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.exception.ExceptionMapper;
import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SkillConfigurationTest {

    private RequestMapper mockMapper;

    @Before
    public void setup() {
        this.mockMapper = mock(RequestMapper.class);
    }

    @Test
    public void get_request_mappers() {
        SkillConfiguration config = SkillConfiguration.builder().withRequestMappers(Collections.singletonList(mockMapper)).build();
        assertEquals(Collections.singletonList(mockMapper), config.getRequestMappers());
    }

    @Test
    public void get_handler_adapters() {
        List<HandlerAdapter> handlerAdapters = Collections.singletonList(mock(HandlerAdapter.class));
        SkillConfiguration config = SkillConfiguration.builder().withHandlerAdapters(handlerAdapters).build();
        assertEquals(handlerAdapters, config.getHandlerAdapters());
    }

    @Test
    public void get_request_interceptors() {
        List<RequestInterceptor> requestInterceptors = Collections.singletonList(mock(RequestInterceptor.class));
        SkillConfiguration config = SkillConfiguration.builder().withRequestInterceptors(requestInterceptors).build();
        assertEquals(requestInterceptors, config.getRequestInterceptors());
    }

    @Test
    public void get_response_interceptors() {
        List<ResponseInterceptor> responseInterceptors = Collections.singletonList(mock(ResponseInterceptor.class));
        SkillConfiguration config = SkillConfiguration.builder().withResponseInterceptors(responseInterceptors).build();
        assertEquals(responseInterceptors, config.getResponseInterceptors());
    }

    @Test
    public void get_exception_mapper() {
        ExceptionMapper exceptionMapper = mock(ExceptionMapper.class);
        SkillConfiguration config = SkillConfiguration.builder().withExceptionMapper(exceptionMapper).build();
        assertEquals(exceptionMapper, config.getExceptionMapper());
    }

    @Test
    public void get_persistence_manager() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        SkillConfiguration config = SkillConfiguration.builder().withPersistenceAdapter(persistenceAdapter).build();
        assertEquals(persistenceAdapter, config.getPersistenceAdapter());
    }

    @Test
    public void get_api_client() {
        ApiClient apiClient = mock(ApiClient.class);
        SkillConfiguration config = SkillConfiguration.builder().withApiClient(apiClient).build();
        assertEquals(apiClient, config.getApiClient());
    }

    @Test
    public void get_skillId() {
        String skillId = "fooSkillId";
        SkillConfiguration config = SkillConfiguration.builder().withSkillId(skillId).build();
        assertEquals(skillId, config.getSkillId());
    }

    @Test
    public void set_get_custom_user_agent() {
        String customUserAgent = "foo";
        SkillConfiguration config = SkillConfiguration.builder().withCustomUserAgent(customUserAgent).build();
        assertEquals(customUserAgent, config.getCustomUserAgent());
    }

    @Test
    public void append_single_user_agent() {
        String customUserAgent = "foo";
        SkillConfiguration config = SkillConfiguration.builder().appendCustomUserAgent(customUserAgent).build();
        assertEquals(customUserAgent, config.getCustomUserAgent());
    }

    @Test
    public void append_multiple_user_agents() {
        String customUserAgent1 = "foo";
        String customUserAgent2 = "bar";
        SkillConfiguration config = SkillConfiguration.builder().appendCustomUserAgent(customUserAgent1).appendCustomUserAgent(customUserAgent2).build();
        assertEquals("foo bar", config.getCustomUserAgent());
    }

}
