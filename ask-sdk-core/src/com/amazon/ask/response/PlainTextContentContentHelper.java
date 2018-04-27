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

import com.amazon.ask.model.interfaces.display.PlainText;
import com.amazon.ask.model.interfaces.display.TextContent;

/**
 * Responsible for building plain text content object using ask-sdk-model in Alexa skills kit display interface
 */
public class PlainTextContentContentHelper extends TextContentHelper {

    /**
     *  In place to prevent instantiation
     */
    PlainTextContentContentHelper() { }

    public TextContent build() {
        return TextContent.builder()
                .withPrimaryText(primaryText != null ? PlainText.builder().withText(primaryText).build() : null)
                .withSecondaryText(secondaryText != null ? PlainText.builder().withText(secondaryText).build() : null)
                .withTertiaryText(tertiaryText != null ? PlainText.builder().withText(tertiaryText).build() : null)
                .build();
    }

}
