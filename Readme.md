<p align="center">
  <img src="https://m.media-amazon.com/images/G/01/mobile-apps/dex/avs/docs/ux/branding/mark1._TTH_.png">
  <br/>
  <h1 align="center">Alexa Skills Kit SDK for Java</h1>
  <p align="center"><a href="https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java"><img src="https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java.png?branch=2.0.x"></a></p>
</p>

The ASK SDK for Java makes it easier for you to build highly engaging skills, by allowing you to spend more time on implementing features and less on writing boiler-plate code.
<br/><br/>
<p align='center'>
<a href='https://developer.amazon.com/docs/alexa-skills-kit-sdk-for-java/set-up-the-sdk.html'><img src='https://camo.githubusercontent.com/db9b9ce26327ad3bac57ec4daf0961a382d75790/68747470733a2f2f6d2e6d656469612d616d617a6f6e2e636f6d2f696d616765732f472f30312f6d6f62696c652d617070732f6465782f616c6578612f616c6578612d736b696c6c732d6b69742f7475746f7269616c732f67656e6572616c2f627574746f6e732f627574746f6e5f6765745f737461727465642e5f5454485f2e706e67'></a>
</p>


## Package Versions
| Module       | Maven           |
| ------------- | ------------- |
|[ask-sdk](./ask-sdk)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk)| 
|[ask-sdk-core](./ask-sdk-core)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-core/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-core)| 
|[ask-sdk-lambda-support](./ask-sdk-lambda-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-lambda-support/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-lambda-support)| 
|[ask-sdk-servlet-support](./ask-sdk-servlet-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-servlet-support/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-servlet-support)| 
|[ask-sdk-dynamodb-persistence-adapter](./ask-sdk-dynamodb-persistence-adapter)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter)| 
|[ask-sdk-apache-client](./ask-sdk-apache-client)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-apache-client/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-apache-client)| 
|[ask-sdk-freemarker](./ask-sdk-freemarker)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-freemarker/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-freemarker)| 
|[ask-sdk-runtime](./ask-sdk-runtime)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-runtime/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-runtime)| 

## Alexa Features Supported by SDK

