/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.cityguide.utils;

import com.amazon.ask.cityguide.POJO.Attraction;
import com.amazon.ask.cityguide.POJO.Restaurant;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class SkillUtils {

    public static ResourceBundle getResourceBundle(HandlerInput handlerInput, String bundleName) {
        final Locale locale = new Locale(handlerInput.getRequestEnvelope().getRequest().getLocale());
        return ResourceBundle.getBundle("Messages", locale);
    }

    public static List<Restaurant> getRestaurantsByMeal(Meal meal, Set<Restaurant> restaurants) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMeals().contains(meal))
                .collect(Collectors.toList());
    }

    public static Optional<Restaurant> getRestaurantByName(String name, Set<Restaurant> restaurants) {
        return restaurants.stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst();
    }

    public static List<Attraction> getAttractionsWithinDistance(double radius, Set<Attraction> attractions) {
        return attractions.stream()
                .filter(attraction -> attraction.getDistance() <= radius)
                .sorted((a1, a2) -> (int) (a1.getDistance() - a2.getDistance()))
                .collect(Collectors.toList());
    }

    public static Optional<Response> getRestaurantDataAsResponse(HandlerInput handlerInput, boolean shouldAddToSessionAttributes,
                                                                 Meal meal, String bundleName, String messageKey, Set<Restaurant> restaurants) {
        final List<Restaurant> filteredRestaurants = getRestaurantsByMeal(meal, restaurants);
        final Restaurant restaurant = filteredRestaurants.get(getRandomIndexInRange(filteredRestaurants.size(), 0));
        final ResourceBundle messages = getResourceBundle(handlerInput, bundleName);
        if (shouldAddToSessionAttributes) {
            final Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
            sessionAttributes.put("restaurant", restaurant.getName());
        }
        final String speechOutput = String.format(messages.getString(messageKey), restaurant.getName());
        return handlerInput.getResponseBuilder()
                .withSpeech(speechOutput)
                .withReprompt(speechOutput)
                .build();
    }

    public static int getRandomIndexInRange(int ceil, int floor) {
        return new Random().nextInt(ceil - floor) + floor;
    }
}
