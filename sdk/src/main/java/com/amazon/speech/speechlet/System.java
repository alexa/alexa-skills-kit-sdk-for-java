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

/**
 * <p>
 * A {@code System} represents a context System execution of a {@code Speechlet} by a user.
 * </p>
 *
 *
 * <p>
 * If the {@code Speechlet} throws a {@code SpeechletException}, modifications to the attributes up
 * to that point are applied, even if the request was not processed successfully.
 * </p>
 *
 * <p>
 * Because a {@code Speechlet} is a cloud-based service, the life-cycle of the actual session object
 * passed in the {@code Speechlet} methods is unrelated to the Alexa skill execution as experienced
 * by the user interacting with the speech-enabled device. A {@code Speechlet} receives a new
 * {@code Session} instance for each {@code Speechlet} invocation. The identifier of the
 * {@code Session} is what uniquely identifies it between invocations. References to a
 * {@code Session} should never be stored as its contents is invalid after returning from a
 * {@code Speechlet} method.
 * </p>
 *
 * @see Speechlet
 * @see SpeechletException
 */
public final class System {

    private final Application application;
    private final Device device;
    private final User user;

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
    private System(final Builder builder) {

        device = builder.device;
        application = builder.application;
        user = builder.user;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param application
     *            the application
     * @param device
     *            the device
     * @param user
     *            the user associated with this session
     */
    private System(@JsonProperty("device") final Device device,
				   @JsonProperty("application") final Application application,
				   @JsonProperty("user") final User user) {
        this.device = device;
        this.application = application;
        this.user = user;
    }

    /**
     * This is only for Jackson to figure out what default values are (e.g. isNew = false).
     */
    private System() {
        this.application = null;
        this.device = null;
        this.user = null;
    }



    /**
     * Returns the application.
     *
     * @return the application
     */
    public Application getApplication() {
        return application;
    }



    /**
     * Returns the user associated with this session.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    public Device getDevice() { return device; }


    /**
     * Builder used to construct a new {@code Session}.
     */
    public static final class Builder {
        private Application application;
        private Device device;
        private User user;

        private Builder() {
        }


        public Builder withApplication(final Application application) {
            this.application = application;
            return this;
        }


        public Builder withUser(final User user) {
            this.user = user;
            return this;
        }

        public Builder withDevice(final Device device) {
            this.device = device;
            return this;
        }

        public System build() {
            Validate.notNull(user, "user must be defined");
            return new System(this);
        }
    }
}
