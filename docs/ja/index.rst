..  toctree::
    :caption: ガイド
    :hidden:
    :maxdepth: 2

    ASK SDKのセットアップ
    初めてのスキル開発
    ASK SDK v2 For Javaへの移行

..  toctree::
    :caption: 技術資料
    :hidden:

    リクエスト処理
    スキルのアトリビュート
    Alexaサービスクライアント
    応答のビルド
    スキル内課金の管理
    Javadocのリファレンス<http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/>

.. toctree::
   :caption: その他のリソース
   :hidden:

   SDKの問題と機能のリクエスト<https://github.com/alexa/alexa-skills-kit-sdk-for-java/issues>
   Alexa機能のリクエスト<https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote>
   Alexa開発者フォーラム<https://forums.developer.amazon.com/spaces/165/index.html>

.. toctree::
   :caption: その他の言語のASK SDK
   :hidden:

   NodeJS SDK <https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs>
   Python SDK <https://github.com/alexa-labs/alexa-skills-kit-sdk-for-python>


ASK SDK for Javaを使うと、ボイラープレートコード（毎回書かなければならないお決まりのコード）を書く手間が不要になります。これにより空いた時間をさまざまな機能の実装に充てることができ、人気のスキルをより簡単に作成できるようになります。


SDKの使用に役立ついくつかのサンプル、リファレンス、ガイドをご用意しました。以下のリンクを使ってこれらのドキュメントを簡単に見つけることができます。また各ドキュメントの内容も説明します。

ガイド
-----

`ASK SDKのセットアップ <<Setting-Up-The-ASK-SDK.html>>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Mavenプロジェクトに依存関係としてSDKを含める方法を説明します。

`初めてのスキル開発 <<Developing-Your-First-Skill.html>>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Hello Worldサンプルをビルドする手順を詳しく説明します。

`ASK SDK v2 For Javaへの移行 <Migrating-To-ASK-SDK-v2-For-Java.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

既存のスキルをv1からv2 SDKに移行する方法を説明します。

技術資料
-------

`リクエスト処理 <Request-Processing.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

受信するリクエストを処理するリクエストハンドラー、およびスキルのエラーを処理する例外ハンドラーを作成する方法、またリクエストと応答のインターセプターを使ってハンドラーの実行前または実行後にタスクを実行する方法を説明します。

`スキルのアトリビュート <Skill-Attributes.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

3つの異なるスコープ（リクエスト、セッション、永続）のスキルのアトリビュートを使ってスキルデータを保存および取得する方法を説明します。

`Alexaサービスクライアント <Alexa-Service-Clients.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

サービスクライアントを使ってスキルからAlexa APIにアクセスする方法を説明します。

`応答のビルド <<Response-Building.html>>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

応答ビルダーを使って、テキスト、カード、オーディオといった複数の要素を使用して1つの応答を簡単に構成する方法を説明します。

`スキルビルダー <Skill-Builders.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

スキルビルダーを使って設定されたSDKインスタンスを簡単に作成する方法を説明します。

`スキル内課金の管理 <Managing-In-Skill-Purchases.html>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

スキル内商品とスキル内課金エクスペリエンスの管理方法を説明します。

`Javadocのリファレンス <http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com>`__
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

SDKの最新バージョンのJavadocのリファレンスです。

フィードバック
-----------

Alexaの機能に関するリクエストや投票は、\ `こちら <https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote>`__\ をご覧ください。
