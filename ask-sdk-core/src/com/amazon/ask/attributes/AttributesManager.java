/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes;

import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Provider for attributes that can be stored on three levels: request, session and persistence.
 */
public class AttributesManager {

    private static Logger logger = getLogger(AttributesManager.class);

    protected final RequestEnvelope requestEnvelope;
    protected final PersistenceAdapter persistenceAdapter;

    protected Map<String, Object> sessionAttributes;
    protected Map<String, Object> persistentAttributes;
    protected Map<String, Object> requestAttributes;

    protected boolean persistenceAttributesSet;

    protected AttributesManager(PersistenceAdapter persistenceAdapter, RequestEnvelope requestEnvelope) {
        this.persistenceAdapter = persistenceAdapter;
        this.requestEnvelope = requestEnvelope;
        this.requestAttributes = Collections.emptyMap();

        if (requestEnvelope.getSession() != null) {
            Map<String, Object> attributes = requestEnvelope.getSession().getAttributes() != null ?
                    requestEnvelope.getSession().getAttributes() :
                    new HashMap<>();
            this.sessionAttributes = attributes;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> getSessionAttributes() {
        if (requestEnvelope.getSession() == null) {
            throw new IllegalStateException("Attempting to read session attributes from out of session request");
        }
        return sessionAttributes;
    }

    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        if (requestEnvelope.getSession() == null) {
            throw new IllegalStateException("Attempting to set session attributes for out of session request");
        }
        this.sessionAttributes = sessionAttributes;
    }

    public Map<String, Object> getPersistentAttributes() {
        Request request = requestEnvelope.getRequest();
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to read persistence attributes without configured persistence adapter");
        }
        if (!persistenceAttributesSet) {
            Optional<Map<String, Object>> retrievedAttributes = persistenceAdapter.getAttributes(requestEnvelope);
            if (retrievedAttributes.isPresent()) {
                logger.debug("[{}] Found existing persistence attributes", request.getRequestId());
                persistentAttributes = retrievedAttributes.get();
            } else {
                logger.debug("[{}] No existing persistence attributes", request.getRequestId());
                persistentAttributes = Collections.emptyMap();
            }
            persistenceAttributesSet = true;
        }
        return persistentAttributes;
    }

    public void setPersistentAttributes(Map<String, Object> persistentAttributes) {
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to set persistence attributes without configured persistence adapter");
        }
        this.persistentAttributes = persistentAttributes;
        persistenceAttributesSet = true;
    }

    public Map<String, Object> getRequestAttributes() {
        return requestAttributes;
    }

    public void setRequestAttributes(Map<String, Object> requestAttributes) {
        this.requestAttributes = requestAttributes;
    }

    public void savePersistentAttributes() {
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to save persistence attributes without configured persistence adapter");
        }
        if (persistenceAttributesSet) {
            persistenceAdapter.saveAttributes(requestEnvelope, persistentAttributes);
        }
    }

    public static final class Builder {
        private PersistenceAdapter persistenceAdapter;
        private RequestEnvelope requestEnvelope;

        private Builder() {
        }

        public Builder withPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        public Builder withRequestEnvelope(RequestEnvelope requestEnvelope) {
            this.requestEnvelope = requestEnvelope;
            return this;
        }

        public AttributesManager build() {
            return new AttributesManager(persistenceAdapter, requestEnvelope);
        }
    }
}
