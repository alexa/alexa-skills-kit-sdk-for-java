package com.amazon.speech.speechlet.dialog.directives;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A Directive which a skill may return to indicate that the skill is asking the user to
 * confirm or deny a slot value. The skill must also provide output speech for the request.
 * If the user confirms the slot value, subsequent requests to the skill for the same dialog
 * session will have a confirmationStatus of
 * {@link com.amazon.speech.slu.ConfirmationStatus#CONFIRMED} for that slot; if the user denies
 * the value, the confirmationStatus will be
 * {@link com.amazon.speech.slu.ConfirmationStatus#DENIED}.
 *
 * @see DialogDirective#setUpdatedIntent
 */
@JsonTypeName("Dialog.ConfirmSlot")
public class ConfirmSlotDirective extends DialogDirective {
    private String slotToConfirm;

    /**
     * Set the name of the slot that the user is being requested to confirm.
     *
     * @param slotToConfirm the slot to confirm
     */
    public void setSlotToConfirm(String slotToConfirm) {
        this.slotToConfirm = slotToConfirm;
    }

    /**
     * Get the name of the slot that the user is being request to confirm.
     *
     * @return the slot to confirm
     */
    public String getSlotToConfirm() {
        return slotToConfirm;
    }
}
