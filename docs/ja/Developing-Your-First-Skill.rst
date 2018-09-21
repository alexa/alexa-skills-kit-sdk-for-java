初めてのスキル開発
===============

このガイドでは、ASK SDK v2 for Javaを使ったスキル開発の手順を説明します。

前提条件
------

-  `Amazon開発者 <https://developer.amazon.com/>`__ アカウント。Alexaスキルの作成と設定に必要です。
-  `アマゾンウェブサービス(AWS) <https://aws.amazon.com/>`__ アカウント。このガイドで、AWS Lambdaでスキルをホスティングする手順を確認できます。
-  SDKに対応したMavenプロジェクト。`ASK SDK v2 for Javaのセットアップ <Setting-Up-The-ASK-SDK.html>`__ をご覧ください。サンプルスキルには、標準SDK配布パッケージが必要です。依存関係をカスタマイズする場合は、コアSDKおよびAWS Lambdaのサポートモジュールを含める必要があります。

リクエストハンドラーを実装する
-------------------------

まず、スキルで受信するさまざまなタイプのリクエストを処理するのに必要なリクエストハンドラーを作成します。

LaunchRequestハンドラー
~~~~~~~~~~~~~~~~~~~~~

以下は、スキルが ``LaunchRequest`` を受信した時に呼び出されるハンドラーを設定するコードのサンプルです。特定のインテントなしでスキルが呼び出された場合、``LaunchRequest`` イベントが発生します。

.. code:: java

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
            String speechText = "ようこそ、アレクサスキルキットへ。こんにちは、と言ってみてください。";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("HelloWorld", speechText)
                    .withReprompt(speechText)
                    .build();
        }

    }

受信したリクエストがLaunchRequestの場合、 ``canHandle`` メソッドはtrueを返します。``handle`` メソッドは、Speech、Card、Repromptなどの応答オブジェクトを使用して基本的なあいさつの応答を生成して返します。これらのオブジェクトの詳細は、 \ `こちら <https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#response-object>`__\ をご覧ください。

HelloWorldIntentハンドラー
~~~~~~~~~~~~~~~~~~~~~~~~

以下は、スキルが ``HelloWorldIntent`` を受信した時に呼び出されるハンドラーを設定するコードのサンプルです。

.. code:: java

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
            String speechText = "こんにちは";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("HelloWorld", speechText)
                    .build();
        }

    }

ハンドラーのcanHandleメソッドは受け取るリクエストが ``IntentRequest`` かどうかを検出し、インテント名が ``HelloWorldIntent`` の場合にtrueを返します。次に、基本的な「こんにちは」という応答が生成されて返されます。

HelpIntentハンドラー
~~~~~~~~~~~~~~~~~~

以下は、 `スキルがビルトインインテント <https://developer.amazon.com/docs/custom-skills/standard-built-in-intents.html#available-standard-built-in-intents>`__ ``AMAZON.HelpIntent`` を受信した時に呼び出されるハンドラーを設定するコードのサンプルです。

.. code:: java   

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
            String speechText = "こんにちは、と言ってみてください。";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("HelloWorld", speechText)
                    .withReprompt(speechText)
                    .build();
        }
    }

さきほどのハンドラー同様、このハンドラーは ``IntentRequest`` を想定されるインテント名と照合します。基本のヘルプ手順が返されます。

CancelAndStopIntentハンドラー
~~~~~~~~~~~~~~~~~~~~~~~~~~~

このハンドラーもビルトインインテントによって呼び出されるため、HelpIntentハンドラーに非常に似ています。ただしこの場合は、1つのハンドラーを使ってAmazon.CancelIntentとAmazon.StopIntentという2つのインテントに応答することにしました。

.. code:: java

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
                    .withSpeech("さようなら")
                    .withSimpleCard("HelloWorld", "さようなら")
                    .build();
        }
    }
これら両方のインテントに対する応答は同じであるため、1つのハンドラーにすることで重複するコードを減らせます。

FallbackIntent handler
~~~~~~~~~~~~~~~~~~~~~~~~~~~

