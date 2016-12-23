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

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackNearlyFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStartedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStoppedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.NextCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PauseCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PlayCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.playbackcontroller.request.PreviousCommandIssuedRequest;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Base class for requests used for {@code SpeechletV2} invocation.
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        // The sub types that extend the SpeechletRequest class.
        @Type(value = LaunchRequest.class),
        @Type(value = IntentRequest.class),
        @Type(value = SessionStartedRequest.class),
        @Type(value = SessionEndedRequest.class),
        @Type(value = PlaybackStartedRequest.class),
        @Type(value = PlaybackStoppedRequest.class),
        @Type(value = PlaybackNearlyFinishedRequest.class),
        @Type(value = PlaybackFinishedRequest.class),
        @Type(value = PlaybackFailedRequest.class),
        @Type(value = PlayCommandIssuedRequest.class),
        @Type(value = PauseCommandIssuedRequest.class),
        @Type(value = NextCommandIssuedRequest.class),
        @Type(value = PreviousCommandIssuedRequest.class),
        @Type(value = ExceptionEncounteredRequest.class)
})
public abstract class SpeechletRequest {
    private final String requestId;

    @JsonSerialize(using = LocaleJsonSerializer.class)
    @JsonDeserialize(using = LocaleJsonDeserializer.class)
    private final Locale locale;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_8601_FORMAT, timezone = "UTC")
    private final Date timestamp;

    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Private constructor to return a new {@code SpeechletRequest} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code SpeechletRequest}
     */
    protected SpeechletRequest(final SpeechletRequestBuilder builder) {
        requestId = builder.requestId;
        locale = builder.locale;
        timestamp = builder.timestamp;
    }

    /**
     * Constructs a new {@code SpeechletRequest} with a request identifier and timestamp.
     *
     * @param requestId
     *            the unique identifier for the request
     * @param timestamp
     *            the timestamp for the request
     * @param locale
     *            the locale of the request
     */
    protected SpeechletRequest(final String requestId, final Date timestamp, final Locale locale) {
        this.requestId = requestId;
        this.timestamp = (timestamp != null) ? new Date(timestamp.getTime()) : null;
        this.locale = locale;
    }

    /**
     * Returns a unique request identifier. This can be used for logging and correlating events
     * linked to a request.
     *
     * @return the request identifier
     */
    public final String getRequestId() {
        return requestId;
    }

    /**
     * Returns the request timestamp.
     *
     * @return the request timestamp
     */
    public final Date getTimestamp() {
        return (timestamp != null) ? new Date(timestamp.getTime()) : null;
    }

    /**
     * Returns the request locale.
     *
     * @return the request locale
     */
    @JsonInclude(Include.NON_NULL)
    public final Locale getLocale() {
        return locale;
    }

    /**
     * Used to serialize Locale objects as IETF BCP 47 language tags.
     */
    private static final class LocaleJsonSerializer extends JsonSerializer<Locale> {
        @Override
        public void serialize(Locale locale, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString(locale.toLanguageTag());
        }
    }

    /**
     * Used to deserialize Locale objects from IETF BCP 47 language tags.
     */
    private static final class LocaleJsonDeserializer extends JsonDeserializer<Locale> {
        @Override
        public Locale deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            return Locale.forLanguageTag(jp.getValueAsString());
        }
    }

    /**
     * Builder used to construct a new {@code SpeechletRequest}.
     */
    public static abstract class SpeechletRequestBuilder<T extends SpeechletRequestBuilder, S extends SpeechletRequest> {
        private String requestId;
        private Date timestamp = new Date();
        private Locale locale;

        public String getRequestId() {
            return requestId;
        }

        public T withRequestId(final String requestId) {
            this.requestId = requestId;
            return (T) this;
        }

        public T withTimestamp(final Date timestamp) {
            this.timestamp = (timestamp != null) ? new Date(timestamp.getTime()) : null;
            return (T) this;
        }

        public T withLocale(final Locale locale) {
            this.locale = locale;
            return (T) this;
        }

        public abstract S build();
    }
}
