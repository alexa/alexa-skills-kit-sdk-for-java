=====
Response Building
=====

Response Builder
------------------------------

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

The following example shows how to construct a response using
``ResponseBuilder`` helper methods.

::

   @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
            .withSimpleCard("title", "cardText")
            .withSpeech("foo")
            .withReprompt("bar")
            .build();
    }
