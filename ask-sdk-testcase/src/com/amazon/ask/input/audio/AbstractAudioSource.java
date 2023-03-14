package com.amazon.ask.input.audio;

import lombok.RequiredArgsConstructor;

/**
 * Abstract Audio Source.
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
@RequiredArgsConstructor
public abstract class AbstractAudioSource implements AudioSource {
    protected final String type;
}
