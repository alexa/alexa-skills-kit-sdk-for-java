/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package savvyconsumer;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * This sample shows how to create a Lambda function for handling Alexa Skill requests that:
 *
 * <ul>
 * <li><b>Web service</b>: Communicate with an the Amazon associates API to get best seller
 * information using aws-lib</li>
 * <li><b>Pagination</b>: Handles paginating a list of responses to avoid overwhelming the customer.
 * </li>
 * <li><b>Custom slot type</b>: demonstrates using custom slot types to handle a finite set of known values</li>
 * <li><b>Dialog and Session state</b>: Handles two models, both a one-shot ask and tell model, and
 * a multi-turn dialog model. If the user provides an incorrect slot in a one-shot model, it will
 * direct to the dialog model. See the examples section for sample interactions of these models.</li>
 * <li><b>SSML</b>: Using SSML tags to control how Alexa renders the text-to-speech</li>
 * </ul>
 * <p>
 * <h2>Examples</h2>
 * <p>
 * <b>Dialog model</b>
 * <p>
 * User: "Alexa, open Savvy Consumer"
 * <p>
 * Alexa: "Welcome to the Savvy Consumer. For which category do you want to hear the best sellers?"
 * <p>
 * User: "books"
 * <p>
 * Alexa: "Getting the best sellers for books. The top seller for books is .... Would you like to
 * hear more?"
 * <p>
 * User: "yes"
 * <p>
 * Alexa: "Second ... Third... Fourth... Would you like to hear more?"
 * <p>
 * User : "no"
 * <p>
 * <b>One-shot model</b>
 * <p>
 * User: "Alexa, ask Savvy Consumer for top books"
 * <p>
 * Alexa: "Getting the best sellers for books. The top seller for books is .... Would you like to
 * hear more?"
 * <p>
 * User: "No"
 */
