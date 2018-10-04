/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response;

import java.io.OutputStream;

/**
 * Output from an {@link com.amazon.ask.AlexaSkill}.
 *
 * @param <T> skill response type
 */
public interface SkillResponse<T> {

    /**
     * Returns true if a response was produced by the skill.
     *
     * @return true if response is present
     */
    boolean isPresent();

    /**
     * Returns the skill's response.
     *
     * @return skill response
     */
    T getResponse();

    /**
     * Returns the skill's response as a byte array.
     *
     * @return skill response as a byte array
     */
    byte[] getRawResponse();

    /**
     * Writes the skill's response to an {@link OutputStream}.
     *
     * @param stream stream to write to
     */
    void writeTo(OutputStream stream);

}
