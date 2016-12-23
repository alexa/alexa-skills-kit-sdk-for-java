/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system;

import com.amazon.speech.speechlet.Interface;

/**
 * Represents the presence of System capabilities of the Alexa client.
 */
public class SystemInterface extends Interface {

    /**
     * The type of state returned for this interface, to be passed in as the second argument for
     * {@code Context#getState(Interface, State<Interface>)}.
     */
    public static final Class<SystemState> STATE_TYPE = SystemState.class;

    /**
     * Returns a new builder instance used to construct a new {@code SystemInterface}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SystemInterface} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SystemInterface}
     */
    private SystemInterface(final Builder builder) {
    }

    /**
     * Private constructor a new {@code SystemInterface}.
     */
    private SystemInterface() {
    }

    /**
     * Builder used to construct a new {@code SystemInterface}.
     */
    public static final class Builder {
        private Builder() {
        }

        public SystemInterface build() {
            return new SystemInterface(this);
        }
    }
}
