# Java Alexa Skills Kit SDK & Samples

## This is a fork of the main aws java sdk repository. Because they haven't been responsive to PRs
I've merged those into this repo and invited authors of the PRs here to collaborate.
I will push these to bintray for now so you can resolve this sdk in your gradle/maven build.

##Alexa Skills Kit Documentation
The documentation for the Alexa Skills Kit is available on the [Amazon Apps and Services Developer Portal](https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/).

## Include the released versions on bintray in your maven/gradle build

You will need to add the repository to your build and use the differnet artifact name:

Gradle:

    repositories {
        maven {
            url  "http://dl.bintray.com/vanderfox/alexa-skills-kit-java" 
        }
    }

Maven:

    <?xml version="1.0" encoding="UTF-8" ?>
    <settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
              xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
        
        <profiles>
            <profile>
                <repositories>
                    <repository>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                        <id>bintray-vanderfox-alexa-skills-kit-java</id>
                        <name>bintray</name>
                        <url>http://dl.bintray.com/vanderfox/alexa-skills-kit-java</url>
                    </repository>
                </repositories>
                <pluginRepositories>
                    <pluginRepository>
                        <snapshots>
                            <enabled>false</enabled>
                        </snapshots>
                        <id>bintray-vanderfox-alexa-skills-kit-java</id>
                        <name>bintray-plugins</name>
                        <url>http://dl.bintray.com/vanderfox/alexa-skills-kit-java</url>
                    </pluginRepository>
                </pluginRepositories>
                <id>bintray</id>
            </profile>
        </profiles>
        <activeProfiles>
            <activeProfile>bintray</activeProfile>
        </activeProfiles>
    </settings>
Then include in your dependencies:
Gradle:

    compile 'com.amazon.alexa:alexa-skills-kit-java:1.1.5_SNAPSHOT'
    
Maven:

    <dependency>
      <groupId>com.amazon.alexa</groupId>
      <artifactId>alexa-skills-kit-java</artifactId>
      <version>1.1.5_SNAPSHOT</version>
      <type>pom</type>
    </dependency>
    
## Changes
# 1.1.6_snapshot

- added access to Context object on relevant Speelet interface methods. You will need this to access the token for pause/resume - for example: 

public SpeechletResponse onIntent(final IntentRequest request, final Session session, Context context)
            throws SpeechletException {}
     
- see example at <a href="https://github.com/rvanderwerf/alexa-groovy-podcast/blob/master/src/main/groovy/com/vanderfox/demo/GroovyPodcastSpeechlet.groovy">https://github.com/rvanderwerf/alexa-groovy-podcast/blob/master/src/main/groovy/com/vanderfox/demo/Speechlet.groovy</a> for usage example.
            
# 1.1.5_snapshot

- added SystemExceptionEncounteredRequest handling so you can log errors with faulty requests send to Alexa
- fixed interface for audio calls to allow a SpeechletResponse so you can send directives back
- note: some audio command only allow certain directives to be used, for now you will have to validate that yourself!
- See docs at https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/system#overview
- link to working example app (this is in Groovy Lambda should be useful: (sample app not complete, it always plays the same podcast for now)
- Example app with working audio skills: https://github.com/rvanderwerf/alexa-groovy-podcast
## Contents
The included samples represent how to use Java AWS Lambda functions as Alexa Skills.
The following samples are included (ordered by complexity, see the Using Alexa Skills Kit Samples
link below for more details):

- HelloWorld: a simple skill that repeats Hello World! on user input
- SpaceGeek : a simple skill that responds to the user with a space fact.
- Session: a simple skill that asks for your favorite color, then repeats it back to you using session attributes.
- MinecraftHelper : a simple skill that responds to the user's recipe queries with formulas.
- WiseGuy : a skill that tells knock knock jokes.
- HistoryBuff : a skill that gives historical information that happened on a user provided day.
- Savvy Consumer : a skill that looks up a category on Amazon and returns the best selling products.
- TidePooler : a skill that looks up tide information for various cities.
- ScoreKeeper : a skill that can keep score of a game.

## Usage
Navigate to the README.md in each sub directory in the samples folder and follow the instructions for getting the sample up and running.

## Resources
Here are a few direct links to our documentation:

- [Using the Alexa Skills Kit Samples](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/using-the-alexa-skills-kit-samples)
- [Getting Started](https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/getting-started-guide)
- [Invocation Name Guidelines](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/choosing-the-invocation-name-for-an-alexa-skill)
- [Developing an Alexa Skill as an AWS Lambda Function](https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/docs/developing-an-alexa-skill-as-a-lambda-function)
