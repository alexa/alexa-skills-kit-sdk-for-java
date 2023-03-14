package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
    Resolved entities in Actual NLU result.
 */
@Data
public class ResolvedEntities {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authority;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResolutionStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResolvedEntityValue> values;

    @JsonCreator
    @Builder
    public ResolvedEntities(@JsonProperty(value = "authority") final String authority,
                          @JsonProperty(value = "status") final ResolutionStatus status,
                          @JsonProperty(value = "values") final List<ResolvedEntityValue> values) {

        this.authority = authority;
        this.status = status;
        this.values = values;
    }
}
