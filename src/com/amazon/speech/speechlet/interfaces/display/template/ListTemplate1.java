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
import com.amazon.speech.speechlet.interfaces.display.template.ListTemplate1.ListItem;
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
@JsonTypeName("ListTemplate1")
public class ListTemplate1 extends ListTemplate<ListItem> {
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
     * This list item is comprised of text and image.
     */
    public static class ListItem extends
            com.amazon.speech.speechlet.interfaces.display.template.ListItem {
        private TextContent textContent;
        private Image image;

        /**
         * Returns the textContent.
         *
         * @return the textContent
         */
        public TextContent getTextContent() {
            return textContent;
        }

        /**
         * Sets the textConent.
         *
         * @param textContent the textContent to set
         */
        public void setTextContent(TextContent textContent) {
            this.textContent = textContent;
        }

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
         * @param image the image
         */
        public void setImage(Image image) {
            this.image = image;
        }

        public static class TextContent extends TripleTextContent {

        }
    }
}
