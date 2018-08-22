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

import com.amazon.ask.model.interfaces.display.RichText;
import com.amazon.ask.model.interfaces.display.TextContent;

/**
 * A helper to assist in building {@link RichText} content.
 */
public class RichContentTextContentHelper extends TextContentHelper {

    /**
     * In place to prevent instantiation
     */
    RichContentTextContentHelper() { }

    public TextContent build() {
        return TextContent.builder()
                .withPrimaryText(primaryText != null ? RichText.builder().withText(primaryText).build() : null)
                .withSecondaryText(secondaryText != null ? RichText.builder().withText(secondaryText).build() : null)
                .withTertiaryText(tertiaryText != null ? RichText.builder().withText(tertiaryText).build() : null)
                .build();
    }

}
