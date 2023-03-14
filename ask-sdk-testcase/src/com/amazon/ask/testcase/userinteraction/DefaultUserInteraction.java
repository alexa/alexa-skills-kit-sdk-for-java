package com.amazon.ask.testcase.userinteraction;

import com.amazon.ask.expected.Expected;
import com.amazon.ask.input.Input;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Default User Interaction.
 */
@Getter
public class DefaultUserInteraction extends UserInteraction {
    private final Input input;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<Expected> expected;

    @Builder
    @JsonCreator
    public DefaultUserInteraction(@NonNull @JsonProperty(value = "input", required = true) final Input input,
                                  @JsonProperty(value = "expected") final List<Expected> expected) {
        super();
        this.input = input;
        this.expected = expected;
    }
}
