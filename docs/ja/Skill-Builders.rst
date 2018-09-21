スキルビルダー
============

SDKには、Skillインスタンスの作成に役立つ2つのスキルビルダーが含まれています。

`CustomSkillBuilder <http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/builder/CustomSkillBuilder.html>`__ は、コアSDKモジュールに含まれ、スキルの完全なインスタンスを設定し、ビルドできます。これには、リクエストハンドラーや例外ハンドラーの追加、スキルIDの設定、 ``ApiClient`` と ``PersistenceAdapter`` 実装の提供を行うビルダーメソッドが含まれます。

`StandardSkillBuilder <http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/builder/StandardSkillBuilder.html>`__ は、標準SDK配布パッケージに含まれ、標準配布パッケージに含まれている機能のビルダーメソッドを追加で提供します。これには、Apache HTTP Client APIクライアントプラグインとDynamoDBPersistenceAdapterが含まれます。たとえば、Dynamoアダプターを明示的に提供する代わりに、ビルダーはwithTableNameなどのメソッドを使用してこのコンフィギュレーションを直接提供し、自動的にアダプターを設定します。Apache APIクライアントも、標準ビルダーを使用している場合は自動的に設定されます。

