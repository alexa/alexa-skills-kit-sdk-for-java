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

import com.amazon.ask.request.SkillRequest;
import com.amazon.ask.util.ValidationUtils;

/**
 * Skill Request implements {@link SkillRequest} and provides utility method to get raw request.
 */
public class BaseSkillRequest implements SkillRequest {

    /**
     * Incoming request in raw format.
     */
    private final byte[] payload;

    /**
     * Construct instance of BaseSkillRequest with the given payload.
     * @param payload request in raw format.
     */
    public BaseSkillRequest(final byte[] payload) {
        this.payload = ValidationUtils.assertNotNull(payload, "payload");
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public byte[] getRawRequest() {
        return payload;
    }

}
