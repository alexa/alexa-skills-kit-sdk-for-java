/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.howto;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.howto.handlers.ErrorHandler;
import com.amazon.ask.howto.handlers.ExitIntentHandler;
import com.amazon.ask.howto.handlers.HelpIntentHandler;
import com.amazon.ask.howto.handlers.LaunchHandler;
import com.amazon.ask.howto.handlers.RecipeIntentHandler;
import com.amazon.ask.howto.handlers.RepeatIntentHandler;
import com.amazon.ask.howto.handlers.SessionEndedHandler;

public class HowToStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new LaunchHandler(),
                        new HelpIntentHandler(),
                        new RecipeIntentHandler(),
                        new RepeatIntentHandler(),
                        new ExitIntentHandler(),
                        new ErrorHandler(),
                        new SessionEndedHandler()
                )
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public HowToStreamHandler() {
        super(getSkill());
    }
}
