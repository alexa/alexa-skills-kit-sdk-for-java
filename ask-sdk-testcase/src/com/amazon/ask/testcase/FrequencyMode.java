package com.amazon.ask.testcase;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract Frequency Mode.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = AtLeastOnceFrequencyMode.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AtLeastOnceFrequencyMode.class, name = "AtLeastOnce")
})
public abstract class FrequencyMode {
}
