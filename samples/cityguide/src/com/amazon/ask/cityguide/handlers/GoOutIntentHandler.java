/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.cityguide.handlers;

import com.amazon.ask.cityguide.utils.HttpUtils;
import com.amazon.ask.cityguide.utils.SkillData;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.util.impl.ObjectMapperFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GoOutIntentHandler implements IntentRequestHandler {

    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();
    private static final String WEATHER_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather";

    /*
     * Retrieved from https://openweathermap.org/current
     * */
    private static final String CITY_ID = "4937829";

    /*
     * should be retrieved from your account in https://openweathermap.org/
     * */
    private static final String API_KEY = "";

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("GoOutIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        Map<String, String> requestParams = new HashMap<String, String>() {
            {
                put("id", CITY_ID);
                put("appid", API_KEY);
            }
        };

        try (CloseableHttpResponse response = HttpUtils.doGet(requestParams, WEATHER_ENDPOINT)) {
            JsonNode node = MAPPER.readTree(response.getEntity().getContent());
            final String temperature = String.valueOf(node.path("main").path("temp").doubleValue() - 273.15);
            final String humidity = node.path("main").path("humidity").asText();
            final String windSpeed = node.path("wind").path("speed").asText();

            final String speechOutput = String.format("Weather in %s is with temperature %s celsius and %s percent humidity. Wind speed is about %s miles per hour",
                    SkillData.CITY_INFORMATION.getCity().getName(),
                    temperature,
                    humidity,
                    windSpeed);

            return handlerInput.getResponseBuilder()
                    .withSpeech(speechOutput)
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse Weather data response", ex);
        }
    }
}
