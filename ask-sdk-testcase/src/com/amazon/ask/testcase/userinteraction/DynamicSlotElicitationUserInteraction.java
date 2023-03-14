package com.amazon.ask.testcase.userinteraction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Dynamic Slot Elicitation User Interaction.
 */
@Getter
public class DynamicSlotElicitationUserInteraction extends UserInteraction {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<DynamicSlotElicitationTurn> turnSet;

    @Builder
    @JsonCreator
    public DynamicSlotElicitationUserInteraction(@JsonProperty(value = "turnSet", required = true)
                                                 final List<DynamicSlotElicitationTurn> turnSet) {
        super();
        this.turnSet = turnSet;
    }
}
