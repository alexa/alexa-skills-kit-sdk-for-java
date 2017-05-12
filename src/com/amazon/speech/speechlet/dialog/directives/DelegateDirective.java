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
