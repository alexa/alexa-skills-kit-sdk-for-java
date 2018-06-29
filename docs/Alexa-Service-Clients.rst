=====
Alexa Service Clients
=====

Creating a service client instance
----------------------------------

The SDK includes service clients that you can use to call Alexa APIs
from within your skill logic.

Service clients can be used in any request handler, exception handler,
and request and response interceptor. The ``ServiceClientFactory``
contained inside the ``HandlerInput`` allows you to retrieve client
instances for every supported Alexa service.

The following example shows the ``handle`` method of a request handler
that creates an instance of the ``DirectivesService`` client. Creating a
service client instance is as simple as calling the appropriate factory
method.

::

   @Override
    public Optional<Response> handle(HandlerInput input) {
        DirectivesService directivesServices = input.getServiceClientFactory().getDirectivesService();    
        directivesServices.enqueue(SendDirectiveRequest.*builder*().build());
        // other handler logic goes here
    }

Once created, you can use the service client instance to call the API
operations exposed by the service.
