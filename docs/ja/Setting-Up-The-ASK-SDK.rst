ASK SDK for Javaのセットアップ
===========================

このガイドでは、プロジェクトでASK SDK v2 for Javaを使用する方法を説明します。

前提条件
------

-  `Apache Maven <https://maven.apache.org/>`__ プロジェクト。
-  適切なJava開発環境。ASK SDK v2 for Javaには、Java 8と互換性のある実行時環境が必要です。

SDKをプロジェクトに追加する
-----------------------

プロジェクトでASK SDK v2を使用するには、それをプロジェクトの ``pom.xml`` ファイルで依存関係として宣言します。標準SDK配布パッケージに依存するか、依存するコンポーネントを個別に選択することができます。SDKを導入して使用を開始するには、標準SDK配布パッケージを選ぶのがもっとも簡単です。これにはコアSDKと、一般的に使用される機能を提供する以下の追加モジュールが含まれています。

-  AWS Lambdaサポートモジュール。スキルをAWS Lambda関数の一部として実行できます。
-  サーブレットサポートモジュール。スキルをウェブサーバーのサーブレットとして実行できます。
-  Amazon DynamoDB永続アダプター。DynamoDBにスキルのアトリビュートを保存できます。
-  Apache HTTPClient APIクライアントプラグイン。Apache HttpClientを利用してAlexaサービスのAPI呼び出しを行います。

すべての標準SDKコンポーネントを含める
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Mavenプロジェクトの ``pom.xml`` から、以下の依存関係を追加して、標準ASK SDK v2 for Java配布パッケージに挿入します。

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk</artifactId>
        <version>2.3.1</version>
    </dependency>

個別の標準SDKコンポーネントを含める
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

次の1つ以上のモジュールの対応するエントリを ``pom.xml`` ファイルに追加して、プロジェクトに含めるモジュールを選択します。依存関係の遮断を最小限に抑えるために、必要なモジュールのみを追加します。

**コアSDK（必須）**

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk-core</artifactId>
        <version>2.3.1</version>
    </dependency>

**AWS Lambdaサポートモジュール**

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk-lambda-support</artifactId>
        <version>2.3.1</version>
    </dependency>

**サーブレットサポートモジュール**

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk-servlet-support</artifactId>
        <version>2.3.1</version>
    </dependency>

**Amazon DynamoDB永続アダプター**

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk-dynamodb-persistence-adapter</artifactId>
        <version>2.3.1</version>
    </dependency>

**Apache HTTPClient APIクライアントプラグイン**

::

    <dependency>
        <groupId>com.amazon.alexa</groupId>
        <artifactId>ask-sdk-apache-client</artifactId>
        <version>2.3.1</version>
    </dependency>

次のステップ
---------

プロジェクトにSDKを追加したら、スキルの開発を開始できます。次の `初めてのスキル開発 <Developing-Your-First-Skill.html>`__ セクションに進み、基本のスキル開発の手順をご覧ください。

