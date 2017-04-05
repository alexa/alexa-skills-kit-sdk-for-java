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

import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.Device;
import com.amazon.speech.speechlet.State;
import com.amazon.speech.speechlet.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SystemState extends State<SystemInterface> {
    private final Application application;
    private final User user;
    private final Device device;
    private final String apiEndpoint;

    /**
     * Returns a new builder instance used to construct a new {@code SystemState}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SystemState} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SystemState}.
     */
    private SystemState(final Builder builder) {
        application = builder.application;
        user = builder.user;
        device = builder.device;
        apiEndpoint = builder.apiEndpoint;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param application
     *            the application object
     * @param user
     *            the user object
     * @param device
     *            the device object
     * @param apiEndpoint
     *            the endpoint to be used to access Alexa API resources for the user
     */
    private SystemState(@JsonProperty("application") final Application application,
            @JsonProperty("user") final User user, @JsonProperty("device") final Device device,
            @JsonProperty("apiEndpoint") final String apiEndpoint) {
        this.application = application;
        this.user = user;
        this.device = device;
        this.apiEndpoint = apiEndpoint;
    }

    /**
     * Returns the {@link Application} associated with this state.
     *
     * @return {@link Application} object
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Returns the {@link User} associated with this state.
     *
     * @return {@link User} object
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the {@link Device} associated with this state.
     *
     * @return {@link Device} object
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Returns the API endpoint value.
     *
     * @return API endpoint value
     */
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    @Override
    public Class<SystemInterface> getInterfaceClass() {
        return SystemInterface.class;
    }

    /**
     * Builder used to construct a new {@code SystemState}.
     */
    public static final class Builder {
        private Application application;
        private User user;
        private Device device;
        private String apiEndpoint;

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

        public Builder withApiEndpoint(final String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public SystemState build() {
            return new SystemState(this);
        }
    }
}
