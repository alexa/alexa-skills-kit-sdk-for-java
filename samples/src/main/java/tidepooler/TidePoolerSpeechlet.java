/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package tidepooler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.amazonaws.util.json.JSONTokener;

/**
 * This sample shows how to create a Lambda function for handling Alexa Skill requests that:
 * <ul>
 * <li><b>Web service</b>: communicate with an external web service to get tide data from NOAA
 * CO-OPS API (http://tidesandcurrents.noaa.gov/api/)</li>
 * <li><b>Multiple optional slots</b>: has 2 slots (city and date), where the user can provide 0, 1,
 * or 2 values, and assumes defaults for the unprovided values</li>
 * <li><b>DATE slot</b>: demonstrates date handling and formatted date responses appropriate for
 * speech</li>
 * <li><b>Custom slot type</b>: demonstrates using custom slot types to handle a finite set of known values</li>
 * <li><b>SSML</b>: Using SSML tags to control how Alexa renders the text-to-speech</li>
 * <li><b>Pre-recorded audio</b>: Uses the SSML 'audio' tag to include an ocean wave sound in the welcome response.</li>
 * <p>
 * - Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a
 * multi-turn dialog model. If the user provides an incorrect slot in a one-shot model, it will
 * direct to the dialog model. See the examples section for sample interactions of these models.
 * </ul>
 * <p>
 * <h2>Examples</h2>
 * <p>
 * <b>One-shot model</b>
 * <p>
 * User: "Alexa, ask Tide Pooler when is the high tide in Seattle on Saturday" Alexa: "Saturday June
 * 20th in Seattle the first high tide will be around 7:18 am, and will peak at ...""
 * <p>
 * <b>Dialog model</b>
 * <p>
 * User: "Alexa, open Tide Pooler"
 * <p>
 * Alexa: "Welcome to Tide Pooler. Which city would you like tide information for?"
 * <p>
 * User: "Seattle"
 * <p>
 * Alexa: "For which date?"
 * <p>
 * User: "this Saturday"
 * <p>
 * Alexa: "Saturday June 20th in Seattle the first high tide will be around 7:18 am, and will peak
 * at ..."
 */
