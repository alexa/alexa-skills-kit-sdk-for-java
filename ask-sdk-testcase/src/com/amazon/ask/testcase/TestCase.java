package com.amazon.ask.testcase;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract Test Case.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = SingleInteractionTestCase.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleInteractionTestCase.class, name = "SingleInteraction"),
        @JsonSubTypes.Type(value = MultiInteractionTestCase.class, name = "MultiInteraction")
})
public abstract class TestCase {
}
