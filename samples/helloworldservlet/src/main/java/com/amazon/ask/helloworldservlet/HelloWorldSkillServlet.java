/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.helloworldservlet;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;

import com.amazon.ask.helloworldservlet.handlers.CancelandStopIntentHandler;
import com.amazon.ask.helloworldservlet.handlers.HelloWorldIntentHandler;
import com.amazon.ask.helloworldservlet.handlers.HelpIntentHandler;
import com.amazon.ask.helloworldservlet.handlers.SessionEndedRequestHandler;
import com.amazon.ask.helloworldservlet.handlers.LaunchRequestHandler;

public class HelloWorldSkillServlet extends SkillServlet {

    public HelloWorldSkillServlet() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                // Add your skill id below
                //.withSkillId("")
                .build();
    }

}
