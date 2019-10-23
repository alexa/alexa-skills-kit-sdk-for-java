/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.impl;

import com.amazon.ask.AlexaSkill;
import com.amazon.ask.request.SkillRequest;
import com.amazon.ask.request.UnmarshalledRequest;
import com.amazon.ask.response.SkillResponse;
import com.amazon.ask.response.impl.BaseSkillResponse;
import com.amazon.ask.util.JsonMarshaller;
import com.amazon.ask.util.JsonUnmarshaller;
import com.amazon.ask.util.ValidationUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Top level SDK entry point.
 * @param <Request> request JSON type
 * @param <Response> response JSON type
 */
public abstract class AbstractSkill<Request, Response> implements AlexaSkill<Request, Response> {

    protected final List<JsonUnmarshaller<Request>> unmarshallerChain;
    protected final JsonMarshaller<Response> marshaller;

    protected AbstractSkill(JsonUnmarshaller<Request> unmarshaller,
                            JsonMarshaller<Response> marshaller) {
        this(Collections.singletonList(unmarshaller), marshaller);
    }

    protected AbstractSkill(List<JsonUnmarshaller<Request>> unmarshallerChain,
                            JsonMarshaller<Response> marshaller) {
        this.unmarshallerChain = ValidationUtils.assertNotNull(unmarshallerChain, "unmarshallerChain");
        this.marshaller = ValidationUtils.assertNotNull(marshaller, "marshaller");
    }

    @Override
    public SkillResponse<Response> execute(SkillRequest request, Object context) {
        Optional<UnmarshalledRequest<Request>> deserializedRequest = Optional.empty();

        for (JsonUnmarshaller<Request> unmarshaller : unmarshallerChain) {
            deserializedRequest = unmarshaller.unmarshall(request.getRawRequest());
            if (deserializedRequest.isPresent()) {
                break;
            }
        }
        if (!deserializedRequest.isPresent()) {
            return null;
        }

        Response response = invoke(deserializedRequest.get(), context);
        return new BaseSkillResponse<>(marshaller, response);
    }

    protected abstract Response invoke(UnmarshalledRequest<Request> unmarshalledRequest, Object context);

}
