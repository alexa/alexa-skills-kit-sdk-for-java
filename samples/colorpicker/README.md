# Alexa Skills Kit SDK Sample - Color Picker
A simple [AWS Lambda](http://aws.amazon.com/lambda) function that demonstrates how to write a color picker skill for the Amazon Echo using the Alexa SDK.

## Concepts
This sample shows how to create a Lambda function for handling Alexa Skill requests that:

- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
Refer to [Hosting a Custom Skill as an AWS Lambda Function](https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html) reference for a walkthrough on creating a AWS Lambda function with the correct role for your skill. When creating the function, select the “Author from scratch” option, and select the Java 8 runtime. 

To build the sample, open a terminal and go to the directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "colorpicker-1.0-jar-with-dependencies.jar" in the target directory.

Once you've created your AWS Lambda function and configured “Alexa Skills Kit” as a trigger, upload the JAR file produced in the previous step and set the handler to the fully qualified class name of your handler function. Finally, copy the ARN for your AWS Lambda function because you’ll need it when configuring your skill in the Amazon Developer console.

### Alexa Skill Setup
Now that the skill code has been uploaded to AWS Lambda we're ready to configure the skill with Alexa. First, navigate to the [Alexa Skills Kit Developer Console](https://developer.amazon.com/alexa/console/ask). Click the “Create Skill” button in the upper right. Enter “ColorPicker” as your skill name. On the next page,  select “Custom” and click “Create skill”.
 
Now we're ready to define the interaction model for the skill. Under “Invocation” tab on the left side, define your Skill Invocation Name to be `color picker`. 
 
Now it’s time to add an intent to the skill. Click the “Add” button under the Intents section of the Interaction Model. Leave “Create custom intent” selected, enter “WhatsMyColorIntent” for the intent name, and create the intent. Now it’s time to add some sample utterances that will be used to invoke the intent. For this example, we’ve provided the following sample utterances, but feel free to add others. 

```
whats my color
what is my color
say my color
tell me my color
whats my favorite color
what is my favorite color
say my favorite color
tell me my favorite color
tell me what my favorite color is
```
Let's add a Slot Type. You can find it below Built-In Intents.Click "Add Slot Type" and under "Create custom slot type", enter the name as "LIST\_OF\_COLORS". Add below values one at a time for this slot type.

```
green
blue
purple
red
orange
yellow
```

Let's add another intent to the skill that has slots called "MyColorIsIntent" for intent name. Skip the sample utterances part for now and create a new slot called "Color". Select Slot Type to be "LIST\_OF\_COLORS".
Now add below sample utterances that uses this slot "Color".

```
my color is {Color}
my favorite color is {Color}
```

Since AMAZON.CancelIntent, AMAZON.HelpIntent, and AMAZON.StopIntent are built-in Alexa intents, sample utterances do not need to be provided as they are automatically inherited.

The Developer Console alternately allows you to edit the entire skill model in JSON format by selecting “JSON Editor” on the navigation bar. For this sample, the following JSON schema can be used.

```
{
  "interactionModel": {
    "languageModel": {
      "invocationName": "color picker",
      "intents": [
        {
          "name": "AMAZON.CancelIntent",
          "samples": []
        },
        {
          "name": "AMAZON.HelpIntent",
          "samples": []
        },
        {
          "name": "AMAZON.StopIntent",
          "samples": []
        },
        {
          "name": "MyColorIsIntent",
          "slots": [
            {
              "name": "Color",
              "type": "LIST_OF_COLORS"
            }
          ],
          "samples": [
            " my color is {Color}",
            " my favorite color is {Color}"
          ]
        },
        {
          "name": "WhatsMyColorIntent",
          "slots": [],
          "samples": [
            "whats my color",
            "what is my color",
            "say my color",
            "tell me my color",
            "whats my favorite color",
            "what is my favorite color",
            "say my favorite color",
            "tell me my favorite color",
            "tell me what my favorite color is"
          ]
        }
      ],
      "types": [
        {
          "name": "LIST_OF_COLORS",
          "values": [
            {
              "name": {
                "value": "green"
              }
            },
            {
              "name": {
                "value": "blue"
              }
            },
            {
              "name": {
                "value": "purple"
              }
            },
            {
              "name": {
                "value": "red"
              }
            },
            {
              "name": {
                "value": "orange"
              }
            },
            {
              "name": {
                "value": "yellow"
              }
            }
          ]
        }
      ]
    }
  }
}
```

Once you’re done editing the interaction model don't forget to save and build the model.
 
Let's move on to the skill configuration section. Under “Endpoint” select “AWS Lambda ARN” and paste in the ARN of the function you created previously. The rest of the settings can be left at their default values. Click “Save Endpoints” and proceed to the next section.
 
Finally you're ready to test the skill! In the “Test” tab of the developer console you can simulate requests, in text and voice form, to your skill. Use the invocation name along with one of the sample utterances we just configured as a guide. You should also be able to go to the [Echo webpage](http://echo.amazon.com/#skills) and see your skill listed under “Your Skills”, where you can enable the skill on your account for testing from an Alexa enabled device.
 
At this point, feel free to start experimenting with your Intent Schema as well as the corresponding request handlers in your skill's implementation. Once you're finished iterating, you can optionally choose to move on to the process of getting your skill certified and published so it can be used by Alexa users worldwide.
