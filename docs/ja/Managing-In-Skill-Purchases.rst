スキル内課金の管理
===============

ASK SDK for Javaには、利用可能なスキル内商品や課金されたスキル内商品を検索したり、スキル内から課金とキャンセルのリクエストを開始したりするためのサービスクライアントとヘルパークラスが含まれています。

スキル内課金サービス
-----------------

ASK SDK for Javaには、inSkillProducts APIを呼び出す ``MonetizationServiceClient`` が用意されています。このAPIでは、現在のスキルに関連付けられているすべてのスキル内商品を取得し、各スキル内商品が課金可能かまたは現在のユーザーがすでに課金済みかを確認できます。次のメソッドがあります。

.. code:: java

    getInSkillProducts(String locale, String purchasableFilter, String entitledFilter,  String productType, String nextToken, BigDecimal maxResults)
    getInSkillProduct(String locale, String inSkillProductId)

| ``locale`` は、``LaunchRequest request = (LaunchRequest) input.getRequestEnvelope().getRequest(); String locale = request.getLocale();`` のリクエストから取得できます。
| ``purchasableFilter`` には、すべてのスキル内商品を取得する場合は ``null`` 、課金可能かどうかに関する応答をフィルターする場合はPURCHASABLEまたはNOT_PURCHASABLEを指定できます。
| ``entitledFilter`` には、すべてのスキル内商品を取得する場合は ``null`` 、エンタイトルメントのステータスに関する応答をフィルターする場合はENTITLEDまたはNOT_ENTITLEDを指定できます。
| ``productType`` には、すべてのタイプのスキル内商品を取得する場合は ``null`` 、商品タイプでフィルターする場合はENTITLEMENTまたはCONSUMABLEまたはSUBSCRIPTIONを指定できます。
| ``nextToken`` は複数ページのクエリーの場合は必須です。 ``maxResults`` ではスキルでAPI呼び出しごとに取得されるレコードの数を制御できます。デフォルトのページサイズは100レコードです。
| ``inSkillProductId`` には取得するスキル内商品を指定します。

getInSkillProducts()
~~~~~~~~~~~~~~~~~~~~

getInSkillProductsメソッドは、現在のスキルに関連付けられているすべてのスキル内商品を取得し、現在のスキルとユーザーについて各商品の課金可能性とエンタイトルメントのステータスを示します。

.. code:: java

    import com.amazon.ask.model.services.monetization.MonetizationServiceClient;
    import com.amazon.ask.model.services.ServiceException;
    import com.amazon.ask.model.services.monetization.InSkillProduct;
    import com.amazon.ask.model.services.monetization.InSkillProductsResponse;

    @Override
    public Optional<Response> handle(HandlerInput input) {
        try {
            MonetizationServiceClient client = input.getServiceClientFactory().getMonetizationService();
            LaunchRequest request = (LaunchRequest) input.getRequestEnvelope().getRequest();
            String locale = request.getLocale();

            // To fetch all products
            InSkillProductsResponse responseProducts = client.getInSkillProducts(locale, null, null, null, null, null);
            System.out.println("There are " + responseProducts.getInSkillProducts().size() + " buyable products");

        } catch (ServiceException e) {
            System.out.println("Exception occurred in calling getInSkillProducts API.Error code: " + e.getStatusCode());
        }
    }

API応答にはスキル内商品レコードの配列が含まれます。

:: 

    {
        "inSkillProducts":[
            {
                "productId": "amzn1.adg.product....",
                "referenceName": "<Product Reference Name as defined by the developer>",
                "type": "SUBSCRIPTION",               // Or ENTITLEMENT
                "name": "<locale specific product name as defined by the developer>",
                "summary": "<locale specific product summary, as provided by the developer>",
                "entitled": "ENTITLED",              // Or NOT_ENTITLED
                "purchasable": "PURCHASABLE",        // Or NOT_PURCHASABLE
                "purchaseMode": "TEST"               // Or LIVE
                "activeEntitlementCount": 1
            }
        ],
        "isTruncated": true,
        "nextToken": "string"
    }

getInSkillProduct()
~~~~~~~~~~~~~~~~~~~

getInSkillProductメソッドは製品IDで識別される単一のスキル内商品の商品レコードを取得します。

.. code:: java

    import com.amazon.ask.model.services.monetization.MonetizationServiceClient;
    import com.amazon.ask.model.services.ServiceException;
    import com.amazon.ask.model.services.monetization.InSkillProduct;
    import com.amazon.ask.model.services.monetization.InSkillProductsResponse;

    @Override
    public Optional<Response> handle(HandlerInput input) {
        try {
            MonetizationServiceClient client = input.getServiceClientFactory().getMonetizationService();
            LaunchRequest request = (LaunchRequest) input.getRequestEnvelope().getRequest();
            String locale = request.getLocale();

            // To fetch a specific in-skill product by product Id
            String productId = "<your product id with the format 'amzn1.adg.product....'>";
            InSkillProduct responseProduct = client.getInSkillProduct(locale, productId);

            if(responseProduct!=null) {
                System.out.println("Found the product with ID" + productId);
            }
        } catch (ServiceException e) {
            System.out.println("Exception occurred in calling getInSkillProduct API.Error code: " + e.getStatusCode());
        }
    }

