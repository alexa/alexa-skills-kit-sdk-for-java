ASK SDK v2 for Javaの移行ガイド
=============================

このガイドでは、Alexa Skills Kit SDK v2 for Javaを使用してSDK v1で開発された既存のスキルをv2に移行する方法を説明します。このバージョンのSDKを使ってスキル開発者はより堅牢なスキルを作成し、コードを適切に整理できます。また、以下で説明する改善されたコア機能も利用できます。

ASK SDK v2をプロジェクトに追加する
------------------------------

``pom.xml`` で、スキルのSDK v1バージョンの以下のコードを使用します。

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>alexa-skills-kit</artifactId>
        <version>1.8.1</version>
        <scope>compile</scope>
    </dependency>

これを以下の新しい依存関係に置き換えます。 ``artifactId`` と ``version`` が変更されています。

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk</artifactId>
        <version>2.3.4</version>
    </dependency>`

リクエストハンドラー
-----------------

SDK v2にはリクエストハンドラーの概念が導入されています。リクエストハンドラーは、受け取った1つ以上のタイプのリクエストを処理できる個別のコンポーネントです。ハンドラーには、ハンドラーを呼び出すかどうかを決定するcanHandleメソッドと、処理ロジックを含むhandleメソッドが含まれています。この方法により、スキルロジックが構造化され整理されたものとなり、単一のモノシリックなクラスに含まれなくなります。

以下は、SDK v1を使用して作成されたHello Worldサンプルの一部です。``getWelcomeResponse`` 、 ``getAskResponse`` 、 ``getHelpResponse`` などのすべてのヘルパー関数実装は、応答ビルダーのセクションで取り上げられているため、省略しています。

.. code:: java

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
                return getAskResponse("HelloWorld", "This is unsupported. Please try something else.");
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
            String speechText = "ようこそ、アレクサスキルキットへ。こんにちは、と言ってみてください。";
            return getAskResponse("HelloWorld", speechText);
        }
        ...
    }

SDK v2では、onLaunch、onSessionEnded、およびonIntentがそれぞれ異なるハンドラーに分けられています。

.. code:: java

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

.. code:: java

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

.. code:: java

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

.. code:: java

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

リクエストハンドラーの詳細については、技術資料の `リクエストハンドラー <https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Request-Processing.html#request-handlers>`__ をご覧ください。

アトリビュートマネージャー
----------------------

現在のセッションの存続期間中存続するセッションアトリビュートは、受信するRequestEnvelopeから取得できますが、他のアトリビュートストレージはSDK v1でサポートされません。SDK v2には、アトリビュートの次のスコープを管理するアトリビュートマネージャーの概念が導入されています。

1.	リクエストレベル。インターセプターを含め、現在のリクエストの間のみ持続します。
2.	セッションレベル。現在のセッションの間持続します。
3.	永続レベル。現在のセッションのスコープを超えて持続し、次回ユーザーがスキルを呼び出すときに取得できます。

SDK v2ではこれらのアトリビュートがリクエストハンドラーインターフェース、インターセプター、および例外ハンドラーに自動的に渡されます。これにより、アトリビュートの取得や設定の際に毎回保存して取得する必要がありません。次に、 ``canHandle`` メソッドでアトリビュートマネージャーを使ってDynamoDBテーブルから永続アトリビュートを取得する例を示します。 ``handle`` メソッドは、永続アトリビュートの設定方法を示します。

.. code:: java

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

インターセプターの詳細については、技術資料の `リクエストと応答のインターセプター <https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Request-Processing.html#request-and-response-interceptors>`__ をご覧ください。

応答ビルダー
----------

応答ビルダーを使うと、 ``SpeechletResponse`` の各要素を作成するヘルパー関数を手動で作成する必要がありません。

次に、SDK v1サンプルから取得したnewAskResponseメソッドのスニペットを示します。

.. code:: java

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

v2 SDKでは、応答ビルダーを使ってhandleメソッド内に応答を作成すると、コードの冗長性が軽減されます。

.. code:: java

    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(outputSpeech)
                .withReprompt(repromptSpeech)
                .build();
    }

応答ビルダーの詳細については、技術資料の `応答のビルド <https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Response-Building.html>`__ をご覧ください。

例外ハンドラー
-----------

例外ハンドラーはリクエストハンドラーに似ていますが、リクエスト処理中に例外がスローされると呼び出される点が異なります。例外ハンドラーには受信する例外タイプで動作する ``canHandle`` メソッドと、例外を処理する ``handle`` メソッドがあります。例外処理にtry-catchブロックを使う代わりに、特定の例外タイプの例外ハンドラー、またはすべての例外でグローバルに機能する1つの例外ハンドラーを作成できます。以下に、catch-all例外ハンドラーの例を示します。

.. code:: java

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

例外ハンドラーの詳細については、技術資料の `例外ハンドラー <https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Request-Processing.html#exception-handlers>`__ をご覧ください。

Alexaサービスのサポート
--------------------

SDK v1では、HouseholdListサービスやDirectiveサービスなどの外部のAlexa APIを呼び出すためのサポートが制限されていました。SDK v2は、Alexa API呼び出しに対して低レベルのプラガブルHTTPクライアントをサポートし、API呼び出しでその呼び出しに関連するリクエストアトリビュートに渡すだけで済むようにエンドポイントと認証情報の解決を処理します。

次に、SDK v1でデバイスアドレスを取得する方法を示す、 `デバイスアドレスサンプル <https://github.com/alexa/skill-samples-java/blob/master/address/src/com/amazon/asksdk/address/DeviceAddressSpeechlet.java>`__ から抜粋したスニペットを示します。

.. code:: java

    SystemState systemState = getSystemState(speechletRequestEnvelope.getContext());
    String apiAccessToken = systemState.getApiAccessToken();
    String deviceId = systemState.getDevice().getDeviceId();
    String apiEndpoint = systemState.getApiEndpoint();

    AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(
            deviceId, apiAccessToken, apiEndpoint);

    Address addressObject = alexaDeviceAddressClient.getFullAddress();

SDK v2では、少ないコードを使ってデバイスアドレスを取得でき、 ``AlexaDeviceAddressClient`` を実装する必要はありません。

.. code:: java

    DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
    String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
    Address address = deviceAddressServiceClient.getFullAddress(deviceId);

Streamハンドラー
--------------

SDK v1の ``RequestSpeechletStreamHandler`` は主にAWS Lambda関数のスキルIDを追加していました。

.. code:: java

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

SDK v2では、スキルインスタンスの作成とハンドラーの登録にビルダーパターンが使用されます。次に、リクエストハンドラー、例外ハンドラー、その他のハンドラーを設定する例を示します。

.. code:: java

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

