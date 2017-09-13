package com.amazon.speech.speechlet.services.householdlist;

import com.amazon.speech.json.SpeechletRequestEnvelope;

public interface HouseholdListEventListener {
    void onListItemsCreated(SpeechletRequestEnvelope<ListItemsCreatedRequest> envelope);

    void onListItemsUpdated(SpeechletRequestEnvelope<ListItemsUpdatedRequest> envelope);

    void onListItemsDeleted(SpeechletRequestEnvelope<ListItemsDeletedRequest> envelope);

}
