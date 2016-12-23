/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a collection of the {@code Interface}s supported by the Alexa client.
 */
public class SupportedInterfaces {
    private final Map<Class<? extends Interface>, Interface> supportedInterfaces;

    /**
     * Returns a new builder instance used to construct a new {@code SupportedInterfaces}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SupportedInterfaces} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SupportedInterfaces}
     */
    private SupportedInterfaces(final Builder builder) {
        this.supportedInterfaces = Collections.unmodifiableMap(builder.supportedInterfaces);
    }

    /**
     * Returns true if the interface class exists, false otherwise.
     *
     * @param clazz
     *            the interface class
     * @param <T>
     *            the interface subtype
     * @return true if the interface class exists, false otherwise
     */
    public <T extends Interface> boolean isInterfaceSupported(final Class<T> clazz) {
        return supportedInterfaces != null && supportedInterfaces.containsKey(clazz);
    }

    /**
     * Returns {@code Interface} for interface class if it exists, null otherwise.
     *
     * @param clazz
     *            the interface class
     * @param <T>
     *            the interface subtype
     * @return the interface for the interface if it exists, null otherwise
     */
    public <T extends Interface> T getSupportedInterface(final Class<T> clazz) {
        if (!isInterfaceSupported(clazz)) {
            return null;
        }

        return (T) supportedInterfaces.get(clazz);
    }

    /**
     * Builder used to construct a new {@code SupportedInterfaces}.
     */
    public static final class Builder {
        private Map<Class<? extends Interface>, Interface> supportedInterfaces = new HashMap<>();

        private Builder() {
        }

        public Builder addSupportedInterface(final Interface supportedInterface) {
            supportedInterfaces.put(supportedInterface.getClass(), supportedInterface);
            return this;
        }

        public SupportedInterfaces build() {
            return new SupportedInterfaces(this);
        }
    }
}
