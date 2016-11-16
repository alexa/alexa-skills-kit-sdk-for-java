/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletRequestVerifierWrapper;
import com.amazon.speech.speechlet.verifier.SpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;

/**
 * Request handler for servlet-based services
 */
public class ServletSpeechletRequestHandler extends SpeechletRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(ServletSpeechletRequestHandler.class);

    public ServletSpeechletRequestHandler() {
        super(requestVerifiers());
    }

    private static List<SpeechletRequestEnvelopeVerifier> requestVerifiers() {
        List<SpeechletRequestEnvelopeVerifier> requestVerifiers =
                new ArrayList<SpeechletRequestEnvelopeVerifier>();
        requestVerifiers.add(getApplicationIdVerifier());
        TimestampSpeechletRequestVerifier timestampVerifier = getTimestampVerifier();
        if (timestampVerifier != null) {
            requestVerifiers.add(new SpeechletRequestVerifierWrapper(timestampVerifier));
        }
        return requestVerifiers;
    }

    /**
     * Returns a {@link TimestampSpeechletRequestVerifier} configured using timestamp tolerance
     * defined by the system property {@link Sdk#TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY}. If a valid
     * timestamp tolerance is missing in the system properties, then {@code null} is returned.
     *
     * @return a configured TimestampSpeechletRequestVerifier or null
     */
    private static TimestampSpeechletRequestVerifier getTimestampVerifier() {
        String timestampToleranceAsString =
                System.getProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY);

        if (!StringUtils.isBlank(timestampToleranceAsString)) {
            try {
                long timestampTolerance = Long.parseLong(timestampToleranceAsString);
                return new TimestampSpeechletRequestVerifier(timestampTolerance, TimeUnit.SECONDS);
            } catch (NumberFormatException ex) {
                log.warn("The configured timestamp tolerance {} is invalid, "
                        + "disabling timestamp verification", timestampToleranceAsString);
            }
        } else {
            log.warn("No timestamp tolerance has been configured, "
                    + "disabling timestamp verification");
        }

        return null;
    }

    /**
     * Returns a {@link ApplicationIdSpeechletRequestEnvelopeVerifier} configured using the
     * supported application IDs provided by the system property
     * {@link Sdk#SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY}. If the supported application IDs are
     * not defined, then an {@link ApplicationIdSpeechletRequestVerifier} initialized with an empty
     * set is returned.
     *
     * @return a configured {@link ApplicationIdSpeechletRequestVerifier}
     */
    private static ApplicationIdSpeechletRequestEnvelopeVerifier getApplicationIdVerifier() {
        // Build an application ID verifier from a comma-delimited system property value
        Set<String> supportedApplicationIds = Collections.emptySet();
        String commaDelimitedListOfSupportedApplicationIds =
                System.getProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY);
        if (!StringUtils.isBlank(commaDelimitedListOfSupportedApplicationIds)) {
            supportedApplicationIds =
                    new HashSet<String>(Arrays.asList(commaDelimitedListOfSupportedApplicationIds
                            .split(",")));
        }

        return new ApplicationIdSpeechletRequestEnvelopeVerifier(supportedApplicationIds);
    }
}
