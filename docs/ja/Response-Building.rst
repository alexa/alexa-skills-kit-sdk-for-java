応答のビルド
==========

応答ビルダー
----------

SDKには応答を作成するためのヘルパーメソッドが含まれています。 ``Response`` には複数の要素が含まれる場合があり、ヘルパーメソッドによって、各応答の要素を初期化したり設定したりする必要がなくなり、応答を生成しやすくなります。

使用できるヘルパーメソッド
~~~~~~~~~~~~~~~~~~~~~

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
-  ``withCanFulfillIntent(CanFulfillIntent canFulfillIntent)``（ `public beta SDK <https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.x_public-beta>`__ でのみ使用可能）

目的の応答要素を追加したら、 ``build()`` メソッドを呼び出して ``Response`` を生成できます。

以下の例は、 ``ResponseBuilder`` ヘルパーメソッドを使用して応答を作成する方法を示しています。

.. code:: java

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSimpleCard("title", "cardText")
                .withSpeech("foo")
                .withReprompt("bar")
                .build();
    }

