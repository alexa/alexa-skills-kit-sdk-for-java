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

/**
 * Interface provides abstract methods to fetch properties common to various types of Skill Configuration.
 * @param <Input> input type.
 * @param <Output> output type.
 */
public interface GenericSkillConfiguration<Input, Output> {

    /**
     * Getter method for list of GenericRequestMapper.
     * @return list of {@link GenericRequestMapper}.
     */
    List<GenericRequestMapper<Input, Output>> getRequestMappers();

    /**
     * Getter method for list of GenericHandlerAdapter.
     * @return list of {@link GenericHandlerAdapter}.
     */
    List<GenericHandlerAdapter<Input, Output>> getHandlerAdapters();

    /**
     * Getter method for list of GenericRequestInterceptor.
     * @return list of {@link GenericRequestInterceptor}.
     */
    List<GenericRequestInterceptor<Input>> getRequestInterceptors();

    /**
     * Getter method for list of GenericResponseInterceptor.
     * @return list of {@link GenericResponseInterceptor}.
     */
    List<GenericResponseInterceptor<Input, Output>> getResponseInterceptors();

    /**
     * Getter method for GenericExceptionMapper.
     * @return {@link GenericExceptionMapper}.
     */
    GenericExceptionMapper<Input, Output> getExceptionMapper();

}
