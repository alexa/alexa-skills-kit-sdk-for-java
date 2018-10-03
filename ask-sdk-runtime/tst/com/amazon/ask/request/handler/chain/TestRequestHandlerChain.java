package com.amazon.ask.request.handler.chain;

import com.amazon.ask.sdk.TestHandlerInput;
import com.amazon.ask.sdk.TestHandlerOutput;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;

import java.util.List;

public class TestRequestHandlerChain extends BaseRequestHandlerChain<TestHandlerInput, TestHandlerOutput> {

    protected TestRequestHandlerChain(GenericRequestHandler<TestHandlerInput, TestHandlerOutput> handler,
                                      List<GenericRequestInterceptor<TestHandlerInput>> requestInterceptors,
                                      List<GenericResponseInterceptor<TestHandlerInput, TestHandlerOutput>> responseInterceptors,
                                      List<GenericExceptionHandler<TestHandlerInput, TestHandlerOutput>> exceptionHandlers) {
        super(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends BaseRequestHandlerChain.Builder<TestHandlerInput, TestHandlerOutput, Builder> {
        @Override
        public TestRequestHandlerChain build() {
            return new TestRequestHandlerChain(handler, requestInterceptors, responseInterceptors, exceptionHandlers);
        }
    }

}
