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
 * A standard card that includes a title, a text content and an image.
 */
@JsonTypeName("Standard")
public class StandardCard extends Card {
    private String text;
    private Image image;

    /**
     * Returns the text of the card.
     *
     * @return the text of the card
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the card.
     *
     * @param text
     *            the text to be set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the image of the card.
     *
     * @return the image of the card
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the image of the card.
     *
     * @param image
     *            the image to be set
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
