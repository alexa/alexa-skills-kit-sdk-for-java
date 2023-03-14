package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Transcription Expected.
 */
@Getter
@JsonTypeName("Transcription")
public class TranscriptionExpected extends AbstractExpected implements Expected  {
    private final String value;

    @Builder
    @JsonCreator
    public TranscriptionExpected(@NonNull @JsonProperty(value = "value", required = true) final String value) {
        super(ExpectedType.Transcription.name());
        this.value = value;
    }

    @Override
    public String getType() {
        return type;
    }
}
