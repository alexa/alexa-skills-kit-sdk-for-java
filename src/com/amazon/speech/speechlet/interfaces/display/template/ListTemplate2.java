/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.template;

import java.util.List;

import com.amazon.speech.speechlet.interfaces.display.element.Image;
import com.amazon.speech.speechlet.interfaces.display.element.TripleTextContent;
import com.amazon.speech.speechlet.interfaces.display.template.ListTemplate2.ListItem;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * This list template is comprised of the following elements:
 * <ul>
 * <li>An optional full-screen background image</li>
 * <li>A scrollable sequence of {@link ListItem}s</li>
 * <li>An optional title</li>
 * </ul>
 *
 * <p>
 * For layout specifics, please refer to our template documentation.
 * </p>
 */
@JsonTypeName("ListTemplate2")
public class ListTemplate2 extends ListTemplate<ListItem> {
    private Image backgroundImage;

    private List<ListItem> listItems;

    private String title;

    /**
     * Returns the background image.
     *
     * @return the background image
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the background image.
     *
     * @param backgroundImage
     *            the background image
     */
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public List<ListItem> getListItems() {
        return listItems;
    }

    /**
     * Sets the list of items.
     *
     * @param listItems
     *            the list of items
     */
    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    /**
     * Returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This list item is comprised of the following elements:
     * <ul>
     * <li>An image (rectangular, square)</li>
     * <li>A text field</li>
     * </ul>
     */
    public static class ListItem extends
            com.amazon.speech.speechlet.interfaces.display.template.ListItem {
        private Image image;

        private TextContent textContent;

        /**
         * Returns the image.
         *
         * @return the image
         */
        public Image getImage() {
            return image;
        }

        /**
         * Sets the image.
         *
         * @param image
         *            the image
         */
        public void setImage(Image image) {
            this.image = image;
        }

        /**
         * Returns the text content.
         *
         * @return the text content
         */
        public TextContent getTextContent() {
            return textContent;
        }

        /**
         * Sets the text content for the list item.
         *
         * @param textContent
         *            the text content
         */
        public void setTextContent(TextContent textContent) {
            this.textContent = textContent;
        }

        public static class TextContent extends TripleTextContent {

        }
    }
}