以下は、 `スキルがビルトインインテント <https://developer.amazon.com/docs/custom-skills/standard-built-in-intents.html#available-standard-built-in-intents>`__ ``AMAZON.FallbackIntent`` を受信した時に呼び出されるハンドラーを設定するコードのサンプルです。このインテントが使用できるのは、現在英語（米国）でのみです。

.. code:: java

    package com.amazon.ask.helloworld.handlers;

    import com.amazon.ask.dispatcher.request.handler.HandlerInput;
    import com.amazon.ask.dispatcher.request.handler.RequestHandler;
    import com.amazon.ask.model.Response;

    import java.util.Optional;

    import static com.amazon.ask.request.Predicates.intentName;

    public class FallbackIntentHandler implements RequestHandler {

        @Override
        public boolean canHandle(HandlerInput input) {
            return input.matches(intentName("AMAZON.FallbackIntent"));
        }

        @Override
        public Optional<Response> handle(HandlerInput input) {
            String speechText = "すみません、それは分かりません。ヘルプと言ってみてください。";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("HelloWorld", speechText)
                    .withReprompt(speechText)
                    .build();
        }
    }

SessionEndedRequestハンドラー
~~~~~~~~~~~~~~~~~~~~~~~~~~~

SessionEndedRequestを受け取った後は、応答を返すことはできませんが、クリーンアップロジックを追加するにはこのハンドラーは最適な場所です。

.. code:: java

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
            //クリーンアップロジックをここに追加します
            return input.getResponseBuilder().build();
        }
    }
 
SkillStreamHandlerを実装する
--------------------------

streamハンドラーは、AWS Lambda関数のエントリーポイントとなります。

次のstreamハンドラーは、SDKで提供されるSDK SkillStreamHandlerクラスを拡張します。このクラスは受信するすべてのリクエストをスキルにルーティングします。``HelloWorldStreamHandler`` は、作成したリクエストハンドラーを使用して設定されたSDKの ``Skill`` インスタンスを作成します。

.. code:: java

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
                     .addRequestHandlers(
                            new CancelandStopIntentHandler(), 
                            new HelloWorldIntentHandler(), 
                            new HelpIntentHandler(), 
                            new LaunchRequestHandler(), 
                            new SessionEndedRequestHandler())
                     .build();
         }
     
         public HelloWorldStreamHandler() {
             super(getSkill());
         }
     
     }

``getSkill`` メソッドは、 ``Skills.standard`` ビルダーを使用してSDKインスタンスを作成します。リクエストハンドラーのインスタンスを作成し、 ``addRequestHandlers`` ビルダーメソッドでスキルに登録します。HelloWorldStreamHandlerコンストラクターは、作成されたSkillインスタンスをスーパークラスSkillStreamHandlerのコンストラクターに渡します。
streamハンドラークラスの完全修飾クラス名は、パッケージとクラス名で構成され、AWS Lambda関数を設定する時は必須です。この例では、完全修飾クラス名は ``com.amazon.ask.helloworld.HelloWorldStreamHandler`` です。

スキルをビルドする
---------------

スキルコードが完成したら、スキルプロジェクトをビルドできます。スキルをAWS Lambdaにアップロードするには、スキルと必要な依存関係を含むJARファイルを作成する必要があります。これを行うには、ターミナルを開き、pom.xmlを含むMavenプロジェクトの上位レベルのディレクトリに移動して、次のコマンドを実行します。

``mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package``

このコマンドで、 ``<my_project_name>.<my_project_version>-jar-with-dependencies.jar`` ファイルがtargetディレクトリに作成されます。

スキルをAWS Lambdaにアップロードする
--------------------------------

1.	AWSのアカウントをまだお持ちでない場合は、`アマゾンウェブサービス <http://aws.amazon.com/>`__ にアクセスしてアカウントを作成します。
2.	`AWSマネジメントコンソール <http://aws.amazon.com/>`__ にログインし、AWS Lambdaに移動します。
3.	コンソールの右上隅にある地域のドロップダウンリストをクリックし、Alexaスキルでサポートされる地域を1つ選択します。 アジアパシフィック（東京）、EU（アイルランド）、米国東部（バージニア北部）、米国西部（オレゴン）のいずれかを選択します。
4.	まだLambda関数がない場合は、今すぐ始めるをクリックします。または、関数の作成をクリックします。
5.	一から作成オプションが選択されていることを確認します。
6.	関数の名前を入力します。
7.	関数のロールを選択します。ロールは、関数がアクセスできるAWSリソースを定義するものです。

    -  既存のロールを使用するには、既存のロールを選択でロールを選択します。
    -  新しいロールを作成するには、後述の `関数の新しいロールを定義する <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#define-new-role>`__ を参照してください。

