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
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecipeIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("RecipeIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        String itemName = "";
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        final ResourceBundle recipes = SkillUtils.getResourceBundle(handlerInput, "Recipes");
        final Slot itemSlot = intentRequest.getIntent().getSlots().get("Item");
        if (itemSlot != null
                && itemSlot.getResolutions() != null
                && itemSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
                itemName = itemSlot.getValue().toLowerCase();
        }
        final String cardTitle = String.format(messages.getString("DISPLAY_CARD_TITLE"), messages.getString("SKILL_NAME"), itemName);
        final String recipeKey = itemName.replaceAll(" ", "_");
        final String recipe = recipes.getString(recipeKey);
        if (recipe != null && !recipe.isEmpty()) {
            // uncomment the reprompt lines if you want to repeat the info
            // and prompt for a subsequent action
            // sessionAttributes.put("repromptSpeech", messages.getString("RECIPE_REPEAT_MESSAGE"));
            final Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
            sessionAttributes.put("speakOutput", recipe);
            return handlerInput.getResponseBuilder()
                .withSimpleCard(cardTitle, recipe)
                .withSpeech(recipe)
                .build();
        }
        final String repromptSpeech = messages.getString("RECIPE_NOT_FOUND_REPROMPT");
        String speakOutput = "";
        if (itemName != null && !itemName.isEmpty()) {
            speakOutput += String.format(messages.getString("RECIPE_NOT_FOUND_WITH_ITEM_NAME"), itemName);
        } else {
            speakOutput += messages.getString("RECIPE_NOT_FOUND_WITHOUT_ITEM_NAME");
        }
        speakOutput += repromptSpeech;
        return handlerInput.getResponseBuilder()
                .withSpeech(speakOutput)
                .withReprompt(repromptSpeech)
                .build();
    }
}
