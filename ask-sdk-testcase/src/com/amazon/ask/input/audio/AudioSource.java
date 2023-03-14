package com.amazon.ask.input.audio;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Audio Source.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(AudioCatalogSource.class)
})
public interface AudioSource {
    /**
     * Get audio source type.
     * @return type
     */
    String getType();
}
