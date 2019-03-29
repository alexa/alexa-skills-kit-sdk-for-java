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

public class Attraction {

    private final String name;
    private final String description;
    private final double distance;


    private Attraction(String name, String description, double distance) {
        this.name = name;
        this.description = description;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getDistance() {
        return distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private double distance;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public Attraction build() {
            return new Attraction(name, description, distance);
        }
    }
}
