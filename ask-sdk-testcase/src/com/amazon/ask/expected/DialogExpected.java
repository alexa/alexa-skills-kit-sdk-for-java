package com.amazon.ask.expected;

import com.amazon.ask.expected.valuematcher.ValueMatcher;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
  Expected under DialogExpectation.
 */
@Getter
public class DialogExpected {
    private final List<ValueMatcher> valueMatchers;

    @Builder
    @JsonCreator
    public DialogExpected(@NonNull @JsonProperty(value = "valueMatchers", required = true)
                              final List<ValueMatcher> valueMatchers) {
        this.valueMatchers = valueMatchers;
    }
}