8.	ランタイムに使用する言語（この例ではJava 8）を選択します。
9.	関数の作成をクリックします。
10.	`こちらの手順 <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#configuring-the-alexa-skills-kit-trigger>`__ に従い、関数のAlexa Skills Kitトリガーを設定します。 `Alexa Skills Kitトリガーを追加 <https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html#add-ask-trigger>`__ していることを確認してください。
11.	前の手順の関数コードで作成したJARファイルをアップロードします。
12.	streamハンドラークラスの完全修飾クラス名を使用してハンドラー情報を入力します。
13.	最後に、AWS Lambda関数のARNをコピーします。このARNはAmazon開発者コンソールでスキルを設定する際に必要となります。これは右上隅で確認できます。

スキルの設定とテストを行う
----------------------

スキルコードをAWS Lambdaにアップロードしたら、Alexaのスキルを設定できます。最初に、 `Alexa Skills Kit開発者コンソール <https://developer.amazon.com/alexa/console/ask>`__ に移動します。右上のスキルの作成ボタンをクリックします。スキル名として「HelloWorld」を入力します。次のページで、カスタムを選択してからスキルを作成をクリックします。
これで、スキルの対話モデルを定義できます。左側の呼び出し名タブで、スキルの呼び出し名をごあいさつになるよう定義します。
次に、インテントをスキルに追加します。対話モデルのインテントセクションの下の追加ボタンをクリックします。カスタムインテントを作成を選択した状態で、インテント名として「HelloWorldIntent」を入力し、インテントを作成します。ここで、ユーザーがこのインテントを呼び出すのに使用できるサンプル発話をいくつか追加します。この例では、以下のサンプル発話を追加しましたが、これ以外でもかまいません。

:: 

    こんにちはと言っ
    ハロー
    こんにちは
    ハイと言って
    ハイワールドと言って
    おはようございます
    ごきげんいかが

AMAZON.CancelIntent、AMAZON.HelpIntent、AMAZON.StopIntentはAlexaのビルトインインテントで、サンプル発話は自動的に継承されるため追加する必要はありません。
開発者コンソールでは、ナビゲーションバーでJSONエディターを選択してスキルモデル全体をJSON形式で編集することもできます。この例では、以下のJSONスキーマを使用できます。

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
                    "こんにちはと言って",
                    ",ハローワールドと言って",
                    "こんにちは",
                    "ハイと言って",
                    ",ハイワールドと言って",
                    "ハイ",
                    "ごきげんいかが"
                ],
                "slots": []
              }
            ],
            "invocationName": "ごあいさつ"
        }
    }

対話モデルの編集が完了したら、モデルを保存してビルドするのを忘れないでください。

スキルのコンフィギュレーションのセクションに進みましょう。エンドポイントでAWS LambdaのARNを選択し、さきほど作成した関数のARNを貼り付けます。残りの設定は、デフォルト値のままでかまいません。エンドポイントを保存をクリックし、次のセクションに進みます。

最後にスキルをテストできます。 開発者コンソールのテストタブで、スキルに対し、テキストや音声でリクエストをシミュレーションできます。呼び出し名と、さきほど設定したサンプル発話のうち1つを使います。たとえば、「アレクサ、ごあいさつを開いてこんにちはと言って」と言うと、スキルは「こんにちは」と答えるはずです。 `Echoウェブページ <http://echo.amazon.com/#skills>`__ に移動し、スキルにリストされているスキルを確認できます。ここでは、Alexa搭載デバイスからのテスト用にアカウントでスキルを有効にできます。

この時点で、インテントスキーマや、スキルの実装に対応するリクエストハンドラーを試してみてください。一通りのテストが完了したら、スキルの認定を申請して世界中のAlexaユーザーに公開するプロセスに進むことができます。

