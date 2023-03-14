package com.amazon.ask.input.audio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

/**
 * Catalog Audio Source.
 */
@Getter
@JsonTypeName("AudioCatalog")
public class AudioCatalogSource extends AbstractAudioSource implements AudioSource {
    private final String uploadId;
    private final String file;

    @Builder
    @JsonCreator
    public AudioCatalogSource(@JsonProperty(value = "uploadId", required = true) final String uploadId,
                              @JsonProperty(value = "file", required = true) final String file) {
        super(AudioSourceType.AudioCatalog.name());
        this.uploadId = uploadId;
        this.file = file;
    }

    @Override
    public String getType() {
        return type;
    }
}
