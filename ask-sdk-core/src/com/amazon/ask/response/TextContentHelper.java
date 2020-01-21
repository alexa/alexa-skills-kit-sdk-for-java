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

import com.amazon.ask.model.interfaces.display.TextContent;

/**
 * A helper to assist in building {@link com.amazon.ask.model.interfaces.display.PlainText} and
 * {@link com.amazon.ask.model.interfaces.display.RichText} content.
 **/
public abstract class TextContentHelper {

    /**
     * Primary text.
     */
    protected String primaryText;

    /**
     * Secondary text.
     */
    protected String secondaryText;

    /**
     * Tertiary text.
     */
    protected String tertiaryText;

    /**
     * Returns an instance of PlainTextContentContentHelper.
     * @return {@link PlainTextContentContentHelper}.
     */
    public static PlainTextContentContentHelper forPlainText() {
        return new PlainTextContentContentHelper();
    }

    /**
     * Returns an instance of RichContentTextContentHelper.
     * @return {@link RichContentTextContentHelper}.
     */
    public static RichContentTextContentHelper forRichText() {
        return new RichContentTextContentHelper();
    }

    /**
     * Adds primary text to TextContentHelper.
     * @param primaryText primary text.
     * @return {@link TextContentHelper}.
     */
    public TextContentHelper withPrimaryText(final String primaryText) {
        this.primaryText = primaryText;
        return this;
    }

    /**
     * Adds secondary text to TextContentHelper.
     * @param secondaryText secondary text.
     * @return {@link TextContentHelper}.
     */
    public TextContentHelper withSecondaryText(final String secondaryText) {
        this.secondaryText = secondaryText;
        return this;
    }

    /**
     * Adds tertiary text to TextContentHelper.
     * @param tertiaryText tertiary text.
     * @return {@link TextContentHelper}.
     */
    public TextContentHelper withTertiaryText(final String tertiaryText) {
        this.tertiaryText = tertiaryText;
        return this;
    }

    /**
     * Abstract method to build an instance of TextContent.
     * @return {@link TextContent}.
     */
    public abstract TextContent build();

}
