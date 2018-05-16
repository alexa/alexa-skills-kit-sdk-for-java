/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.verifiers;

import com.amazon.ask.model.RequestEnvelope;

import javax.servlet.http.HttpServletRequest;

/**
 * Verifiers are run against incoming requests to verify authenticity and integrity of the request before processing
 * it.
 */
public interface SkillServletVerifier {

    /**
     * Verifies an incoming request.
     *
     * @param servletRequest servlet request
     * @param serializedRequestEnvelope the request envelope, in serialized form
     * @param deserializedRequestEnvelope the request envelope, in deserialized form
     * @throws SecurityException if verification fails.
     */
    void verify(HttpServletRequest servletRequest, byte[] serializedRequestEnvelope, RequestEnvelope deserializedRequestEnvelope) throws SecurityException;

}
