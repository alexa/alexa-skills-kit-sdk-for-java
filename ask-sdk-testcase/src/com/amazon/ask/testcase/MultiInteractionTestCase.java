package com.amazon.ask.testcase;

import com.amazon.ask.testcase.userinteraction.UserInteraction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Single Interaction Test Case.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiInteractionTestCase extends TestCase {
    private List<UserInteraction> userInteractions;
    private List<DialogExpectation> dialogExpectations;

    @Builder
    @JsonCreator
    public MultiInteractionTestCase(@NonNull @JsonProperty(value = "userInteractions", required = true)
                                        final List<UserInteraction> userInteractions,
                                    @JsonProperty(value = "dialogExpectations")
                                    final List<DialogExpectation> dialogExpectations) {
        super();
        this.userInteractions = userInteractions;
        this.dialogExpectations = dialogExpectations;
    }
}
