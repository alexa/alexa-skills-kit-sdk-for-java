/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.amazon.speech.speechlet.dialog.directives.ConfirmIntentDirective;
import com.amazon.speech.speechlet.dialog.directives.ConfirmSlotDirective;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.ClearQueueDirective;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.PlayDirective;
import com.amazon.speech.speechlet.interfaces.audioplayer.directive.StopDirective;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An instruction returned from a skill in its response to a request, informing the Alexa service to
 * take action on the customer's device. This may include rendering audio content on the device.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(PlayDirective.class),
        @JsonSubTypes.Type(StopDirective.class),
        @JsonSubTypes.Type(ClearQueueDirective.class),
        @JsonSubTypes.Type(DelegateDirective.class),
        @JsonSubTypes.Type(ElicitSlotDirective.class),
        @JsonSubTypes.Type(ConfirmSlotDirective.class),
        @JsonSubTypes.Type(ConfirmIntentDirective.class),
})
public abstract class Directive {
}
