package com.amazon.speech.speechlet.interfaces.core;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.interfaces.core.*;

public interface SkillEventListener {

    void onSkillEnabled(SpeechletRequestEnvelope<SkillEnabledEventRequest> envelope);

    void onSkillDisabled(SpeechletRequestEnvelope<SkillDisabledEventRequest> envelope);

    void onPermissionAccepted(SpeechletRequestEnvelope<PermissionAcceptedRequest> envelope);

    void onPermissionChanged(SpeechletRequestEnvelope<PermissionChangedRequest> envelope);

    void onAccountLinked(SpeechletRequestEnvelope<AccountLinkedRequest> envelope);


}
