package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Entity values in actual NLU result.
 */
@Getter
public class ResolvedEntityValue {
    private final String name;
    private final String id;

    @JsonCreator
    @Builder
    public ResolvedEntityValue(@JsonProperty(value = "name") final String name,
                            @JsonProperty(value = "id") final String id) {

        this.name = name;
        this.id = id;
    }
}
