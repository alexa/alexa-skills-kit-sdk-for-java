Migration Guide for ASK SDK v2 for Java
=======================================

This guide shows how to use Alexa Skills Kit SDK v2 for Java to migrate
existing skills developed with the SDK v1 to v2.

Adding the ASK SDK v2 to Your Project
-------------------------------------

In the ``pom.xml``, from the SDK v1 version of the skill, replace the
following:

::

   <dependency>
         <groupId>com.amazon.alexa</groupId>
         <artifactId>alexa-skills-kit</artifactId>
         <version>1.8.1</version>
         <scope>compile</scope>
   </dependency>

With the following new dependency. Note the change in ``artifactId`` and
``version``.

::

   <dependency>
         <groupId>com.amazon.alexa</groupId>
         <artifactId>ask-sdk</artifactId>
         <version>2.3.4</version>
   </dependency>

Request handlers
----------------

The SDK v2 introduces the concept of request handlers. Request handlers
are discrete components that can handle one or more types of incoming
requests. Handlers contain the canHandle method, which is responsible
for deciding whether the handler should be invoked, and a handle method
that contains the handling logic. This way, skill logic is structured
and organized and not contained in one monolithic class.

The following is from the Hello World sample written using the SDK v1.
All the helper function implementations such as ``getWelcomeResponse``,
``getAskResponse``, and ``getHelpResponse`` have been omitted because
they are mentioned in a section on the response builder.

::

   public class HelloWorldSpeechlet implements SpeechletV2 {
       private static final Logger log = LoggerFactory.getLogger(HelloWorldSpeechlet.class);

       @Override
       public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
           log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                   requestEnvelope.getSession().getSessionId());
           // any initialization logic goes here
       }

       @Override
       public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
           log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                   requestEnvelope.getSession().getSessionId());
           return getWelcomeResponse();
       }

       @Override
       public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
           IntentRequest request = requestEnvelope.getRequest();
           log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                   requestEnvelope.getSession().getSessionId());

           Intent intent = request.getIntent();
           String intentName = (intent != null) ? intent.getName() : null;

           if ("HelloWorldIntent".equals(intentName)) {
               return getHelloResponse();
           } else if ("AMAZON.HelpIntent".equals(intentName)) {
               return getHelpResponse();
           } else {
               return getAskResponse("HelloWorld", "This is unsupported.  Please try something else.");
           }
       }

       @Override
       public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
           log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                   requestEnvelope.getSession().getSessionId());
           // any cleanup logic goes here
       }

       /**
        * Creates and returns a {@code SpeechletResponse} with a welcome message.
        *
        * @return SpeechletResponse spoken and visual response for the given intent
        */
       private SpeechletResponse getWelcomeResponse() {
           String speechText = "Welcome to the Alexa Skills Kit, you can say hello";
           return getAskResponse("HelloWorld", speechText);
       }
       ...
   }  

In the SDK v2, each onLaunch, onSessionEnded, and ontIntent case is
separated out into a different handler.

::

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

::

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

::

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

::

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

Attributes manager
------------------

Session attributes, which persist throughout the lifetime of the current
session, can be retrieved from an incoming RequestEnvelope, but other
attribute storage is not supported in the SDK v1. The SDK v2 introduces
the concept of attribute manager that manages the following scopes of
attributes:

1. Request level, which only last through the current request, including
   interceptors
2. Session level, which last through the current session
3. Persistence level, which persist beyond the scope of the current
   session and can be retrieved the next time a user invokes the skill

The SDK v2 passes these attributes automatically to the request handler
interface, interceptors, and exception handlers, meaning that you can
get and set attributes without saving and retrieving them. The following
example shows how to retrieve a persistent attribute from a DynamoDB
table using attribute manager in the ``canHandle`` method. The
``handle`` method shows how to set a persistent attribute.

::

   @Override
   public boolean canHandle(HandlerInput input) {
       Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
       return persistentAttributes.get("title").equals("AWSPodcast");
   }

   @Override
   public Optional<Object> handle(HandlerInput input) {
       Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
       persistentAttributes.put("title", "JavaPodcast");
       input.getAttributesManager().setPersistentAttributes(persistentAttributes);
       input.getAttributesManager().savePersistentAttributes();
       return input.getResponseBuilder().build();
   }

Response builder
----------------

The response builder allows you to avoid manually writing helper
functions to construct each element of ``SpeechletResponse``.

The following shows a snippet of the newAskResponse method taken from
the SDK v1 sample.

