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
