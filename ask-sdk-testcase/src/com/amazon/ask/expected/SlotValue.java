package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * SlotValue.
 */
@Getter
@EqualsAndHashCode
public class SlotValue {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SlotValue> values;
    private final SlotValueType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final RecognizedResolutions resolutions;

    @Builder
    @JsonCreator
    public SlotValue(@NonNull @JsonProperty(value = "type", required = true) final SlotValueType type,
                     @JsonProperty(value = "value") final String value,
                     @JsonProperty(value = "values") final List<SlotValue> values,
                     @JsonProperty(value = "resolutions") final RecognizedResolutions resolutions) {
        this.type = type;
        this.value = value;
        this.values = values;
        this.resolutions = resolutions;
    }
}
