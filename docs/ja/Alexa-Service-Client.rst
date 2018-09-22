Alexaサービスクライアント
======================

サービスクライアントインスタンスを作成する
-----------------------------------

SDKには、スキルのロジックからAlexa APIを呼び出すのに使用するサービスクライアントが含まれます。

サービスクライアントは、リクエストハンドラー、例外ハンドラー、リクエストと応答のインターセプターで使用できます。 ``HandlerInput`` に含まれる ``ServiceClientFactory`` により、サポートされているすべてのAlexaサービスのクライアントインスタンスを取得することができます。

以下は、リクエストハンドラーの ``handle`` メソッドの例です。 ``DirectivesService`` クライアントのインスタンスが作成されます。サービスクライアントインスタンスは、適切なfactoryメソッドを呼び出すだけで簡単に作成できます。

.. code:: java

    @Override
    public Optional<Response> handle(HandlerInput input) {
        DirectivesService directivesServices = input.getServiceClientFactory().getDirectivesService();
        directivesServices.enqueue(SendDirectiveRequest.builder().build());
        // other handler logic goes here
    }
 
作成したら、サービスクライアントインスタンスを使用して、サービスで明示されたAPI操作を呼び出すことができます。

