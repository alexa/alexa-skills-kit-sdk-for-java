/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask;

import com.amazon.ask.request.SkillRequest;
import com.amazon.ask.response.SkillResponse;

/**
 * Interface allows user to configure different types of skills (Custom, Music etc.).
 * @param <Request> input type.
 * @param <Response> output type.
 */
public interface AlexaSkill<Request, Response> {

    /**
     * Processes an incoming request and returns a response.
     * @param request instance of type {@link SkillRequest}.
     * @return {@link SkillResponse}.
     */
    default SkillResponse<Response> execute(final SkillRequest request) {
        return execute(request, null);
    }

    /**
     * Processes an incoming request and returns a response.
     * @param request instance of type {@link SkillRequest}.
     * @param context object passed to handler by AWS Lambda running your function.
     * @return {@link SkillResponse}.
     */
    SkillResponse<Response> execute(SkillRequest request, Object context);

}
