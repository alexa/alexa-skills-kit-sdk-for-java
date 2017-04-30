package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

public class DelegateDirective extends Directive {
    Intent updatedIntent;

    public static DelegateDirective.Builder builder() {
        return new DelegateDirective.Builder();
    }

    protected DelegateDirective(final DelegateDirective.Builder builder) {
        this.updatedIntent = builder.updatedIntent;
    }

    protected DelegateDirective(@JsonProperty("updatedIntent") Intent updatedIntent) {
        this.updatedIntent = updatedIntent;
    }

    public Intent getUpdatedIntent() {
        return updatedIntent;
    }

    public static class Builder {
        Intent updatedIntent;

        protected Builder() {
        }

        public Builder withUpdatedIntent(final Intent updatedIntent) {
            this.updatedIntent = updatedIntent;
            return this;
        }

        public DelegateDirective build() {
            return new DelegateDirective(this);
        }
    }
}
