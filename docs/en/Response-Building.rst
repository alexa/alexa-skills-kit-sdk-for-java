Response Building
=================

Response Builder
----------------

The SDK includes helper methods for constructing responses. A
``Response`` may contain multiple elements, and the helper methods aid
in generating responses, reducing the need to initialize and set the
elements of each response.

Available helper methods
~~~~~~~~~~~~~~~~~~~~~~~~

-  ``withSpeech(String speechText)``
-  ``withSimpleCard(String cardTitle, String cardText)``
-  ``withStandardCard(String cardTitle, String cardText, Image image)``
-  ``withReprompt(String text)``
-  ``withShouldEndSession(Boolean shouldEndSession)``
-  ``addHintDirective(String hintText)``
-  ``addVideoAppLaunchDirective(String source, String title, String subTitle)``
-  ``addTemplateDirective(Template template)``
-  ``addAudioPlayerPlayDirective(PlayBehavior playBehavior, Long offsetInMilliseconds, String expectedPreviousToken, String token, String url)``
-  ``addAudioPlayerStopDirective()``
-  ``addAudioPlayerClearQueueDirective(ClearBehavior clearBehavior)``
-  ``addDirective(Directive directive)``
-  ``withCanFulfillIntent(CanFulfillIntent canFulfillIntent)`` (only
   available in the `public beta
   SDK <https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.x_public-beta>`__)

Once you add the desired response elements, you can generate a
``Response`` by calling the ``build()`` method.

Example usage of Response Builder
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The following example shows how to construct a response using
``ResponseBuilder`` helper methods.

.. code:: java

   @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSimpleCard("title", "cardText")
                .withSpeech("foo")
                .withReprompt("bar")
                .build();
    }

Construct a Response with a Template
-------------------------------------
The SDK allows you to use templates to construct skill responses. A *template* is similar to a *view* in the model–view–controller (MVC) pattern, which is commonly used to build dynamic web pages. Using a template to construct a response provides the following benefits:

- It can help you generate a ``Response`` by separating the presentation logic from the request handling logic.
- It can help make it easier to generate a ``Response`` with a complex and nested structure, for example when using `Alexa Presentation Language <https://developer.amazon.com/docs/alexa-presentation-language/apl-overview.html>`__.
- It can help to reduce duplicate code by reusing common templates across skills.

You can include multiple response components in a single template, for example ``outputSpeech``, ``card``, ``shouldEndSession``, ``directives``, and more. These components can contain static data and placeholders for dynamic data, and will be built into a full skill response.

To generate a ``Response`` using a template, you need to configure the ``Template Factory`` when building the skill.

Template Factory
~~~~~~~~~~~~~~~~
The template factory interface processes a response template by injecting dynamic data to generate the skill response. It is the entry point that you should call when building a skill response inside of a ``RequestHandler``. You can implement your own unique template factory, or leverage the ``BaseTemplateFactory`` provided in the SDK, which consists of a list of ``Template Loader`` and ``Template Renderer`` objects.

Template Loader
~~~~~~~~~~~~~~~

The template loader interface loads template content from data storage. The SDK provides the ``LocalTemplateFileLoader`` to load a template file from the local file system. You can implement your own loader to load a template from a different data storage location.

To use ``LocalTemplateFileLoader``, you must provide the directory path and the file extension for the template files. If you host your skill on AWS Lambda, you must include the template files as resources in the skill project JAR, and you must configure the resource directory in your Maven POM.

**Template directory structure**

The following screenshot of an example skill shows the template directory structure containing FreeMarker template files:

.. image:: images/template_directory_structure.png

**Maven resource configuration**
::

    <resources>
      <resource>
        <directory>src/resources</directory>
      </resource>
    </resources>

**Template Enumerator**

You can generate different responses for different locales. To use different template files for different locales, the SDK provides the ``TemplateEnumerator`` interface and a ``LocaleTemplateEnumerator`` implementation to enumerate possible template locations and names based on the `locale <https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#request-locale>`__ property in the skill request. For example, ``en-US`` is the locale property for a template file located at ``template/en/US`` or named ``template_en_US``. 

