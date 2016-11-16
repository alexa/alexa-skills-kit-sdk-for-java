/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.ui;

/**
 * Defines how the skill reprompts when a user does not reply to a request or when a user replies
 * with something that was not understood.
 */
public class Reprompt {
    /**
     * The speech to render to the user when a reprompt occurs.
     */
    private OutputSpeech outputSpeech;

    /**
     * Returns the output speech for the reprompt.
     *
     * @return the speech that should be read on a reprompt
     */
    public OutputSpeech getOutputSpeech() {
        return this.outputSpeech;
    }

    /**
     * Sets the output speech for the reprompt. If nothing is provided, the session ends instead of
     * reprompting the user.
     *
     * @param outputSpeech
     *            the speech that should be read on a reprompt
     */
    public void setOutputSpeech(OutputSpeech outputSpeech) {
        this.outputSpeech = outputSpeech;
    }
}