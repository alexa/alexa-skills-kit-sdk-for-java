package skills

import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skill
import com.amazon.ask.Skills
import skills.handlers.*

class HelloWorldStreamHandler : SkillStreamHandler {
    constructor() : super(
        Skills.standard()
            .addRequestHandlers(
                LaunchRequestHandler(),
                FallbackIntentHandler(),
                HelloWorldIntentHandler(),
                CancelandStopIntentHandler(),
                SessionEndedRequestHandler(),
                TellTimeIntentHandler()
            ).build()
        )
}