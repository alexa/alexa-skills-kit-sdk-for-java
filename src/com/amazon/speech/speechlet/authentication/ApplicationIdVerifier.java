/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.authentication;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestVerifier;

/**
 * Verifies whether or not individual application IDs are supported by the web service.
 * 
 * @deprecated Use {@link ApplicationIdSpeechletRequestVerifier} instead of this class.
 */
@Deprecated
public class ApplicationIdVerifier {
    private final Set<String> supportedApplicationIds;
    private static final Logger log = LoggerFactory.getLogger(ApplicationIdVerifier.class);

    /**
     * Constructs a new application ID verifier with the provided set of supported application IDs.
     * If the set is empty, this verifier will support any application ID (including null values).
     *
     * @param supportedApplicationIds
     *            a set of application IDs supported by the application service
     */
    public ApplicationIdVerifier(Set<String> supportedApplicationIds) {
        this.supportedApplicationIds =
                Collections.unmodifiableSet(new HashSet<String>(supportedApplicationIds));
    }

    /**
     * Returns true if the provided application ID is supported by this verifier. If this verifier
     * was constructed from an empty set of supported application IDs, this method will return true
     * for any application ID (including null values).
     *
     * @param applicationId
     *            the application ID to verify
     * @return true if the provided application ID is supported by this verifier
     */
    public boolean isSupported(String applicationId) {
        if (supportedApplicationIds.isEmpty()) {
            log.warn("Application ID verification has been disabled, "
                    + "allowing request for application ID {}", applicationId);
            return true;
        } else if (applicationId == null) {
            return false;
        } else {
            return supportedApplicationIds.contains(applicationId);
        }
    }
}
