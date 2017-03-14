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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Base class for output speech to be rendered to the user.
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
// The sub types that extend the SpeechletRequest class.
@JsonSubTypes({
        @Type(value = PlainTextOutputSpeech.class),
        @Type(value = SsmlOutputSpeech.class)
})
public abstract class OutputSpeech {
    private String id;

    /**
     * Returns the identifier assigned to this speech.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the identifier assigned to this speech.
     *
     * @param id
     *            the identifier to set
     */
    public void setId(final String id) {
        this.id = id;
    }
}
