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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This class represents a rich, interactive display template that can be rendered to the screen of
 * an Alexa-enabled device. The Alexa Skills Kit provides two main categories of display templates:
 * <ul>
 * <li>A <b>body template</b> displays text and images.</li>
 * <li>A <b>list template</b> displays a scrollable list of items, each with associated text and
 * optional images.</li>
 * </ul>
 *
 * <p>
 * A number of templates are available for each category of template. The templates vary in the
 * size, number, and positioning of the text and images, and the scrolling of lists.
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(ListTemplate1.class),
        @JsonSubTypes.Type(ListTemplate2.class),

        @JsonSubTypes.Type(BodyTemplate1.class),
        @JsonSubTypes.Type(BodyTemplate2.class),
        @JsonSubTypes.Type(BodyTemplate3.class),
        @JsonSubTypes.Type(BodyTemplate6.class)
})
public abstract class Template {
    private String token;
    private BackButtonBehavior backButtonBehavior = BackButtonBehavior.VISIBLE;

    /**
     * Returns the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns whether the template should allow the user to return to the previous template.
     *
     * @return whether the user can go back
     */
    @JsonGetter("backButton")
    public BackButtonBehavior getBackButtonBehavior() {
        return backButtonBehavior;
    }

    /**
     * Sets whether the template should allow the user to return to the previous template.
     *
     * @param backButtonBehavior
     *            behavior of the back button
     */
    @JsonSetter("backButton")
    public void setBackButtonBehavior(BackButtonBehavior backButtonBehavior) {
        this.backButtonBehavior = backButtonBehavior;
    }

    /**
     * Template back button behavior.
     */
    public static enum BackButtonBehavior {
        HIDDEN,
        VISIBLE;
    }
}
