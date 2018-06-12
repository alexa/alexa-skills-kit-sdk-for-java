# ASK SDK for Java [![Build Status](https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java.png?branch=2.0.x)](https://travis-ci.org/alexa/alexa-skills-kit-sdk-for-java)

| Module       | Maven           |
| ------------- | ------------- |
|[ask-sdk](./ask-sdk)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk)| 
|[ask-sdk-core](./ask-sdk-core)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-core/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-core)| 
|[ask-sdk-lambda-support](./ask-sdk-lambda-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-lambda-support/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-lambda-support)| 
|[ask-sdk-servlet-support](./ask-sdk-servlet-support)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-servlet-support/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-servlet-support)| 
|[ask-sdk-dynamodb-persistence-adapter](./ask-sdk-dynamodb-persistence-adapter)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-dynamodb-persistence-adapter)| 
|[ask-sdk-apache-client](./ask-sdk-apache-client)| [![Maven Central](https://img.shields.io/maven-central/v/com.amazon.alexa/ask-sdk-apache-client/2.3.svg)](http://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk-apache-client)| 

The ASK SDK for Java makes it easier for you to build highly engaging skills, by allowing you to spend more time on implementing features and less on writing boiler-plate code.

To help you get started more with the SDK we have included several samples, references, and guides. We've collected links to all of them here to make them easy to find, as well as a description for what each document covers.

## Guides
### [Setting Up The ASK SDK](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Setting-Up-The-ASK-SDK)
This guide will show you how to include the SDK as a dependency in your Maven project.

### [Developing Your First Skill](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Developing-Your-First-Skill)
Walks you through step-by-step instructions for building the Hello World sample.

## Samples
### [Hello World](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/helloworld)
  This code sample will allow you to hear a response from Alexa when you trigger it. It is a minimal sample to get you familiarized with the Alexa Skills Kit and AWS Lambda.

### [Color Picker](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/colorpicker)
  This is a step-up in functionality from Hello World. It allows you to capture input from your user and demonstrates the use of Slots.

## Tutorials
### [States Quiz](https://github.com/alexa/skill-sample-java-quiz-game)
  This tutorial will guide you in building a fully-featured skill. The skill features Built-In and Custom slots, as well as state management. The skill you build with this tutorial can be customized to build your own game.

## Technical Documentation
### [Request Processing](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Request-Processing)
  Covers how you can build request handlers to handle incoming requests, exception handlers to handle errors in your skill, and use request and response interceptors to perform tasks before or after handler execution.

### [Skill Attributes](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Skill-Attributes)
  Covers how skill attributes can be used on three different scopes (request, session and persistent), to store and retrieve skill data.

### [Alexa Service Clients](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Alexa-Service-Clients)
  Covers how service clients can be used by your skill to access Alexa APIs.

### [Response Building](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Response-Building)
  Covers how the Response Builder can be used to easily compose multiple elements, like text, cards and audio, into a single response.

### [Managing In-Skill Purchases](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Managing-in-skill-purchase)
Covers how you can manage in-skill products and the purchase experience in your skills.

### [Javadoc Reference](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com)
Javadoc references from the latest version of the SDK.

## Public Beta SDK
The available [public beta version of the ASK SDK v2 for Java](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.x_public-beta) may contain one or more Alexa features that are currently in public beta and not included in the production SDK version.

## Got Feedback?
Request and vote for Alexa features [here](https://alexa.uservoice.com/forums/906892-alexa-skills-developer-voice-and-vote)!
