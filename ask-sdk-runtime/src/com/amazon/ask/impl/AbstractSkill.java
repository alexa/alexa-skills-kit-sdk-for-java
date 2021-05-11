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

    /**
     * List of JSON unmarshallers.
     */
    protected final List<JsonUnmarshaller<Request>> unmarshallerChain;

    /**
     * JSON marshaller.
     */
    protected final JsonMarshaller<Response> marshaller;

    /**
     * Constructor to build an instance of AbstractSkill.
     * @param unmarshaller JSON unmarshaller.
     * @param marshaller JSON marshaller.
     */
    protected AbstractSkill(final JsonUnmarshaller<Request> unmarshaller,
                            final JsonMarshaller<Response> marshaller) {
        this(Collections.singletonList(unmarshaller), marshaller);
    }

    /**
     * Constructor to build an instance of AbstractSkill.
     * @param unmarshallerChain list of JSON unmarshallers.
     * @param marshaller JSON marshaller.
     */
    protected AbstractSkill(final List<JsonUnmarshaller<Request>> unmarshallerChain,
                            final JsonMarshaller<Response> marshaller) {
        this.unmarshallerChain = ValidationUtils.assertNotNull(unmarshallerChain, "unmarshallerChain");
        this.marshaller = ValidationUtils.assertNotNull(marshaller, "marshaller");
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public SkillResponse<Response> execute(final SkillRequest request, final Object context) {
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
        if (response == null) {
            return null;
        }

        return new BaseSkillResponse<>(marshaller, response);
    }

    /**
     * Abstract method to kick off the request handling process.
     * @param unmarshalledRequest unmarshalled request.
     * @param context object passed to handler by AWS Lambda running your function.
     * @return {@link Response}.
     */
    protected abstract Response invoke(UnmarshalledRequest<Request> unmarshalledRequest, Object context);

}
