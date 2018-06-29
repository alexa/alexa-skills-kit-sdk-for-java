=====
Developing Your First Skill
=====

This guide walks you through developing your first skill with the ASK
SDK v2 for Java.

Prerequisites
-------------

-  An `Amazon Developer <https://developer.amazon.com/>`__ account. This
   is required to create and configure Alexa skills.
-  An `Amazon Web Services (AWS) <https://aws.amazon.com/>`__ account.
   This guide will walk you through hosting a skill on AWS Lambda.
-  A Maven project with a dependency on the SDK as described in the
   **Setting up the ASK SDK v2 for Java** section. The sample skill
   requires either the standard SDK distribution, or if customizing your
   dependencies, requires that you include the support modules for the
   core SDK and AWS Lambda.

Implementing request handlers
-----------------------------

First we’ll create the request handlers needed to handle the different
types of incoming requests to our skill.

LaunchRequest handler
~~~~~~~~~~~~~~~~~~~~~

The following code example shows how to configure a handler to be
invoked when the skill receives a ``LaunchRequest``. The
``LaunchRequest`` event occurs when the skill is invoked without a
specific intent.

::

   package com.amazon.ask.helloworld.handlers;
     
    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.LaunchRequest;
    import com.amazon.ask.model.Response;
    import com.amazon.ask.request.Predicates;

    import java.util.Optional;
     
    public class LaunchRequestHandler implements RequestHandler {
     
         @Override
         public boolean canHandle(HandlerInput input) {
             return input.matches(Predicates.requestType(LaunchRequest.class));
         }
     
         @Override
         public Optional<Response> handle(HandlerInput input) {
             String speechText = "Welcome to the Alexa Skills Kit, you can say hello";
             return input.getResponseBuilder()
                     .withSpeech(speechText)
                     .withSimpleCard("HelloWorld", speechText)
                     .withReprompt(speechText)
                     .build();
         }
     
    }

The ``canHandle`` method returns true if the incoming request is a
LaunchRequest. The ``handle`` method generates and returns a basic
greeting response with response objects such as Speech, Card, and
Reprompt which you can find more information
`here <https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#response-object>`__.

HelloWorldIntent handler
~~~~~~~~~~~~~~~~~~~~~~~~

The following code example shows how to configure a handler to be
invoked when the skill receives the ``HelloWorldIntent``.

::

   package com.amazon.ask.helloworld.handlers;
     
    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.Response;
    import com.amazon.ask.request.Predicates;

    import java.util.Optional;
     
    public class HelloWorldIntentHandler implements RequestHandler {
     
         @Override
         public boolean canHandle(HandlerInput input) {
            return input.matches(Predicates.intentName("HelloWorldIntent"));
         }
     
         @Override
         public Optional<Response> handle(HandlerInput input) {
             String speechText = "Hello world";
             return input.getResponseBuilder()
                     .withSpeech(speechText)
                     .withSimpleCard("HelloWorld", speechText)
                     .build();
         }
     
    }

The handler’s canHandle method detects if the incoming request is an
``IntentRequest``, and returns true if the intent name is
``HelloWorldIntent``. A basic “Hello world” response is then generated
and returned.

HelpIntent handler
~~~~~~~~~~~~~~~~~~

The following code example shows how to configure a handler to be
invoked when the skill receives the `built in
intent <https://developer.amazon.com/docs/custom-skills/standard-built-in-intents.html#available-standard-built-in-intents>`__
``AMAZON.HelpIntent``.

::

   package com.amazon.ask.helloworld.handlers;

    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.Response;
    import static com.amazon.ask.request.Predicates.intentName;

    import java.util.Optional;

    public class HelpIntentHandler implements RequestHandler {

        @Override
        public boolean canHandle(HandlerInput input) {
            return input.matches(intentName("AMAZON.HelpIntent"));
        }

        @Override
        public Optional<Response> handle(HandlerInput input) {
            String speechText = "You can say hello to me!";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("HelloWorld", speechText)
                    .withReprompt(speechText)
                    .build();
        }
    }

Similar to the previous handler, this handler matches an
``IntentRequest`` with the expected intent name. Basic help instructions
are returned.

CancelandStopIntent handler
~~~~~~~~~~~~~~~~~~~~~~~~~~~

