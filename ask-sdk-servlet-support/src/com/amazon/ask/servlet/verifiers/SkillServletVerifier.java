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

/**
 * Verifiers are run against incoming requests to verify authenticity and integrity of the request before processing
 * it.
 */
public interface SkillServletVerifier {

    /**
     * Verifies an incoming serverRequest.
     *
     * @param serverRequest serverRequest
     * @throws SecurityException if verification fails.
     */
    void verify(ServerRequest serverRequest) throws SecurityException;

}