As shown in the preceding screenshot, you can name the template file ``example_response``, or put it in a locale-specific folder inside the ``base_response_template`` folder. The ``LocaleTemplateEnumerator`` first tries to find the most specific template file (for example, ``base_response_template/de/DE``), then falls back to a less specific one (``base_response_template/de``), then finally a global one (``base_response_template``).

You can implement your own template enumerator to enumerate templates according to your preference.

**Template Cache**

To facilitate the template loading process, the SDK provides the ``TemplateCache`` interface, and provides a ``ConcurrentLRUTemplateCache`` to cache loaded templates for future use. The ``ConcurrentLRUTemplateCache`` supports concurrent caching, has a capacity of 5 MB, and a time-to-live (TTL) of 24 hours by default. You can modify these values according to your needs, or implement your own template cache. 

Template Renderer
~~~~~~~~~~~~~~~~~

The template renderer interface renders a full template including dynamic data, and converts it into a skill ``Response``. The SDK provides the ``FreeMarkerTemplateRenderer`` implementation to render a `FreeMarker template <https://freemarker.apache.org/>`__. This allows you to leverage FreeMarker features including `macro <https://freemarker.apache.org/docs/ref_directive_macro.html>`__, `import <https://freemarker.apache.org/docs/ref_directive_import.html>`__, and more. You can also implement your own template renderer to support other template engines. 

To use the ``FreeMarkerTemplateRenderer``, you must add a dependency on ``ask-sdk-freemarker`` in your Maven project:

::

    <dependency>
      <groupId>com.amazon.alexa</groupId>
      <artifactId>ask-sdk-freemarker</artifactId>
      <version>${version}</version>
    </dependency>

Example Usage of the Template Factory
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The following example shows how to configure the ``BaseTemplateFactory`` using the default ``LocalTemplateFileLoader`` and ``ConcurrentLRUTemplateCache`` to construct a skill response with a FreeMarker template.

**Configure Template Factory with Template Loader and Template Renderer to Skill Builder**

.. code:: java

    private static Skill getSkill() {

        // Configure LocalTemplateFileLoader
        TemplateLoader loader = LocalTemplateFileLoader.builder()
                .withDirectoryPath("com/amazon/ask/example/")
                .withFileExtension("ftl")
                .build();

        // Configure FreeMarkerTemplateRenderer
        JacksonJsonUnmarshaller jacksonJsonUnmarshaller = JacksonJsonUnmarshaller
                .withTypeBinding(Response.class);
        TemplateRenderer renderer = FreeMarkerTemplateRenderer.builder()
                .withUnmarshaller(jacksonJsonUnmarshaller)
                .build();

        // Configure BaseTemplateFactory
        TemplateFactory templateFactory = BaseTemplateFactory.builder()
                .withTemplateRenderer(renderer)
                .addTemplateLoader(loader)
                .build();

        // Build Skill
        return Skills.standard()
                .withTemplateFactory(templateFactory)
                .addRequestHandlers(
                        new LaunchRequestHandler(),
                        ... ...
                        new SessionEndedRequestHandler())
                .build();
    }

**Generate a response using a template in the Request Handler**

.. code:: java

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Hi, welcome to templating response.";

        // Provide dynamic data to template
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("outputSpeechText", speechText);

        return input.generateTemplateResponse("base_response_template", datamap);
    }

Using the preceding example code, the following example of a FreeMarker template should have a full resource path of ``/com/amazon/ask/example/base_response_template/en/US.ftl``, with the directory path ``/com/amazon/ask/example/`` and file extension ``ftl`` passed into the ``LocalTemplateFileLoader``, and the locale property of ``en-US`` from the ``Request`` passed into the ``LocaleTemplateEnumerator``.

**Example FreeMarker template**

The following example shows a FreeMarker template for the `OutputSpeech <https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#outputspeech-object>`__ component in a skill response.

::

  {
    "outputSpeech": {
        "type": "PlainText",
        "text": "${outputSpeechText}"
    }

