/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.dialog.directives;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;

/**
 * Used by a skill to update the intent name and/or one or more slot values during a multi-turn
 * dialog.
 *
 * @see DialogDirective#setUpdatedIntent(DialogIntent)
 * @see DialogIntent#setSlots(java.util.Map)
 */
public class DialogSlot {

    private String name;
    private String value;
    private ConfirmationStatus confirmationStatus;

    /**
     * Constructs a dialog slot from name, value and confirmation status.
     *
     * @param name
     *            the name of dialog slot
     * @param value
     *            the value of dialog slot or {@code null} if undefined
     * @param confirmationStatus
     *            the confirmation status for dialog slot, or {@link ConfirmationStatus#NONE} if
     *            unknown
     * @see ConfirmationStatus
     */
    public DialogSlot(final String name, final String value,
            final ConfirmationStatus confirmationStatus) {
        this.name = name;
        this.value = value;
        this.confirmationStatus = confirmationStatus;
    }

    /**
     * Constructs a dialog slot from name and value, sets confirmation status as {@code null}.
     *
     * @param name
     *            the name of dialog slot
     * @param value
     *            the value of dialog slot or {@code null} if undefined
     */
    public DialogSlot(final String name, final String value) {
        this(name, value, null);
    }

    /**
     * Constructs a dialog slot, sets name, value and confirmation status as {@code null}.
     */
    public DialogSlot() {
        this(null, null, null);
    }

    /**
     * Creates a slot object for a dialog response from a given {@link Slot} object from the
     * {@link Intent} object within an {@link com.amazon.speech.speechlet.IntentRequest}.
     *
     * @param requestSlot  the slot given
     */
    public DialogSlot(final Slot requestSlot) {
        this(requestSlot.getName(), requestSlot.getValue(), requestSlot.getConfirmationStatus());
    }

    /**
     * Returns the name of dialog slot.
     *
     * @return the name of dialog slot
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for dialog slot.
     *
     * @param name
     *            the name of dialog slot
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the value of dialog slot or {@code none} if undefined.
     *
     * @return the value of dialog slot or {@code none} if undefined
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value for dialog slot.
     *
     * @param value
     *            the value for dialog slot.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Returns the confirmation status for dialog slot.
     *
     * @return the confirmation status for dialog slot
     * @see ConfirmationStatus
     */
    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    /**
     * Sets the confirmation status for dialog slot.
     *
     * @param confirmationStatus
     *            the confirmation status for dialog slot
     * @see ConfirmationStatus
     */
    public void setConfirmationStatus(final ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

}
