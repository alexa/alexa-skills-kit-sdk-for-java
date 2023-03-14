package com.amazon.ask.input.audio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Audio Asset.
 */
@Getter
public class AudioAsset {
    private final String downloadUrl;
    private final String expiryTime;

    @Builder
    @JsonCreator
    public AudioAsset(@NonNull @JsonProperty(value = "downloadUrl", required = true) final String downloadUrl,
                      @NonNull @JsonProperty(value = "expiryTime", required = true) final String expiryTime) {
        this.downloadUrl = downloadUrl;
        this.expiryTime = expiryTime;
    }
}
