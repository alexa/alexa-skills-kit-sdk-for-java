# ASK SDK Local Debug (Java)

## About

ASK SDK Local Debug is a package which enables you to test your skill code locally against your skill invocations by routing requests to your developer machine. This enables you to verify changes quickly to skill code as you can test without needing to deploy skill code to Lambda.

> NOTE: Feature is currently only available to customers in the NA region(https://developer.amazon.com/en-US/docs/alexa/custom-skills/develop-skills-in-multiple-languages.html#h2-multiple-endpoints).

## Installation

Add the maven package - ask-sdk-local-debug >= v2.30.2 as a [test dependency](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#dependency-scope) to your skill package.

## Configuration

### USING with other IDEs and Debuggers

1. To instantiate a connection to the local debugging service, run the local debug application with `com.amazon.ask.localdebug.LocalDebuggerInvoker` as the start up program and the following arguments:
```
--accessToken <ACCESS_TOKEN>
--skillID <SKILL_ID>
--skillStreamHandlerClass <CLASS_NAME>
```
    1. ACCESS_TOKEN:
        1. Install ASK CLI v2
        2. npm install ask-cli@2 -g
        3. Generate the accessToken using ASK CLI
        4. ask util generate-lwa-tokens --scopes alexa::ask:skills:debug
        5. You will be directed to a Login with Amazon page. Sign in and retrieve your ACCESS_TOKEN from the terminal.
    2. SKILL_ID: The ID of the skill you are trying to debug. Ensure that the developer account you used to login to obtain the access token has access to this skill.
    3. SKILL_STREAM_HANDLER_CLASS: The fully qualified name of the class in your skill package that implements either the SkillStreamHandler or SkillServlet class.
2. Configure your preferred IDE or other debugging tool to attach to the above process or execute directly from your preferred IDE. For example, in VS Code, this would be included in the `launch.json`:
```
{
   "type": "java",
   "request": "launch",
   "name": "Skill Debug",
   "program": "<5. Program>",
   "args": [
        "--accessToken","<ACCESS_TOKEN>",
        "--skillId", "<SKILL_ID>",
        "--skillStreamHandlerClass", "<SKILL_STREAM_HANDLER_CLASS>"
    ]
}
```

## Things to note

1. Local debugging is only available for a skillâ€™s **`development`** stage.
2. A connection remains active for **1 hour. **You will need to reinstantiate the connection after 1 hour.
3. All Alexa requests for the skill will be routed to your development machine while the connection is active.
4. Only one connection session may be active for a given skill ID and developer account.