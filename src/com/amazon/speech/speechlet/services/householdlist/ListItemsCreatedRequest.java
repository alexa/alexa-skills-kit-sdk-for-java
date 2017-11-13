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
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonTypeName("AlexaHouseholdListEvent.ItemsCreated")
public class ListItemsCreatedRequest extends AlexaHouseholdListEventRequest {
    private final ListItemBody body;

    public static Builder builder() {
        return new Builder();
    }

    private ListItemsCreatedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
    }

    private ListItemsCreatedRequest(@JsonProperty("requestId") final String requestId,
                                    @JsonProperty("timestamp") final Date timestamp,
                                    @JsonProperty("locale") final Locale locale,
                                    @JsonProperty("body") final ListItemBody body) {
        super(requestId, timestamp, locale);
        this.body = body;
    }

    public String getListId() {
        return body.getListId();
    }

    public List<String> getListItemIds() {
        return body.getListItemIds();
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, ListItemsCreatedRequest> {
        private ListItemBody body;

        private Builder() {

        }

        public Builder withListItemBody(final ListItemBody body) {
            this.body = body;
            return this;
        }

        @Override
        public ListItemsCreatedRequest build() {
            return new ListItemsCreatedRequest(this);
        }
    }
}