API応答には単一のスキル内商品レコードが含まれます。

::

    {
        "productId": "amzn1.adg.product....",
        "referenceName": "<Product Reference Name as defined by the developer>",
        "type": "SUBSCRIPTION",               // Or ENTITLEMENT
        "name": "<locale specific product name as defined by the developer>",
        "summary": "<locale specific product summary, as provided by the developer>",
        "entitled": "ENTITLED",              // Or NOT_ENTITLED
        "purchasable": "PURCHASABLE",        // Or NOT_PURCHASABLE
        "purchaseMode": "TEST"               // Or LIVE
        "activeEntitlementCount": 1
    }

スキル実装でのこれらのAPIとその使い方の詳細については、こちらをご覧ください。 `カスタムスキルへのスキル内課金の追加 <https://developer.amazon.com/docs/in-skill-purchase/add-isps-to-a-skill.html>`__ 。

スキル内課金のインターフェース
-------------------------

ASK SDK for Javaには、スキルでAlexaからスキル内課金とキャンセルのリクエストを開始するためのSendRequestDirectiveが用意されています。Amazonシステムはユーザーとの音声による対話を管理し、課金取引を処理して、ステータス応答をリクエスト元のスキルに返します。このインターフェースを使用して、Upsell、Buy、Cancelの3つのアクションがサポートされます。
これらのアクションと推奨されるユースケースの詳細については、こちらをご覧ください。 `カスタムスキルへのスキル内課金の追加 <https://developer.amazon.com/docs/in-skill-purchase/add-isps-to-a-skill.html>`__ 。

Upsell
~~~~~~

スキルは、ユーザーが明示的にコンテキストをリクエストしなかった場合にスキルのコンテキストを提供するためにUpsellアクションを開始する必要があります。たとえば、無料のコンテンツが提供されている間または後です。Upsellアクションを開始するには、製品IDとアップセルメッセージが必要です。アップセルメッセージを使って、開発者はAlexaで価格を提示する前にユーザーにスキル内商品を提示する方法を指定できます。

.. code:: java

    // Additional include in your handler source file
    import com.amazon.ask.model.interfaces.connections.SendRequestDirective;

    // Prepare the directive payload
    Map<String,Object> mapObject = new HashMap<String,Object>();
    Map<String, Object> inskillProduct = new HashMap<>();
    inskillProduct.put("productId", "< your product id in the format amzn1.adg.product....>"); // Replace productId with your productId
    mapObject.put("upsellMessage","Will you like to buy this product?");
    mapObject.put("InSkillProduct", inskillProduct);

    // Prepare the directive request
    SendRequestDirective directive = SendRequestDirective.builder()
        .withPayload(mapObject)
        .withName("Upsell")
        .withToken("correlationToken")
        .build();
    Optional<Response> response = input.getResponseBuilder()
            .addDirective(directive)
            .withShouldEndSession(true)
            .build();
    // Return directive from Skill context to trigger the action request
    return response;

Buy
~~~

スキルは、ユーザーが特定のスキル内商品の課金をリクエストしたときにBuyアクションを開始します。Buyアクションを開始するには、製品IDが必要です。

.. code:: java

    // Additional include in your handler source
    import com.amazon.ask.model.interfaces.connections.SendRequestDirective;

    // Prepare the directive payload
    Map<String,Object> mapObject = new HashMap<String,Object>();
    Map<String, Object> inskillProduct = new HashMap<>();
    inskillProduct.put("productId", "< your product id in the format amzn1.adg.product....>"); // Replace productId with your productId
    mapObject.put("InSkillProduct", inskillProduct);

    // Prepare the directive request
    SendRequestDirective directive = SendRequestDirective.builder()
        .withPayload(mapObject)
        .withName("Buy")
        .withToken("sometoken")
        .build();
    Optional<Response> response = input.getResponseBuilder()
            .addDirective(directive)
            .withShouldEndSession(true)
            .build();
    // Return directive from Skill context to trigger the action request
    return response;

Cancel
~~~~~~

スキルは、ユーザーがサポートされているスキル内商品の既存のエンタイトルメントまたはサブスクリプションのキャンセルをリクエストしたときにCancelアクションを開始します。Cancelアクションを開始するには、製品IDが必要です。

.. code:: java

    // Additional include in your handler source
    import com.amazon.ask.model.interfaces.connections.SendRequestDirective;

    // Prepare the directive payload
    Map<String,Object> mapObject = new HashMap<String,Object>();
    Map<String, Object> inskillProduct = new HashMap<>();
    inskillProduct.put("productId", "< your product id in the format amzn1.adg.product....>"); // Replace productId with your productId
    mapObject.put("InSkillProduct", inskillProduct);

    // Prepare the directive request
    SendRequestDirective directive = SendRequestDirective.builder()
        .withPayload(mapObject)
        .withName("Cancel")
        .withToken("sometoken")
        .build();
    Optional<Response> response = input.getResponseBuilder()
            .addDirective(directive)
            .withShouldEndSession(true)
            .build();
    // Return directive from Skill context to trigger the action request
    return response;

