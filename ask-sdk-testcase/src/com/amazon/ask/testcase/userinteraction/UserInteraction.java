package com.amazon.ask.testcase.userinteraction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract User Interaction.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = DefaultUserInteraction.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefaultUserInteraction.class, name = "Default"),
        @JsonSubTypes.Type(value = DynamicSlotElicitationUserInteraction.class, name = "DynamicSlotElicitation")
})
public abstract class UserInteraction {
}
