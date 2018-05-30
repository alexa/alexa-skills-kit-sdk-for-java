# ASK SDK v2 for Java (Public Beta) - Readme

## Disclaimer

This repo contains public beta versions of the ASK SDK v2 for Java, which may contain one or more Alexa features that are currently available in public beta. These features may contain unexpected bugs or be subject to future breaking changes. Production skills should use non-beta versions of the ASK SDK.

## Prerequisites

* An [Apache Maven](https://maven.apache.org/) project.
* A suitable Java development environment. The ASK SDK v2 for Java (Public Beta) requires a Java 8 compatible runtime environment.

## Adding the SDK to your project

To use the ASK SDK v2 for Java (Public Beta) in your project, declare it as a dependency on your project's `pom.xml` file. You can choose to depend on the standard SDK distribution or individually select the components on which to depend. The standard SDK distribution is the easiest way to quickly get up and running with the SDK. It includes the core SDK and the following additional modules that provide commonly used functionality:

* AWS Lambda support module, which allows skills to be run as part of an AWS Lambda function
* Servlet support module, which allows skills to be run as a servlet on a web server
* Amazon DynamoDB persistence adapter, which enables storing skill attributes to DynamoDB
* Apache HTTPClient API client plugin, which leverages the Apache HttpClient to make API calls to Alexa services

### Including all standard SDK components

From your Maven project's `pom.xml` add the following dependency to pull in the standard ASK SDK v2 for Java (Public Beta) distribution:

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk</artifactId>
  <version>2.4.0-beta.1</version>
</dependency>`
```

### Including individual SDK components

Select one or more of the following modules to include in your project by adding the corresponding entry to your `pom.xml` file. To minimize dependency closure, add only the modules that you need.

**Core SDK (required)**

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk-core</artifactId>
  <version>2.4.0-beta.1</version>
</dependency>`
```

**AWS Lambda support module**

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk-lambda-support</artifactId>
  <version>`2.4.0-beta.1`</version>
</dependency>`
```

**Servlet support module**

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk-servlet-support</artifactId>
  <version>`2.4.0-beta.1`</version>
</dependency>`
```

**Amazon DynamoDB persistence adapter**

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk-dynamodb-persistence-adapter</artifactId>
  <version>`2.4.0-beta.1`</version>
</dependency>`
```

**Apache HTTPClient API client plugin**

```
`<dependency>
  <groupId>com.amazon.alexa</groupId>
  <artifactId>ask-sdk-apache-client</artifactId>
  <version>`2.4.0-beta.1`</version>
</dependency>`
```


