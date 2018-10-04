package com.amazon.ask.builder;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.builder.impl.AbstractSkillConfiguration;
import com.amazon.ask.request.exception.mapper.GenericExceptionMapper;
import com.amazon.ask.request.handler.adapter.GenericHandlerAdapter;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.amazon.ask.request.mapper.GenericRequestMapper;

import java.util.List;

public class TestSkillConfiguration extends AbstractSkillConfiguration<TestHandlerInput, TestHandlerOutput> {

    protected TestSkillConfiguration(List<GenericRequestMapper<TestHandlerInput, TestHandlerOutput>> requestMappers,
                                     List<GenericHandlerAdapter<TestHandlerInput, TestHandlerOutput>> handlerAdapters,
                                     List<GenericRequestInterceptor<TestHandlerInput>> requestInterceptors,
                                     List<GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput>> responseInterceptors,
                                     GenericExceptionMapper<TestHandlerInput, TestHandlerOutput> exceptionMapper) {
        super(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors, exceptionMapper);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AbstractSkillConfiguration.Builder<TestHandlerInput, TestHandlerOutput, Builder> {
        private Builder() {
        }

        public TestSkillConfiguration build() {
            return new TestSkillConfiguration(requestMappers, handlerAdapters, requestInterceptors, responseInterceptors,
                    exceptionMapper);
        }
    }

}
