package com.amazon.ask.expected;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SlotValue type.
 */
public enum SlotValueType {
    @JsonProperty("List") List,
    @JsonProperty("Simple") Simple;
}
