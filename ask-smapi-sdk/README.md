# ASK SMAPI SDK - Alexa Skills Management API Java Library

> `ask-smapi-sdk` is a library for Alexa Skills Kit’s Skill Management APIs (SMAPI).
> Learn more about SMAPI by reviewing the SMAPI [documentation](https://developer.amazon.com/docs/smapi/smapi-overview.html).

## Getting Started

### Add the dependency to your maven project’s pom.xml file

> 
> 
> ``` sh
> <dependency>
>      <groupId>com.amazon.alexa</groupId>
>      <artifactId>ask-smapi-sdk</artifactId>
>      <version>${ask-smapi-sdk.version}</version>
> </dependency>
> ```

### Installation from Github

> 
> 
> ``` sh
> $ git clone https://github.com/alexa/alexa-skills-kit-sdk-for-java.git
> $ cd alexa-skills-kit-sdk-for-java/ask-smapi-sdk
> $ mvn clean install
> ```

### Install NPM and the ASK CLI

> 1. Install NPM using the instructions provided [here](https://www.npmjs.com/get-npm). This is needed to get started with the ASK CLI, which will be used to generate Login with Amazon tokens you will need to access SMAPI.
> 2. Install [ask-cli](https://www.npmjs.com/package/ask-cli).

### Generate LWA (Login with Amazon) Keys

> 1. Create a new security profile for your Amazon Developer account by following the instructions provided [here](https://developer.amazon.com/docs/smapi/ask-cli-command-reference.html#generate-lwa-tokens).
>  This will generate `Client ID` and `Client Secret` keys.
> 2. Using the ASK CLI, run: `ask util generate-lwa-tokens`. You will be asked to provide the `Client ID` and `Client Secret` keys from the previous step. 
>  This will return the following JSON with a `Refresh Token`:

> 
> 
> ``` sh
> {
>   "access_token": "ACCESS_TOKEN",
>   "refresh_token": "REFRESH_TOKEN",
>   "token_type": "bearer",
>   "expires_in": 3600,
>   "expires_at": "2019-11-19T20:25:06.584Z"
> }
> ```


## Usage and Getting Started

> Using the `Client ID`, `Client Secret` and `Refresh Token` retrieved in the previous step to configure a new SMAPI client:

> 
> 
> ``` java
> final SkillManagementService defaultSmapiClient = SmapiClients.createDefault(
>    "<clientId here>",
>    "<client secret here>",
>    "<refresh token here>"
> );
> 
> final SkillManagementService customSmapiClient = SmapiClients.custom()
>    .withClientId("<clientId here>")
>    .withClientSecret("<client secret here>")
>    .withRefreshToken("<refresh token here>")
>    .withApiClient() // Any class object which implements com.amazon.ask.model.services.ApiClient
>    .withApiEndpoint("<api end point>")
>    .withSerializer() // Any class object which implements com.amazon.ask.model.services.Serializer
>    .build();
> 
> try {
>       // response with just the body.
>       Vendors response = customSmapiClient.getVendorListV1();
> 
>       // For full response with header, body and status code.
>       ApiResponse<Vendors> resp = customSmapiClient.callGetVendorListV1();
> 
>     } catch (ServiceException e) {
>       System.out.println(e.getMessage());
> }
> ```

## Usage Examples

> For the complete list of functions, please see the SMAPI SDK reference documentation.

### Get Skill Manifest

> 
> 
> ``` sh
> try {
>    SkillManifestEnvelope skillManifestEnvelope = customSmapiClient.getSkillManifestV1(skillId, "developement");
> } catch (ServcieException e) {
>    System.out.println(e.getMessage());
> }
> ```

### List all Skills

> 
> 
> ``` sh
> try {
>     // response with just the body.
>     Vendors response = customSmapiClient.listSkills();
> 
>     // For full response with header, body and status code.
>     ApiResponse<Vendors> resp = customSmapiClient.callListSKills();
> } catch (ServiceException e) {
>     System.out.println(e.getMessage());
> }
> ```

## Documentatiion

* [SMAPI SDK Reference Documentation](http://smapi-sdk-javadocs.s3-website-us-west-2.amazonaws.com/)
* [SMAPI Documentation](https://developer.amazon.com/docs/smapi/smapi-overview.html)

## Got Feedback?

>   - We would like to hear about your bugs, feature requests, questions
>     or quick feedback. Please search for the [existing
>     issues](https://github.com/alexa/alexa-skills-kit-sdk-for-java/issues)
>     before opening a new one. It would also be helpful if you follow
>     the templates for issue and pull request creation. Please follow
>     the [contributing
>     guidelines](https://github.com/alexa/alexa-skills-kit-sdk-for-java/blob/master/CONTRIBUTING.md)\!
>   - Request and vote for [Alexa
>     features](https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote)\!
