package com.amazon.ask.input;

import lombok.RequiredArgsConstructor;

/**
 * Abstract Input.
 */
@SuppressWarnings("checkstyle:visibilitymodifier")
@RequiredArgsConstructor
public abstract class AbstractInput implements Input {
    protected final String type;
}
