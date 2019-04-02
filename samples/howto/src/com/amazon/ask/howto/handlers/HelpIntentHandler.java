/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.howto.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.howto.utils.SkillUtils;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelpIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.HelpIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        final String item = SkillUtils.getResourceBundle(handlerInput, "Recipes").getKeys().nextElement();
        final String speechText = String.format(messages.getString("HELP_MESSAGE"), item);
        final String repromptText = String.format(messages.getString("HELP_REPROMPT"), item);
        final Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
        sessionAttributes.put("speakOutput", speechText);
        sessionAttributes.put("repromptSpeech", repromptText);
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}