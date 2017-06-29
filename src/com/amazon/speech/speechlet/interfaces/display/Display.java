/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.display.request.ElementSelectedRequest;

/**
 * Interactions between a user and a screen-enabled Alexa device can include rich, interactive
 * display templates shown on the screen. These are graphical elements that complement the voice
 * interaction. A custom skill can include display templates in its response. This interface allows
 * you to handle input events associated with these on-screen elements.
 */
public interface Display {
    /**
     * Entry point for an element being touched or selected on the display.
     *
     * @param requestEnvelope
     *            the request envelope containing the element selected request to handle
     * @return the response, spoken and visual, to the request
     */
    SpeechletResponse onElementSelected(
            SpeechletRequestEnvelope<ElementSelectedRequest> requestEnvelope);
}
