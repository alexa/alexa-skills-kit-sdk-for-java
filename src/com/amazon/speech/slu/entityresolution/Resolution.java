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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * A {@code Resolution} is an object representing possible authority for entity resolution.
 * An authority represents the source for the data provided for the slot.
 * For a custom slot type, the authority is the slot type customer defined.
 * </p>
 *
 * For example:
 * <p>
 * For value "rain", developer can define "shower" as its synonym.
 * When user says "shower" instead of "rain", the resolution sent to Alexa skill would be: <br>
 *
 * <pre>
 * {
 *     "resolutions": {
 *         "resolutionsPerAuthority": [{
 *              "authority": "{authority-url}",
 *              "status": {
 *                  "code": "ER_SUCCESS_MATCH"
 *              },
 *              "values": [{
 *                  "value": {
 *                      "name": "rain",
 *                      "id": "RAIN"
 *                  }
 *              }]
 *         }]
 *     }
 * }
 * </pre>
 */
public final class Resolution {

    private final String authority;
    private final Status status;
    private final List<ValueWrapper> values;

    /**
     * Returns a new builder instance used to construct a new {@code Resolution}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Resolution} from a {@code Builder}.
     *
     * @param builder
     *              the builder used to construct the {@code Resolution}
     */
    private Resolution(final Builder builder) {
        authority = builder.authority;
        status = builder.status;
        values = Collections.unmodifiableList(builder.values);
    }

    /**
     * Private constructor to return a new {@code Resolution}.
     * @param authority
     *                  The name of the authority for the slot values.
     *                  For custom slot types, this authority label incorporates skill ID and the slot type name.
     * @param status
     *              The status of entity resolution for the slot.
     * @param values
     *              The resolved value of the slot
     */
    private Resolution(@JsonProperty("authority") final String authority,
                       @JsonProperty("status") final Status status,
                       @JsonProperty("values") final List<ValueWrapper> values) {
        this.authority = authority;
        this.status = status;
        if (values != null) {
            this.values = Collections.unmodifiableList(values);
        } else {
            this.values = Collections.emptyList();
        }
    }

    /**
     * Returns the authority of this {@code Resolution}.
     *
     * @return the authority.
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Returns the {@code Status} of this {@code Resolution}.
     *
     * @return the status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the list of {@code ValueWrapper} of this {@code Resolution}.
     *
     * @return an immutable list of all valuesWrapper.
     */
    public List<ValueWrapper> getValueWrappers() {
        return values;
    }

    /**
     * Returns the {@code ValueWrapper} with the provided index.
     *
     * @param index
     *          index of the {@code ValueWrapper} to retrieve
     * @return the {@code ValueWrapper}
     */

    public ValueWrapper getValueWrapperAtIndex(final int index) {
        return values.get(index);
    }

    /**
     * Builder used to construct a new {@code Resolution}.
     */
    public static final class Builder {
        private String authority;
        private Status status;
        private final List<ValueWrapper> values = new ArrayList<>();

        public Builder withAuthority(final String authority) {
            this.authority = authority;
            return this;
        }

        public Builder withStatus(final Status status) {
            this.status = status;
            return this;
        }

        public Builder withValues(final List<ValueWrapper> values) {
            this.values.addAll(values);
            return this;
        }

        public Builder withValue(final ValueWrapper value) {
            this.values.add(value);
            return this;
        }

        public Resolution build() {
            return new Resolution(this);
        }
    }
}
