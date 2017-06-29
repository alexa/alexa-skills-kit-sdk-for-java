/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu;


/**
 * Indication of whether an intent or slot has been explicitly confirmed or denied by the user, or neither.
 *
 * Intents can be confirmed or denied using
 * {@link com.amazon.speech.speechlet.dialog.directives.ConfirmIntentDirective}, or by indicating
 * in the skill's configured dialog that the intent requires confirmation.
 *
 * Slots can be confirmed or denied using {@link com.amazon.speech.speechlet.dialog.directives.ConfirmSlotDirective},
 * or by indicating in the skill's configured dialog that the intent requires confirmation.
 */
public enum ConfirmationStatus {
    NONE,
    CONFIRMED,
    DENIED
}