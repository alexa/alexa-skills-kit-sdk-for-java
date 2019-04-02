# Alexa Skills Kit SDK Sample - HowTo (Minecraft Helper)
A simple [AWS Lambda](http://aws.amazon.com/lambda) function that demonstrates how to write a "how to" skill for the Amazon Echo using the Alexa SDK.

## Concepts
This sample shows how to create a Lambda function for handling Alexa Skill requests that:

- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.
- Usage of ResourceBundle to handle internalization.

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in Lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
Refer to [Hosting a Custom Skill as an AWS Lambda Function](https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html) reference for a walkthrough on creating a AWS Lambda function with the correct role for your skill. When creating the function, select the “Author from scratch” option, and select the Java 8 runtime.

To build the sample, open a terminal and go to the directory containing pom.xml, and run 'mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "h=HowTo-1.0-jar-with-dependencies.jar" in the target directory.

Once you've created your AWS Lambda function and configured “Alexa Skills Kit” as a trigger, upload the JAR file produced in the previous step and set the handler to the fully qualified class name of your handler function. Finally, copy the ARN for your AWS Lambda function because you’ll need it when configuring your skill in the Amazon Developer console.

### Alexa Skill Setup
Now that the skill code has been uploaded to AWS Lambda we're ready to configure the skill with Alexa. First, navigate to the [Alexa Skills Kit Developer Console](https://developer.amazon.com/alexa/console/ask). Click the “Create Skill” button in the upper right. Enter “HowTo” as your skill name. On the next page,  select “Custom” and click “Create skill”.

Now we're ready to define the interaction model for the skill. Under “Invocation” tab on the left side, define your Skill Invocation Name to be `minecraft helper`.

Now it’s time to add an intent to the skill. Click the “Add” button under the Intents section of the Interaction Model. Leave “Create custom intent” selected, enter “RecipeIntent” for the intent name, and create the intent. Now it’s time to add some sample utterances that will be used to invoke the intent. For this example, we’ve provided the following sample utterances, but feel free to add others.

```
what are the ingredients for a {Item}
what are the ingredients for an {Item}
what are the ingredients for {Item}
what do I need for a {Item}
what do I need for an {Item}
how can I build a {Item}
how can I build an {Item}
how can I build {Item}
how can I craft a {Item}
how can I craft an {Item}
```

Since AMAZON.CancelIntent, AMAZON.HelpIntent, and AMAZON.StopIntent are built-in Alexa intents, sample utterances do not need to be provided as they are automatically inherited.

The Developer Console alternately allows you to edit the entire skill model in JSON format by selecting “JSON Editor” on the navigation bar. For this sample, the following JSON schema can be used.

```
{
  "interactionModel": {
    "languageModel": {
      "invocationName": "minecraft helper",
      "intents": [
        {
          "name": "RecipeIntent",
          "slots": [
            {
              "name": "Item",
              "type": "LIST_OF_ITEMS"
            }
          ],
          "samples": [
            "how can I build a {Item}",
            "how can I build an {Item}",
            "how can I build {Item}",
            "how can I craft a {Item}",
            "how can I craft an {Item}",
            "how can I craft {Item}",
            "how can I get a {Item}",
            "how can I get an {Item}",
            "how can I get {Item}",
            "how can I make a {Item}",
            "how can I make an {Item}",
            "how can I make {Item}",
            "how can you build a {Item}",
            "how can you build an {Item}",
            "how can you build {Item}",
            "how can you craft a {Item}",
            "how can you craft an {Item}",
            "how can you craft {Item}",
            "how can you get a {Item}",
            "how can you get an {Item}",
            "how can you get {Item}",
            "how can you make a {Item}",
            "how can you make an {Item}",
            "how can you make {Item}",
            "how do I build a {Item}",
            "how do I build an {Item}",
            "how do I build {Item}",
            "how do I craft a {Item}",
            "how do I craft an {Item}",
            "how do I craft {Item}",
            "how do I get a {Item}",
            "how do I get an {Item}",
            "how do I get {Item}",
            "how do I make a {Item}",
            "how do I make an {Item}",
            "how do I make {Item}",
            "how do you build a {Item}",
            "how do you build an {Item}",
            "how do you build {Item}",
            "how do you craft a {Item}",
            "how do you craft an {Item}",
            "how do you craft {Item}",
            "how do you get a {Item}",
            "how do you get an {Item}",
            "how do you get {Item}",
            "how do you make a {Item}",
            "how do you make an {Item}",
            "how do you make {Item}",
            "how is a {Item} built",
            "how is a {Item} crafted",
            "how is a {Item} made",
            "how is an {Item} built",
            "how is an {Item} crafted",
            "how is an {Item} made",
            "how is {Item} built",
            "how is {Item} crafted",
            "how is {Item} made",
            "how {Item} is made",
            "recipe for a {Item}",
            "recipe for an {Item}",
            "recipe for {Item}",
            "recipes for a {Item}",
            "recipes for an {Item}",
            "recipes for {Item}",
            "what are the ingredients for a {Item}",
            "what are the ingredients for an {Item}",
            "what are the ingredients for {Item}",
            "what do I need for a {Item}",
            "what do I need for an {Item}",
            "what do I need for {Item}",
            "what do I need to build a {Item}",
            "what do I need to build an {Item}",
            "what do I need to build {Item}",
            "what do I need to craft a {Item}",
            "what do I need to craft an {Item}",
            "what do I need to craft {Item}",
            "what do I need to get for a {Item}",
            "what do I need to get for an {Item}",
            "what do I need to get for {Item}",
            "what do I need to get to build a {Item}",
            "what do I need to get to build an {Item}",
            "what do I need to get to build {Item}",
            "what do I need to get to craft a {Item}",
            "what do I need to get to craft an {Item}",
            "what do I need to get to craft {Item}",
            "what do I need to get to make a {Item}",
            "what do I need to get to make an {Item}",
            "what do I need to get to make {Item}",
            "what do I need to have to build a {Item}",
            "what do I need to have to build an {Item}",
            "what do I need to have to build {Item}",
            "what do I need to have to craft a {Item}",
            "what do I need to have to craft an {Item}",
            "what do I need to have to craft {Item}",
            "what do I need to have to make a {Item}",
            "what do I need to have to make an {Item}",
            "what do I need to have to make {Item}",
            "what do I need to make a {Item}",
            "what do I need to make an {Item}",
            "what do I need to make {Item}",
            "what do you need for a {Item}",
            "what do you need for an {Item}",
            "what do you need for {Item}",
            "what ingredients do I need to build a {Item}",
            "what ingredients do I need to build an {Item}",
            "what ingredients do I need to build {Item}",
            "what ingredients do I need to craft a {Item}",
            "what ingredients do I need to craft an {Item}",
            "what ingredients do I need to craft {Item}",
            "what ingredients do I need to make a {Item}",
            "what ingredients do I need to make an {Item}",
            "what ingredients do I need to make {Item}",
            "what is in a {Item}",
            "what is in an {Item}",
            "what is in {Item}",
            "what is the recipe for a {Item}",
            "what is the recipe for an {Item}",
            "what is the recipe for {Item}",
            "what's in a {Item}",
            "what's in an {Item}",
            "what's in {Item}",
            "what's the recipe for a {Item}",
            "what's the recipe for an {Item}",
            "what's the recipe for {Item}",
            "how to craft {Item}",
            "how to craft a {Item}",
            "how to craft an {Item}",
            "how to make a {Item}",
            "how to make an {Item}",
            "how to make {Item}",
            "how to build a {Item}",
            "how to build an {Item}",
            "how to build {Item}",
            "how to get a {Item}",
            "how to get an {Item}",
            "how to get {Item}"
          ]
        },
        {
          "name": "AMAZON.RepeatIntent"
        },
        {
          "name": "AMAZON.HelpIntent"
        },
        {
          "name": "AMAZON.StopIntent"
        },
        {
          "name": "AMAZON.CancelIntent"
        }
      ],
      "types": [
        {
          "values": [
            {
              "name": {
                "value": "snow golem"
              }
            },
            {
              "name": {
                "value": "pillar quartz block"
              }
            },
            {
              "name": {
                "value": "firework rocket"
              }
            },
            {
              "name": {
                "value": "rabbit stew"
              }
            },
            {
              "name": {
                "value": "cauldron"
              }
            },
            {
              "name": {
                "value": "stone shovel"
              }
            },
            {
              "name": {
                "value": "red carpet"
              }
            },
            {
              "name": {
                "value": "book and quill"
              }
            },
            {
              "name": {
                "value": "item frame"
              }
            },
            {
              "name": {
                "value": "map"
              }
            },
            {
              "name": {
                "value": "sticky piston"
              }
            },
            {
              "name": {
                "value": "bread"
              }
            },
            {
              "name": {
                "value": "wooden pick ax"
              }
            },
            {
              "name": {
                "value": "shears"
              }
            },
            {
              "name": {
                "value": "raw beef"
              }
            },
            {
              "name": {
                "value": "smooth red sandstone"
              }
            },
            {
              "name": {
                "value": "prismarine crystals"
              }
            },
            {
              "name": {
                "value": "oak wood slab"
              }
            },
            {
              "name": {
                "value": "wooden sword"
              }
            },
            {
              "name": {
                "value": "stairs"
              }
            },
            {
              "name": {
                "value": "jungle fence"
              }
            },
            {
              "name": {
                "value": "activator rail"
              }
            },
            {
              "name": {
                "value": "farmland"
              }
            },
            {
              "name": {
                "value": "gold ore"
              }
            },
            {
              "name": {
                "value": "andesite"
              }
            },
            {
              "name": {
                "value": "rose red"
              }
            },
            {
              "name": {
                "value": "iron axe"
              }
            },
            {
              "name": {
                "value": "light blue dye"
              }
            },
            {
              "name": {
                "value": "gray dye"
              }
            },
            {
              "name": {
                "value": "blue stained glass pane"
              }
            },
            {
              "name": {
                "value": "iron horse armor"
              }
            },
            {
              "name": {
                "value": "red stained glass pane"
              }
            },
            {
              "name": {
                "value": "brick stairs"
              }
            },
            {
              "name": {
                "value": "golden leggings"
              }
            },
            {
              "name": {
                "value": "dark oak fence gate"
              }
            },
            {
              "name": {
                "value": "wither mob head"
              }
            },
            {
              "name": {
                "value": "spider eye"
              }
            },
            {
              "name": {
                "value": "magenta stained glass"
              }
            },
            {
              "name": {
                "value": "brown stained glass pane"
              }
            },
            {
              "name": {
                "value": "pumpkin pie"
              }
            },
            {
              "name": {
                "value": "snowball"
              }
            },
            {
              "name": {
                "value": "juke box"
              }
            },
            {
              "name": {
                "value": "sand"
              }
            },
            {
              "name": {
                "value": "dead bush"
              }
            },
            {
              "name": {
                "value": "brick slab"
              }
            },
            {
              "name": {
                "value": "lily pad"
              }
            },
            {
              "name": {
                "value": "leather pants"
              }
            },
            {
              "name": {
                "value": "mossy cobblestone wall"
              }
            },
            {
              "name": {
                "value": "eleven disc"
              }
            },
            {
              "name": {
                "value": "purple stained glass pane"
              }
            },
            {
              "name": {
                "value": "magenta stained glass pane"
              }
            },
            {
              "name": {
                "value": "mall disc"
              }
            },
            {
              "name": {
                "value": "jungle wood"
              }
            },
            {
              "name": {
                "value": "diamond ax"
              }
            },
            {
              "name": {
                "value": "empty map"
              }
            },
            {
              "name": {
                "value": "stone pressure plate"
              }
            },
            {
              "name": {
                "value": "trapdoor"
              }
            },
            {
              "name": {
                "value": "gold boots"
              }
            },
            {
              "name": {
                "value": "stall disc"
              }
            },
            {
              "name": {
                "value": "red stone wire"
              }
            },
            {
              "name": {
                "value": "piston"
              }
            },
            {
              "name": {
                "value": "white stained glass"
              }
            },
            {
              "name": {
                "value": "mine cart with furnace"
              }
            },
            {
              "name": {
                "value": "sugar"
              }
            },
            {
              "name": {
                "value": "chain mail chest plate"
              }
            },
            {
              "name": {
                "value": "feather"
              }
            },
            {
              "name": {
                "value": "light blue stained glass"
              }
            },
            {
              "name": {
                "value": "oak fence"
              }
            },
            {
              "name": {
                "value": "stone pick ax"
              }
            },
            {
              "name": {
                "value": "pink carpet"
              }
            },
            {
              "name": {
                "value": "monster spawner"
              }
            },
            {
              "name": {
                "value": "golden carrot"
              }
            },
            {
              "name": {
                "value": "stick"
              }
            },
            {
              "name": {
                "value": "diamond block"
              }
            },
            {
              "name": {
                "value": "green stained glass"
              }
            },
            {
              "name": {
                "value": "cooked chicken"
              }
            },
            {
              "name": {
                "value": "thirteen disc"
              }
            },
            {
              "name": {
                "value": "wooden trapdoor"
              }
            },
            {
              "name": {
                "value": "oak fence gate"
              }
            },
            {
              "name": {
                "value": "crafting bench"
              }
            },
            {
              "name": {
                "value": "nether quartz ore"
              }
            },
            {
              "name": {
                "value": "golden sword"
              }
            },
            {
              "name": {
                "value": "gray stained clay"
              }
            },
            {
              "name": {
                "value": "light blue stained clay"
              }
            },
            {
              "name": {
                "value": "diamond boots"
              }
            },
            {
              "name": {
                "value": "stone hoe"
              }
            },
            {
              "name": {
                "value": "coal ore"
              }
            },
            {
              "name": {
                "value": "dark oak leaves"
              }
            },
            {
              "name": {
                "value": "t.n.t."
              }
            },
            {
              "name": {
                "value": "brick"
              }
            },
            {
              "name": {
                "value": "potato"
              }
            },
            {
              "name": {
                "value": "orange carpet"
              }
            },
            {
              "name": {
                "value": "ink sack"
              }
            },
            {
              "name": {
                "value": "red stone"
              }
            },
            {
              "name": {
                "value": "dark oak wood"
              }
            },
            {
              "name": {
                "value": "diorite"
              }
            },
            {
              "name": {
                "value": "workbench"
              }
            },
            {
              "name": {
                "value": "gray stained glass"
              }
            },
            {
              "name": {
                "value": "jungle fence gate"
              }
            },
            {
              "name": {
                "value": "magenta wool"
              }
            },
            {
              "name": {
                "value": "dirt"
              }
            },
            {
              "name": {
                "value": "armor stand"
              }
            },
            {
              "name": {
                "value": "cooked fish"
              }
            },
            {
              "name": {
                "value": "lever"
              }
            },
            {
              "name": {
                "value": "leash"
              }
            },
            {
              "name": {
                "value": "cooked rabbit"
              }
            },
            {
              "name": {
                "value": "jungle wood stairs"
              }
            },
            {
              "name": {
                "value": "mine cart with hopper"
              }
            },
            {
              "name": {
                "value": "fermented spider eye"
              }
            },
            {
              "name": {
                "value": "raw fish"
              }
            },
            {
              "name": {
                "value": "brown stained glass"
              }
            },
            {
              "name": {
                "value": "sign"
              }
            },
            {
              "name": {
                "value": "t.n.t"
              }
            },
            {
              "name": {
                "value": "hopper"
              }
            },
            {
              "name": {
                "value": "blaze rod"
              }
            },
            {
              "name": {
                "value": "red stone comparator"
              }
            },
            {
              "name": {
                "value": "bone"
              }
            },
            {
              "name": {
                "value": "golden horse armor"
              }
            },
            {
              "name": {
                "value": "golden pants"
              }
            },
            {
              "name": {
                "value": "rail"
              }
            },
            {
              "name": {
                "value": "wheat seeds"
              }
            },
            {
              "name": {
                "value": "birch fence"
              }
            },
            {
              "name": {
                "value": "purple stained clay"
              }
            },
            {
              "name": {
                "value": "golden chestplate"
              }
            },
            {
              "name": {
                "value": "leather boots"
              }
            },
            {
              "name": {
                "value": "diamond shovel"
              }
            },
            {
              "name": {
                "value": "leather leggings"
              }
            },
            {
              "name": {
                "value": "red sand"
              }
            },
            {
              "name": {
                "value": "vines"
              }
            },
            {
              "name": {
                "value": "glass bottle"
              }
            },
            {
              "name": {
                "value": "red stone ore"
              }
            },
            {
              "name": {
                "value": "emerald block"
              }
            },
            {
              "name": {
                "value": "granite"
              }
            },
            {
              "name": {
                "value": "brown wool"
              }
            },
            {
              "name": {
                "value": "golden apple"
              }
            },
            {
              "name": {
                "value": "birch leaves"
              }
            },
            {
              "name": {
                "value": "white wool"
              }
            },
            {
              "name": {
                "value": "purple carpet"
              }
            },
            {
              "name": {
                "value": "hardened clay"
              }
            },
            {
              "name": {
                "value": "zombie mob head"
              }
            },
            {
              "name": {
                "value": "iron trapdoor"
              }
            },
            {
              "name": {
                "value": "flower pot"
              }
            },
            {
              "name": {
                "value": "iron ore"
              }
            },
            {
              "name": {
                "value": "jungle wood slab"
              }
            },
            {
              "name": {
                "value": "birch wood slab"
              }
            },
            {
              "name": {
                "value": "jungle door"
              }
            },
            {
              "name": {
                "value": "golden ax"
              }
            },
            {
              "name": {
                "value": "packed ice"
              }
            },
            {
              "name": {
                "value": "bricks"
              }
            },
            {
              "name": {
                "value": "light blue carpet"
              }
            },
            {
              "name": {
                "value": "dead shrub"
              }
            },
            {
              "name": {
                "value": "dropper"
              }
            },
            {
              "name": {
                "value": "chest"
              }
            },
            {
              "name": {
                "value": "raw chicken"
              }
            },
            {
              "name": {
                "value": "raw salmon"
              }
            },
            {
              "name": {
                "value": "tripwire hook"
              }
            },
            {
              "name": {
                "value": "oak wood stairs"
              }
            },
            {
              "name": {
                "value": "mine cart with command block"
              }
            },
            {
              "name": {
                "value": "eye of ender"
              }
            },
            {
              "name": {
                "value": "block of coal"
              }
            },
            {
              "name": {
                "value": "nether star"
              }
            },
            {
              "name": {
                "value": "gravel"
              }
            },
            {
              "name": {
                "value": "blue wool"
              }
            },
            {
              "name": {
                "value": "nether quartz"
              }
            },
            {
              "name": {
                "value": "rotten flesh"
              }
            },
            {
              "name": {
                "value": "magenta carpet"
              }
            },
            {
              "name": {
                "value": "white stained clay"
              }
            },
            {
              "name": {
                "value": "birch fence gate"
              }
            },
            {
              "name": {
                "value": "carrots"
              }
            },
            {
              "name": {
                "value": "purple dye"
              }
            },
            {
              "name": {
                "value": "lapis lazuli block"
              }
            },
            {
              "name": {
                "value": "obsidian"
              }
            },
            {
              "name": {
                "value": "mellohi disc"
              }
            },
            {
              "name": {
                "value": "gray wool"
              }
            },
            {
              "name": {
                "value": "trapped chest"
              }
            },
            {
              "name": {
                "value": "light gray stained glass pane"
              }
            },
            {
              "name": {
                "value": "iron pick ax"
              }
            },
            {
              "name": {
                "value": "red mushroom"
              }
            },
            {
              "name": {
                "value": "puffer fish"
              }
            },
            {
              "name": {
                "value": "emerald"
              }
            },
            {
              "name": {
                "value": "wooden shovel"
              }
            },
            {
              "name": {
                "value": "golden helmet"
              }
            },
            {
              "name": {
                "value": "melon"
              }
            },
            {
              "name": {
                "value": "clay block"
              }
            },
            {
              "name": {
                "value": "anvil"
              }
            },
            {
              "name": {
                "value": "daylight sensor"
              }
            },
            {
              "name": {
                "value": "lead"
              }
            },
            {
              "name": {
                "value": "sandstone"
              }
            },
            {
              "name": {
                "value": "leather tunic"
              }
            },
            {
              "name": {
                "value": "black stained glass"
              }
            },
            {
              "name": {
                "value": "lime stained clay"
              }
            },
            {
              "name": {
                "value": "mine cart with t.n.t"
              }
            },
            {
              "name": {
                "value": "clock"
              }
            },
            {
              "name": {
                "value": "black carpet"
              }
            },
            {
              "name": {
                "value": "cocoa"
              }
            },
            {
              "name": {
                "value": "gold ingot"
              }
            },
            {
              "name": {
                "value": "stone brick slab"
              }
            },
            {
              "name": {
                "value": "clown fish"
              }
            },
            {
              "name": {
                "value": "pumpkin seeds"
              }
            },
            {
              "name": {
                "value": "mossy stone brick"
              }
            },
            {
              "name": {
                "value": "cobweb"
              }
            },
            {
              "name": {
                "value": "milk bucket"
              }
            },
            {
              "name": {
                "value": "iron helmet"
              }
            },
            {
              "name": {
                "value": "yellow stained clay"
              }
            },
            {
              "name": {
                "value": "light gray stained clay"
              }
            },
            {
              "name": {
                "value": "diamond"
              }
            },
            {
              "name": {
                "value": "stone sword"
              }
            },
            {
              "name": {
                "value": "cobblestone stairs"
              }
            },
            {
              "name": {
                "value": "bed"
              }
            },
            {
              "name": {
                "value": "birch wood"
              }
            },
            {
              "name": {
                "value": "quartz slab"
              }
            },
            {
              "name": {
                "value": "sponge"
              }
            },
            {
              "name": {
                "value": "skeleton mob head"
              }
            },
            {
              "name": {
                "value": "bucket"
              }
            },
            {
              "name": {
                "value": "diamond sword"
              }
            },
            {
              "name": {
                "value": "golden shovel"
              }
            },
            {
              "name": {
                "value": "spruce wood stairs"
              }
            },
            {
              "name": {
                "value": "iron bars"
              }
            },
            {
              "name": {
                "value": "raw rabbit"
              }
            },
            {
              "name": {
                "value": "yellow carpet"
              }
            },
            {
              "name": {
                "value": "carrot on a stick"
              }
            },
            {
              "name": {
                "value": "raw pork chop"
              }
            },
            {
              "name": {
                "value": "furnace"
              }
            },
            {
              "name": {
                "value": "nether brick fence"
              }
            },
            {
              "name": {
                "value": "fence"
              }
            },
            {
              "name": {
                "value": "diamond horse armor"
              }
            },
            {
              "name": {
                "value": "red sandstone slab"
              }
            },
            {
              "name": {
                "value": "birch wood plank"
              }
            },
            {
              "name": {
                "value": "bedrock"
              }
            },
            {
              "name": {
                "value": "saddle"
              }
            },
            {
              "name": {
                "value": "light gray dye"
              }
            },
            {
              "name": {
                "value": "jungle leaves"
              }
            },
            {
              "name": {
                "value": "blocks disc"
              }
            },
            {
              "name": {
                "value": "pumpkin"
              }
            },
            {
              "name": {
                "value": "baked potato"
              }
            },
            {
              "name": {
                "value": "leather"
              }
            },
            {
              "name": {
                "value": "light gray carpet"
              }
            },
            {
              "name": {
                "value": "iron boots"
              }
            },
            {
              "name": {
                "value": "end stone"
              }
            },
            {
              "name": {
                "value": "chainmail leggings"
              }
            },
            {
              "name": {
                "value": "rabbit's foot"
              }
            },
            {
              "name": {
                "value": "glass"
              }
            },
            {
              "name": {
                "value": "stone"
              }
            },
            {
              "name": {
                "value": "prismarine"
              }
            },
            {
              "name": {
                "value": "compass"
              }
            },
            {
              "name": {
                "value": "green stained glass pane"
              }
            },
            {
              "name": {
                "value": "gold leggings"
              }
            },
            {
              "name": {
                "value": "command block"
              }
            },
            {
              "name": {
                "value": "dispenser"
              }
            },
            {
              "name": {
                "value": "poisonous potato"
              }
            },
            {
              "name": {
                "value": "apple"
              }
            },
            {
              "name": {
                "value": "red flower"
              }
            },
            {
              "name": {
                "value": "magenta dye"
              }
            },
            {
              "name": {
                "value": "brown carpet"
              }
            },
            {
              "name": {
                "value": "prismarine shard"
              }
            },
            {
              "name": {
                "value": "red stone block"
              }
            },
            {
              "name": {
                "value": "yellow stained glass"
              }
            },
            {
              "name": {
                "value": "dandelion yellow"
              }
            },
            {
              "name": {
                "value": "acacia wood stairs"
              }
            },
            {
              "name": {
                "value": "sandstone slab"
              }
            },
            {
              "name": {
                "value": "gray carpet"
              }
            },
            {
              "name": {
                "value": "polished granite"
              }
            },
            {
              "name": {
                "value": "mine cart"
              }
            },
            {
              "name": {
                "value": "brown stained clay"
              }
            },
            {
              "name": {
                "value": "cyan stained glass"
              }
            },
            {
              "name": {
                "value": "book"
              }
            },
            {
              "name": {
                "value": "mine cart with a chest"
              }
            },
            {
              "name": {
                "value": "acacia sapling"
              }
            },
            {
              "name": {
                "value": "paper"
              }
            },
            {
              "name": {
                "value": "gold chest plate"
              }
            },
            {
              "name": {
                "value": "cyan stained clay"
              }
            },
            {
              "name": {
                "value": "mushroom stew"
              }
            },
            {
              "name": {
                "value": "nether brick"
              }
            },
            {
              "name": {
                "value": "magma cream"
              }
            },
            {
              "name": {
                "value": "snow"
              }
            },
            {
              "name": {
                "value": "orange dye"
              }
            },
            {
              "name": {
                "value": "potion"
              }
            },
            {
              "name": {
                "value": "lime stained glass pane"
              }
            },
            {
              "name": {
                "value": "white stained glass pane"
              }
            },
            {
              "name": {
                "value": "boat"
              }
            },
            {
              "name": {
                "value": "dark prismarine"
              }
            },
            {
              "name": {
                "value": "oak door"
              }
            },
            {
              "name": {
                "value": "cocoa beans"
              }
            },
            {
              "name": {
                "value": "oak wood"
              }
            },
            {
              "name": {
                "value": "red wool"
              }
            },
            {
              "name": {
                "value": "cooked mutton"
              }
            },
            {
              "name": {
                "value": "dark oak fence"
              }
            },
            {
              "name": {
                "value": "iron door"
              }
            },
            {
              "name": {
                "value": "painting"
              }
            },
            {
              "name": {
                "value": "spruce leaves"
              }
            },
            {
              "name": {
                "value": "pink stained clay"
              }
            },
            {
              "name": {
                "value": "acacia wood slab"
              }
            },
            {
              "name": {
                "value": "dark oak wood slab"
              }
            },
            {
              "name": {
                "value": "chiseled sandstone"
              }
            },
            {
              "name": {
                "value": "oak wood plank"
              }
            },
            {
              "name": {
                "value": "cooked salmon"
              }
            },
            {
              "name": {
                "value": "iron shovel"
              }
            },
            {
              "name": {
                "value": "enchanting table"
              }
            },
            {
              "name": {
                "value": "red stone repeater"
              }
            },
            {
              "name": {
                "value": "far disc"
              }
            },
            {
              "name": {
                "value": "green carpet"
              }
            },
            {
              "name": {
                "value": "iron ingot"
              }
            },
            {
              "name": {
                "value": "orange wool"
              }
            },
            {
              "name": {
                "value": "jungle sapling"
              }
            },
            {
              "name": {
                "value": "stone slab"
              }
            },
            {
              "name": {
                "value": "light blue stained glass pane"
              }
            },
            {
              "name": {
                "value": "cyan dye"
              }
            },
            {
              "name": {
                "value": "steak "
              }
            },
            {
              "name": {
                "value": "orange stained glass"
              }
            },
            {
              "name": {
                "value": "blue stained glass"
              }
            },
            {
              "name": {
                "value": "spruce fence"
              }
            },
            {
              "name": {
                "value": "coal"
              }
            },
            {
              "name": {
                "value": "potatoes"
              }
            },
            {
              "name": {
                "value": "light gray stained glass"
              }
            },
            {
              "name": {
                "value": "fence gate"
              }
            },
            {
              "name": {
                "value": "egg"
              }
            },
            {
              "name": {
                "value": "spruce wood plank"
              }
            },
            {
              "name": {
                "value": "orange stained glass pane"
              }
            },
            {
              "name": {
                "value": "acacia wood"
              }
            },
            {
              "name": {
                "value": "light gray wool"
              }
            },
            {
              "name": {
                "value": "hay bale"
              }
            },
            {
              "name": {
                "value": "clay"
              }
            },
            {
              "name": {
                "value": "bowl"
              }
            },
            {
              "name": {
                "value": "leather helmet"
              }
            },
            {
              "name": {
                "value": "pink stained glass pane"
              }
            },
            {
              "name": {
                "value": "diamond chest plate"
              }
            },
            {
              "name": {
                "value": "gray stained glass pane"
              }
            },
            {
              "name": {
                "value": "yellow stained glass pane"
              }
            },
            {
              "name": {
                "value": "snowman"
              }
            },
            {
              "name": {
                "value": "diamond ore"
              }
            },
            {
              "name": {
                "value": "banner"
              }
            },
            {
              "name": {
                "value": "stone ax"
              }
            },
            {
              "name": {
                "value": "firework star"
              }
            },
            {
              "name": {
                "value": "nether wart"
              }
            },
            {
              "name": {
                "value": "red stained clay"
              }
            },
            {
              "name": {
                "value": "quartz block"
              }
            },
            {
              "name": {
                "value": "poppy"
              }
            },
            {
              "name": {
                "value": "acacia fence"
              }
            },
            {
              "name": {
                "value": "cactus"
              }
            },
            {
              "name": {
                "value": "spruce door"
              }
            },
            {
              "name": {
                "value": "cat disc"
              }
            },
            {
              "name": {
                "value": "flint and steel"
              }
            },
            {
              "name": {
                "value": "gold pants"
              }
            },
            {
              "name": {
                "value": "slime ball"
              }
            },
            {
              "name": {
                "value": "cooked porkchop"
              }
            },
            {
              "name": {
                "value": "polished andesite"
              }
            },
            {
              "name": {
                "value": "prismarine bricks"
              }
            },
            {
              "name": {
                "value": "wither"
              }
            },
            {
              "name": {
                "value": "lime dye"
              }
            },
            {
              "name": {
                "value": "blue carpet"
              }
            },
            {
              "name": {
                "value": "door"
              }
            },
            {
              "name": {
                "value": "tripwire"
              }
            },
            {
              "name": {
                "value": "enchantment table"
              }
            },
            {
              "name": {
                "value": "cyan carpet"
              }
            },
            {
              "name": {
                "value": "polished diorite"
              }
            },
            {
              "name": {
                "value": "golden boots"
              }
            },
            {
              "name": {
                "value": "iron block"
              }
            },
            {
              "name": {
                "value": "acacia leaves"
              }
            },
            {
              "name": {
                "value": "dark oak sapling"
              }
            },
            {
              "name": {
                "value": "diamond hoe"
              }
            },
            {
              "name": {
                "value": "dandelion"
              }
            },
            {
              "name": {
                "value": "lapis lazuli"
              }
            },
            {
              "name": {
                "value": "sugar cane"
              }
            },
            {
              "name": {
                "value": "birch door"
              }
            },
            {
              "name": {
                "value": "cactus green"
              }
            },
            {
              "name": {
                "value": "gold block"
              }
            },
            {
              "name": {
                "value": "smooth sandstone"
              }
            },
            {
              "name": {
                "value": "magenta stained clay"
              }
            },
            {
              "name": {
                "value": "note block"
              }
            },
            {
              "name": {
                "value": "snow block"
              }
            },
            {
              "name": {
                "value": "mob head"
              }
            },
            {
              "name": {
                "value": "diamond leggings"
              }
            },
            {
              "name": {
                "value": "red sandstone"
              }
            },
            {
              "name": {
                "value": "orange stained clay"
              }
            },
            {
              "name": {
                "value": "slime block"
              }
            },
            {
              "name": {
                "value": "bookshelf"
              }
            },
            {
              "name": {
                "value": "cake"
              }
            },
            {
              "name": {
                "value": "wooden button"
              }
            },
            {
              "name": {
                "value": "chiseled quartz block"
              }
            },
            {
              "name": {
                "value": "stone button"
              }
            },
            {
              "name": {
                "value": "glow stone dust"
              }
            },
            {
              "name": {
                "value": "lime stained glass"
              }
            },
            {
              "name": {
                "value": "cobblestone wall"
              }
            },
            {
              "name": {
                "value": "name tag"
              }
            },
            {
              "name": {
                "value": "gunpowder"
              }
            },
            {
              "name": {
                "value": "blaze powder"
              }
            },
            {
              "name": {
                "value": "brewing stand"
              }
            },
            {
              "name": {
                "value": "torch"
              }
            },
            {
              "name": {
                "value": "yellow wool"
              }
            },
            {
              "name": {
                "value": "watermelon"
              }
            },
            {
              "name": {
                "value": "wait disc"
              }
            },
            {
              "name": {
                "value": "glistering melon"
              }
            },
            {
              "name": {
                "value": "netherrack"
              }
            },
            {
              "name": {
                "value": "leather chest plate"
              }
            },
            {
              "name": {
                "value": "wooden slab"
              }
            },
            {
              "name": {
                "value": "nether brick stairs"
              }
            },
            {
              "name": {
                "value": "ice"
              }
            },
            {
              "name": {
                "value": "large fern"
              }
            },
            {
              "name": {
                "value": "nether brick slab"
              }
            },
            {
              "name": {
                "value": "spruce wood slab"
              }
            },
            {
              "name": {
                "value": "iron hoe"
              }
            },
            {
              "name": {
                "value": "fern"
              }
            },
            {
              "name": {
                "value": "sea lantern"
              }
            },
            {
              "name": {
                "value": "cookie"
              }
            },
            {
              "name": {
                "value": "melon seeds"
              }
            },
            {
              "name": {
                "value": "gold nugget"
              }
            },
            {
              "name": {
                "value": "red stone ore block"
              }
            },
            {
              "name": {
                "value": "diamond helmet"
              }
            },
            {
              "name": {
                "value": "spruce fence gate"
              }
            },
            {
              "name": {
                "value": "golden hoe"
              }
            },
            {
              "name": {
                "value": "acacia wood plank"
              }
            },
            {
              "name": {
                "value": "diamond pick ax"
              }
            },
            {
              "name": {
                "value": "lapis lazuli ore"
              }
            },
            {
              "name": {
                "value": "raw mutton"
              }
            },
            {
              "name": {
                "value": "quartz stairs"
              }
            },
            {
              "name": {
                "value": "brick block"
              }
            },
            {
              "name": {
                "value": "cobblestone slab"
              }
            },
            {
              "name": {
                "value": "emerald ore"
              }
            },
            {
              "name": {
                "value": "yellow flower"
              }
            },
            {
              "name": {
                "value": "jack o'lantern"
              }
            },
            {
              "name": {
                "value": "red stained glass"
              }
            },
            {
              "name": {
                "value": "iron leggings"
              }
            },
            {
              "name": {
                "value": "pink stained glass"
              }
            },
            {
              "name": {
                "value": "black dye"
              }
            },
            {
              "name": {
                "value": "wooden hoe"
              }
            },
            {
              "name": {
                "value": "purple stained glass"
              }
            },
            {
              "name": {
                "value": "mine cart with chest"
              }
            },
            {
              "name": {
                "value": "detector rail"
              }
            },
            {
              "name": {
                "value": "cyan wool"
              }
            },
            {
              "name": {
                "value": "iron chestplate"
              }
            },
            {
              "name": {
                "value": "cobblestone"
              }
            },
            {
              "name": {
                "value": "dark oak wood stairs"
              }
            },
            {
              "name": {
                "value": "creeper mob head"
              }
            },
            {
              "name": {
                "value": "carrot"
              }
            },
            {
              "name": {
                "value": "rabbit hide"
              }
            },
            {
              "name": {
                "value": "grass"
              }
            },
            {
              "name": {
                "value": "strad disc"
              }
            },
            {
              "name": {
                "value": "birch sapling"
              }
            },
            {
              "name": {
                "value": "mossy stone"
              }
            },
            {
              "name": {
                "value": "pink dye"
              }
            },
            {
              "name": {
                "value": "red stone torch"
              }
            },
            {
              "name": {
                "value": "green stained clay"
              }
            },
            {
              "name": {
                "value": "tall grass"
              }
            },
            {
              "name": {
                "value": "black wool"
              }
            },
            {
              "name": {
                "value": "wheat crops"
              }
            },
            {
              "name": {
                "value": "ender pearl"
              }
            },
            {
              "name": {
                "value": "moss stone"
              }
            },
            {
              "name": {
                "value": "fishing rod"
              }
            },
            {
              "name": {
                "value": "bone meal"
              }
            },
            {
              "name": {
                "value": "quartz"
              }
            },
            {
              "name": {
                "value": "dark oak wood plank"
              }
            },
            {
              "name": {
                "value": "powered rail"
              }
            },
            {
              "name": {
                "value": "beacon"
              }
            },
            {
              "name": {
                "value": "blue stained clay"
              }
            },
            {
              "name": {
                "value": "dark oak door"
              }
            },
            {
              "name": {
                "value": "iron golem"
              }
            },
            {
              "name": {
                "value": "spruce wood"
              }
            },
            {
              "name": {
                "value": "pink wool"
              }
            },
            {
              "name": {
                "value": "fire charge"
              }
            },
            {
              "name": {
                "value": "acacia door"
              }
            },
            {
              "name": {
                "value": "iron pants"
              }
            },
            {
              "name": {
                "value": "chirp disc"
              }
            },
            {
              "name": {
                "value": "oak leaves"
              }
            },
            {
              "name": {
                "value": "arrow"
              }
            },
            {
              "name": {
                "value": "wooden ax"
              }
            },
            {
              "name": {
                "value": "glass pane"
              }
            },
            {
              "name": {
                "value": "gold helmet"
              }
            },
            {
              "name": {
                "value": "ender chest"
              }
            },
            {
              "name": {
                "value": "light blue wool"
              }
            },
            {
              "name": {
                "value": "string"
              }
            },
            {
              "name": {
                "value": "red sandstone stairs"
              }
            },
            {
              "name": {
                "value": "purple wool"
              }
            },
            {
              "name": {
                "value": "cyan stained glass pane"
              }
            },
            {
              "name": {
                "value": "black stained clay"
              }
            },
            {
              "name": {
                "value": "enchanted book"
              }
            },
            {
              "name": {
                "value": "button"
              }
            },
            {
              "name": {
                "value": "lime wool"
              }
            },
            {
              "name": {
                "value": "coarse dirt"
              }
            },
            {
              "name": {
                "value": "ghast tear"
              }
            },
            {
              "name": {
                "value": "mine cart with a furnace"
              }
            },
            {
              "name": {
                "value": "wheat"
              }
            },
            {
              "name": {
                "value": "soul sand"
              }
            },
            {
              "name": {
                "value": "brown mushroom"
              }
            },
            {
              "name": {
                "value": "chainmail helmet"
              }
            },
            {
              "name": {
                "value": "written book"
              }
            },
            {
              "name": {
                "value": "spruce sapling"
              }
            },
            {
              "name": {
                "value": "oak sapling"
              }
            },
            {
              "name": {
                "value": "inverted daylight sensor"
              }
            },
            {
              "name": {
                "value": "sandstone stairs"
              }
            },
            {
              "name": {
                "value": "red stone lamp"
              }
            },
            {
              "name": {
                "value": "crafting table"
              }
            },
            {
              "name": {
                "value": "glow stone"
              }
            },
            {
              "name": {
                "value": "wooden pressure plate"
              }
            },
            {
              "name": {
                "value": "charcoal"
              }
            },
            {
              "name": {
                "value": "chainmail boots"
              }
            },
            {
              "name": {
                "value": "ladder"
              }
            },
            {
              "name": {
                "value": "mossy cobblestone"
              }
            },
            {
              "name": {
                "value": "golden pick ax"
              }
            },
            {
              "name": {
                "value": "seeds"
              }
            },
            {
              "name": {
                "value": "acacia fence gate"
              }
            },
            {
              "name": {
                "value": "birch wood stairs"
              }
            },
            {
              "name": {
                "value": "ward disc"
              }
            },
            {
              "name": {
                "value": "music disc"
              }
            },
            {
              "name": {
                "value": "enchanted golden apple"
              }
            },
            {
              "name": {
                "value": "white carpet"
              }
            },
            {
              "name": {
                "value": "iron sword"
              }
            },
            {
              "name": {
                "value": "diamonds pants"
              }
            },
            {
              "name": {
                "value": "chiseled red sandstone"
              }
            },
            {
              "name": {
                "value": "stone brick stairs"
              }
            },
            {
              "name": {
                "value": "pressure plate"
              }
            },
            {
              "name": {
                "value": "jungle wood plank"
              }
            },
            {
              "name": {
                "value": "green wool"
              }
            },
            {
              "name": {
                "value": "sugar canes"
              }
            },
            {
              "name": {
                "value": "bow"
              }
            },
            {
              "name": {
                "value": "flint"
              }
            },
            {
              "name": {
                "value": "black stained glass pane"
              }
            }
          ],
          "name": "LIST_OF_ITEMS"
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