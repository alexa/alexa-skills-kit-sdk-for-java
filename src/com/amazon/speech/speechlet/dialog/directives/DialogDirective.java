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

import com.amazon.speech.speechlet.Directive;

/**
 * Abstract superclass for Dialog.* directives. Each Dialog directive supports an optional
 * updatedIntent field. A skill may use this field to update slot values or confirmation statuses
 * of the intent, for example, to fill in empty slots with information the skill already knows
 * about a user; or to clear an existing slot value/confirmation status if the value is no
 * longer valid.
 */
public abstract class DialogDirective extends Directive {

    private DialogIntent updatedIntent;

    /**
     * Get the updated intent object to use in subsequent turns of the dialog. Null indicates
     * there are no updates to the existing slot values or confirmations.
     *
     * @return the updated intent
     */
    public DialogIntent getUpdatedIntent() {
        return updatedIntent;
    }

    /**
     * Provide an updated intent object to use in subsequent turns of the dialog. All slot values
     * and confirmation provided by the updated intent will replace the existing values and
     * confirmations; if no slot values or confirmations are provided, then they will all be
     * unset. May be left unset or set to null to indicate that that there are no updates to the
     * existing slot values or confirmations.
     *
     * @param updatedIntent the updated intent
     */
    public void setUpdatedIntent(final DialogIntent updatedIntent) {
        this.updatedIntent = updatedIntent;
    }

}
