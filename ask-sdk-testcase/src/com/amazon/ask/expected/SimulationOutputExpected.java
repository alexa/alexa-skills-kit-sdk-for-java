package com.amazon.ask.expected;

import com.amazon.ask.expected.valuematcher.ValueMatcher;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Simulation Output Expected.
 */
@Getter
@JsonTypeName("SimulationOutput")
public class SimulationOutputExpected extends AbstractExpected implements Expected {
    private final List<ValueMatcher> valueMatchers;

    @Builder
    @JsonCreator
    public SimulationOutputExpected(@NonNull @JsonProperty(value = "valueMatchers", required = true)
                                        final List<ValueMatcher> valueMatchers) {
        super(ExpectedType.SimulationOutput.name());
        this.valueMatchers = valueMatchers;
    }

    @Override
    public String getType() {
        return type;
    }
}

