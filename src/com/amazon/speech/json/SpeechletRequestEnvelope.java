/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.json;

import java.io.IOException;
import java.io.InputStream;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Envelope to wrap the various parameters of a {@code SpeechletV2} invocation.
 *
 * @param <T>
 *            the specific type of request the envelope is for
 *
 * @see com.amazon.speech.speechlet.SpeechletV2
 * @see com.amazon.speech.speechlet.IntentRequest
 * @see com.amazon.speech.speechlet.Session
 * @see com.amazon.speech.speechlet.Context
 */
public class SpeechletRequestEnvelope<T extends SpeechletRequest> {
    /**
     * A Jackson {@code ObjectMapper} configured for our deserialization use case.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        /*
         * This flag is set to support forward compatibility. We can safely add new fields to
         * request objects, they will just be ignored.
         */
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        OBJECT_MAPPER.registerModule(new SpeechletRequestModule());
    }

    // ----------
    // Attributes

    private final String version;
    private final Session session;
    private final Context context;
    private final T request;

    /**
     * Returns a new builder instance used to construct a new {@code SpeechletRequestEnvelope}.
     *
     * @param <E> SpeechletRequest
     * @return the builder
     */
    public static <E extends SpeechletRequest> Builder<E> builder() {
        return new Builder<E>();
    }

    /**
     * Private constructor to return a new {@code SpeechletRequestEnvelope} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SpeechletRequestEnvelope}.
     */
    private SpeechletRequestEnvelope(final Builder<T> builder) {
        version = builder.version;
        session = builder.session;
        context = builder.context;
        request = builder.request;
    }

    /**
     * Private constructor for JSON serialization.
     *
     * @param version
     *            the version of the request envelope
     * @param session
     *            the session
     * @param context
     *            the context
     * @param request
     *            the speechlet request
     */
    private SpeechletRequestEnvelope(@JsonProperty("version") final String version,
            @JsonProperty("session") final Session session,
            @JsonProperty("context") final Context context,
            @JsonProperty("request") final T request) {
        this.version = version;
        this.session = session;
        this.context = context;
        this.request = request;
    }

    // ---------
    // Accessors

    /**
     * Returns the envelope version.
     *
     * @return version the envelope version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the session, if present. It will only be present if the parameterized type {@code T}
     * is {@link com.amazon.speech.speechlet.IntentRequest},
     * {@link com.amazon.speech.speechlet.LaunchRequest}, or
     * {@link com.amazon.speech.speechlet.SessionEndedRequest}.
     *
     * @return the session, if present
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns the context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Returns the request.
     *
     * @return the request
     */
    public T getRequest() {
        return request;
    }

    // ---------------------
    // JSON deserialization

    /**
     * Read a {@code SpeechletRequestEnvelope} from an {@code InputStream}. The byte stream must be
     * UTF-8 encoded.
     *
     * @param in
     *            the input stream to read from
     * @return the envelope read from the stream
     * @throws IOException
     *             if deserialization fails
     */
    public static SpeechletRequestEnvelope<?> fromJson(final InputStream in) throws IOException {
        return OBJECT_MAPPER.readValue(in, SpeechletRequestEnvelope.class);
    }

    /**
     * Read a {@code SpeechletRequestEnvelope} from a byte array. The byte array must be UTF-8
     * encoded.
     *
     * @param json
     *            the bytes to read from.
     * @return the envelope read from the bytes.
     * @throws IOException
     *             if deserialization fails.
     */
    public static SpeechletRequestEnvelope<?> fromJson(final byte[] json) throws IOException {
        return OBJECT_MAPPER.readValue(json, SpeechletRequestEnvelope.class);
    }

    /**
     * Read a {@code SpeechletRequestEnvelope} from a {@code String}.
     *
     * @param json
     *            the String to read from
     * @return the envelope read from the String
     * @throws IOException
     *             if deserialization fails
     */
    public static SpeechletRequestEnvelope<?> fromJson(final String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, SpeechletRequestEnvelope.class);
    }

    /**
     * Builder used to construct a new {@code SpeechletRequestEnvelope}.
     */
    public static final class Builder<T extends SpeechletRequest> {
        private String version = Sdk.VERSION;
        private Session session;
        private Context context;
        private T request;

        private Builder() {
        }

        public Builder<T> withVersion(final String version) {
            this.version = version;
            return this;
        }

        public Builder<T> withSession(final Session session) {
            this.session = session;
            return this;
        }

        public Builder<T> withContext(final Context context) {
            this.context = context;
            return this;
        }

        public Builder<T> withRequest(final T request) {
            this.request = request;
            return this;
        }

        public SpeechletRequestEnvelope<T> build() {
            return new SpeechletRequestEnvelope<T>(this);
        }
    }
}
