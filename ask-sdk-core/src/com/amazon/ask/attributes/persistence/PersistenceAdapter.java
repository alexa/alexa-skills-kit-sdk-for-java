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
 * Persistence adapter is responsible for storing and retrieving attributes from a persistence layer.
 */
public interface PersistenceAdapter {

    Optional<Map<String, Object>> getAttributes(RequestEnvelope envelope) throws PersistenceException;

    void saveAttributes(RequestEnvelope envelope, Map<String, Object> attributes) throws PersistenceException;

}
