package com.amazon.speech.speechlet.interfaces.dialog;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ElicitSlotDirective extends DelegateDirective {
    String slotToElicit;

    public static ElicitSlotDirective.Builder builder() {
        return new ElicitSlotDirective.Builder();
    }

    private ElicitSlotDirective(final ElicitSlotDirective.Builder builder) {
        super(builder.updatedIntent);
        this.slotToElicit = builder.slotToElicit;
    }

    private ElicitSlotDirective(@JsonProperty("slotToElicit") String slotToElicit,
                                @JsonProperty("updatedIntent") Intent updatedIntent) {
        super(updatedIntent);
        this.slotToElicit = slotToElicit;
    }

    public String getSlotToElicit() {
        return slotToElicit;
    }

    public static class Builder extends DelegateDirective.Builder {
        String slotToElicit;

        private Builder() {
            super();
        }

        public Builder withSlotToElicit(final String slotToElicit) {
            this.slotToElicit = slotToElicit;
            return this;
        }

        public ElicitSlotDirective build() {
            return new ElicitSlotDirective(this);
        }
    }
}
