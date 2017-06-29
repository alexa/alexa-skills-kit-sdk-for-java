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
