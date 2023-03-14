package com.amazon.ask.expected.valuematcher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Equal Value Matcher.
 */
@Getter
public class IncludeValueMatcher extends ValueMatcher {
    private final String dataSource;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final JsonNode value;

    @Builder
    @JsonCreator
    public IncludeValueMatcher(@JsonProperty(value = "dataSource") final String dataSource,
                               @NonNull @JsonProperty(value = "value", required = true) final JsonNode value) {
        this.dataSource = dataSource;
        this.value = value;
    }
}
