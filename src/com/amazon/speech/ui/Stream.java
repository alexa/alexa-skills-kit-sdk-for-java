package com.amazon.speech.ui;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

public class Stream {
    private String token;
    private String url;
    private int offsetInMilliseconds;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setOffsetInMilliseconds(int offsetInMilliseconds) {
        this.offsetInMilliseconds = offsetInMilliseconds;
    }

    public int getOffsetInMilliseconds() {
        return this.offsetInMilliseconds;
    }
}
