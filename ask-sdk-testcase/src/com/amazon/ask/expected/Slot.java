package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Slot.
 */
@Getter
@EqualsAndHashCode
public class Slot {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SlotValue slotValue;

    @Builder
    @JsonCreator
    public Slot(@JsonProperty(value = "slotValue") final SlotValue slotValue) {
        this.slotValue = slotValue;
    }
}