- [Amazon Pay](https://developer.amazon.com/docs/amazon-pay/integrate-skill-with-amazon-pay.html)
- [Audio Player](https://developer.amazon.com/docs/custom-skills/audioplayer-interface-reference.html)
- [Display – Body templates for devices with a screen](https://developer.amazon.com/docs/custom-skills/create-skills-for-alexa-enabled-devices-with-a-screen.html)
- [Gadgets/Game Engine/Echo Buttons](https://developer.amazon.com/docs/custom-skills/game-engine-interface-reference.html)
- [Directive Service (Progressive Response)](https://developer.amazon.com/docs/custom-skills/send-the-user-a-progressive-response.html)
- [Messaging](https://developer.amazon.com/docs/smapi/send-a-message-request-to-a-skill.html)
- [Monetization](https://developer.amazon.com/alexa-skills-kit/make-money)
- [Video](https://developer.amazon.com/docs/custom-skills/videoapp-interface-reference.html)
- [Device Address](https://developer.amazon.com/docs/custom-skills/device-address-api.html)
- [Lists](https://developer.amazon.com/docs/custom-skills/access-the-alexa-shopping-and-to-do-lists.html#alexa-lists-access)
- [Request for customer contact information](https://developer.amazon.com/docs/smapi/alexa-settings-api-reference.html)
- [Obtain customer settings information](https://developer.amazon.com/docs/smapi/alexa-settings-api-reference.html)
- [Account Linking](https://developer.amazon.com/docs/account-linking/understand-account-linking.html)
- [Entity Resolution](https://developer.amazon.com/docs/custom-skills/define-synonyms-and-ids-for-slot-type-values-entity-resolution.html)
- [Dialog Management](https://developer.amazon.com/docs/custom-skills/dialog-interface-reference.html)
- [Reminders](https://developer.amazon.com/docs/smapi/alexa-reminders-overview.html)

### Preview Features

The following features are released as public preview. The interfaces might change in future releases.

- [Connections](https://developer.amazon.com/blogs/alexa/post/7b332b32-893e-4cad-be07-a5877efcbbb4/skill-connections-preview-now-skills-can-work-together-to-help-customers-get-more-done)
- [Alexa Presentation Language](https://developer.amazon.com/docs/alexa-presentation-language/apl-overview.html)
- [Name-free Interactions](https://developer.amazon.com/docs/custom-skills/understand-name-free-interaction-for-custom-skills.html)


## Technical Documentation

- [English](https://developer.amazon.com/docs/alexa-skills-kit-sdk-for-java/overview.html)
- [日本語](https://alexa-skills-kit-sdk-for-java.readthedocs.io/ja/latest)

## Models

The SDK works on model classes rather than native Alexa JSON requests and responses. These model classes are generated using the Request, Response JSON schemas from the [developer docs](https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html). The source code for the model classes can be found [here](https://github.com/alexa/alexa-apis-for-java).

## Samples

### [Hello World](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/helloworld)
This code sample will allow you to hear a response from Alexa when you trigger it. It is a minimal sample to get you familiarized with the Alexa Skills Kit and AWS Lambda.

### [Color Picker](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/colorpicker)
This is a step-up in functionality from Hello World. It allows you to capture input from your user and demonstrates the use of Slots.

### [How To](https://github.com/alexa/skill-sample-java-how-to)
This sample will walk first-time Alexa skill developers through all the required steps involved in creating a parameter-based skill using a template called ‘Minecraft Helper’. Ask how to craft an item in the game Minecraft, and this skill will give you instructions. The skill showcases:

- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.
- Usage of ResourceBundle to handle internalization

### [City Guide](https://github.com/alexa/skill-sample-java-city-guide)
Template for a local recommendations skill. Alexa uses the data that you provide to offer recommendations according to the user stated preferences. This Skill showcases:

- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.
- Usage of ResourceBundle to handle internalization
- Calling external APIs from the skill.

### [Movie Quotes](https://github.com/alexa/skill-sample-java-movie-quotes-quiz)
This Alexa sample skill is a basic Movie Quotes Quiz skill that makes use of Template Resolver feature of the Java SDK. A user can open up the skill, select a movie from a list, and then hear partial quotes from the selected movie. Then, they have to complete the quote.

### [Pet Match](https://github.com/alexa/skill-sample-java-pet-match)
Pet Match matches the user with a pet. When launched, this Alexa Skill will prompt the user for the information it needs to determine a match. Once all of the required information is collected, it will send the data to an external web service which processes the data and returns the match. Through this workshop, you will learn how to use advanced Alexa Skills Kit features to create and configure an Alexa Skill and AWS Lambda.
The skill showcases the usage of Request and Response interceptors.

## Tutorials
### [States Quiz](https://github.com/alexa/skill-sample-java-quiz-game)
This tutorial will guide you in building a fully-featured skill. The skill features Built-In and Custom slots, as well as state management. The skill you build with this tutorial can be customized to build your own game.

### [Airplane Facts](https://github.com/alexa/skill-sample-java-fact)
This tutorial will demonstrate how to write skills for Echo Show and Echo Spot in Java using the Alexa Skills Kit (ASK) SDK v2.
 
### [Pager Karaoke](https://github.com/alexa-labs/skill-sample-java-pager-karaoke)
This tutorial demonstrates 3 features of APL: the [Pager Component](https://developer.amazon.com/docs/alexa-presentation-language/apl-pager.html), [SpeakItem Command](https://developer.amazon.com/docs/alexa-presentation-language/apl-commands-components.html), and accessing [device characteristics](https://developer.amazon.com/docs/alexa-presentation-language/apl-viewport-characteristics.html) in the skill code.

## Other Language Alexa Skills Kit SDKs
<a href="https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs"><img src="https://github.com/konpa/devicon/blob/master/icons/nodejs/nodejs-original.svg?sanitize=true" width="25px" /> Alexa Skills Kit SDK for NodeJS</a>

<a href="https://github.com/alexa/alexa-skills-kit-sdk-for-python"><img src="https://github.com/konpa/devicon/blob/master/icons/python/python-original.svg?sanitize=true" width="25px" /> Alexa Skills Kit SDK for Python</a>

## Got Feedback?
Request and vote for Alexa features [here](https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote/filters/top?category_id=322783)!
