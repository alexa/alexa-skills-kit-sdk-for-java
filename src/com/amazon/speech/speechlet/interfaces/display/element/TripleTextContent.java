/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.element;

/**
 * This interface represents a text content (a wrapper class) that can be embedded within a display
 * template. It encapsulates primary, secondary and tertiary fields.
 */
public abstract class TripleTextContent {
    private TextField primaryText;
    private TextField secondaryText;
    private TextField tertiaryText;

    /**
     * Returns the primary text field.
     *
     * @return the primary text field
     */
    public TextField getPrimaryText() {
        return primaryText;
    }

    /**
     * Sets the primary text field.
     *
     * @param primaryText
     *            the primary text field
     */
    public void setPrimaryText(TextField primaryText) {
        this.primaryText = primaryText;
    }

    /**
     * Returns the secondary text field.
     *
     * @return the secondary text field
     */
    public TextField getSecondaryText() {
        return secondaryText;
    }

    /**
     * Sets the secondary text field.
     *
     * @param secondaryText
     *            the secondary text field
     */
    public void setSecondaryText(TextField secondaryText) {
        this.secondaryText = secondaryText;
    }

    /**
     * Returns the tertiary text field.
     *
     * @return the tertiary text field
     */
    public TextField getTertiaryText() {
        return tertiaryText;
    }

    /**
     * Sets the tertiary text field.
     *
     * @param tertiaryText
     *            the tertiary text field
     */
    public void setTertiaryText(TextField tertiaryText) {
        this.tertiaryText = tertiaryText;
    }

}