This handler is very similar to the HelpIntent handler, as it will also
be triggered by built in intents. But in this case, we decided to use a
single handler to respond to two different intents, Amazon.CancelIntent
and Amazon.StopIntent.

::

   package com.amazon.ask.helloworld.handlers;

    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.Response;
    import static com.amazon.ask.request.Predicates.intentName;

    import java.util.Optional;

    public class CancelandStopIntentHandler implements RequestHandler {

        @Override
        public boolean canHandle(HandlerInput input) {
            return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent")));
        }

        @Override
        public Optional<Response> handle(HandlerInput input) {
            return input.getResponseBuilder()
                    .withSpeech("Goodbye")
                    .withSimpleCard("HelloWorld", "Goodbye")
                    .build();
        }
    }

Our response to both these intents will be the same, so having a single
handler reduces repetitive code.

SessionEndedRequest handler
~~~~~~~~~~~~~~~~~~~~~~~~~~~

Despite not being able to send a response back after receiving a
SessionEndedRequest, this handler gives us a good place for us to put
our cleanup logic.

::

   package com.amazon.ask.helloworld.handlers;

    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.Response;
    import com.amazon.ask.model.SessionEndedRequest;
    import static com.amazon.ask.request.Predicates.requestType;

    import java.util.Optional;

    public class SessionEndedRequestHandler implements RequestHandler {

        @Override
        public boolean canHandle(HandlerInput input) {
            return input.matches(requestType(SessionEndedRequest.class));
        }

        @Override
        public Optional<Response> handle(HandlerInput input) {
            //any cleanup logic goes here
            return input.getResponseBuilder().build();
        }
    }

Implementing the SkillStreamHandler
-----------------------------------

The stream handler is the entry point for your AWS Lambda function. The
following stream handler extends the SDK ``SkillStreamHandler`` class
provided in the SDK, which routes all inbound requests to our skill. The
``HelloWorldStreamHandler`` creates an SDK ``Skill`` instance configured
with the request handlers we just created.

::

   package com.amazon.ask.helloworld;

    import com.amazon.ask.Skill;
    import com.amazon.ask.Skills;
    import com.amazon.ask.SkillStreamHandler;

    import com.amazon.ask.helloworld.handlers.CancelandStopIntentHandler;
    import com.amazon.ask.helloworld.handlers.HelloWorldIntentHandler;
    import com.amazon.ask.helloworld.handlers.HelpIntentHandler;
    import com.amazon.ask.helloworld.handlers.SessionEndedRequestHandler;
    import com.amazon.ask.helloworld.handlers.LaunchRequestHandler;
     
     public class HelloWorldStreamHandler extends SkillStreamHandler {
     
         private static Skill getSkill() {
             return Skills.standard()
                     .addRequestHandlers(new CancelandStopIntentHandler(), new HelloWorldIntentHandler(), new HelpIntentHandler(), new LaunchRequestHandler(), new SessionEndedRequestHandler())
                     .build();
         }
     
         public HelloWorldStreamHandler() {
             super(getSkill());
         }
     
     }

The ``getSkill`` method creates an SDK instance using the
``Skills.standard`` builder. We create instances of our request handlers
and register them with our skill with the ``addRequestHandlers`` builder
method. The HelloWorldStreamHandler constructor passes the constructed
Skill instance to the constructor for the superclass SkillStreamHandler.

The fully qualified class name of your stream handler class consists of
the package and class name and is required when configuring your AWS
Lambda function. In this example, the fully qualified class name is
``com.amazon.ask.helloworld.HelloWorldStreamHandler``.

Building the skill
------------------

With our skill code complete, we are ready to build our skill project.
To prepare the skill for upload to AWS Lambda, we’ll need to produce a
JAR file that contains the skill plus all necessary dependencies. To do
so, open a terminal and navigate to your Maven project’s top level
directory that contains pom.xml, and run the following command:

``mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package``

This command produces a
``<my_project_name>.<my_project_version>-jar-with-dependencies.jar``
file in the ``target`` directory.

Uploading your skill to AWS Lambda
----------------------------------

1.  If you do not already have an account on AWS, go to `Amazon Web
    Services <http://aws.amazon.com/>`__ and create an account.
2.  Log in to the `AWS Management Console <http://aws.amazon.com/>`__
    and navigate to AWS Lambda.
