/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.impl;

import com.amazon.ask.request.UnmarshalledRequest;
import com.amazon.ask.util.ValidationUtils;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Default implementation of an {@link UnmarshalledRequest}
 * @param <Type> unmarshalled type
 */
public class BaseUnmarshalledRequest<Type> implements UnmarshalledRequest<Type> {

    private final Type unmarshalledRequest;
    private final JsonNode json;

    public BaseUnmarshalledRequest(Type unmarshalledRequest, JsonNode json) {
        this.unmarshalledRequest = ValidationUtils.assertNotNull(unmarshalledRequest, "unmarshalled request");
        this.json = json;
    }

    @Override
    public Type getUnmarshalledRequest() {
        return unmarshalledRequest;
    }

    @Override
    public JsonNode getRequestJson() {
        return json;
    }

}
