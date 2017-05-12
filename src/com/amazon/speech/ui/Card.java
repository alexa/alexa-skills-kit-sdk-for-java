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
 * <p>
 * Base class for cards to display in the companion application.
 * </p>
 * <p>
 * The common elements of all UI cards are laid out as follows:
 * </p>
 *
 * <div style="font-family: 'Helvetica Neue',Arial,sans-serif; background-color: #EEE; padding: 5px;
 * "> <div style=" border-width: 1px; border-style: solid; border-color: #EEE #CCC #CCC #EEE;
 * background-color: #FFF; padding: 6px;"> <span style="font-size: 20px; font-weight:
 * 400;">Title</span><br>
 * <br>
 * <br>
 * </div> </div>
 *
 * @see com.amazon.speech.speechlet.SpeechletResponse#setCard(Card)
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        // The sub types that extend the Card class.
        @Type(value = SimpleCard.class),
        @Type(value = LinkAccountCard.class),
        @Type(value = StandardCard.class),
        @Type(value = AskForPermissionsConsentCard.class)
})
public abstract class Card {
    private String title;

    /**
     * Returns the title for the UI card.
     *
     * @return the card title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for the UI card.
     *
     * @param title
     *            the card title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
