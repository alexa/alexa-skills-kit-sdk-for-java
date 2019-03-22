Request Processing
==================

Request handlers
----------------

Request handlers are responsible for handling one or more types of
incoming requests.

You can create request handlers by implementing the ``RequestHandler``
interface, or a typed request handler interface, both of which consist of two
methods:

-  ``canHandle``, which is called by the SDK to determine if the given
   handler is capable of processing the incoming request. This method
   returns true if the handler can handle the request, or false if not.
   You have the flexibility to choose the conditions on which to base
   this determination, including the type or parameters of the incoming
   request, or skill attributes.
-  ``handle``, which is called by the SDK when invoking the request
   handler. This method contains the handler’s request processing logic,
   and returns an optional ``Response``.

Generic request handlers
^^^^^^^^^
The generic ``RequestHandler`` interface can be used when you want to handle
multiple request types, useful if you have a similar code path across different
types of requests.

The following example shows a request handler that is configured to
handle an incoming ``SkillEnabledRequest`` or ``SkillDisabledRequest``.

.. code:: java

   public class SkillEventsHandler implements RequestHandler {
       @Override
       public boolean canHandle(HandlerInput input) {
           return input.getRequest() instanceof SkillEnabledRequest
                   || input.getRequest() instanceof SkillDisabledRequest;
       }

       @Override
       public Optional<Response> handle(HandlerInput input) {
            logger.info("A skill event was recieved");

            if (input.getRequest() instanceof SkillEnabledRequest) {
                SkillEnabledRequest request = (SkillEnabledRequest) input.getRequest();
                logger.info("User enabled the skill at {}", request.getEventCreationTime());
            }

            if (input.getRequest() instanceof SkillDisabledRequest) {
                SkillDisabledRequest request = (SkillDisabledRequest) input.getRequest();
                logger.info("User disabled the skill at {}", request.getEventCreationTime());
            }
       }
   }

Typed request handlers
^^^^^^^^^
Request handler interfaces for each SDK supported request type (e.g.
IntentRequest, LaunchRequest, SkillEnabledRequest) are available and
provide a type safe way of writing handlers for a particular type of request.
When using the generic ``RequestHandler`` interface you must do a type check, then
cast to work against a particular request type. The typed handlers eliminate
this requirement and handle the type checking for you. For example, a handler
implementing the ``LaunchRequestHandler`` interface will only be invoked if the
incoming request is a ``LaunchRequest``.

A list of the currently available type specific request handlers is available `here
<https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/ask-sdk-core/src/com/amazon/ask/dispatcher/request/handler/impl>`_.

The following example shows a request handler that is configured to
handle the ``HelloWorldIntent``. As you can see, the handler implements the
``IntentRequestHandler`` interface since we are going to be handling an intent request.

.. code:: java

   public class HelloWorldIntentHandler implements IntentRequestHandler {
       @Override
       public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
           return intentRequest.getIntent().getName().equals("HelloWorldIntent");
       }

       @Override
       public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
           String speechText = "Hello world";

           Intent intent = intentRequest.getIntent();
           String intentName = intent.getName();

           return input.getResponseBuilder()
                   .withSpeech(speechText)
                   .withSimpleCard(intentName, speechText)
                   .build();
       }
   }

This type of handler will only be invoked if the incoming request is an
``IntentRequest``. The handler’s ``canHandle`` method is configured to check the
request's intent name, and then return true if the intent name is
``HelloWorldIntent``. A basic “Hello world” response is then generated and
returned by the ``handle`` method. By modifiying the ``canHandle`` condition,
you can choose to make this handler more granular (for example, by inspecting
Intent slot values), or more generic by returning true for any
``IntentRequest``.

CanHandle Predicates
^^^^^^^^^
The SDK includes a set of prebuilt canHandle predicates that make it easy to
evaluate common conditions, such as intent name, slot values, and attributes.
These make it simpler and more concise to write complex ``canHandle``
conditional logic. A list of the available Preciates provided with the SDK is
available `here
<https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/2.0.x/ask-sdk-core/src/com/amazon/ask/request/Predicates.java>`_.

For example, the above sample handler for a HelloWorld intent can have its
CanHandle condition simplified by using Predicates:

.. code:: java

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return input.matches(Predicates.intentName("HelloWorldIntent"));
    }

Likewise, the generic RequestHandler example showing a handler for both
``SkillEnabledRequest`` or ``SkillDisabledRequest`` would look like this:

.. code:: java

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(SkillEnabledRequest.class)
            .or(Predicates.requestType(SkillDisabledRequest.class)));
    }

