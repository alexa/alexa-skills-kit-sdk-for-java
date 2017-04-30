package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfirmSlotDirective extends DelegateDirective {
    String slotToConfirm;

    public static ConfirmSlotDirective.Builder builder() {
        return new ConfirmSlotDirective.Builder();
    }

    private ConfirmSlotDirective(final ConfirmSlotDirective.Builder builder) {
        super(builder.updatedIntent);
        this.slotToConfirm = builder.slotToConfirm;
    }

    private ConfirmSlotDirective(@JsonProperty("slotToConfirm") String slotToConfirm, @JsonProperty("updatedIntent") Intent updatedIntent) {
        super(updatedIntent);
        this.slotToConfirm = slotToConfirm;
    }

    public String getSlotToConfirm() {
        return slotToConfirm;
    }

    public static class Builder extends DelegateDirective.Builder {
        String slotToConfirm;

        private Builder() {
        }

        public Builder withSlotToConfirm(final String slotToConfirm) {
            this.slotToConfirm = slotToConfirm;
            return this;
        }

        public ConfirmSlotDirective build() {
            return new ConfirmSlotDirective(this);
        }
    }
}
