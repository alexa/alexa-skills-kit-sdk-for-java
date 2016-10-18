package com.amazon.speech.ui;

public class AudioItem {
    private Stream stream;

    public AudioItem(Stream stream) {
        this.stream = stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public Stream getStream() {
        return this.stream;
    }
}
