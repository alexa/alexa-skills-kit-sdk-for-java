package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountLinkedBody {
    private final String accessToken;

    public  static Builder builder() {
        return new Builder();
    }

    private AccountLinkedBody(final Builder builder) {
        accessToken = builder.accessToken;
    }

    private AccountLinkedBody(@JsonProperty("accessToken") final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public static final class Builder {
        private String accessToken;

        public Builder withAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AccountLinkedBody build() {
            return new AccountLinkedBody(this);
        }
    }
}
