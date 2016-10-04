package com.amazon.speech.ui;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AudioPlayer.Play")
public class AudioDirectivePlay extends AudioDirective {
    private String playBehavior = "REPLACE_ALL";
    private AudioItem audioItem;

    public AudioDirectivePlay(AudioItem audioItem) {
        this.audioItem = audioItem;
    }

    public void setAudioItem(AudioItem audioItem) {
        this.audioItem = audioItem;
    }

    public AudioItem getAudioItem() {
        return this.audioItem;
    }

    public void setPlayBehaviour(String playBehavior) {
        this.playBehavior = playBehavior;
    }

    public String getPlayBehaviour() {
        return this.playBehavior;
    }
}
