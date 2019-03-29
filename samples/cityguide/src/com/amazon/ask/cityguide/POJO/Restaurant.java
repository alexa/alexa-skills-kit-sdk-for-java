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

import com.amazon.ask.cityguide.utils.Meal;

import java.util.Objects;
import java.util.Set;

public class Restaurant {

    private final String name;
    private final String address;
    private final String phone;
    private final Set<Meal> meals;
    private final String description;

    private Restaurant(String name, String address, String phone, Set<Meal> meals, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.meals = meals;
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return name.equals(that.name) &&
                address.equals(that.address) &&
                phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {

        private String name;
        private String address;
        private String phone;
        private Set<Meal> meals;
        private String description;

        protected Builder(){}

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }


        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }


        public Builder withMeals(Set<Meal> meals) {
            this.meals = meals;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(name, address, phone, meals, description);
        }
    }
}