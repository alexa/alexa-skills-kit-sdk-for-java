/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.cityguide.POJO;

import com.amazon.ask.cityguide.utils.City;

import java.util.Objects;
import java.util.Set;

public class CityInformation {

    private final City city;
    private final String state;
    private final Set<Restaurant> restaurants;
    private final Set<Attraction> attractions;

    private CityInformation(City city, String state, Set<Restaurant> restaurants, Set<Attraction> attractions) {
        this.city = city;
        this.state = state;
        this.restaurants = restaurants;
        this.attractions = attractions;
    }

    public City getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Set<Attraction> getAttractions() {
        return attractions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityInformation)) return false;
        CityInformation that = (CityInformation) o;
        return city == that.city &&
                state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, state);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private City city;
        private String state;
        private Set<Restaurant> restaurants;
        private Set<Attraction> attractions;

        public Builder(){}

        public Builder withCity(City city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withrestaurants(Set<Restaurant> restaurants) {
            this.restaurants = restaurants;
            return this;
        }

        public Builder withAttractions(Set<Attraction> attractions) {
            this.attractions = attractions;
            return this;
        }

        public CityInformation build() {
            return new CityInformation(city, state, restaurants, attractions);
        }
    }

}
