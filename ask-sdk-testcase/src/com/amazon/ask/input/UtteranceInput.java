package com.amazon.ask.input;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Utterance Input Type.
 */
@Getter
@JsonTypeName("Utterance")
public class UtteranceInput extends AbstractInput implements Input {
    private final String value;

    @JsonCreator
    @Builder
    public UtteranceInput(@NonNull @JsonProperty(value = "value", required = true) final String value) {
        super(InputType.Utterance.name());
        this.value = value;
    }

    @Override
    public String getType() {
        return type;
    }
}
