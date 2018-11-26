/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.colorpicker.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.colorpicker.handlers.WhatsMyColorIntentHandler.COLOR_KEY;
import static com.amazon.ask.colorpicker.handlers.WhatsMyColorIntentHandler.COLOR_SLOT;
import static com.amazon.ask.request.Predicates.intentName;

public class MyColorIsIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MyColorIsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from user input.
        Slot favoriteColorSlot = slots.get(COLOR_SLOT);
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if(favoriteColorSlot != null
            && favoriteColorSlot.getResolutions() != null
            && favoriteColorSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.
            String favoriteColor = favoriteColorSlot.getValue();
            input.getAttributesManager().setSessionAttributes(Collections.singletonMap(COLOR_KEY, favoriteColor));

            speechText =
                    String.format("I now know that your favorite color is %s. You can ask me your "
                            + "favorite color by saying, what's my favorite color?", favoriteColor);
            repromptText =
                    "You can ask me your favorite color by saying, what's my favorite color?";

        } else {
            // Render an error since user input is out of list of color defined in interaction model.
            speechText = "Please provide a valid color, your options are green, blue, purple, red, orange and yellow. " +
                    "Please try again.";
            repromptText =
                    "I'm not sure what your favorite color is. You can tell me your favorite "
                            + "color by saying, my color is red.";
        }

        return input.getResponseBuilder()
                .withSimpleCard("ColorSession", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(false)
                .build();
    }

}
