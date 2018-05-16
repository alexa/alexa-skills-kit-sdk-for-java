/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.persistence.impl;

import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.User;
import com.amazon.ask.model.interfaces.system.SystemState;

import java.util.Optional;
import java.util.function.Function;

public class PartitionKeyGenerators {

    /** Prevent instantiation */
    private PartitionKeyGenerators() {}

    /**
     * Produces a partition key from the user ID contained in an incoming request.
     * @return partition key derived from user ID
     * @throws PersistenceException if user ID cannot be retrieved
     */
    public static Function<RequestEnvelope, String> userId() {
        return r -> Optional.ofNullable(r).map(RequestEnvelope::getContext)
                .map(Context::getSystem)
                .map(SystemState::getUser)
                .map(User::getUserId)
                .orElseThrow(() -> new PersistenceException("Could not retrieve user ID from request envelope to generate persistence ID"));
    }

    /**
     * Produces a partition key from the device ID contained in an incoming request.
     * @return partition key derived from device ID
     * @throws PersistenceException if device ID cannot be retrieved
     */
    public static Function<RequestEnvelope, String> deviceId() {
        return r -> Optional.ofNullable(r).map(RequestEnvelope::getContext)
                .map(Context::getSystem)
                .map(SystemState::getDevice)
                .map(Device::getDeviceId)
                .orElseThrow(() -> new PersistenceException("Could not retrieve device ID from request envelope to generate persistence ID"));
    }

}
