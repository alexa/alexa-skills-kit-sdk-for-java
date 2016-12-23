/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.verifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;

/**
 * Verifies whether or not individual application IDs are supported by the service.
 */
public class ApplicationIdSpeechletRequestEnvelopeVerifier implements SpeechletRequestEnvelopeVerifier {
    private final Set<String> supportedApplicationIds;
    private static final Logger log = LoggerFactory
            .getLogger(ApplicationIdSpeechletRequestEnvelopeVerifier.class);

    /**
     * Constructs a new application ID verifier with the provided set of supported application IDs.
     * If the set is empty, this verifier will support any application ID (including null values).
     *
     * @param supportedApplicationIds
     *            a set of application IDs supported by the service
     */
    public ApplicationIdSpeechletRequestEnvelopeVerifier(Set<String> supportedApplicationIds) {
        this.supportedApplicationIds =
                Collections.unmodifiableSet(new HashSet<String>(supportedApplicationIds));
    }

    /**
     * Returns true if the provided {@code applicationId} is supported by this verifier. If this
     * verifier was constructed from an empty set of supported {@code applicationId}s, this method
     * returns true for any application ID (including null values). Otherwise, this method returns
     * true only if the {@link SpeechletRequest}'s {@code applicationId} is supported. If the
     * {@code applicationId} cannot be found, the method will return false.
     *
     * @param requestEnvelope
     *            the request in which to verify a valid application ID
     * @return true if the provided application ID is supported by this verifier
     */
    @Override
    public boolean verify(SpeechletRequestEnvelope<?> requestEnvelope) {
        if (supportedApplicationIds.isEmpty()) {
            log.warn("Application ID verification has been disabled, allowing request for all "
                    + "application IDs");
            return true;
        }

        Session session = requestEnvelope.getSession();

        boolean applicationIdIsMissingFromSession = session == null || session.getApplication() == null
                || session.getApplication().getApplicationId() == null;

        if (!applicationIdIsMissingFromSession) {
            /*
             * Note: we are still looking at the Session and not just the Context because some
             * clients may not yet be sending Context.
             */
            return supportedApplicationIds.contains(session.getApplication().getApplicationId());
        }

        Context context = requestEnvelope.getContext();
        if (context == null) {
            return false;
        }

        SystemState systemState = context.getState(SystemInterface.class, SystemInterface.STATE_TYPE);

        if (systemState == null || systemState.getApplication() == null
                || systemState.getApplication().getApplicationId() == null) {
            return false;
        }

        return supportedApplicationIds.contains(systemState.getApplication().getApplicationId());
    }
}
