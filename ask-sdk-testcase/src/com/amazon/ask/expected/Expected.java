package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Expected Interface.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IntentExpected.class, name = "Intent"),
        @JsonSubTypes.Type(value = TranscriptionExpected.class, name = "Transcription"),
        @JsonSubTypes.Type(value = SimulationOutputExpected.class, name = "SimulationOutput")
})
public interface Expected {
    /**
     * Get expected type.
     * @return type
     */
    String getType();
}
