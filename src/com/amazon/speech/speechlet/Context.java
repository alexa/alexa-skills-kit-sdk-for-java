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
 * The context for a {@code SpeechletRequest} containing the state of the interfaces.
 */
public final class Context {
    private final Map<Class<? extends Interface>, State<?>> states;

    /**
     * Returns a new builder instance used to construct a new {@code Context}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Context} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Context}.
     */
    private Context(final Builder builder) {
        this.states = Collections.unmodifiableMap(builder.states);
    }

    /**
     * Returns true if context has {@code State} for the interface, false otherwise.
     *
     * @param clazz
     *            the interface class
     * @param <T>
     *            the interface subtype
     * @return true if context has {@code State} for the interface, false otherwise
     */
    public <T extends Interface> boolean hasState(final Class<T> clazz) {
        return states != null && states.containsKey(clazz);
    }

    /**
     * Returns {@code State} for interface if it exists, null otherwise.
     *
     * @param interfaceClass
     *            the interface class
     * @param castClass
     *            the class type that should be returned by this method. This is typically denoted
     *            by a static field called {@code STATE_TYPE} of the {@code interfaceClass}.
     * @param <T>
     *            the interface subtype
     * @param <S>
     *            the state type for the interface
     * @return the state for the interface if it exists, null otherwise
     */
    public <T extends Interface, S extends State<T>> S getState(
            final Class<T> interfaceClass, final Class<S> castClass) {
        if (!hasState(interfaceClass)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        S stateForInterface = (S) states.get(interfaceClass);

        return stateForInterface;
    }

    /**
     * Builder used to construct a new {@code Context}.
     */
    public static final class Builder {
        private Map<Class<? extends Interface>, State<?>> states = new HashMap<>();

        private Builder() {
        }

        public Builder addState(final State<? extends Interface> state) {
            states.put(state.getInterfaceClass(), state);
            return this;
        }

        public Context build() {
            return new Context(this);
        }
    }
}
