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
 * Speech using the SSML (Speech Synthesis Markup Language) representation.
 */
@JsonTypeName("SSML")
public class SsmlOutputSpeech extends OutputSpeech {
    private String ssml;

    /**
     * Returns the SSML to speak.
     *
     * @return the SSML to speak
     */
    public String getSsml() {
        return ssml;
    }

    /**
     * Sets the SSML to speak.
     *
     * @param ssml
     *            the SSML to speak to set
     */
    public void setSsml(final String ssml) {
        this.ssml = ssml;
    }
}
