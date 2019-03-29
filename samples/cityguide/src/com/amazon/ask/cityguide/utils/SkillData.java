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
import com.amazon.ask.cityguide.POJO.CityInformation;
import com.amazon.ask.cityguide.POJO.Restaurant;

import java.util.Arrays;
import java.util.HashSet;

public class SkillData {

    public static final CityInformation CITY_INFORMATION = CityInformation.builder()
            .withCity(City.GLOUCESTER)
            .withState("MA")
            .withAttractions(new HashSet<>(Arrays.asList(
                    Attraction.builder()
                            .withName("Whale Watching")
                            .withDescription("Gloucester has tour boats that depart twice daily from Rogers street at the harbor.  Try either the 7 Seas Whale Watch, or Captain Bill and Sons Whale Watch. ")
                            .withDistance(0)
                            .build(),
                    Attraction.builder()
                            .withName("Good Harbor Beach")
                            .withDescription("Facing the Atlantic Ocean, Good Harbor Beach has huge expanses of soft white sand that attracts hundreds of visitors every day during the summer.")
                            .withDistance(2)
                            .build(),
                    Attraction.builder()
                            .withName("Rockport")
                            .withDescription("A quaint New England town, Rockport is famous for rocky beaches, seaside parks, lobster fishing boats, and several art studios.")
                            .withDistance(4)
                            .build(),
                    Attraction.builder()
                            .withName("Fenway Park")
                            .withDescription("Home of the Boston Red Sox, Fenway park hosts baseball games From April until October, and is open for tours. ")
                            .withDistance(38)
                            .build()
            )))
            .withrestaurants(new HashSet<>(Arrays.asList(
                    Restaurant.builder()
                            .withName("Zeke's Place")
                            .withAddress("66 East Main Street")
                            .withPhone("978-283-0474")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.BREAKFAST, Meal.LUNCH)))
                            .withDescription("A cozy and popular spot for breakfast.  Try the blueberry french toast!")
                            .build(),
                    Restaurant.builder()
                            .withName("Morning Glory Coffee Shop")
                            .withAddress("25 Western Avenue")
                            .withPhone("978-281-1851")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.COFFEE, Meal.BREAKFAST, Meal.LUNCH)))
                            .withDescription("A home style diner located just across the street from the harbor sea wall.")
                            .build(),
                    Restaurant.builder()
                            .withName("Sugar Magnolias")
                            .withAddress("112 Main Street")
                            .withPhone("978-281-5310")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.BREAKFAST, Meal.LUNCH)))
                            .withDescription("A quaint eatery, popular for weekend brunch. Try the carrot cake pancakes.")
                            .build(),
                    Restaurant.builder()
                            .withName("Seaport Grille")
                            .withAddress("6 Rowe Square")
                            .withPhone("978-282-9799")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.LUNCH, Meal.DINNER)))
                            .withDescription("Serving seafood, steak and casual fare.  Enjoy harbor views on the deck.")
                            .build(),
                    Restaurant.builder()
                            .withName("Latitude 43")
                            .withAddress("25 Rogers Street")
                            .withPhone("978-281-0223")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.LUNCH, Meal.DINNER)))
                            .withDescription("Features artsy decor and sushi specials.  Live music evenings at the adjoining Minglewood Tavern.")
                            .build(),
                    Restaurant.builder()
                            .withName("George's Coffee Shop")
                            .withAddress("178 Washington Street")
                            .withPhone("978-281-1910")
                            .withMeals(new HashSet<>(Arrays.asList(Meal.COFFEE, Meal.BREAKFAST, Meal.LUNCH)))
                            .withDescription("A highly rated local diner with generously sized plates.")
                            .build()
            )))
            .build();
}
