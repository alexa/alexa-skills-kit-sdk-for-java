/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.cityguide.handlers;

import com.amazon.ask.cityguide.POJO.Attraction;
import com.amazon.ask.cityguide.utils.SkillData;
import com.amazon.ask.cityguide.utils.SkillUtils;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.List;
import java.util.Optional;

public class AttractionIntentHandler implements IntentRequestHandler {

    private static final double MAX_RADIUS = 200;

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AttractionIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final Slot distanceSlot = intentRequest.getIntent().getSlots().get("distance");
        double radius = MAX_RADIUS;
        if (distanceSlot != null
                && distanceSlot.getResolutions() != null
                && distanceSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            radius = Double.valueOf(distanceSlot.getValue());
        }

        final List<Attraction> attractions = SkillUtils.getAttractionsWithinDistance(radius, SkillData.CITY_INFORMATION.getAttractions());

        final Attraction nearestAttraction = attractions.get(SkillUtils.getRandomIndexInRange(attractions.size(), 0));
        final String speechOutput = String.format("Try %s which is %s miles away. Have fun! %s",
                nearestAttraction.getName(),
                nearestAttraction.getDistance(),
                nearestAttraction.getDescription());
        return handlerInput.getResponseBuilder()
                .withSpeech(speechOutput)
                .build();
    }
}
