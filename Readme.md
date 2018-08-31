<p align="center">
  <img src="https://m.media-amazon.com/images/G/01/mobile-apps/dex/avs/docs/ux/branding/mark1._TTH_.png">
  <br/>
  <h1 align="center">Alexa Skills Kit SDK for Java</h1>
  <p align="center"><a href="https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java"><img src="https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java.png?branch=2.0.x"></a> <a href="https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/?badge=latest"><img src="https://readthedocs.org/projects/alexa-skills-kit-sdk-for-java/badge/?version=latest"></a></p>
  </p>
The ASK SDK for Java makes it easier for you to build highly engaging skills, by allowing you to spend more time on implementing features and less on writing boiler-plate code.
<br/><br/>
<p align='center'>
<a href='https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Setting-Up-The-ASK-SDK.html'><img src='https://camo.githubusercontent.com/db9b9ce26327ad3bac57ec4daf0961a382d75790/68747470733a2f2f6d2e6d656469612d616d617a6f6e2e636f6d2f696d616765732f472f30312f6d6f62696c652d617070732f6465782f616c6578612f616c6578612d736b696c6c732d6b69742f7475746f7269616c732f67656e6572616c2f627574746f6e732f627574746f6e5f6765745f737461727465642e5f5454485f2e706e67'></a>
</p>


## Latest Versions
| Module       | Maven           |
| ------------- | ------------- |
|[ask-sdk](./ask-sdk)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk)| 
|[ask-sdk-core](./ask-sdk-core)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-core/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-core)| 
|[ask-sdk-lambda-support](./ask-sdk-lambda-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-lambda-support/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-lambda-support)| 
|[ask-sdk-servlet-support](./ask-sdk-servlet-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-servlet-support/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-servlet-support)| 
|[ask-sdk-dynamodb-persistence-adapter](./ask-sdk-dynamodb-persistence-adapter)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter)| 
|[ask-sdk-apache-client](./ask-sdk-apache-client)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-apache-client/2.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-apache-client)| 

## [SDK Documentation](https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/)

## Models
The SDK works on model classes rather than native Alexa JSON requests and
responses. These model classes are generated using the Request, Response JSON
schemas from the [developer docs](https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html). The source code for the model classes can be found [here](https://github.com/alexa/alexa-apis-for-java).

## Samples
### [Hello World](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/helloworld)
  This code sample will allow you to hear a response from Alexa when you trigger it. It is a minimal sample to get you familiarized with the Alexa Skills Kit and AWS Lambda.

### [Color Picker](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/colorpicker)
  This is a step-up in functionality from Hello World. It allows you to capture input from your user and demonstrates the use of Slots.

## Tutorials
### [States Quiz](https://github.com/alexa/skill-sample-java-quiz-game)
  This tutorial will guide you in building a fully-featured skill. The skill features Built-In and Custom slots, as well as state management. The skill you build with this tutorial can be customized to build your own game.

## Public Beta SDK
The available [public beta version of the ASK SDK v2 for Java](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.x_public-beta) may contain one or more Alexa features that are currently in public beta and not included in the production SDK version.

## Other Language Alexa Skills Kit SDKs
<a href="https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs"><img src="https://github.com/konpa/devicon/blob/master/icons/nodejs/nodejs-original.svg?sanitize=true" width="25px" /> Alexa Skills Kit SDK for NodeJS</a>

<a href="https://github.com/alexa-labs/alexa-skills-kit-sdk-for-python"><img src="https://github.com/konpa/devicon/blob/master/icons/python/python-original.svg?sanitize=true" width="25px" /> Alexa Skills Kit SDK for Python</a>

## Got Feedback?
Request and vote for Alexa features [here](https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote)!
