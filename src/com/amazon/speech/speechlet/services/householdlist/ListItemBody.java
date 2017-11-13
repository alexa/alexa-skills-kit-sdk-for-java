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

import java.util.Collections;
import java.util.List;

public class ListItemBody {
    private final String listId;
    private final List<String> listItemIds;

    public  static Builder builder() {
        return new Builder();
    }

    private ListItemBody(final Builder builder) {
        listId = builder.listId;
        listItemIds = Collections.unmodifiableList(builder.listItemIds);
    }

    private ListItemBody(@JsonProperty("listId") final String listId,
                         @JsonProperty("listItemIds") final List<String> listItemIds) {
        this.listId = listId;
        if(listItemIds != null ) {
            this.listItemIds = Collections.unmodifiableList(listItemIds);
        } else {
            this.listItemIds = Collections.emptyList();
        }
    }

    public String getListId() {
        return listId;
    }

    public List<String> getListItemIds() {
        return listItemIds;
    }

    public static final class Builder {
        private String listId;
        private List<String> listItemIds;

        public Builder withListId(final String listId) {
            this.listId = listId;
            return this;
        }

        public Builder withListItemIds(final List<String> listItemIds) {
            this.listItemIds = listItemIds;
            return this;
        }

        public ListItemBody build() {
            return new ListItemBody(this);
        }
    }
}