Handler processing order
^^^^^^^^^
The SDK calls the ``canHandle`` methods on its request handlers in the order in
which they were provided to the Skill builder. You can implement as may request
handlers as you like for a skill, including multiple handlers for the same type
of request (e.g. multiple handlers implementing ``IntentRequestHandler``). In
this case, the SDK will access these handlers in the order they were registered
and utilize the handler that's ``canHandle`` method returns ``true`` first.

.. code:: java

   return Skills.standard()
       .addHandlers(new FooHandler(), new BarHandler(), new BazHandler())
       .build();

In this example, the SDK calls request handlers in the following order:

1. FooHandler
2. BarHandler
3. BazHandler

The SDK always chooses the first handler that is capable of handling a
given request. In this example, if both ``FooHandler`` and
``BarHandler`` are capable of handling a particular request,
``FooHandler`` is always be invoked. Keep this in mind when designing
and registering request handlers.

Request and response interceptors
---------------------------------

The SDK supports request and response interceptors that execute before
and after ``RequestHandler`` execution, respectively. You can implement
request interceptors by implementing the ``RequestInterceptor``
interface, or the ``ResponseInterceptor`` interface for response
interceptors.

Both interceptor interfaces expose a single ``process`` method with a
void return type. Request interceptors have access to the
``HandlerInput`` instance, while response interceptors have access to
the ``HandlerInput`` as well as the ``Optional<Response>`` produced by
the ``RequestHandler``.

.. code:: java

   public class PersistenceSavingResponseInterceptor implements ResponseInterceptor {
       @Override
       public void process(HandlerInput input, Optional<Response> output) {
           input.getAttributesManager().savePersistentAttributes();
       }
   }

Request interceptors are invoked immediately prior to execution of the
request handler for an incoming request. Request attributes provide a
way for request interceptors to pass data and entities on to request
handlers.

Response interceptors are invoked immediately after execution of the
request handler. Because response interceptors have access to the output
generated from execution of the request handler, they are ideal for
tasks such as response sanitization and validation.

The following example shows how to register an interceptor with the SDK
on the ``Skill`` builder:

.. code:: java

   return Skills.standard()
        .addHandlers(new FooHandler(), new BarHandler(), new BazHandler())
        .addRequestInterceptors(new FooRequestInterceptor())
        .addResponseInterceptors(new BarResponseInterceptor())
        .build();

Exception handlers
------------------

Exception handlers are similar to request handlers, but are instead
responsible for handling one or more types of exceptions. They are
invoked by the SDK when an unhandled exception is thrown during the
course of request processing.

All exception handlers must implement the ExceptionHandler interface,
consisting of the following two methods:

-  ``canHandle``, which is called by the SDK to determine if the given
   handler is capable of handling the exception. This method returns
   **true** if the handler can handle the exception, or **false** if
   not. A catch-all handler can be easily introduced by simply returning
   true in all cases.
-  ``handle``, which is called by the SDK when invoking the exception
   handler. This method contains all exception handling logic, and
   returns an output which optionally may contain a ``Response``.

The following example shows an example exception handler that is
configured to handle any exception of type ``AskSdkException``.

.. code:: java

   public class MyExecptionHandler implements ExceptionHandler {
       @Override
       public boolean canHandle(HandlerInput input, Throwable throwable) {
           return throwable instanceof AskSdkException;
       }

       @Override
       public HandlerOutput handle(HandlerInput input, Throwable throwable) {
           return input.getResponseBuilder()
                       .withSpeech("An error was encountered while handling your request. Try again later.")
                       .build();
       }
   }

The handler’s ``canHandle`` method returns true if the incoming
exception is an instance of ``AskSdkException``. The handle method
returns a graceful error response to the user.

Exception handlers are executed similarly to request handlers, where the
SDK accesses handlers in the order in which they were provided to the
``Skill``.

Handler Input
-------------

Request handlers, request and response interceptors, and exception
handlers are all passed a HandlerInput instance when invoked. This class
exposes various entities useful in request processing, including:

-  **RequestEnvelope:** Contains the incoming ``Request`` and other
   context.
-  **AttributesManager:** Provides access to request, session, and
   persistence attributes.
-  **ServiceClientFactory:** Constructs service clients capable of
   calling Alexa APIs.
-  **ResponseBuilder:** Helps to build responses.
-  **Context:** Provides an optional, context object passed in by the
   host container. For example, for skills running on AWS Lambda, this
   is the context object for the AWS Lambda function.
