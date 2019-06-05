====================================
Host a Custom Skill as a Web Service
====================================

You can build a custom skill for Alexa by extending a servlet that
accepts requests from and sends responses to the Alexa service in the cloud.

The servlet must meet certain requirements to handle requests sent by Alexa
and adhere to the Alexa Skills Kit interface standards. For more information,
see
`Host a Custom Skill as a Web Service <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-a-web-service.html>`__
in the Alexa Skills Kit technical documentation.

ASK SDK Servlet Support
-----------------------

The Alexa Skills Kit SDK (ASK SDK) for Java provides boilerplate code for
`request verification <https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/2.0.x/ask-sdk-servlet-support/src/com/amazon/ask/servlet/verifiers/SkillRequestSignatureVerifier.java>`__
and `timestamp verification <https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/2.0.x/ask-sdk-servlet-support/src/com/amazon/ask/servlet/verifiers/SkillRequestTimestampVerifier.java>`__
through the
`ask-sdk-servlet-support <https://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-servlet-support/>`__
package. This package provides the verification components and
`SkillServlet <https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/2.0.x/ask-sdk-servlet-support/src/com/amazon/ask/servlet/SkillServlet.java>`__
for skill invocation.

Installation
~~~~~~~~~~~~

You can import the latest version of ``ask-sdk-servlet-support`` by adding it as a maven
`dependency <https://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-servlet-support>`__
in your project's ``pom.xml``.

Skill Servlet
~~~~~~~~~~~~~
The
`SkillServlet <https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/2.0.x/ask-sdk-servlet-support/src/com/amazon/ask/servlet/SkillServlet.java>`__
class registers the skill instance from the ``SkillBuilder`` object, and provides a ``doPost`` method which is responsible for de-serialization of the incoming request, verification of the input request before invoking the skill and serialization of the generated response.

Usage
~~~~~

.. code-block:: java

    public class HelloWorldSkillServlet extends SkillServlet {

        @Override
        public HelloWorldSkillServlet() {
            super(getSkill());
        }

        @Override
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

Sample skill with servlet support can be found
`here <https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/helloworldservlet>`__.
