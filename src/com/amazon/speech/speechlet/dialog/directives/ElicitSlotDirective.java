package com.amazon.speech.speechlet.dialog.directives;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A Directive which a skill may return to indicate that the skill is requesting the user to
 * provide a value for a slot. The skill must also provide output speech for the request.
 *
 * @see DialogDirective#setUpdatedIntent
 */
@JsonTypeName("Dialog.ElicitSlot")
public class ElicitSlotDirective extends DialogDirective {
    private String slotToElicit;

    /**
     * Set the name of the slot for which the user is being requested to provide a value.
     *
     * @param slotToElicit the name of the slot
     */
    public void setSlotToElicit(String slotToElicit) {
        this.slotToElicit = slotToElicit;
    }

    /**
     * Get the name of the slot for which the user is being requested to provide a value.
     *
     * @return the name of the slot
     */
    public String getSlotToElicit() {
        return slotToElicit;
    }

}
