package com.amazon.ask.testcase;

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
 * Single Interaction Test Case.
 */
@Getter
public class SingleInteractionTestCase extends TestCase {
    private final Input input;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<Expected> expected;

    @Builder
    @JsonCreator
    public SingleInteractionTestCase(@NonNull @JsonProperty(value = "input", required = true) final Input input,
                                     @JsonProperty(value = "expected") final List<Expected> expected) {
        super();
        this.input = input;
        this.expected = expected;
    }
}
