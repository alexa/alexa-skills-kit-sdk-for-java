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
import com.amazon.ask.model.interfaces.display.RichText;
import com.amazon.ask.model.interfaces.display.TextContent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextContentHelperTest {
    @Test
    public void build_rich_text() {
        RichText richText = RichText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withPrimaryText(richText).build();
        assertEquals(textContent, TextContentHelper.forRichText().withPrimaryText("foo").build());
    }

    @Test
    public void build_plain_text() {
        PlainText plainText = PlainText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withPrimaryText(plainText).build();
        assertEquals(textContent, TextContentHelper.forPlainText().withPrimaryText("foo").build());
    }

    @Test
    public void build_rich_text_secondary() {
        RichText richText = RichText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withSecondaryText(richText).build();
        assertEquals(textContent, TextContentHelper.forRichText().withSecondaryText("foo").build());
    }

    @Test
    public void build_plain_text_secondary() {
        PlainText plainText = PlainText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withSecondaryText(plainText).build();
        assertEquals(textContent, TextContentHelper.forPlainText().withSecondaryText("foo").build());
    }

    @Test
    public void build_rich_text_tertiary() {
        RichText richText = RichText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withTertiaryText(richText).build();
        assertEquals(textContent, TextContentHelper.forRichText().withTertiaryText("foo").build());
    }

    @Test
    public void build_plain_text_tertiary() {
        PlainText plainText = PlainText.builder().withText("foo").build();
        TextContent textContent = TextContent.builder().withTertiaryText(plainText).build();
        assertEquals(textContent, TextContentHelper.forPlainText().withTertiaryText("foo").build());
    }
}
