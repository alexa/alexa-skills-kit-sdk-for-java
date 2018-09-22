リクエスト処理
============

リクエストハンドラー
-----------------

リクエストハンドラーは、受け取った1つ以上のタイプのリクエストを処理します。

リクエストハンドラーを作成するには、次の2つのメソッドで構成される ``RequestHandler`` インターフェースを実装します。

-  ``canHandle``。SDKによって呼び出され、指定されたハンドラーが受け取ったリクエストを処理できるかどうかを判断します。ハンドラーがリクエストを処理できる場合はtrue、できない場合はfalseを返します。受信するリクエストのタイプやパラメーター、スキルのアトリビュートなど、この判断を行うための条件を柔軟に選択できます。
-  ``handle``。リクエストハンドラーを呼び出す時にSDKによって呼び出されます。このメソッドには、ハンドラーのリクエスト処理ロジックが含まており、オプションの ``Response`` を返します。

以下は、 ``HelloWorldIntent`` を処理するよう設定されたリクエストハンドラーの例です。

.. code:: java

    public class HelloWorldHandler implements RequestHandler {
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

ハンドラーの ``canHandle`` メソッドは、受信するリクエストが、予想されるインテント名の ``IntentRequest`` かどうかを検出し、インテント名が ``HelloWorldIntent`` の場合はtrueを返すよう設定されています。 ``handle`` メソッドで基本の「こんにちは」という応答が生成され、返されます。

SDKには、インテント名、スロット値、アトリビュートなど共通の条件を簡単に評価できる、プリビルドされた一連のcanHandle述部が含まれています。

SDKは、リクエストハンドラーでスキルビルダーに提供した順序で ``canHandle`` メソッドを呼び出します。

.. code:: java

    return Skills.standard()
        .addHandlers(new FooHandler(), new BarHandler(), new BazHandler())
        .build();

この例では、SDKが以下の順序でリクエストハンドラーを呼び出します。

1.	FooHandler
2.	BarHandler
3.	BazHandler

SDKは、指定されたリクエストを処理できる最初のハンドラーを常に選択します。この例では、 ``FooHandler`` と ``BarHandler`` の両方が特定のリクエストを処理できる場合、常に ``FooHandler`` が呼び出されます。リクエストハンドラーのデザインや登録を行う際には、この点を考慮に入れてください。

リクエストと応答のインターセプター
----------------------------

SDKは、 ``RequestHandler`` の実行前と実行後に実行するリクエストと応答のインターセプターをサポートします。リクエストインターセプターは、応答インセプターに ``RequestInterceptor`` インターフェースか ``ResponseInterceptor`` インターフェースを使用して実装できます。

どちらのインターセプターインターフェースも、戻り値の型がvoidである ``process`` メソッドを1つ実行します。リクエストのインターセプターは ``HandlerInput`` インスタンスにアクセスでき、応答のインターセプターは ``HandlerInput`` と、 ``RequestHandler`` によって生成される ``Optional<Response>`` にアクセスできます。

.. code:: java

    public class PersistenceSavingResponseInterceptor implements ResponseInterceptor {
        @Override
        public void process(HandlerInput input, Optional<Response> output) {
            input.getAttributesManager().savePersistentAttributes();
        }
    }

リクエストのインターセプターは、受け取るリクエストのリクエストハンドラーが実行される直前に呼び出されます。リクエストアトリビュートは、リクエストのインターセプターがリクエストハンドラーにデータやエンティティを渡す方法を提供します。

応答のインターセプターは、リクエストハンドラーが実行された直後に呼び出されます。応答のインターセプターはリクエストハンドラーを実行して生成される出力結果にアクセスできるため、応答のサニタイズや検証といったタスクに適しています。

以下の例は、 ``スキル`` ビルダーのSDKを使ってインターセプターを登録する方法を示しています。

.. code:: java

    return Skills.standard()
        .addHandlers(new FooHandler(), new BarHandler(), new BazHandler())
        .addRequestInterceptors(new FooRequestInterceptor())
        .addResponseInterceptors(new BarResponseInterceptor())
        .build();

例外ハンドラー
------------

例外ハンドラーはリクエストハンドラーに似ていますが、リクエストではなく1つまたは複数のタイプの例外を処理します。リクエストの処理中に未処理の例外がスローされると、SDKが例外ハンドラーを呼び出します。

すべての例外ハンドラーは、ExceptionHandlerインターフェースを実装する必要があります。このインターフェースは以下の2つのメソッドで構成されます。
-  ``canHandle`` は、SDKによって呼び出され、指定されたハンドラーが例外を処理できるかどうかを判断します。ハンドラーが例外を処理できる場合は **true**、できない場合は**false**を返します。catch-allハンドラーは常にtrueを返すだけで簡単に導入できます。
-  ``handle`` は、例外ハンドラーを呼び出すときにSDKによって呼び出されます。このメソッドには、例外処理ロジックがすべて含まれ、オプションで ``Response`` を含む場合がある出力を返します。

以下は、``AskSdkException`` タイプの例外を処理するよう設定された例外ハンドラーの例です。

.. code:: java

    public class MyExecptionHandler implements ExceptionHandler {
        @Override
        public boolean canHandle(HandlerInput input, Throwable throwable) {
            return throwable instanceof AskSdkException;
        }

        @Override
        public HandlerOutput handle(HandlerInput input, Throwable throwable) {
            return input.getResponseBuilder()
    .withSpeech("An error was encountered while handling your request.Try again later.")
    .build();
        }
    }

ハンドラーの ``canhandle`` メソッドは、受け取る例外が ``AskSdkException`` のインスタンスである場合にtrueを返します。handleメソッドは、ユーザーに正常なエラー応答を返します。

例外ハンドラーはリクエストハンドラーと同様に実行され、SDKは ``Skill`` で指定した順序でハンドラーにアクセスします。

##ハンドラー入力
--------------

リクエストハンドラー、リクエストと応答のインターセプター、例外ハンドラーにはすべて、呼び出し時にHandlerInputインスタンスが渡されます。このクラスには、リクエスト処理に有効な各種エンティティが含まれます。以下はその例です。

-  **RequestEnvelope**： 受信する ``Request`` と他のコンテキストが含まれます。
-  **AttributesManager**： リクエスト、セッション、永続アトリビュートへのアクセスを提供します。
-  **ServiceClientFactory**： Alexa APIの呼び出しが可能なサービスクライアントを構築します。
-  **ResponseBuilder**： 応答を作成するのを支援します。
-  **Context**： ホストコンテナが渡すオプションのcontextオブジェクトを提供します。たとえば、AWS Lambdaで実行されるスキルの場合は、AWS Lambda関数のcontextオブジェクトになります。
