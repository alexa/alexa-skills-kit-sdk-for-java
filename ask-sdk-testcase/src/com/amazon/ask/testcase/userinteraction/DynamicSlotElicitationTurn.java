package com.amazon.ask.testcase.userinteraction;

import com.amazon.ask.expected.Expected;
import com.amazon.ask.input.Input;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Dynamic Slot Elicitation Turn.
 */
@Getter
public class DynamicSlotElicitationTurn {
    private final Input input;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<Expected> expected;

    private final MatchingResponse matchingResponse;

    @Builder
    public DynamicSlotElicitationTurn(@NonNull @JsonProperty(value = "input", required = true) final Input input,
                                      @JsonProperty(value = "expected") final List<Expected> expected,
                                      @NonNull @JsonProperty(value = "matchingResponse", required = true)
                                      final MatchingResponse matchingResponse) {
        this.input = input;
        this.expected = expected;
        this.matchingResponse = matchingResponse;
    }
}

