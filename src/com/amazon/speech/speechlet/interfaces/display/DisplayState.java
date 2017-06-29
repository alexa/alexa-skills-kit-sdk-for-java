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

import com.amazon.speech.speechlet.State;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the state of the Display component on the device.
 */
public final class DisplayState extends State<DisplayInterface> {
    private final String token;

    /**
     * Returns a new builder instance used to construct a new {@code DisplayState}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code DisplayState} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code DisplayState}
     */
    private DisplayState(final Builder builder) {
        token = builder.token;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param token
     *            the current token
     */
    private DisplayState(@JsonProperty("token") final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Class<DisplayInterface> getInterfaceClass() {
        return DisplayInterface.class;
    }

    /**
     * Builder used to construct a new {@code DisplayState}.
     */
    public static final class Builder {
        private String token;

        private Builder() {
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public DisplayState build() {
            return new DisplayState(this);
        }
    }
}
