Managing In-Skill Purchases
===========================

The ASK SDK for Java includes a service client and helper classes for
discovering available and purchased in-skill products and for initiating
purchase and cancellation requests from within a skill.

In-Skill Purchase Service
-------------------------

The ASK SDK for Java provides a ``MonetizationServiceClient`` that
invokes inSkillProducts API to retrieve all in-skill products associated
with the current skill along with indications if each in-skill product
is purchasable and/or already purchased by the current customer. The
following methods are provided:

.. code:: java

   getInSkillProducts(String locale, String purchasableFilter, String entitledFilter,  String productType, String nextToken, BigDecimal maxResults)  
   getInSkillProduct(String locale, String inSkillProductId) 

| ``locale`` can be retrieved from the request at
  ``input.getRequestEnvelope().getRequest().getLocale()``.
| ``purchasableFilter`` can be provided as ``null`` to retrieve all
  in-skill products and as PURCHASABLE or NOT_PURCHASABLE to filter the
  response on purchasability.
| ``entitledFilter`` can be provided as ``null`` to retrieve all
  in-skill products and as ENTITLED or NOT_ENTITLED to filter the
  response on entitlement status.
| ``productType`` can be provided as ``null`` to retrieve in-skill
  products of all types or as ENTITLEMENT, CONSUMABLE or SUBSCRIPTION to
  filter by product type.
| ``nextToken`` is required for paginated queries. ``maxResults`` allows
  skills to control records retrieved per API call. The default page
  size is 100 records.
| ``inSkillProductId`` specifies the in-skill product to be retrieved.

getInSkillProducts()
~~~~~~~~~~~~~~~~~~~~

The getInSkillProducts method retrieves all associated in-skill products
for the current skill along with purchasability and entitlement status
for each product for the current skill and customer.

.. code:: java

    import com.amazon.ask.model.services.monetization.MonetizationServiceClient;
    import com.amazon.ask.model.services.ServiceException;
    import com.amazon.ask.model.services.monetization.InSkillProduct;
    import com.amazon.ask.model.services.monetization.InSkillProductsResponse;

    @Override
    public Optional<Response> handle(HandlerInput input) {
      try {
        MonetizationServiceClient client = input.getServiceClientFactory().getMonetizationService();
        String locale = input.getRequestEnvelope().getRequest().getLocale();
        
        // To fetch all products
        InSkillProductsResponse responseProducts = client.getInSkillProducts(locale, null, null, null, null, null);
        System.out.println("There are " + responseProducts.getInSkillProducts().size() + " buyable products");

     } catch (ServiceException e) {
        System.out.println("Exception occurred in calling getInSkillProducts API. Error code: " + e.getStatusCode());
       }
    }

The API response contains an array of in-skill product records.

.. code:: java

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
~~~~~~~~~~~~~~~~~~~~

The getInSkillProduct method retrieves the product record for a single
in-skill product identified by a productId.

.. code:: java

   import com.amazon.ask.model.services.monetization.MonetizationServiceClient;
   import com.amazon.ask.model.services.ServiceException;
   import com.amazon.ask.model.services.monetization.InSkillProduct;
   import com.amazon.ask.model.services.monetization.InSkillProductsResponse;

   @Override
   public Optional<Response> handle(HandlerInput input) {
    try {
          MonetizationServiceClient client = input.getServiceClientFactory().getMonetizationService();
          String locale = input.getRequestEnvelope().getRequest().getLocale();

          // To fetch a specific in-skill product by product Id
          String productId = "<your product id with the format 'amzn1.adg.product....'>";
          InSkillProduct responseProduct = client.getInSkillProduct(locale, productId);

          if(responseProduct!=null) {
            System.out.println("Found the product with ID" + productId);
        }
      } catch (ServiceException e) {
          System.out.println("Exception occurred in calling getInSkillProduct API. Error code: " + e.getStatusCode());
      }
   }

The API response contains a single in-skill product record.

.. code:: java

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

More information on these APIs and their usage for skill implementation
is available here: `Add In-Skill Purchases to a Custom
Skill <https://developer.amazon.com/docs/in-skill-purchase/add-isps-to-a-skill.html>`__ .

In-Skill Purchase Interface
---------------------------

The ASK SDK for Java provides the ``SendRequestDirective`` for skills to
initiate in-skill purchase and cancellation requests through Alexa.
Amazon systems then manage the voice interaction with customers, handle
the purchase transaction and return a status response back to the
requesting skill. Three different ``actions`` are supported using this
interface: + ``Upsell`` + ``Buy`` + ``Cancel``

More details about these ``actions`` and recommended usecases is
available here: `Add In-Skill Purchases to a Custom
Skill <https://developer.amazon.com/docs/in-skill-purchase/add-isps-to-a-skill.html>`__ .

Upsell
~~~~~~

Skills should initiate the Upsell action to present an in-skill
contextually when the user did not explicitly ask for it. E.g. During or
after the free content has been served. A productId and upsell message
is required to initiate the Upsell action. The upsell message allows
developers to specify how Alexa can present the in-skill product to the
user before presenting the pricing offer.

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
~~

Skills should initiate the Buy action when a customer asks to buy a
specific in-skill product. A productId is required to initiate the Buy
action.

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

Skills should initiate the Cancel action when a customer asks to cancel
an existing entitlement or Subscription for a supported in-skill
product. A productId is required to initiate the Cancel action.

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
