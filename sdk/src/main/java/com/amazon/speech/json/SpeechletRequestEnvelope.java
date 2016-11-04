/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Envelope to wrap the various parameters of a {@code Speechlet} invocation.
 *
 * @see com.amazon.speech.speechlet.Speechlet
 * @see com.amazon.speech.speechlet.IntentRequest
 * @see com.amazon.speech.speechlet.Session
 */
public final class SpeechletRequestEnvelope {
    /**
     * A Jackson {@code ObjectMapper} configured for our deserialization use case.
     */
    private static final Logger log = LoggerFactory.getLogger(SpeechletRequestEnvelope.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        /*
         * This flag is set to support forward compatibility. We can safely add new fields to
         * request objects, they will just be ignored.
         */
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    // ----------
    // Attributes

    private final String version;
    private final Session session;
    private final SpeechletRequest request;
    private final Context context;
    //TODO add Content with AudoPlayer and System objects and parsing

    /**
     * Returns a new builder instance used to construct a new {@code SpeechletRequestEnvelope}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code SpeechletRequestEnvelope} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SpeechletRequestEnvelope}.
     */
    private SpeechletRequestEnvelope(final Builder builder) {
        version = builder.version;
        session = builder.session;
        request = builder.request;
        context = builder.context;
    }

    /**
     * Private constructor for JSON serialization.
     *
     * @param version
     *            the version of the request envelope
     * @param session
     *            the session
     * @param request
     *            the speechlet request
     * @param context
     *            the speechlet request

     */
    private SpeechletRequestEnvelope(@JsonProperty("version") final String version,
            @JsonProperty("session") final Session session,
            @JsonProperty("context") final Context context,
            @JsonProperty("request") final SpeechletRequest request) {
        this.version = version;
        this.session = session;
        this.request = request;
        this.context = context;
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
     * Returns the {@code Speechlet} session.
     *
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns the {@code Speechlet} request.
     *
     * @return the request
     */
    public SpeechletRequest getRequest() {
        return request;
    }

    /**
     * returns the context object
     * @return the context
     */
    public Context getContext() { return context; }
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
    public static SpeechletRequestEnvelope fromJson(final InputStream in) throws IOException {
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
    public static SpeechletRequestEnvelope fromJson(final byte[] json) throws IOException {
        log.debug("Mapping json string to class:"+new String(json));
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
    public static SpeechletRequestEnvelope fromJson(final String json) throws IOException {
        System.out.println("Mapping json string to class:"+json);
        return OBJECT_MAPPER.readValue(json, SpeechletRequestEnvelope.class);
    }

    /**
     * Builder used to construct a new {@code SpeechletRequestEnvelope}.
     */
    public static final class Builder {
        private String version = Sdk.VERSION;
        private Session session;
        private SpeechletRequest request;
        private Context context;

        private Builder() {
        }

        public Builder withVersion(final String version) {
            this.version = version;
            return this;
        }

        public Builder withSession(final Session session) {
            this.session = session;
            return this;
        }

        public Builder withRequest(final SpeechletRequest request) {
            this.request = request;
            return this;
        }

        public Builder withContext(final Context context) {
            this.context = context;
            return this;
        }


        public SpeechletRequestEnvelope build() {
            return new SpeechletRequestEnvelope(this);
        }
    }
}
