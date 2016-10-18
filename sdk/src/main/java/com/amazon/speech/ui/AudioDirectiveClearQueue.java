package com.amazon.speech.ui;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AudioPlayer.ClearQueue")
public class AudioDirectiveClearQueue extends AudioDirective {
    // CLEAR_ENQUEUED | CLEAR_ALL
    private String clearBehavior;

    public void setClearBehaviour(String clearBehavior) {
        this.clearBehavior = clearBehavior;
    }

    public String getClearBehaviour() {
        return this.clearBehavior;
    }
}
