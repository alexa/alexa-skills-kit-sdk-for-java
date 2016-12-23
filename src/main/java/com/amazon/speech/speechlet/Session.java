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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * A {@code Session} represents a single execution of a {@code SpeechletV2} by a user.
 * </p>
 *
 * <p>
 * Multiple executions of a {@code SpeechletV2} are tied to separate {@code Session}s with different
 * identifiers. If the user explicitly exits the {@code SpeechletV2} and restarts it immediately
 * after, the new execution of the {@code SpeechletV2} will be associated with a new
 * {@code Session}, regardless of how much time elapsed between the two runs.
 * </p>
 *
 * <p>
 * A {@code Session} is tied to the specific user registered to the device initiating the
 * {@code SpeechletV2} {@code Session}. A {@code Session} provides state that is maintained between
 * the multiple invocations of a {@code SpeechletV2}.
 * </p>
 *
 * <p>
 * The attributes of a {@code Session} are simple key-value pairs that are maintained between
 * invocations. You can use them to store lightweight data. They are comparable to session cookies
 * that you would use in a system where the client is a browser. Ending the session by closing the
 * browser would clear those cookies.
 * </p>
 *
 * <p>
 * Attributes can be modified within a {@code SpeechletV2} invocation, but not after the
 * {@code SpeechletV2} method has returned. The modified attributes are sent back with the response
 * and kept for the next {@code SpeechletV2} invocation. The one exception is the {@link
 * SpeechletV2#onSessionEnded(SpeechletRequestEnvelope)}, as the
 * {@code Session} and its attributes are discarded immediately after.
 * </p>
 *
 * <p>
 * If the {@code SpeechletV2} throws an exception, modifications to the attributes up to that point
 * are applied, even if the request was not processed successfully.
 * </p>
 *
 * <p>
 * Because a {@code SpeechletV2} is a cloud-based service, the life-cycle of the actual session
 * object passed in the {@code SpeechletV2} methods is unrelated to the Alexa skill execution as
 * experienced by the user interacting with the speech-enabled device. A {@code SpeechletV2}
 * receives a new {@code Session} instance for each {@code SpeechletV2} invocation. The identifier
 * of the {@code Session} is what uniquely identifies it between invocations. References to a
 * {@code Session} should never be stored as its contents is invalid after returning from a
 * {@code SpeechletV2} method.
 * </p>
 *
 * @see SpeechletV2
 * @see SpeechletException
 */
public class Session {
    private final boolean isNew;
    private final String sessionId;
    private final Application application;
    private final Map<String, Object> attributes;
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
    private Session(final Builder builder) {
        isNew = builder.isNew;
        sessionId = builder.sessionId;
        application = builder.application;
        attributes = builder.attributes;
        user = builder.user;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param isNew
     *            {@code true} if this is a new session; otherwise, {@code false} if this is a
     *            subsequent request within an existing session
     * @param sessionId
     *            a unique identifier for the session
     * @param application
     *            the application
     * @param attributes
     *            all the session attributes
     * @param user
     *            the user associated with this session
     */
    private Session(@JsonProperty("new") final boolean isNew,
            @JsonProperty("sessionId") final String sessionId,
            @JsonProperty("application") final Application application,
            @JsonProperty("attributes") final Map<String, Object> attributes,
            @JsonProperty("user") final User user) {
        this.isNew = isNew;
        this.sessionId = sessionId;
        this.application = application;

        if (attributes != null) {
            this.attributes = attributes;
        } else {
            this.attributes = new HashMap<String, Object>();
        }

        this.user = user;
    }

    /**
     * This is only for Jackson to figure out what default values are (e.g. isNew = false).
     */
    private Session() {
        this.isNew = false;
        this.sessionId = null;
        this.application = null;
        this.attributes = new HashMap<String, Object>();
        this.user = null;
    }

    /**
     * Return whether this is a new session, meaning the current request is the initial request
     * starting the speechlet.
     *
     * @return {@code true} if this is a new session; otherwise, {@code false} if this is a
     *         subsequent request within an existing session
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Returns the session Id.
     *
     * @return a unique identifier for the session
     */
    public String getSessionId() {
        return sessionId;
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
     * Returns all the session attributes.
     *
     * @return a mutable map of all the attributes that may be altered directly
     */
    @JsonInclude(Include.NON_EMPTY)
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Returns the attribute associated with the provided name.
     *
     * @param name
     *            the name of the attribute to retrieve
     * @return the value or {@code null}
     */
    public Object getAttribute(final String name) {
        return attributes.get(name);
    }

    /**
     * Add or modify the attribute with the provided name.
     *
     * @param name
     *            the name of the attribute to set
     * @param value
     *            the new value for the attribute
     */
    public void setAttribute(final String name, final Object value) {
        attributes.put(name, value);
    }

    /**
     * Remove the attribute associated with the provided name.
     *
     * @param name
     *            the name of the attribute to remove
     */
    public void removeAttribute(final String name) {
        attributes.remove(name);
    }

    /**
     * Returns the user associated with this session.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Builder used to construct a new {@code Session}.
     */
    public static final class Builder {
        private boolean isNew = false;
        private String sessionId;
        private Application application;
        private Map<String, Object> attributes = new HashMap<>();
        private User user;

        private Builder() {
        }

        public Builder withIsNew(final boolean isNew) {
            this.isNew = isNew;
            return this;
        }

        public Builder withSessionId(final String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder withApplication(final Application application) {
            this.application = application;
            return this;
        }

        public Builder withAttributes(final Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder withUser(final User user) {
            this.user = user;
            return this;
        }

        public Session build() {
            Validate.notBlank(sessionId, "SessionId must be defined");
            return new Session(this);
        }
    }
}
