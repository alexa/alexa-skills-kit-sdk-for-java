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
 * A Directive which a skill may return to indicate that dialog management should be delegated
 * to Alexa, based on the required slots and confirmations configured in the Skill Builder.
 *
 * @see DialogDirective#setUpdatedIntent
 */
@JsonTypeName("Dialog.Delegate")
public class DelegateDirective extends DialogDirective {

}
