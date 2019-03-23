/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.persistence;

import java.util.Map;
import java.util.Optional;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.exception.PersistenceException;

/**
 * Persistence adapters allow an {@link com.amazon.ask.attributes.AttributesManager} to store skill attributes to a
 * persistence layer. The adapter encapsulates all initialization, get, and save logic specific to the persistence
 * implementation.
 */
public interface PersistenceAdapter {

    /**
     * Retrieves attributes from persistence. A {@link RequestEnvelope} is passed to the adapter so current request
     * parameters can be used as an attribute key.
     *
     * @param envelope the current request envelope
     * @return {@link Optional} containing existing attributes if they exist, or empty if not
     * @throws PersistenceException if attributes could not be retrieved due to an error
     */
    Optional<Map<String, Object>> getAttributes(RequestEnvelope envelope) throws PersistenceException;

    /**
     * Saves attributes to persistence. A {@link RequestEnvelope} is passed to the adapter so current request parameters
     * can be used as an attribute key.
     *
     * @param envelope the current request envelope
     * @param attributes attributes to persist
     * @throws PersistenceException if attributes could not be saved due to an error
     */
    void saveAttributes(RequestEnvelope envelope, Map<String, Object> attributes) throws PersistenceException;

    /**
     * Deletes attributes from persistence. A {@link RequestEnvelope} is passed to the adapter so current request parameters
     * can be used as an attribute key.
     *
     * @param envelope the current request envelope.
     * @throws PersistenceException if attributes could not be deleted due to an error.
     */
    void deleteAttributes(RequestEnvelope envelope) throws PersistenceException;

}
