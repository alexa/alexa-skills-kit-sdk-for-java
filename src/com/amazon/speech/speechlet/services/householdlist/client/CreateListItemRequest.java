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

package com.amazon.speech.speechlet.services.householdlist.client;

public class CreateListItemRequest {

    private String value;
    private ListItemState status;

    private CreateListItemRequest(Builder builder) {
        setValue(builder.value);
        setStatus(builder.status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ListItemState getStatus() {
        return status;
    }

    public void setStatus(ListItemState status) {
        this.status = status;
    }

    public static final class Builder {
        private String value;
        private ListItemState status;

        private Builder() {
        }

        public Builder withValue(String val) {
            value = val;
            return this;
        }

        public Builder withStatus(ListItemState val) {
            status = val;
            return this;
        }

        public CreateListItemRequest build() {
            return new CreateListItemRequest(this);
        }
    }
}
