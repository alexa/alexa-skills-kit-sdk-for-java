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
import java.io.OutputStream;
import java.util.Map;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Envelope to wrap the response of a {@code SpeechletV2} invocation as well as mutable elements of
 * the {@code SpeechletV2} invocation parameters.
 *
 * @see com.amazon.speech.speechlet.SpeechletResponse
 */
public class SpeechletResponseEnvelope {
    /**
     * A Jackson {@code ObjectMapper} configured for our serialization use case.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private String version;
    private SpeechletResponse response;
    private Map<String, Object> sessionAttributes;

    /**
     * Returns the envelope version.
     *
     * @return version the envelope version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the envelope version.
     *
     * @param version
     *            the envelope version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the {@code SpeechletV2} response.
     *
     * @return the response
     */
    public SpeechletResponse getResponse() {
        return response;
    }

    /**
     * Sets the {@code SpeechletV2} response.
     *
     * @param response
     *            the response to set
     */
    public void setResponse(SpeechletResponse response) {
        this.response = response;
    }

    /**
     * Returns the session attributes to send back to the server.
     *
     * @return the session attributes
     */
    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    /**
     * Sets the session attributes to send back to the server.
     *
     * @param sessionAttributes
     *            the session attributes to set
     */
    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    // ---------------------
    // JSON deserialization

    /**
     * Write a {@code SpeechletResponseEnvelope} to an {@code OutputStream}. The output is encoded
     * using UTF-8.
     *
     * @param out
     *            the OutputStream to write to
     * @throws IOException
     *             if serialization fails
     */
    public void toJson(final OutputStream out) throws IOException {
        OBJECT_MAPPER.writeValue(out, this);
    }

    /**
     * Write a {@code SpeechletResponseEnvelope} as a JSON byte array. The output is encoded using
     * UTF-8.
     *
     * @return the JSON as bytes
     * @throws IOException
     *             if serialization fails
     */
    public byte[] toJsonBytes() throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(this);
    }

    /**
     * Write a {@code SpeechletResponseEnvelope} as a JSON {@code String}.
     *
     * @return the JSON as a String
     * @throws IOException
     *             if serialization fails
     */
    public String toJsonString() throws IOException {
        return OBJECT_MAPPER.writeValueAsString(this);
    }
}