public class TidePoolerSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(TidePoolerSpeechlet.class);

    private static final String SLOT_CITY = "City";
    private static final String SLOT_DATE = "Date";
    private static final String SESSION_CITY = "city";
    private static final String SESSION_STATION = "station";
    private static final String SESSION_DATE_DISPLAY = "displayDate";
    private static final String SESSION_DATE_REQUEST = "requestDateParam";

    private static final String DATUM = "MLLW";
    private static final String ENDPOINT = "http://tidesandcurrents.noaa.gov/api/datagetter";

    // NOAA station codes
    private static final int STATION_CODE_SEATTLE = 9447130;
    private static final int STATION_CODE_SAN_FRANCISCO = 9414290;
    private static final int STATION_CODE_MONTEREY = 9413450;
    private static final int STATION_CODE_LOS_ANGELES = 9410660;
    private static final int STATION_CODE_SAN_DIEGO = 9410170;
    private static final int STATION_CODE_BOSTON = 8443970;
    private static final int STATION_CODE_NEW_YORK = 8518750;
    private static final int STATION_CODE_VIRGINIA_BEACH = 8638863;
    private static final int STATION_CODE_WILMINGTON = 8658163;
    private static final int STATION_CODE_CHARLESTON = 8665530;
    private static final int STATION_CODE_BEAUFORT = 8656483;
    private static final int STATION_CODE_MYRTLE_BEACH = 8661070;
    private static final int STATION_CODE_MIAMI = 8723214;
    private static final int STATION_CODE_TAMPA = 8726667;
    private static final int STATION_CODE_NEW_ORLEANS = 8761927;
    private static final int STATION_CODE_GALVESTON = 8771341;

    private static final int MONTH_TWO_DIGIT_THRESHOLD = 10;
    private static final double ROUND_TO_HALF_THRESHOLD = 0.75;
    private static final double ROUND_DOWN_THRESHOLD = 0.25;

    // example city to NOAA station mapping. Can be found on: http://tidesandcurrents.noaa.gov/map/
    private static final HashMap<String, Integer> STATIONS = new HashMap<String, Integer>();

    static {
        STATIONS.put("seattle", STATION_CODE_SEATTLE);
        STATIONS.put("san francisco", STATION_CODE_SAN_FRANCISCO);
        STATIONS.put("monterey", STATION_CODE_MONTEREY);
        STATIONS.put("los angeles", STATION_CODE_LOS_ANGELES);
        STATIONS.put("san diego", STATION_CODE_SAN_DIEGO);
        STATIONS.put("boston", STATION_CODE_BOSTON);
        STATIONS.put("new york", STATION_CODE_NEW_YORK);
        STATIONS.put("virginia beach", STATION_CODE_VIRGINIA_BEACH);
        STATIONS.put("wilmington", STATION_CODE_WILMINGTON);
        STATIONS.put("charleston", STATION_CODE_CHARLESTON);
        STATIONS.put("beaufort", STATION_CODE_BEAUFORT);
        STATIONS.put("myrtle beach", STATION_CODE_MYRTLE_BEACH);
        STATIONS.put("miami", STATION_CODE_MIAMI);
        STATIONS.put("tampa", STATION_CODE_TAMPA);
        STATIONS.put("new orleans", STATION_CODE_NEW_ORLEANS);
        STATIONS.put("galveston", STATION_CODE_GALVESTON);
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

        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = intent.getName();

        if ("OneshotTideIntent".equals(intentName)) {
            return handleOneshotTideRequest(intent, session);
        } else if ("DialogTideIntent".equals(intentName)) {
            // Determine if this turn is for city, for date, or an error.
            // We could be passed slots with values, no slots, slots with no value.
            Slot citySlot = intent.getSlot(SLOT_CITY);
            Slot dateSlot = intent.getSlot(SLOT_DATE);
            if (citySlot != null && citySlot.getValue() != null) {
                return handleCityDialogRequest(intent, session);
            } else if (dateSlot != null && dateSlot.getValue() != null) {
                return handleDateDialogRequest(intent, session);
            } else {
                return handleNoSlotDialogRequest(intent, session);
            }
        } else if ("SupportedCitiesIntent".equals(intentName)) {
            return handleSupportedCitiesRequest(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return handleHelpRequest();
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
    }

    private SpeechletResponse getWelcomeResponse() {
        String whichCityPrompt = "Which city would you like tide information for?";
        String speechOutput = "<speak>"
                + "Welcome to Tide Pooler. "
                + "<audio src='https://s3.amazonaws.com/ask-storage/tidePooler/OceanWaves.mp3'/>"
                + whichCityPrompt
                + "</speak>";
        String repromptText =
                "I can lead you through providing a city and "
                        + "day of the week to get tide information, "
                        + "or you can simply open Tide Pooler and ask a question like, "
                        + "get tide information for Seattle on Saturday. "
                        + "For a list of supported cities, ask what cities are supported. "
                        + whichCityPrompt;

        return newAskResponse(speechOutput, true, repromptText, false);
    }

    private SpeechletResponse handleHelpRequest() {
        String repromptText = "Which city would you like tide information for?";
        String speechOutput =
                "I can lead you through providing a city and "
                        + "day of the week to get tide information, "
                        + "or you can simply open Tide Pooler and ask a question like, "
                        + "get tide information for Seattle on Saturday. "
                        + "For a list of supported cities, ask what cities are supported. "
                        + "Or you can say exit. " + repromptText;

        return newAskResponse(speechOutput, repromptText);
    }

    /**
     * Handles the case where we need to know which city the user needs tide information for.
     */
    private SpeechletResponse handleSupportedCitiesRequest(final Intent intent,
            final Session session) {
        // get city re-prompt
        String repromptText = "Which city would you like tide information for?";
        String speechOutput =
                "Currently, I know tide information for these coastal cities: "
                        + getAllStationsText() + repromptText;

        return newAskResponse(speechOutput, repromptText);
    }

    /**
     * Handles the dialog step where the user provides a city.
     */
    private SpeechletResponse handleCityDialogRequest(final Intent intent, final Session session) {
        CityDateValues<String, String> cityStation;
        try {
            cityStation = getCityStationFromIntent(intent, false);
        } catch (Exception e) {
            String speechOutput =
                    "Currently, I know tide information for these coastal cities: "
                            + getAllStationsText()
                            + "Which city would you like tide information for?";

            // repromptText is the speechOutput
            return newAskResponse(speechOutput, speechOutput);
        }

        // if we don't have a date yet, go to date. If we have a date, we perform the final request
        if (session.getAttributes().containsKey(SESSION_DATE_DISPLAY)) {
            String displayDate = (String) session.getAttribute(SESSION_DATE_DISPLAY);
            String requestDateParam = (String) session.getAttribute(SESSION_DATE_REQUEST);
            CityDateValues<String, String> dateObject =
                    new CityDateValues<String, String>(displayDate, requestDateParam);
            return getFinalTideResponse(cityStation, dateObject);
        } else {
            // set city in session and prompt for date
            session.setAttribute(SESSION_CITY, cityStation.speechValue);
            session.setAttribute(SESSION_STATION, cityStation.apiValue);
            String speechOutput = "For which date?";
            String repromptText =
                    "For which date would you like tide information for " + cityStation.speechValue
                            + "?";

            return newAskResponse(speechOutput, repromptText);
        }
    }

    /**
     * Handles the dialog step where the user provides a date.
     */
    private SpeechletResponse handleDateDialogRequest(final Intent intent, final Session session) {
        CityDateValues<String, String> dateObject = getDateFromIntent(intent);
        // if we don't have a city yet, go to city. If we have a city, we perform the final request
        if (session.getAttributes().containsKey(SESSION_CITY)) {
            String city = (String) session.getAttribute(SESSION_CITY);
            String station = (String) session.getAttribute(SESSION_STATION);
            CityDateValues<String, String> cityStationMap =
                    new CityDateValues<String, String>(city, station);
            return getFinalTideResponse(cityStationMap, dateObject);
        } else {
            // The user provided a date out of turn. Set date in session and prompt for city
            session.setAttribute(SESSION_DATE_DISPLAY, dateObject.speechValue);
            session.setAttribute(SESSION_DATE_REQUEST, dateObject.apiValue);
            String speechOutput =
                    "For which city would you like tide information for " + dateObject.speechValue
                            + "?";
            String repromptText = "For which city?";

            return newAskResponse(speechOutput, repromptText);
        }
    }

    /**
     * Handle no slots, or slot(s) with no values. In the case of a dialog based skill with multiple
     * slots, when passed a slot with no value, we cannot have confidence it is is the correct slot
     * type so we rely on session state to determine the next turn in the dialog, and reprompt.
     */
    private SpeechletResponse handleNoSlotDialogRequest(final Intent intent, final Session session) {
        if (session.getAttributes().containsKey(SESSION_CITY)) {
            // get date re-prompt
            String speechOutput =
                    "Please try again saying a day of the week, for example, Saturday";

            // repromptText is the speechOutput
            return newAskResponse(speechOutput, speechOutput);
        } else {
            // get city re-prompt
            return handleSupportedCitiesRequest(intent, session);
        }
    }

    /**
     * This handles the one-shot interaction, where the user utters a phrase like: 'Alexa, open Tide
     * Pooler and get tide information for Seattle on Saturday'. If there is an error in a slot,
     * this will guide the user to the dialog approach.
     */
    private SpeechletResponse handleOneshotTideRequest(final Intent intent, final Session session) {
        // Determine city, using default if none provided
        CityDateValues<String, String> cityObject = null;
        try {
            cityObject = getCityStationFromIntent(intent, true);
        } catch (Exception e) {
            // invalid city. move to the dialog
            String speechOutput =
                    "Currently, I know tide information for these coastal cities: "
                            + getAllStationsText()
                            + "Which city would you like tide information for?";

            // repromptText is the same as the speechOutput
            return newAskResponse(speechOutput, speechOutput);
        }

        // Determine custom date
        CityDateValues<String, String> dateObject = getDateFromIntent(intent);

        // all slots filled, either from the user or by default values. Move to final request
        return getFinalTideResponse(cityObject, dateObject);
    }

    /**
     * Both the one-shot and dialog based paths lead to this method to issue the request, and
     * respond to the user with the final answer.
     */
    private SpeechletResponse getFinalTideResponse(CityDateValues<String, String> cityStation,
            CityDateValues<String, String> date) {
        return makeTideRequest(cityStation, date);
    }

    /**
     * Uses NOAA.gov API, documented at noaa.gov. Results can be verified at:
     * http://tidesandcurrents.noaa.gov/noaatidepredictions/NOAATidesFacade.jsp?Stationid=[id] .
     *
     * @see <a href = "http://tidesandcurrents.noaa.gov/api/">noaa.gov</a>
     * @throws IOException
     */
    private SpeechletResponse makeTideRequest(CityDateValues<String, String> cityStation,
            CityDateValues<String, String> date) {
        String queryString =
                String.format("?%s&station=%s&product=predictions&datum=%s&units=english"
                        + "&time_zone=lst_ldt&format=json", date.apiValue, cityStation.apiValue,
                        DATUM);

        String speechOutput = "";

        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            URL url = new URL(ENDPOINT + queryString);
            inputStream = new InputStreamReader(url.openStream(), Charset.forName("US-ASCII"));
            bufferedReader = new BufferedReader(inputStream);
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            // reset builder to a blank string
            builder.setLength(0);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(bufferedReader);
        }

        if (builder.length() == 0) {
            speechOutput =
                    "Sorry, the National Oceanic tide service is experiencing a problem. "
                            + "Please try again later.";
        } else {
            try {
                JSONObject noaaResponseObject = new JSONObject(new JSONTokener(builder.toString()));
                if (noaaResponseObject != null) {
                    HighTideValues highTideResponse = findHighTide(noaaResponseObject);
                    speechOutput =
                            new StringBuilder()
                                    .append(date.speechValue)
                                    .append(" in ")
                                    .append(cityStation.speechValue)
                                    .append(", the first high tide will be around ")
                                    .append(highTideResponse.firstHighTideTime)
                                    .append(", and will peak at about ")
                                    .append(highTideResponse.firstHighTideHeight)
                                    .append(", followed by a low tide at around ")
                                    .append(highTideResponse.lowTideTime)
                                    .append(" that will be about ")
                                    .append(highTideResponse.lowTideHeight)
                                    .append(". The second high tide will be around ")
                                    .append(highTideResponse.secondHighTideTime)
                                    .append(", and will peak at about ")
                                    .append(highTideResponse.secondHighTideHeight)
                                    .append(".")
                                    .toString();
                }
            } catch (JSONException | ParseException e) {
                log.error("Exception occoured while parsing service response.", e);
            }
        }

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Tide Pooler");
        card.setContent(speechOutput);

        // Create the plain text output
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        return SpeechletResponse.newTellResponse(outputSpeech, card);
    }

    /**
     * Algorithm to find the 2 high tides for the day, the first of which is smaller and occurs
     * mid-day, the second of which is larger and typically in the evening.
     *
     * @throws JSONException
     * @throws ParseException
     */
    private HighTideValues findHighTide(JSONObject responseObject) throws JSONException,
            ParseException {
        JSONArray predictions = (JSONArray) responseObject.get("predictions");
        JSONObject lastPrediction = null, firstHighTide = null, secondHighTide = null, lowTide =
                null;
        boolean firstTideDone = false;

        for (int i = 0; i < predictions.length(); i++) {
            JSONObject prediction = (JSONObject) predictions.get(i);

            if (lastPrediction == null) {
                lastPrediction = prediction;
                continue;
            }

            if (isTideIncreasing(lastPrediction, prediction)) {
                if (!firstTideDone) {
                    firstHighTide = prediction;
                } else {
                    secondHighTide = prediction;
                }
            } else { // we're decreasing
                if (!firstTideDone && firstHighTide != null) {
                    firstTideDone = true;
                } else if (secondHighTide != null) {
                    break; // we're decreasing after having found the 2nd tide. We're done.
                }

                if (firstTideDone) {
                    lowTide = prediction;
                }
            }
            lastPrediction = prediction;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d HH:mm");
        return new HighTideValues(AlexaDateUtil.getFormattedTime(dateFormat.parse(firstHighTide
                .getString("t"))), getFormattedHeight(firstHighTide.getString("v")),
                AlexaDateUtil.getFormattedTime(dateFormat.parse(lowTide.getString("t"))),
                getFormattedHeight(lowTide.getString("v")),
                AlexaDateUtil.getFormattedTime(dateFormat.parse(secondHighTide.getString("t"))),
                getFormattedHeight(secondHighTide.getString("v")));
    }

    /**
     * Formats the height, rounding to the nearest 1/2 foot. e.g. 4.354 -> "four and a half feet".
     */
    private String getFormattedHeight(String heightString) {
        double height = Double.parseDouble(heightString);
        boolean isNegative = false;
        if (height < 0) {
            height = Math.abs(height);
            isNegative = true;
        }

        double remainder = height % 1;
        int feet;
        String remainderText;

        if (remainder < ROUND_DOWN_THRESHOLD) {
            remainderText = "";
            feet = (int) Math.floor(height);
        } else if (remainder < ROUND_TO_HALF_THRESHOLD) {
            remainderText = " and a half";
            feet = (int) Math.floor(height);
        } else {
            remainderText = "";
            feet = (int) Math.ceil(height);
        }

        if (isNegative) {
            feet *= -1;
        }

        String formattedHeight = feet + remainderText + " feet";
        return formattedHeight;
    }

    /**
     * Gets the city from the intent, or throws an error.
     */
    private CityDateValues<String, String> getCityStationFromIntent(final Intent intent,
            final boolean assignDefault) throws Exception {
        Slot citySlot = intent.getSlot(SLOT_CITY);
        CityDateValues<String, String> cityObject = null;
        // slots can be missing, or slots can be provided but with empty value.
        // must test for both.
        if (citySlot == null || citySlot.getValue() == null) {
            if (!assignDefault) {
                throw new Exception("");
            } else {
                // For sample skill, default to Seattle.
                cityObject =
                        new CityDateValues<String, String>("seattle", Integer.toString(STATIONS
                                .get("seattle")));
            }
        } else {
            // lookup the city. Sample skill uses well known mapping of a few known cities to
            // station id.
            String cityName = citySlot.getValue();
            if (STATIONS.containsKey(cityName.toLowerCase())) {
                cityObject =
                        new CityDateValues<String, String>(cityName, Integer.toString(STATIONS
                                .get(cityName.toLowerCase())));
            } else {
                throw new Exception(cityName);
            }
        }
        return cityObject;
    }

    private static boolean isTideIncreasing(JSONObject lastPrediction, JSONObject currentPrediction)
            throws NumberFormatException, JSONException {
        return Double.parseDouble(lastPrediction.getString("v")) < Double
                .parseDouble(currentPrediction.getString("v"));
    }

    /**
     * Gets the date from the intent, defaulting to today if none provided, or returns an error.
     */
    private CityDateValues<String, String> getDateFromIntent(final Intent intent) {
        Slot dateSlot = intent.getSlot(SLOT_DATE);
        CityDateValues<String, String> dateObject;

        // slots can be missing, or slots can be provided but with empty value.
        // must test for both
        if (dateSlot == null || dateSlot.getValue() == null) {
            // default to today
            dateObject = new CityDateValues<String, String>("Today", "date=today");
            return dateObject;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d");
            Date date;
            try {
                date = dateFormat.parse(dateSlot.getValue());
            } catch (ParseException e) {
                date = new Date();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            String monthString =
                    month < MONTH_TWO_DIGIT_THRESHOLD ? "0" + Integer.toString(month) : Integer
                            .toString(month);
            int day = calendar.get(Calendar.DATE);
            String dayString =
                    day < MONTH_TWO_DIGIT_THRESHOLD ? "0" + Integer.toString(day) : Integer
                            .toString(day);
            String requestDay =
                    "begin_date=" + calendar.get(Calendar.YEAR) + monthString + dayString
                            + "&range=24";

            dateObject =
                    new CityDateValues<String, String>(AlexaDateUtil.getFormattedDate(date),
                            requestDay);
            return dateObject;
        }
    }

    private String getAllStationsText() {
        StringBuilder stationList = new StringBuilder();
        for (String station : STATIONS.keySet()) {
            stationList.append(station);
            stationList.append(", ");
        }
        return stationList.toString();
    }

    /**
     * Wrapper for creating the Ask response from the input strings with
     * plain text output and reprompt speeches.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
        return newAskResponse(stringOutput, false, repromptText, false);
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
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(stringOutput);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

    /**
     * Encapsulates the return values for high tide information in a single object.
     */
    private static class HighTideValues {
        private final String firstHighTideTime, firstHighTideHeight, lowTideTime, lowTideHeight,
                secondHighTideTime, secondHighTideHeight;

        HighTideValues(String firstHighTideTime, String firstHighTideHeight, String lowTideTime,
                String lowTideHeight, String secondHighTideTime, String secondHighTideHeight) {
            this.firstHighTideTime = firstHighTideTime;
            this.firstHighTideHeight = firstHighTideHeight;
            this.lowTideTime = lowTideTime;
            this.lowTideHeight = lowTideHeight;
            this.secondHighTideTime = secondHighTideTime;
            this.secondHighTideHeight = secondHighTideHeight;
        }
    }

    /**
     * Encapsulates the speech and api value for date and citystation objects.
     *
     * @param <L>
     *            text that will be spoken to the user
     * @param <R>
     *            text that will be passed in as an input to an API
     */
    private static class CityDateValues<L, R> {
        private final L speechValue;
        private final R apiValue;

        public CityDateValues(L speechValue, R apiValue) {
            this.speechValue = speechValue;
            this.apiValue = apiValue;
        }
    }
}
