package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
    Resolution status in NLU result.
 */
@Data
public class ResolutionStatus {
    private ResolutionStatusValue code;

    @JsonCreator
    @Builder
    public ResolutionStatus(@JsonProperty(value = "code") final ResolutionStatusValue code) {
        this.code = code;
    }
}
