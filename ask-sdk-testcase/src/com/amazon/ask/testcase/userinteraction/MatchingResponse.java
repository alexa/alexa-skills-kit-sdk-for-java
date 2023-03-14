package com.amazon.ask.testcase.userinteraction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Matching Response.
 */
@Getter
public class MatchingResponse {
    private final ResponseDialogAct dialogAct;

    @Builder
    @JsonCreator
    public MatchingResponse(@NonNull @JsonProperty(value = "dialogAct", required = true)
                                final ResponseDialogAct dialogAct) {
        this.dialogAct = dialogAct;
    }
}