::

   private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
           String repromptText, boolean isRepromptSsml) {
       OutputSpeech outputSpeech, repromptOutputSpeech;
       if (isOutputSsml) {
           outputSpeech = new SsmlOutputSpeech();
           ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
       } else {
           outputSpeech = new PlainTextOutputSpeech();
           ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
       }

   if (isRepromptSsml) {
           repromptOutputSpeech = new SsmlOutputSpeech();
           ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
       } else {
           repromptOutputSpeech = new PlainTextOutputSpeech();
           ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
       }
       Reprompt reprompt = new Reprompt();
       reprompt.setOutputSpeech(repromptOutputSpeech);
       return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
   }

With the response builder, in each request handler you construct the
response within the ``handle`` method, which reduces the verbosity of
your code.

::

   public Optional<Response> handle(HandlerInput input) {
       return input.getResponseBuilder()
               .withSpeech(outputSpeech)
               .withReprompt(repromptSpeech)
               .build();
   }

Exception handlers
------------------

Exception handlers are similar to request handlers, but are instead
invoked when exceptions are thrown during request processing. An
exception handler has a ``canHandle`` method that operates on the
incoming exception type, and a ``handle`` method that handles the
exception. Instead of using try-catch blocks to handle exceptions, you
can create exception handlers for specific exception types, or a single
exception handler that operates globally for all exceptions. The
following example shows a catch-all exception handler.

::

   public class GenericExceptionHandler implements ExceptionHandler {
       private static Logger LOG = getLogger(SessionEndedRequestHandler.class);
    
       @Override
       public boolean canHandle(HandlerInput input, Throwable throwable) {
           return true;
       }
    
       @Override
       public Optional<Response> handle(HandlerInput input, Throwable throwable) {
           LOG.debug("Exception handled: " +  throwable.getMessage());
           return input.getResponseBuilder()
                   .withSpeech(EXCEPTION_MESSAGE)
                   .build();
        }
   }

Alexa service support
---------------------

Support for calling external Alexa APIs such as HouseholdList service or
the Directive service was limited in the SDK v1. The SDK v2 supports
low-level pluggable HTTP clients for Alexa API calls, and handles
endpoint and credential resolution so that API calls only require
passing in request attributes relevant to the call.

The following snippet shows how to get a device address in the SDK v1,
taken from `Device Address
sample <https://github.com/alexa/skill-samples-java/blob/master/address/src/com/amazon/asksdk/address/DeviceAddressSpeechlet.java>`__.

::

   SystemState systemState = getSystemState(speechletRequestEnvelope.getContext());
   String apiAccessToken = systemState.getApiAccessToken();
   String deviceId = systemState.getDevice().getDeviceId();
   String apiEndpoint = systemState.getApiEndpoint();

   AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(
           deviceId, apiAccessToken, apiEndpoint);

   Address addressObject = alexaDeviceAddressClient.getFullAddress();

In the SDK v2, you can get a device address using less code, and there
is no need to implement ``AlexaDeviceAddressClient``.

::

   DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
   String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
   Address address = deviceAddressServiceClient.getFullAddress(deviceId);

Stream Handler
--------------

``RequestSpeechletStreamHandler`` in the SDK v1 mainly added the skill
ID for the AWS Lambda function.

::

   public class DeviceAddressSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
       private static final Set<String> supportedApplicationIds;

       static {
           /*
            * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
            * Alexa Skill and put the relevant Application Ids in this Set.
            */
           supportedApplicationIds = new HashSet<String>();
           // supportedApplicationIds.add("[unique-value-here]");
       }

       public DeviceAddressSpeechletRequestStreamHandler() {
           super(new DeviceAddressSpeechlet(), supportedApplicationIds);
       }
   }

In the SDK v2, the following example shows how to configure request
handlers, exception handlers, and other handlers.

::

   public class DeviceAddressStreamHandler extends SkillStreamHandler {
       private static Skill getSkill() {
           return Skills.standard()
                   .addRequestHandlers(
                       new LaunchRequestHandler(),
                       new GetAddressIntentHandler(),
                       new HelpIntentHandler(),
                       new ExitHandler(),
                       new SessionEndedRequestHandler(),
                       new FallbackIntentHandler(),
                   .addExceptionHandler(new GenericExceptionHandler())
                   .withAutoCreateTable(true)
                   .withTableName("HighLowGame")
                   // Add your skill id below
                   //.withSkillId("")
                   .build();
       }

       public DeviceAddressStreamHandler() { super(getSkill()); }
   }
