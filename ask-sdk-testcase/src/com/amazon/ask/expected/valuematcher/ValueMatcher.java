package com.amazon.ask.expected.valuematcher;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract Value Matcher.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = EqualValueMatcher.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EqualValueMatcher.class, name = "Equal"),
        @JsonSubTypes.Type(value = NotEqualValueMatcher.class, name = "NotEqual"),
        @JsonSubTypes.Type(value = IncludeValueMatcher.class, name = "Include"),
        @JsonSubTypes.Type(value = NotIncludeValueMatcher.class, name = "NotInclude"),
        @JsonSubTypes.Type(value = RegexValueMatcher.class, name = "Regex"),
})
public abstract class ValueMatcher {
}
