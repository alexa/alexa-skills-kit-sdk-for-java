# Alexa Skills Kit SDK Sample - Hello World
A simple [AWS Lambda](http://aws.amazon.com/lambda) function that demonstrates how to write a Hello World skill for the Amazon Echo using the Alexa SDK.

## Concepts
This simple sample has no external dependencies or session management, and shows the most basic example of how to create a Lambda function for handling Alexa Skill requests.

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
Refer to [Hosting a Custom Skill as an AWS Lambda Function](https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html) reference for a walkthrough on creating a AWS Lambda function with the correct role for your skill. When creating the function, select the “Author from scratch” option, and select the Java 8 runtime.

To build the sample, open a terminal and go to the directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "helloworld-1.0-jar-with-dependencies.jar" in the target directory.
 
Once you've created your AWS Lambda function and configured “Alexa Skills Kit” as a trigger, upload the JAR file produced in the previous step and set the handler to the fully qualified class name of your handler function. Finally, copy the ARN for your AWS Lambda function because you’ll need it when configuring your skill in the Amazon Developer console.

### Alexa Skill Setup
Please refer to [Developing Your First Skill](https://github.com/alexa/alexa-skills-kit-sdk-for-java/wiki/Developing-Your-First-Skill) for detailed instructions.
