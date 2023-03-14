package com.amazon.ask.testcase;

import com.amazon.ask.expected.DialogExpected;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Dialog Expectation.
 */
@Getter
public class DialogExpectation {
    private final List<DialogExpected> expected;
    private final FrequencyMode frequencyMode;

    @Builder
    @JsonCreator
    public DialogExpectation(@NonNull @JsonProperty(value = "expected", required = true) final List<DialogExpected>
                                         expected, @JsonProperty(value = "frequencyMode")
    final FrequencyMode frequencyMode) {
        this.expected = expected;
        this.frequencyMode = frequencyMode;
    }
}
