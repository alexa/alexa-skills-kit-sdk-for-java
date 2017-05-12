package com.amazon.speech.speechlet.dialog.directives;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A Directive which a skill may return to indicate that the skill is asking the user to
 * confirm or deny the overall intent. The skill must also provide output speech for the request.
 * If the user confirms the intent, subsequent requests to the skill for the same dialog
 * session will have a confirmationStatus of
 * {@link com.amazon.speech.slu.ConfirmationStatus#CONFIRMED} for the intent; if the user denies
 * the value, the confirmationStatus will be
 * {@link com.amazon.speech.slu.ConfirmationStatus#DENIED}.
 *
 * When a user confirms the intent, it is expected that the skill will then try to fulfill the
 * intent. When a user denies the intent, or the user confirms the intent but the skill is unable
 * to fulfill it, the skill may either end the session or give the
 * user the opportunity to (re-)confirm or change one or more slot values via a sequence of
 * {@link ConfirmSlotDirective} and/or {@link ElicitSlotDirective}. In this case, the skill
 * should use {@link #setUpdatedIntent} to clear any values or confirmation statuses as
 * necessary.
 *
 * @see DialogDirective#setUpdatedIntent
 */
@JsonTypeName("Dialog.ConfirmIntent")
public class ConfirmIntentDirective extends DialogDirective {
}