public class SavvyConsumerSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(SavvyConsumerSpeechlet.class);

    /**
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "Your AWS Access Key";

    /**
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_SECRET_KEY = "Your AWS Secret Key";

    /**
     * The Associates Tag.
     */
    private static final String AWS_ASSOCIATES_TAG = "associates_tag";

    /**
     * The key to find the current index from the session attributes.
     */
    private static final String SESSION_CURRENT_INDEX = "current";

    /**
     * The key to find the current category from the session attributes.
     */
    private static final String SESSION_CURRENT_CATEGORY = "category";

    /**
     * The Max number of items for Alexa to read from a request to Amazon.
     */
    private static final int MAX_ITEMS = 10;

    /**
     * The number of items read for each pagination request, until we reach the MAX_ITEMS.
     */
    private static final int PAGINATION_SIZE = 3;

    /**
     * The Category slot.
     */
    private static final String SLOT_CATEGORY = "Category";

    /**
     * Mapping of the browse node ID to the category for the Amazon catalog. Use a tree map so gets
     * can be case insensitive.
     */
    private static final Map<String, String> browseNodeMap = new TreeMap<String, String>(
            String.CASE_INSENSITIVE_ORDER);

    /**
     * A Mapping of alternative ways a user will say a category to how Amazon has defined the
     * category. Use a tree map so gets can be case insensitive.
     */
    private static final Map<String, String> spokenNameToCategory = new TreeMap<String, String>(
            String.CASE_INSENSITIVE_ORDER);

    static {
        spokenNameToCategory.put("movies", "DVD");
        spokenNameToCategory.put("movie", "DVD");
        spokenNameToCategory.put("novel", "Books");
        spokenNameToCategory.put("novels", "Books");

        browseNodeMap.put("Apparel", "1036592");
        browseNodeMap.put("Appliances", "2619526011");
        browseNodeMap.put("ArtsAndCrafts", "2617942011");
        browseNodeMap.put("Automotive", "15690151");
        browseNodeMap.put("Baby", "165797011");
        browseNodeMap.put("Beauty", "11055981");
        browseNodeMap.put("Books", "1000");
        browseNodeMap.put("Classical", "301668");
        browseNodeMap.put("Collectibles", "4991426011");
        browseNodeMap.put("DVD", "2625374011");
        browseNodeMap.put("DigitalMusic", "624868011");
        browseNodeMap.put("Electronics", "493964");
        browseNodeMap.put("GiftCards", "2864120011");
        browseNodeMap.put("GourmetFood", "16310211");
        browseNodeMap.put("Grocery", "16310211");
        browseNodeMap.put("HealthPersonalCare", "3760931");
        browseNodeMap.put("HomeGarden", "1063498");
        browseNodeMap.put("Industrial", "16310161");
        browseNodeMap.put("Jewelry", "2516784011");
        browseNodeMap.put("KindleStore", "133141011");
        browseNodeMap.put("Kitchen", "284507");
        browseNodeMap.put("LawnAndGarden", "3238155011");
        browseNodeMap.put("MP3Downloads", "624868011");
        browseNodeMap.put("Magazines", "599872");
        browseNodeMap.put("Miscellaneous", "10304191");
        browseNodeMap.put("MobileApps", "2350150011");
        browseNodeMap.put("Music", "301668");
        browseNodeMap.put("MusicalInstruments", "11965861");
        browseNodeMap.put("OfficeProducts", "1084128");
        browseNodeMap.put("OutdoorLiving", "2972638011");
        browseNodeMap.put("PCHardware", "541966");
        browseNodeMap.put("PetSupplies", "2619534011");
        browseNodeMap.put("Photo", "502394");
        browseNodeMap.put("Shoes", "672124011");
        browseNodeMap.put("Software", "409488");
        browseNodeMap.put("SportingGoods", "3375301");
        browseNodeMap.put("Tools", "468240");
        browseNodeMap.put("Toys", "165795011");
        browseNodeMap.put("UnboxVideo", "2858778011");
        browseNodeMap.put("VHS", "2625374011");
        browseNodeMap.put("Video", "404276");
        browseNodeMap.put("VideoGames", "11846801");
        browseNodeMap.put("Watches", "378516011");
        browseNodeMap.put("Wireless", "2335753011");
        browseNodeMap.put("WirelessAccessories", "13900851");
    }

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        String speechOutput =
                "Welcome to the Savvy Consumer. For which category do you want to "
                        + "hear the best sellers?";
        String repromptText = "Please choose a category by saying, " +
                "books <break time=\"0.2s\" /> " +
                "fashion <break time=\"0.2s\" /> " +
                "movie <break time=\"0.2s\" /> " +
                "kitchen";

        // Here we are prompting the user for input
        return newAskResponse(speechOutput, false, "<speak>" + repromptText + "</speak>", true);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("TopSellers".equals(intentName)) {
            return getTopSellers(intent, session);
        } else if ("HearMore".equals(intentName)) {
            return getNextPageOfItems(intent, session);
        } else if ("DontHearMore".equals(intentName)) {
            PlainTextOutputSpeech output = new PlainTextOutputSpeech();
            output.setText("");
            return SpeechletResponse.newTellResponse(output);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelp();
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            throw new SpeechletException("Invalid Intent");
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
     * Calls the Product Advertising API to get the top sellers for a category. Then Creates a
     * {@code SpeechletResponse} for the intent.
     *
     * @param intent
     *            intent for the request
     * @return SpeechletResponse spoken and visual response for the given intent
     * @throws SpeechletException
     * @see <a href="https://affiliate-program.amazon.com/gp/advertising/api/detail/main.html">
     *      Product Advertising API </a>
     */
    private SpeechletResponse getTopSellers(final Intent intent, final Session session)
            throws SpeechletException {
        String repromptText = "";

        // Check if we are in a session, and if so then reprompt for yes or no
        if (session.getAttributes().containsKey(SESSION_CURRENT_INDEX)) {
            String speechOutput = "Would you like to hear more?";
            repromptText = "Would you like to hear more top sellers? Please say yes or no.";
            return newAskResponse(speechOutput, false, repromptText, false);
        }

        Slot categorySlot = intent.getSlot(SLOT_CATEGORY);

        // Find the lookup word for the given category.
        String lookupCategory = getLookupWord(categorySlot);

        // Remove the periods to fix things like d. v. d.s to dvds
        String category = categorySlot.getValue().replaceAll("\\.\\s*", "");

        if (lookupCategory != null) {
            List<String> items = fetchTitles(lookupCategory);

            // Configure the card and speech output.
            String cardTitle = "Top Sellers for " + category;
            StringBuilder cardOutput = new StringBuilder();
            cardOutput.append("The Top Sellers for ").append(category).append(" are: ");
            StringBuilder speechOutput = new StringBuilder();
            speechOutput.append("Here are the top sellers for ").append(category).append(". ");
            session.setAttribute(SESSION_CURRENT_CATEGORY, category);

            // Iterate through the response and set the intial response, as well as the
            // session attributes for pagination.
            int i = 0;
            for (String item : items) {
                int numberInList = i + 1;
                if (numberInList == 1) {
                    // Set the speech output and current index for just the top item in the list.
                    // Other results are paginated based on subsequent user intents
                    speechOutput.append("The top seller is: ").append(item).append(". ");
                    session.setAttribute(SESSION_CURRENT_INDEX, numberInList);
                }

                // Set the session attributes and full card output
                session.setAttribute(Integer.toString(i), item);
                cardOutput.append(numberInList).append(". ").append(item).append(".");
                i++;
            }

            if (i == 0) {
                // There were no items returned for the specified item.
                SsmlOutputSpeech output = new SsmlOutputSpeech();
                output.setSsml("<speak>I'm sorry, I cannot get the top sellers for " + category
                        + " at this time. Please try again later. Goodbye.</speak>");
                return SpeechletResponse.newTellResponse(output);
            }

            speechOutput.append(" Would you like to hear the rest?");
            repromptText = "Would you like to hear the rest? Please say yes or no.";

            SimpleCard card = new SimpleCard();
            card.setContent(cardOutput.toString());
            card.setTitle(cardTitle);

            SpeechletResponse response = newAskResponse("<speak>" + speechOutput.toString() + "</speak>", true,
                    repromptText, false);
            response.setCard(card);

            return response;
        } else {

            // The category didn't match one of our predefined categories. Reprompt the user.
            String speechOutput = "I'm not sure what the category is, please try again";
            repromptText =
                    "I'm not sure what the category is, you can say " +
                    "books <break time=\"0.2s\" /> " +
                    "fashion <break time=\"0.2s\" /> " +
                    "movie <break time=\"0.2s\" /> " +
                    "kitchen.";
            return newAskResponse(speechOutput, false, "<speak>" + repromptText + "</speak>", true);
        }
    }

    /**
     * Fetches the top ten selling titles from the Product Advertising API.
     *
     * @throws SpeechletException
     */
    private List<String> fetchTitles(String category) throws SpeechletException {
        List<String> titles = new LinkedList<String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Make the actual http call and get the xml response.
            Document doc = db.parse(getRequestUrl(category));
            NodeList nodeList = doc.getElementsByTagName("Title");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                titles.add(node.getTextContent());
            }
        } catch (Exception e) {
            throw new SpeechletException(e);
        }
        return titles;
    }

    /**
     * Gets the request URL with the proper parameters and signs it.
     */
    private String getRequestUrl(String category) throws InvalidKeyException,
            IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // Set up the signed requests helper
        SignedRequestsHelper helper =
                SignedRequestsHelper.getInstance("ecs.amazonaws.com", AWS_ACCESS_KEY_ID,
                        AWS_SECRET_KEY);

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-10-01");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", category);
        params.put("BrowseNode", browseNodeMap.get(category));
        params.put("ResponseGroup", "Small");
        params.put("AssociateTag", AWS_ASSOCIATES_TAG);

        return helper.sign(params);
    }

    /**
     * Gets the 2nd-MAX_ITEMS number of titles from the session attributes.
     */
    private SpeechletResponse getNextPageOfItems(final Intent intent, final Session session) {
        if (session.getAttributes().containsKey(SESSION_CURRENT_INDEX)) {
            int currentIndex = (Integer) session.getAttribute(SESSION_CURRENT_INDEX);
            int currentItemNumberInList = currentIndex + 1;
            StringBuilder speechOutput = new StringBuilder();

            // Iterate through the session attributes to create the next n results for the user.
            for (int i = 0; i < PAGINATION_SIZE; i++) {
                String currentString =
                        (String) session.getAttribute(Integer.toString(currentIndex));
                if (currentString != null) {
                    if (currentItemNumberInList < MAX_ITEMS) {
                        speechOutput.append("<say-as interpret-as=\"ordinal\">" + currentItemNumberInList
                                + "</say-as>. " + currentString + ". ");
                    } else {
                        speechOutput.append("And the <say-as interpret-as=\"ordinal\">"
                                + currentItemNumberInList
                                + "</say-as> top seller is. " + currentString
                                + ". Those were the 10 top sellers in Amazon's "
                                + session.getAttribute(SESSION_CURRENT_CATEGORY) + " department");
                    }
                    currentIndex++;
                    currentItemNumberInList++;
                }
            }

            // Set the new index and end the session if the newIndex is greater than the MAX_ITEMS
            session.setAttribute(SESSION_CURRENT_INDEX, currentIndex);
            if (currentIndex < MAX_ITEMS) {
                speechOutput.append(" Would you like to hear more?");
                return newAskResponse(speechOutput.toString(), true,
                        "Would you like to hear more top sellers? Please say yes or no.", false);
            } else {
                SsmlOutputSpeech output = new SsmlOutputSpeech();
                output.setSsml("<speak>" + speechOutput.toString() + "</speak>");
                return SpeechletResponse.newTellResponse(output);
            }
        } else {
            // The user attempted to get more results without ever uttering the category.
            // Reprompt the user for the proper usage.
            String speechOutput =
                    "Welcome to the Savvy Consumer. For which category do you want "
                            + "to hear the best sellers?.";
            String repromptText = "Please choose a category by saying, " +
                    "books <break time=\"0.2s\" /> " +
                    "fashion <break time=\"0.2s\" /> " +
                    "movie <break time=\"0.2s\" /> " +
                    "kitchen";
            return newAskResponse(speechOutput, false, "<speak>" + repromptText + "</speak>", true);
        }
    }

    /**
     * Gets the lookup word based on the input category slot. The lookup word will be from the
     * BROWSE_NODE_MAP and will attempt to get an exact match. However, if no exact match exists
     * then the function will check for a contains.
     *
     * @param categorySlot
     *            the input category slot
     * @returns {string} the lookup word for the BROWSE_NODE_MAP
     */
    private String getLookupWord(Slot categorySlot) {
        String lookupCategory = null;
        if (categorySlot != null && categorySlot.getValue() != null) {
            // Lower case the incoming slot and remove spaces
            String category =
                    categorySlot
                            .getValue()
                            .toLowerCase()
                            .replaceAll("\\s", "")
                            .replaceAll("\\.", "")
                            .replaceAll("three", "3");

            // Check for spoken names
            lookupCategory = spokenNameToCategory.get(category);
            if (lookupCategory == null) {
                // The lookup category wasn't in the spoken names map. Look for it in the browse
                // node map.
                for (String key : browseNodeMap.keySet()) {
                    if (key.equalsIgnoreCase(category)) {
                        lookupCategory = key;
                        break;
                    }
                }
            }

            if (lookupCategory == null) {
                // The lookup category still wasn't found so try to see if the keys contain the
                // category
                for (String key : browseNodeMap.keySet()) {
                    if (key.toLowerCase().contains(category)
                            || category.contains(key.toLowerCase())) {
                        lookupCategory = key;
                        break;
                    }
                }

            }
        }

        return lookupCategory;
    }

    /**
     * Instructs the user on how to interact with this skill.
     */
    private SpeechletResponse getHelp() {
        String speechOutput =
                "You can ask for the best sellers on Amazon for a given category. "
                        + "For example, get best sellers for books, or you can say exit. "
                        + "Now, what can I help you with?";
        String repromptText =
                "I'm sorry I didn't understand that. You can say things like," +
                "books <break time=\"0.2s\" /> " +
                "movies <break time=\"0.2s\" /> " +
                "music. Or you can say exit. Now, what can I help you with?";
        return newAskResponse(speechOutput, false, "<speak>" + repromptText + "</speak>", true);
    }

    /**
     * Wrapper for creating the Ask response from the input strings.
     * 
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
            String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }
}
