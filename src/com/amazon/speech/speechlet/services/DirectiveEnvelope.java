/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Envelope to wrap the directive of a {@code DirectiveService} invocation as well as mutable elements of
 * the {@code DefaultDirectiveService} invocation parameters.
 */
public class DirectiveEnvelope {
    /**
     * A Jackson {@code ObjectMapper} configured for our serialization use case.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private DirectiveEnvelopeHeader header = null;
    private SpeakDirective directive = null;

    /**
     * Constructs a DirectiveEnvelope.
     */
    public DirectiveEnvelope() {

    }

    /**
     * Returns a new builder instance used to construct a new {@code DirectiveEnvelope}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code DirectiveEnvelope} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code DirectiveEnvelope}
     */
    private DirectiveEnvelope(final Builder builder) {
        header = builder.header;
        directive = builder.directive;
    }

    /**
     * Returns the header.
     *
     * @return head the header of the directive
     *
     */
    public DirectiveEnvelopeHeader getHeader() {
        return header;
    }

    /**
     * Returns the {@code SpeakDirective}.
     *
     * @return the directive
     */
    public SpeakDirective getDirective() {
        return directive;
    }

    /**
     * Builder used to construct a new {@code DirectiveEnvelope}.
     */
    public static final class Builder {
        private DirectiveEnvelopeHeader header;
        private SpeakDirective directive;

        public Builder withHeader(final DirectiveEnvelopeHeader header) {
            this.header = header;
            return this;
        }

        public Builder withDirective(final SpeakDirective directive) {
            this.directive = directive;
            return this;
        }

        public DirectiveEnvelope build() {
            return new DirectiveEnvelope(this);
        }

    }

    // ---------------------
    // JSON deserialization

    /**
     * Write a {@code DirectiveEnvelope} to an {@code OutputStream}. The output is encoded
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
     * Write a {@code DirectiveEnvelope} as a JSON byte array. The output is encoded using
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
