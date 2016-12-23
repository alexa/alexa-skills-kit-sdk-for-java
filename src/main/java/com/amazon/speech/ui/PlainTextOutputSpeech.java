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

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Speech represented by plain text.
 */
@JsonTypeName("PlainText")
public class PlainTextOutputSpeech extends OutputSpeech {
    private String text;

    /**
     * Returns the text to speak.
     *
     * @return the text to speak
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text to speak.
     *
     * @param text
     *            the text to speak to set
     */
    public void setText(final String text) {
        this.text = text;
    }
}
