/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display;

import com.amazon.speech.speechlet.Interface;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the presence of Display capabilities of the Alexa client.
 */
public final class DisplayInterface extends Interface {

    /**
     * Default templateVersion to be used if not specified.
     */
    private static final String DEFAULT_TEMPLATE_VERSION = "1.0";

    /**
     * Default markupVersion to be used if not specified.
     */
    private static final String DEFAULT_MARKUP_VERSION = "1.0";

    /**
     * The type of state returned for this interface, to be passed in as the second argument for
     * {@code Context#getState(Interface, State<Interface>)}.
     */
    public static final Class<DisplayState> STATE_TYPE = DisplayState.class;

    /**
     * Supported templateVersion.
     */
    private final String templateVersion;

    /**
     * Supported markupVersion.
     */
    private final String markupVersion;

    /**
     * Returns a new builder instance used to construct a new {@code DisplayInterface}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param templateVersion
     *            the supported template version
     * @param markupVersion
     *            the supported markup version
     */
    private DisplayInterface(@JsonProperty("templateVersion") final String templateVersion,
            @JsonProperty("markupVersion") final String markupVersion) {
        this.templateVersion = templateVersion;
        this.markupVersion = markupVersion;
    }

    /**
     * Private constructor to return a new {@code DisplayInterface} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code DisplayInterface}
     */
    private DisplayInterface(final Builder builder) {
        this.templateVersion = builder.templateVersion;
        this.markupVersion = builder.markupVersion;
    }

    /**
     * Returns the current supported {@code templateVersion}.
     */
    public String getTemplateVersion() {
        return this.templateVersion;
    }

    /**
     * Returns the current supported {@code markupVersion}.
     */
    public String getMarkupVersion() {
        return this.markupVersion;
    }

    /**
     * Builder used to construct a new {@code DisplayInterface}.
     */
    public static final class Builder {
        private String templateVersion = DisplayInterface.DEFAULT_TEMPLATE_VERSION;

        private String markupVersion = DisplayInterface.DEFAULT_MARKUP_VERSION;

        private Builder() {
        }

        public Builder withTemplateVersion(String templateVersion) {
            this.templateVersion = templateVersion;
            return this;
        }

        public Builder withMarkupVersion(String markupVersion) {
            this.markupVersion = markupVersion;
            return this;
        }

        public DisplayInterface build() {
            return new DisplayInterface(this);
        }
    }
}
