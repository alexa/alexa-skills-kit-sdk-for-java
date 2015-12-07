/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package scorekeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * This sample shows how to create a Lambda function for handling Alexa Skill requests that:
 *
 * <ul>
 * <li><b>Multiple slots</b>: has 2 slots (name and score)</li>
 * <li><b>Database Interaction</b>: demonstrates how to read and write data to DynamoDB.</li>
 * <li><b>NUMBER slot</b>: demonstrates how to handle number slots.</li>
 * <li><b>Custom slot type</b>: demonstrates using custom slot types to handle a finite set of known values</li>
 * <li><b>Dialog and Session state</b>: Handles two models, both a one-shot ask and tell model, and
 * a multi-turn dialog model. If the user provides an incorrect slot in a one-shot model, it will
 * direct to the dialog model. See the examples section below for sample interactions of these
 * models.</li>
 * </ul>
 * <p>
 * <h2>Examples</h2>
 * <p>
 * <b>Dialog model</b>
 * <p>
 * User: "Alexa, tell score keeper to reset."
 * <p>
 * Alexa: "New game started without players. Who do you want to add first?"
 * <p>
 * User: "Add the player Bob"
 * <p>
 * Alexa: "Bob has joined your game"
 * <p>
 * User: "Add player Jeff"
 * <p>
 * Alexa: "Jeff has joined your game"
 * <p>
 * (skill saves the new game and ends)
 * <p>
 * User: "Alexa, tell score keeper to give Bob three points."
 * <p>
 * Alexa: "Updating your score, three points for Bob"
 * <p>
 * (skill saves the latest score and ends)
 * <p>
 * <b>One-shot model</b>
 * <p>
 * User: "Alexa, what's the current score?"
 * <p>
 * Alexa: "Jeff has zero points and Bob has three"
 */
public class ScoreKeeperSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(ScoreKeeperSpeechlet.class);

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private ScoreKeeperManager scoreKeeperManager;

    private SkillContext skillContext;

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeComponents();

        // if user said a one shot command that triggered an intent event,
        // it will start a new session, and then we should avoid speaking too many words.
        skillContext.setNeedsMoreHelp(false);
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        skillContext.setNeedsMoreHelp(true);
        return scoreKeeperManager.getLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();
        if ("NewGameIntent".equals(intent.getName())) {
            return scoreKeeperManager.getNewGameIntentResponse(session, skillContext);

        } else if ("AddPlayerIntent".equals(intent.getName())) {
            return scoreKeeperManager.getAddPlayerIntentResponse(intent, session, skillContext);

        } else if ("AddScoreIntent".equals(intent.getName())) {
            return scoreKeeperManager.getAddScoreIntentResponse(intent, session, skillContext);

        } else if ("TellScoresIntent".equals(intent.getName())) {
            return scoreKeeperManager.getTellScoresIntentResponse(intent, session);

        } else if ("ResetPlayersIntent".equals(intent.getName())) {
            return scoreKeeperManager.getResetPlayersIntentResponse(intent, session);

        } else if ("AMAZON.HelpIntent".equals(intent.getName())) {
            return scoreKeeperManager.getHelpIntentResponse(intent, session, skillContext);

        } else if ("AMAZON.CancelIntent".equals(intent.getName())) {
            return scoreKeeperManager.getExitIntentResponse(intent, session, skillContext);

        } else if ("AMAZON.StopIntent".equals(intent.getName())) {
            return scoreKeeperManager.getExitIntentResponse(intent, session, skillContext);

        } else {
            throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Initializes the instance components if needed.
     */
    private void initializeComponents() {
        if (amazonDynamoDBClient == null) {
            amazonDynamoDBClient = new AmazonDynamoDBClient();
            scoreKeeperManager = new ScoreKeeperManager(amazonDynamoDBClient);
            skillContext = new SkillContext();
        }
    }
}
