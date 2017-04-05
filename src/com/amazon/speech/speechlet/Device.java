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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents information about the Alexa-enabled device, such as what interfaces the device
 * supports.
 */
public final class Device {
    private final String deviceId;
    private final SupportedInterfaces supportedInterfaces;

    /**
     * Returns a new builder instance used to construct a new {@code Device}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Device} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Device}
     */
    private Device(final Builder builder) {
        deviceId = builder.deviceId;
        supportedInterfaces = builder.supportedInterfaces;
    }

    /**
     * Private constructor used for JSON serialization and for extending this class.
     *
     * @param supportedInterfaces
     *            supported interfaces
     */
    private Device(@JsonProperty("deviceId") final String deviceId,
            @JsonProperty("supportedInterfaces") final SupportedInterfaces supportedInterfaces) {
        this.deviceId = deviceId;
        this.supportedInterfaces = supportedInterfaces;
    }

    /**
     * Returns the ID of the device.
     *
     * @return the device ID
     */
    @JsonInclude(Include.NON_NULL)
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Returns an object representing the list of interfaces supported by the device.
     *
     * @return an object representing the list of interfaces supported by the device.
     */
    public SupportedInterfaces getSupportedInterfaces() {
        return supportedInterfaces;
    }

    /**
     * Builder used to construct a new {@code Device}.
     */
    public static final class Builder {
        private String deviceId;
        private SupportedInterfaces supportedInterfaces;

        private Builder() {
        }

        public Builder withDeviceId(final String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder withSupportedInterfaces(final SupportedInterfaces supportedInterfaces) {
            this.supportedInterfaces = supportedInterfaces;
            return this;
        }

        public Device build() {
            return new Device(this);
        }
    }
}
