package com.amazon.ask.input;

import com.amazon.test.testsets.input.audio.AudioAsset;
import com.amazon.test.testsets.input.audio.AudioSource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Audio Input.
 */
@Getter
@JsonTypeName("Audio")
public class AudioInput extends AbstractInput implements Input {
    private final AudioSource source;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final AudioAsset asset;

    @Builder
    @JsonCreator
    public AudioInput(@NonNull @JsonProperty(value = "source", required = true) final AudioSource source,
                      @JsonProperty("asset") final AudioAsset asset) {
        super(InputType.Audio.name());
        this.source = source;
        this.asset = asset;
    }

    @Override
    public String getType() {
        return type;
    }
}
