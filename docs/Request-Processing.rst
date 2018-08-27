=====
Request Processing
=====

Request handlers
----------------

Request handlers are responsible for handling one or more types of
incoming requests.

You can create request handlers by implementing the ``RequestHandler``
interface, which consists of two methods:

-  ``canHandle``, which is called by the SDK to determine if the given
   handler is capable of processing the incoming request. This method
   returns true if the handler can handle the request, or false if not.
   You have the flexibility to choose the conditions on which to base
   this determination, including the type or parameters of the incoming
   request, or skill attributes.
-  ``handle``, which is called by the SDK when invoking the request
   handler. This method contains the handler’s request processing logic,
   and returns an optional ``Response``.

The following example shows a request handler that is configured to
handle the ``HelloWorldIntent``.

::

   public class HelloWorldHandler implements RequestHandler {
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

The handler’s ``canHandle`` method is configured to detect if the
incoming request is an ``IntentRequest`` with the expected intent name,
and then return true if the intent name is ``HelloWorldIntent``. A basic
“Hello world” response is then generated and returned by the ``handle``
method.

The SDK includes a set of prebuilt canHandle predicates that make it
easy to evaluate common conditions, such as intent name, slot values,
and attributes.

The SDK calls the ``canHandle`` methods on its request handlers in the
order in which they were provided to the Skill builder.

::

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

::

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

::

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

::

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
------------------

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
