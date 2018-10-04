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

import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;

import java.util.List;

public interface GenericSkillConfiguration<Input, Output> {

    List<GenericRequestMapper<Input, Output>> getRequestMappers();

    List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters();

    List<GenericRequestInterceptor<Input>> getRequestInterceptors();

    List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors();

    GenericExceptionMapper<Input, Output> getExceptionMapper();

}
