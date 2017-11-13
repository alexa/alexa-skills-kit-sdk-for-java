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
import java.util.Locale;

@JsonTypeName("AlexaHouseholdListEvent.ListDeleted")
public class ListDeletedRequest extends AlexaHouseholdListEventRequest {
    private final ListBody body;


    public static Builder builder() {
        return new Builder();
    }

    private ListDeletedRequest(final Builder builder) {
        super(builder);
        this.body = builder.body;
    }

    private ListDeletedRequest(@JsonProperty("requestId") final String requestId,
                               @JsonProperty("timestamp") final Date timestamp,
                               @JsonProperty("locale") final Locale locale,
                               @JsonProperty("body") final ListBody body) {
        super(requestId, timestamp, locale);
        this.body = body;
    }

    public String getListId() {
        return this.body.getListId();
    }

    public static final class Builder extends SpeechletRequestBuilder<Builder, ListDeletedRequest> {
        private ListBody body;

        private Builder() {

        }

        public Builder withListBody(final ListBody body) {
            this.body = body;
            return this;
        }

        @Override
        public ListDeletedRequest build() {
            return new ListDeletedRequest(this);
        }
    }
}