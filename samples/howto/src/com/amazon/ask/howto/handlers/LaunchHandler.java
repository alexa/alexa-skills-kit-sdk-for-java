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
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler;
import com.amazon.ask.howto.utils.SkillUtils;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;
import java.util.ResourceBundle;

public class LaunchHandler implements LaunchRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        final String item = SkillUtils.getResourceBundle(handlerInput, "Recipes").getKeys().nextElement().replaceAll("_", " ");
        final String speechText = String.format(messages.getString("WELCOME_MESSAGE"), messages.getString("SKILL_NAME"), item);
        final String repromptText = messages.getString("WELCOME_REPROMPT");
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}
