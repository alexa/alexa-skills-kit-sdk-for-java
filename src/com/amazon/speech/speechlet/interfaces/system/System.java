/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;

/**
 * The interface for interacting with system events, such as device exceptions.
 */
public interface System {
    /**
     * Entry point for a runtime exception occurring on the device.
     *
     * @param requestEnvelope
     *            the request envelope containing the exception encountered request to handle
     */
    void onExceptionEncountered(
            SpeechletRequestEnvelope<ExceptionEncounteredRequest> requestEnvelope);
}