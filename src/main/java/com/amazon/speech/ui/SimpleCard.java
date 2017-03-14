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
 * A basic card that includes a title and a simple string as the content.
 *
 * @see Card
 */
@JsonTypeName("Simple")
public class SimpleCard extends Card {
    private String content;

    /**
     * Sets the content for the {@code SimpleCard}.
     *
     * @param content
     *            the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the content for the {@code SimpleCard}.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
