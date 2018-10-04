/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.dispatcher.request.handler.impl;

import com.amazon.ask.dispatcher.request.handler.HandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;

import java.util.Optional;

/**
 * {@link HandlerAdapter} implementation for {@link RequestHandler} implementations.
 */
@Deprecated
public class DefaultHandlerAdapter extends BaseHandlerAdapter<HandlerInput, Optional<Response>, RequestHandler> implements HandlerAdapter {

    public DefaultHandlerAdapter() {
        super(RequestHandler.class);
    }

}
