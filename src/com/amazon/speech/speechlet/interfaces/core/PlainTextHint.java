/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A textual hint to be rendered to the user.
 */
@JsonTypeName("PlainText")
public class PlainTextHint extends Hint {
    private String text;

    /**
     * Returns the text of the hint.
     *
     * @return the hint text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text for the hint.
     * 
     * @param text
     *            the hint text
     */
    public void setText(String text) {
        this.text = text;
    }
}
