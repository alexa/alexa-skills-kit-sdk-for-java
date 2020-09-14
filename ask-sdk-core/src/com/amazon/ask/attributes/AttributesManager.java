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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Manager for all skill attributes. This manager is passed to request handlers, request and response interceptors,
 * and exception handlers to expose attribute retrieval and saving operations.
 * <br><br>
 * Skill attributes can be stored on three levels:
 * <ul>
 * <li>
 * Request: Request attributes only last within a single request processing lifecycle. Request attributes are initially
 *          empty when a request comes in, and are discarded once a response has been produced.
 * </li>
 * <li>
 * Session: Session attributes persist throughout the lifespan of the current skill session. Session attributes are
 *          available for use with any in-session request. Any attributes set during the request processing lifecycle
 *          are sent back to the Alexa service and provided via the next request in the same session.
 * </li>
 * <li>
 * Persistent: Persistent attributes persist beyond the lifecycle of the current session. How these attributes are
 *             stored, including key scope (user ID or device ID), TTL, and storage layer depends on skill configuration
 *             and/or the persistence adapter being used. If no persistence adapter is configured persistent attributes
 *             cannot be used.
 * </li></ul>
 */
public class AttributesManager {

    /**
     * Logger to log information for debugging purposes.
     */
    private static Logger logger = getLogger(AttributesManager.class);

    /**
     * Request envelope object which encapsulates Request, Session, context and the current version.
     */
    protected final RequestEnvelope requestEnvelope;

    /**
     * Adapter layer used to persist Skill data.
     */
    protected final PersistenceAdapter persistenceAdapter;

    /**
     * Data stored in session as key-value pairs.
     */
    protected Map<String, Object> sessionAttributes;

    /**
     * Data stored in persistence layer (Eg: AWS DynamoDB).
     */
    protected Map<String, Object> persistentAttributes;

    /**
     * Request attributes.
     */
    protected Map<String, Object> requestAttributes;

    /**
     * Check to see if persistence attributes have been set.
     */
    protected boolean persistenceAttributesSet;

    /**
     * Constructor for AttributesManager.
     * @param persistenceAdapter Adapter layer used to persist Skill data.
     * @param requestEnvelope Request envelope object which encapsulates Request, Session, context and the current version.
     */
    protected AttributesManager(final PersistenceAdapter persistenceAdapter, final RequestEnvelope requestEnvelope) {
        this.persistenceAdapter = persistenceAdapter;
        this.requestEnvelope = requestEnvelope;
        this.requestAttributes = new HashMap<>();

        if (requestEnvelope.getSession() != null) {
            Map<String, Object> attributes = requestEnvelope.getSession().getAttributes() != null
                    ? requestEnvelope.getSession().getAttributes() : new HashMap<>();
            this.sessionAttributes = attributes;
        }
    }

    /**
     * Static builder method which returns an instance of Builder.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Retrieves current session attributes. If existing session attributes are present, they will be contained
     * in the returned map. If not, an empty map will be returned. Modifications to this map will be saved back to
     * the session. An exception is thrown if this method is called while processing an out of session request.
     *
     * @return map containing existing session attributes, or an empty map if none exist
     * @throws IllegalStateException if attempting to retrieve session attributes from an out of session request
     */
    public Map<String, Object> getSessionAttributes() {
        if (requestEnvelope.getSession() == null) {
            throw new IllegalStateException("Attempting to read session attributes from out of session request");
        }
        return sessionAttributes;
    }

    /**
     * Sets session attributes, replacing any existing attributes already present in the session. An exception is thrown
     * if this method is called while processing an out of session request. Use this method when bulk replacing attributes
     * is desired.
     *
     * @param sessionAttributes session attributes to set
     * @throws IllegalStateException if attempting to retrieve session attributes from an out of session request
     */
    public void setSessionAttributes(final Map<String, Object> sessionAttributes) {
        if (requestEnvelope.getSession() == null) {
            throw new IllegalStateException("Attempting to set session attributes for out of session request");
        }
        this.sessionAttributes = sessionAttributes;
    }

