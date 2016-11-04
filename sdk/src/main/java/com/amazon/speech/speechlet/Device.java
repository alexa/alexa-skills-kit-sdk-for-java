/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;


public final class Device {

    private final Map<String, Object> supportedInterfaces;


    /**
     * Returns a new builder instance used to construct a new {@code Session}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Session} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Session}.
     */
    private Device(final Builder builder) {
        supportedInterfaces = builder.supportedInterfaces;
    }

    /**
     * Private constructor used for JSON serialization.
     *

     * @param supportedInterfaces
     *            all the session attributes
     */
    private Device(@JsonProperty("supportedInterfaces") final Map<String, Object> supportedInterfaces) {


        if (supportedInterfaces != null) {
            this.supportedInterfaces = supportedInterfaces;
        } else {
            this.supportedInterfaces = new HashMap<String, Object>();
        }

    }

    /**
     * This is only for Jackson to figure out what default values are (e.g. isNew = false).
     */
    private Device() {
        this.supportedInterfaces = new HashMap<String, Object>();
    }



    /**
     * Returns all the session attributes.
     *
     * @return a mutable map of all the attributes that may be altered directly
     */
    @JsonInclude(Include.NON_EMPTY)
    public Map<String, Object> getSupportedInterfaces() {
        return supportedInterfaces;
    }

    /**
     * Returns the attribute associated with the provided name.
     *
     * @param name
     *            the name of the attribute to retrieve
     * @return the value or {@code null}
     */
    public Object getAttribute(final String name) {
        return supportedInterfaces.get(name);
    }

    /**
     * Add or modify the attribute with the provided name.
     *
     * @param name
     *            the name of the attribute to set
     * @param value
     *            the new value for the attribute
     */
    public void setAttribute(final String name, final Object value) {
        supportedInterfaces.put(name, value);
    }

    /**
     * Remove the attribute associated with the provided name.
     *
     * @param name
     *            the name of the attribute to remove
     */
    public void removeAttribute(final String name) {
        supportedInterfaces.remove(name);
    }



    /**
     * Builder used to construct a new {@code Session}.
     */
    public static final class Builder {

        private Map<String, Object> supportedInterfaces = new HashMap<>();


        private Builder() {
        }


        public Builder withSupportedInterfaces(final Map<String, Object> supportedInterfaces) {
            this.supportedInterfaces = supportedInterfaces;
            return this;
        }


        public Device build() {
            Validate.notNull(supportedInterfaces, "supportedInterfaces must be defined");
            return new Device(this);
        }
    }
}
