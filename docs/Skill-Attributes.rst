=====
Skill Attributes
=====

Attributes
----------

The SDK allows you to store and retrieve attributes at different scopes.
You can use the attributes to, for example, store data that you retrieve
on subsequent requests. You can also use attributes in handler
``canHandle`` logic to influence request routing.

An attribute consists of a key and a value. The key is enforced as a
``String`` type and the value is an unbounded ``Object``. For session
and persistent attributes, you must ensure that value types are
serializable so they can be properly stored for subsequent retrieval.
This restriction does not apply to request-level attributes because they
do not persist outside of the request processing lifecycle.

Attribute Scopes
----------------

Request attributes
~~~~~~~~~~~~~~~~~~

Request attributes only last within a single request processing
lifecycle. Request attributes are initially empty when a request comes
in, and are discarded once a response has been produced.

Request attributes are useful with request and response interceptors.
For example, you can inject additional data and helper classes into
request attributes through a request interceptor so they are retrievable
by request handlers.

Session attributes
~~~~~~~~~~~~~~~~~~

Session attributes persist throughout the lifespan of the current skill
session. Session attributes are available for use with any in-session
request. Any attributes set during the request processing lifecycle are
sent back to the Alexa service and provided via the next request in the
same session.

Session attributes do not require the use of an external storage
solution. They are not available for use when handling out-of-session
requests.

Persistent attributes
~~~~~~~~~~~~~~~~~~~~~

Persistent attributes persist beyond the lifecycle of the current
session. How these attributes are stored, including key scope (user ID
or device ID), TTL, and storage layer depends on the configuration of
the skill.

Persistent attributes are only available when you configure the skill
instance with a ``PersistenceAdapter``. Calls to the
``AttributesManager`` to retrieve and save persistent attributes throw
an ``IllegalStateException`` if a ``PersistenceAdapter`` has not been
configured. Call ``savePersistentAttributes()`` on the
``AttributesManager`` to save any changes back to the persistence layer.

AttributesManager
-----------------

The ``AttributesManager`` exposes attributes that you can retrieve and
update in your handlers. ``AttributesManager`` is available to handlers
via the ``HandlerInput`` container class. The ``AttributesManager``
takes care of attributes retrieval and saving so that you can interact
directly with attributes needed by your skill.

The following example shows how you can retrieve and save persistent
attributes.

::

   @Override
   public boolean canHandle(HandlerInput input) {
       Map<String, Object> persistentAttributes = handlerInput.getAttributesManager().getPersistentAttributes();
       return persistentAttributes.get("foo").equals("bar");
   }

   @Override
   public Optional<Response> handle(HandlerInput input) {
       Map<String, Object> persistentAttribues = handlerInput.getAttributesManager().getPersistentAttributes();
       persistentAttributes.put("foo", "baz");
       return Optional.empty();
   }
