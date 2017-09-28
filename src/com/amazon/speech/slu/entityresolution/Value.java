/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu.entityresolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

/**
 * <p>
 * {@code Value} is part of an {@link ValueWrapper}.
 * A Value object represents the resolved value for the slot, based on the user’s utterance and slot type definition.
 * </p>
 */
public final class Value {

    private final String name;
    private final String id;

    /**
     * Returns a new builder instance used to construct a new {@code Value}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Value} from a {@code Builder}.
     *
     * @param builder
     *              the builder used to construct the {@code Value}
     */
    private Value(final Builder builder) {
        name = builder.name;
        id = builder.id;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param name
     *            The string for the resolved slot value.
     * @param id
     *            The unique ID defined for the resolved slot value.
     */
    private Value(@JsonProperty("name") final String name,
                  @JsonProperty("id") final String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the name for this {@code Value}.
     *
     * @return the name of the {@code Value}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Id for this {@code Value}.
     *
     * @return the ID of the {@code Value}
     */
    public String getId() {
        return id;
    }

    /**
     * Builder used to construct a new {@code Value}.
     */
    public static final class Builder {
        private String name;
        private String id;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Value build() {
            Validate.notBlank(name, "Value name must be defined");
            return new Value(this);
        }

    }
}