3.  Click the region drop-down in the upper-right corner of the console
    and select one of the regions supported for Alexa skills: Asia
    Pacific (Tokyo), EU (Ireland), US East (N. Virginia), or US West
    (Oregon).
4.  If you have no Lambda functions yet, click Get Started Now.
    Otherwise, click Create function.
5.  Make sure to confirm that “Author from scratch” option is selected.
6.  Enter a Name for the function.
7.  Select the Role for the function. This defines the AWS resources the
    function can access.

    -  To use an existing role, select the role under Existing role.
    -  To create a new role, see `Defining a new Role for the
       Function <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#define-new-role>`__

8.  Select the language you want to use for the Runtime which is Java 8
    in our case.
9.  Click “Create function”.
10. Configure the Alexa Skills Kit trigger for the function as
    `described
    here <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#configuring-the-alexa-skills-kit-trigger>`__.
    Make sure you have completed `adding an alexa Skills Kit
    Trigger <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#add-ask-trigger>`__.
11. Upload the JAR file produced in the previous step under Function
    code.
12. Fill in the Handler information with fully qualified class name of
    your stream handler class.
13. Finally, copy the ARN of your AWS Lambda function because you will
    need it when configuring your skill in the Amazon Developer console.
    You can find this on the top right corner.

Configuring and testing your skill
----------------------------------

Now that the skill code has been uploaded to AWS Lambda we’re ready to
configure the skill with Alexa. First, navigate to the `Alexa Skills Kit
Developer Console <https://developer.amazon.com/alexa/console/ask>`__.
Click the “Create Skill” button in the upper right. Enter “HelloWorld”
as your skill name. On the next page, select “Custom” and click “Create
skill”.

Now we’re ready to define the interaction model for the skill. Under
“Invocation” tab on the left side, define your Skill Invocation Name to
be ``greeter``.

Now it’s time to add an intent to the skill. Click the “Add” button
under the Intents section of the Interaction Model. Leave “Create custom
intent” selected, enter “HelloWorldIntent” for the intent name, and
create the intent. Now it’s time to add some sample utterances that will
be used to invoke the intent. For this example, we’ve provided the
following sample utterances, but feel free to add others.

::

   say hello
   say hello world
   hello
   say hi
   say hi world
   hi
   how are you

Since AMAZON.CancelIntent, AMAZON.HelpIntent, and AMAZON.StopIntent are
built-in Alexa intents, sample utterances do not need to be provided as
they are automatically inherited.

The Developer Console alternately allows you to edit the entire skill
model in JSON format by selecting “JSON Editor” on the navigation bar.
For this sample, the following JSON schema can be used.

::

   {
      "languageModel": {
        "intents": [
          {
            "name": "AMAZON.CancelIntent",
            "samples": []
          },
          {
            "name": "AMAZON.HelpIntent",
            "samples": []
          },
          {
            "name": "AMAZON.StopIntent",
            "samples": []
          },
          {
            "name": "HelloWorldIntent",
            "samples": [
              "say hello",
              "say hello world",
              "hello",
              "say hi",
              "say hi world",
              "hi",
              "how are you"
            ],
            "slots": []
          }
        ],
        "invocationName": "greeter"
      }
    }

Once you’re done editing the interaction model don’t forget to save and
build the model.

Let’s move on to the skill configuration section. Under “Endpoint”
select “AWS Lambda ARN” and paste in the ARN of the function you created
previously. The rest of the settings can be left at their default
values. Click “Save Endpoints” and proceed to the next section.

Finally you’re ready to test the skill! In the “Test” tab of the
developer console you can simulate requests, in text and voice form, to
your skill. Use the invocation name along with one of the sample
utterances we just configured as a guide. For example, “tell greeter to
say hello” should result in your skill responding with “Hello world”.
You should also be able to go to the `Echo
webpage <http://echo.amazon.com/#skills>`__ and see your skill listed
under “Your Skills”, where you can enable the skill on your account for
testing from an Alexa enabled device.

At this point, feel free to start experimenting with your Intent Schema
as well as the corresponding request handlers in your skill’s
implementation. Once you’re finished iterating, you can optionally
choose to move on to the process of getting your skill certified and
published so it can be used by Alexa users worldwide.
