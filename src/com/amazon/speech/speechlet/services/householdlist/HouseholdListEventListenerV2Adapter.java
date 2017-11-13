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

import com.amazon.speech.json.SpeechletRequestEnvelope;

/**
 * This class is intended for internal use only.
 *
 * It adapts a {@link HouseholdListEventListener} into a {@link HouseholdListEventListenerV2}.
 */
public class HouseholdListEventListenerV2Adapter implements HouseholdListEventListenerV2 {

    private final HouseholdListEventListener v1Listener;

    public HouseholdListEventListenerV2Adapter(HouseholdListEventListener v1Listener) {
        this.v1Listener = v1Listener;
    }

    @Override
    public void onListItemsCreated(SpeechletRequestEnvelope<ListItemsCreatedRequest> envelope) {
        v1Listener.onListItemsCreated(envelope);
    }

    @Override
    public void onListItemsUpdated(SpeechletRequestEnvelope<ListItemsUpdatedRequest> envelope) {
        v1Listener.onListItemsUpdated(envelope);
    }

    @Override
    public void onListItemsDeleted(SpeechletRequestEnvelope<ListItemsDeletedRequest> envelope) {
        v1Listener.onListItemsDeleted(envelope);
    }

    @Override
    public void onListCreated(SpeechletRequestEnvelope<ListCreatedRequest> envelope) {
        // no-op
    }

    @Override
    public void onListUpdated(SpeechletRequestEnvelope<ListUpdatedRequest> envelope) {
        // no-op
    }

    @Override
    public void onListDeleted(SpeechletRequestEnvelope<ListDeletedRequest> envelope) {
        // no-op
    }
}
