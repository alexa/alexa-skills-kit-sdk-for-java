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

public class UpdateListRequest {

    private String name;
    private ListState state;
    private long version;

    private UpdateListRequest(Builder builder) {
        setName(builder.name);
        setState(builder.state);
        setVersion(builder.version);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListState getState() {
        return state;
    }

    public void setState(ListState state) {
        this.state = state;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public static final class Builder {
        private String name;
        private ListState state;
        private long version;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withState(ListState val) {
            state = val;
            return this;
        }

        public Builder withVersion(long val) {
            version = val;
            return this;
        }

        public UpdateListRequest build() {
            return new UpdateListRequest(this);
        }
    }
}
