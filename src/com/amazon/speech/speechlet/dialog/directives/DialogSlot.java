package com.amazon.speech.speechlet.dialog.directives;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;

/**
 * Used by a skill to update the intent name and/or one or more slot values during a multi-turn dialog.
 *
 * @see DialogDirective#setUpdatedIntent(DialogIntent)
 */
public class DialogSlot {

    private String name;
    private String value;
    private ConfirmationStatus confirmationStatus;

    public DialogSlot(final String name, final String value, final ConfirmationStatus confirmationStatus) {
        this.name = name;
        this.value = value;
        this.confirmationStatus = confirmationStatus;
    }

    public DialogSlot(final String name, final String value) {
        this(name, value, null);
    }

    public DialogSlot() {
        this(null, null, null);
    }

    /**
     * Create a slot object for a dialog response from a given {@link Slot} object from the {@link Intent} object
     * within an {@link com.amazon.speech.speechlet.IntentRequest}.
     *
     * @param requestSlot  the slot given
     */
    public DialogSlot(final Slot requestSlot) {
        this(requestSlot.getName(), requestSlot.getValue(), requestSlot.getConfirmationStatus());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(final ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

}
