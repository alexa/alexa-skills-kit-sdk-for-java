package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

/**
 * Intent Expected.
 */
@Getter
@JsonTypeName("Intent")
public class IntentExpected extends AbstractExpected implements Expected {
    private final String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, Slot> slots;

    @Builder
    @JsonCreator
    public IntentExpected(@NonNull @JsonProperty(value = "name", required = true) final String name,
                          @JsonProperty("slots") final Map<String, Slot> slots) {
        super(ExpectedType.Intent.name());
        this.name = name;
        this.slots = slots;
    }

    @Override
    public String getType() {
        return type;
    }
}
