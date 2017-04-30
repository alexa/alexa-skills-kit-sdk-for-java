#Sample AWS Lambda function for Alexa
A simple [AWS Lambda](http://aws.amazon.com/lambda) function that demonstrates how to write a skill for the Amazon Echo using the Alexa SDK
and the Alexa Skill Builder (Beta).

## Concepts
This sample shows how to create a Lambda function for handling Alexa Skill requests that:

 - The user asks alexa to book a fictitious trip and alex needs to ask the user questions in return like origin, destination and time.
 - This example includes a predefined builder-model.json file that can be pasted into the builder tool and includes all the intents and slot dialog speech text.
 - This example implements two intent handlers one which demonstrates the use of automatic dialog using the delegate dialog directive and the other which sets up a manual dialog and uses the elicit and confirmation dialog directives.

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
1. Go to the AWS Console and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda.
2. Click on the Create a Lambda Function or Get Started Now button.
3. Skip the blueprint
4. Name the Lambda Function "PlanTripDialog-Example-Skill".
5. Select the runtime as Java 8
6. Go to the the root directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" in the target directory.
7. Select Code entry type as "Upload a .ZIP file" and then upload the "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" file from the build directory to Lambda
8. Set the Handler as plantripdialog.PlanTripDialogSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip).
9. Create a basic execution role and click create.
10. Leave the Advanced settings as the defaults.
11. Click "Next" and review the settings then click "Create Function"
12. Click the "Event Sources" tab and select "Add event source"
13. Set the Event Source type as Alexa Skills kit and Enable it now. Click Submit.
14. Copy the ARN from the top right to be used later in the Alexa Skill Setup.

### Alexa Skill Setup
1. Go to the [Alexa Console](https://developer.amazon.com/edw/home.html) and click Add a New Skill.
2. Set "TripPlanner" as the skill name and "trip planner" as the invocation name, this is what is used to activate your skill.
For example you would say: "Alexa, Ask trip planner I want to go on a trip to new york"
3. Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
4. Click Launch Skill Builder Beta button and then click on the </> Code Editor tab
5. Replace and paste in the contents of the builder-model.json file.  Click apply changes.  Click Build Model.
6. Click Configuration button. Copy the appId. Paste the appId into the PlanTripDialogSpeechletRequestStreamHandler.java file for the variable supportedApplicationIds,
   then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
7. You are now able to start testing your sample skill! You should be able to go to the [Echo webpage](http://echo.amazon.com/#skills) and see your skill enabled.
8. Try it out by saying some of the utterances defined in the model.
9. Try it with the manual dialog handler (comment out the PlanMyTripIntentHandler and uncomment PlanMyTripNoDelegateIntentHandler in the GuiceModule IOC class)
