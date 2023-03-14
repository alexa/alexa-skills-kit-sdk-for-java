package com.amazon.ask.expected;

import lombok.RequiredArgsConstructor;

/**
 * Abstract Expected.
 */
@SuppressWarnings("checkstyle:visibilitymodifier")
@RequiredArgsConstructor
public abstract class AbstractExpected implements Expected {
    protected final String type;
}
