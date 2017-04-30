package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfirmIntentDirective extends DelegateDirective {

    public static ConfirmIntentDirective.Builder builder() {
        return new ConfirmIntentDirective.Builder();
    }

    private ConfirmIntentDirective(final ConfirmIntentDirective.Builder builder) {
        super(builder.updatedIntent);
    }

    private ConfirmIntentDirective(@JsonProperty("updatedIntent") Intent updatedIntent) {
        super(updatedIntent);
    }

    public static class Builder extends DelegateDirective.Builder {
        private Builder() {
        }

        public ConfirmIntentDirective build() {
            return new ConfirmIntentDirective(this);
        }
    }
}
