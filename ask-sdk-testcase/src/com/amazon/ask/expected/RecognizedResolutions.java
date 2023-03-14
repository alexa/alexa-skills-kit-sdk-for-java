package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Resolutions in actual NLU result.
 */
@Getter
public class RecognizedResolutions {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ResolvedEntities> resolutionsPerAuthority;

    @JsonCreator
    @Builder
    public RecognizedResolutions(
            @JsonProperty(value = "resolutionsPerAuthority") final List<ResolvedEntities> resolutionsPerAuthority) {
        this.resolutionsPerAuthority = resolutionsPerAuthority;
    }
}
