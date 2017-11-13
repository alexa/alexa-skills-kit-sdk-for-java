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

public class UpdateListItemRequest {

    private String value;
    private ListItemState status;
    private long version;

    private UpdateListItemRequest(Builder builder) {
        setValue(builder.value);
        setStatus(builder.status);
        setVersion(builder.version);
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public static final class Builder {
        private String value;
        private ListItemState status;
        private long version;

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

        public Builder withVersion(long val) {
            version = val;
            return this;
        }

        public UpdateListItemRequest build() {
            return new UpdateListItemRequest(this);
        }
    }
}
