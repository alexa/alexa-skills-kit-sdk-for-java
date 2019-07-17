# Alexa Skills Kit SDK Sample - Hello World Servlet
A simple Servlet example that demonstrates how to use the Alexa SkillServlet. 

For more information, please refer to [Developing Your First Skill](https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Developing-Your-First-Skill.html) for detailed instructions.

Test your skill on Tomcat server locally
========================================

Requirements
------------

1. Tomcat server. [Download and installation instructions](https://tomcat.apache.org/download-80.cgi)

2. ngrok (required since alexa requires a https endpoint to send request to). [Download and install](https://ngrok.com/download)

Steps
-----

- Change your current working directory to root `alexa-skills-kit-sdk-for-java/samples/helloworldservlet` and run `mvn compile war:war`. This will generate a `helloworldservlet.war` file in
the target folder of the skill's repo.

- Move the war file to webapps folder of tomcat installation directory.

- Run the following command to start tomcat server on port 8080, `<tomcat_installation_directory>/bin/startup.sh`.

- Go to ngrok installation directory and run `./ngrok http 8080`. This will generate a http and https endpoints for usage in Alexa dev portal.

- In the [dev portal](https://developer.amazon.com/alexa/console/ask), go to your skill > endpoints > https, add the https url generated above followed by /helloworldservlet/main. Eg: https://d455eb41.ngrok.io/helloworldservlet/main.

- Upload a self signed certificate or select `My development endpoint is a sub-domain....` option from the dropdown and click `save endpoint` at the top of the page.

- Go to `Test` tab in the dev portal and launch your skill.

- The dev portal will send a https request to the ngrok endpoint which will route it to your skill running on tomcat server.

- After testing is complete, close your tomcat server by running `<tomcat_installation_directory>/bin/shutdown.sh`.


