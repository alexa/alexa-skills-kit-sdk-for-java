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

import java.util.List;

public class AlexaListsMetadata {

    private List<AlexaListMetadata> lists;

    public List<AlexaListMetadata> getLists() {
        return lists;
    }

    public void setLists(final List<AlexaListMetadata> lists) {
        this.lists = lists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlexaListsMetadata that = (AlexaListsMetadata) o;

        return lists != null ? lists.equals(that.lists) : that.lists == null;
    }

    @Override
    public int hashCode() {
        return lists != null ? lists.hashCode() : 0;
    }

}
