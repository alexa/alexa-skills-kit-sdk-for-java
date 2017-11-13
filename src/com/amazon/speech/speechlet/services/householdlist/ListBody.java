/*
 * Copyright 2016-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 * this file except in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazon.speech.speechlet.services.householdlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListBody {
    private final String listId;

    public  static Builder builder() { return new Builder();}

    private ListBody(final Builder builder){
        listId=builder.listId;
    }

    private ListBody(@JsonProperty("listId") final String listId){
        this.listId = listId;
    }

    public String getListId() { return listId; }

    public static final class Builder {
        private String listId;

        public Builder withListId(final String listId) {
            this.listId = listId;
            return this;
        }

        public ListBody build() {
            return new ListBody(this);
        }
    }
}