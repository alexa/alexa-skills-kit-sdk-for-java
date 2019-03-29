/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.cityguide;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.cityguide.handlers.AboutIntentHandler;
import com.amazon.ask.cityguide.handlers.AttractionIntentHandler;
import com.amazon.ask.cityguide.handlers.BreakfastIntentHandler;
import com.amazon.ask.cityguide.handlers.CoffeeIntentHandler;
import com.amazon.ask.cityguide.handlers.DinnerIntentHandler;
import com.amazon.ask.cityguide.handlers.ErrorHandler;
import com.amazon.ask.cityguide.handlers.FallbackIntentHandler;
import com.amazon.ask.cityguide.handlers.GoOutIntentHandler;
import com.amazon.ask.cityguide.handlers.HelpIntentHandler;
import com.amazon.ask.cityguide.handlers.LaunchHandler;
import com.amazon.ask.cityguide.handlers.LunchIntentHandler;
import com.amazon.ask.cityguide.handlers.SessionEndedHandler;
import com.amazon.ask.cityguide.handlers.StopHandler;
import com.amazon.ask.cityguide.handlers.YesIntentHandler;

public class CityGuideStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new AboutIntentHandler(),
                        new AttractionIntentHandler(),
                        new GoOutIntentHandler(),
                        new BreakfastIntentHandler(),
                        new LunchIntentHandler(),
                        new CoffeeIntentHandler(),
                        new DinnerIntentHandler(),
                        new FallbackIntentHandler(),
                        new StopHandler(),
                        new HelpIntentHandler(),
                        new LaunchHandler(),
                        new SessionEndedHandler(),
                        new YesIntentHandler(),
                        new ErrorHandler()
                )
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public CityGuideStreamHandler() {
        super(getSkill());
    }
}
