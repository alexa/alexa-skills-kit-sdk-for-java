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