package com.amazon.ask.input;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Test Set Input.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(UtteranceInput.class),
        @JsonSubTypes.Type(AudioInput.class)
})
public interface Input {
    /**
     * Get input type.
     * @return type
     */
    String getType();
}