    /**
     * Retrieves current persistence attributes. If existing persistence attributes are present, they will be contained
     * in the returned map. If not, an empty map will be returned. Modifications to this map will not be persisted
     * back unless {@link #savePersistentAttributes()} is called. An exception is thrown if this method is called
     * when a {@link PersistenceAdapter} is not configured on the SDK.
     *
     * @return map containing existing persistence attributes, or an empty map if none exist
     * @throws IllegalStateException if no {@link PersistenceAdapter} is configured
     */
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
                persistentAttributes = new HashMap<>();
            }
            persistenceAttributesSet = true;
        }
        return persistentAttributes;
    }

    /**
     * Sets persistent attributes. The attributes set using this method will replace any existing persistent attributes,
     * but the changes will not be persisted back until {@link #savePersistentAttributes()} is called. Use this method
     * when bulk replacing attributes is desired. An exception is thrown if this method is called when a
     * {@link PersistenceAdapter} is not configured on the SDK.
     *
     * @param persistentAttributes persistent attributes to set
     * @throws IllegalStateException if no {@link PersistenceAdapter} is configured
     */
    public void setPersistentAttributes(final Map<String, Object> persistentAttributes) {
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to set persistence attributes without configured persistence adapter");
        }
        this.persistentAttributes = persistentAttributes;
        persistenceAttributesSet = true;
    }

    /**
     * Retrieves current request attributes. If existing request attributes are present, they will be contained
     * in the returned map. If not, an empty map will be returned.
     *
     * @return map containing existing request attributes, or an empty map if none exist
     */
    public Map<String, Object> getRequestAttributes() {
        return requestAttributes;
    }

    /**
     * Sets request attributes. This attributes set using this method will replace any existing request attributes.
     * Use this method when bulk replacing attributes is desired.
     *
     * @param requestAttributes request attributes to set
     */
    public void setRequestAttributes(final Map<String, Object> requestAttributes) {
        this.requestAttributes = requestAttributes;
    }

    /**
     * Saves the current persistent attribute state back to the persistence layer. This method should only be called
     * when all attribute modifications are complete, and generally should only be called once since to avoid
     * unnecessary writes to the persistence tier. An exception is thrown if this method is called when a
     * {@link PersistenceAdapter} is not configured on the SDK.
     *
     * @throws IllegalStateException if no {@link PersistenceAdapter} is configured
     */
    public void savePersistentAttributes() {
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to save persistence attributes without configured persistence adapter");
        }
        if (persistenceAttributesSet) {
            persistenceAdapter.saveAttributes(requestEnvelope, persistentAttributes);
        }
    }

    /**
     * Deletes the persistent attributes from the persistence layer. An exception is thrown if this method is called when a
     * {@link PersistenceAdapter} is not configured on the SDK.
     *
     * @throws IllegalStateException if no {@link PersistenceAdapter} is configured
     */
    public void deletePersistentAttributes() {
        if (persistenceAdapter == null) {
            throw new IllegalStateException("Attempting to delete persistence attributes without a configured persistence adapter");
        }
        persistenceAdapter.deleteAttributes(requestEnvelope);
        persistentAttributes = null;
        persistenceAttributesSet = false;
    }

    /**
     * Attributes Manager Builder class.
     */
    public static final class Builder {

        /**
         * Adapter layer used to persist Skill data.
         */
        private PersistenceAdapter persistenceAdapter;

        /**
         * Request envelope object which encapsulates Request, Session, context and the current version.
         */
        private RequestEnvelope requestEnvelope;

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Adds PersistenceAdapter to AttributesManager.
         * @param persistenceAdapter persistence adapter.
         * @return {@link Builder}.
         */
        public Builder withPersistenceAdapter(final PersistenceAdapter persistenceAdapter) {
            this.persistenceAdapter = persistenceAdapter;
            return this;
        }

        /**
         * Adds RequestEnvelope to AttributesManager.
         * @param requestEnvelope request envelope.
         * @return {@link Builder}.
         */
        public Builder withRequestEnvelope(final RequestEnvelope requestEnvelope) {
            this.requestEnvelope = requestEnvelope;
            return this;
        }

        /**
         * Builds an instance of AttributesManager with the provided configuration.
         * @return {@link AttributesManager}.
         */
        public AttributesManager build() {
            return new AttributesManager(persistenceAdapter, requestEnvelope);
        }
    }
}
