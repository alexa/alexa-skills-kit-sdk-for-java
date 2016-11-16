/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.CardSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.OutputSpeechSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.ResponseSizeSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletRequestEnvelopeVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletResponseVerifier;

/**
 * The SpeechletRequestHandler processes an incoming request from the Alexa service, decides what
 * action to take on the configured {@code SpeechletV2}, executes that call, and returns the resulting
 * response bytes. This class can be hosted by a servlet (for instance, for a web server) or by AWS
 * Lambda. In either case, the logic is the same with different surrounding logic to handle the
 * generated output bytes.
 */
@SuppressWarnings("deprecation")
public class SpeechletRequestHandler {
    private final List<SpeechletRequestVerifier> requestVerifiers;
    private final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers;
    private final List<SpeechletResponseVerifier> responseVerifiers;

    @Deprecated
    public SpeechletRequestHandler(final List<SpeechletRequestVerifier> requestVerifiers,
            List<SpeechletResponseVerifier> responseVerifiers) {
        this(new ArrayList<SpeechletRequestEnvelopeVerifier>(1), requestVerifiers, responseVerifiers);
        addApplicationIdSpeechletRequestEnvelopeVerifierIfNecessary();
    }

    public SpeechletRequestHandler(final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers) {
        this(requestEnvelopeVerifiers, Collections.<SpeechletRequestVerifier>emptyList(), Arrays.asList(
                new ResponseSizeSpeechletResponseVerifier(),
                new OutputSpeechSpeechletResponseVerifier(), new CardSpeechletResponseVerifier()));
    }

    private SpeechletRequestHandler(
            final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers,
            final List<SpeechletRequestVerifier> requestVerifiers,
            final List<SpeechletResponseVerifier> responseVerifiers) {
        this.requestEnvelopeVerifiers = requestEnvelopeVerifiers;
        this.requestVerifiers = requestVerifiers;
        this.responseVerifiers = responseVerifiers;
    }

    /**
     * Processes the provided bytes as a request from the Alexa service and generates an appropriate
     * response after dispatching the request to the appropriate method calls on the
     * {@code Speechlet} provided at construction time.
     *
     * @param speechlet
     *            the speechlet to be invoked
     * @param serializedSpeechletRequest
     *            the request coming from Alexa service
     * @return the response that should be returned to the Alexa service. This comes from the
     *         appropriate method call in the configured {@code SpeechletV2}
     * @throws IOException
     *             may occur during request or response serialization
     * @throws SpeechletRequestHandlerException
     *             indicates a problem with the request or response
     * @throws SpeechletException
     *             indicates a problem from within the included {@code SpeechletV2}
     * @deprecated Please use {@code handleSpeechletCall(SpeechletV2, byte[])} instead.
     */
    @Deprecated
    public byte[] handleSpeechletCall(Speechlet speechlet, byte[] serializedSpeechletRequest)
            throws IOException, SpeechletRequestHandlerException, SpeechletException {
        return internalHandleSpeechletCall(new SpeechletToSpeechletV2Adapter(speechlet),
                serializedSpeechletRequest);
    }

    /**
     * Processes the provided bytes as a request from the Alexa service and generates an appropriate
     * response after dispatching the request to the appropriate method calls on the
     * {@code SpeechletV2} provided at construction time.
     *
     * @param speechlet
     *            the speechlet to be invoked
     * @param serializedSpeechletRequest
     *            the request coming from Alexa service
     * @return the response that should be returned to the Alexa service. This comes from the
     *         appropriate method call in the configured {@code SpeechletV2}
     * @throws IOException
     *             may occur during request or response serialization
     * @throws SpeechletRequestHandlerException
     *             indicates a problem with the request or response
     * @throws SpeechletException
     *             indicates a problem from within the included {@code SpeechletV2}
     */
    public byte[] handleSpeechletCall(SpeechletV2 speechlet, byte[] serializedSpeechletRequest)
            throws IOException, SpeechletRequestHandlerException, SpeechletException {
        return internalHandleSpeechletCall(speechlet, serializedSpeechletRequest);
    }

    private byte[] internalHandleSpeechletCall(SpeechletV2 speechlet,
            byte[] serializedSpeechletRequest) throws IOException,
            SpeechletRequestHandlerException, SpeechletException {

        final SpeechletRequestEnvelope<?> requestEnvelope =
                SpeechletRequestEnvelope.fromJson(serializedSpeechletRequest);

        final SpeechletRequest request = requestEnvelope.getRequest();
        final Session session = requestEnvelope.getSession();

        // Verify request
        for (SpeechletRequestVerifier verifier : requestVerifiers) {
            if (!verifier.verify(request, session)) {
                String message =
                        String.format("Could not validate SpeechletRequest %s using verifier %s, "
                                + "rejecting request", request != null ? request.getRequestId()
                                : "null", verifier.getClass().getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        for (SpeechletRequestEnvelopeVerifier verifier : requestEnvelopeVerifiers) {
            if (!verifier.verify(requestEnvelope)) {
                String message =
                        String.format("Could not validate SpeechletRequest %s using verifier %s, "
                                + "rejecting request", request != null ? request.getRequestId()
                                : "null", verifier.getClass().getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        // Dispatch request to Speechlet
        SpeechletRequestDispatcher dispatcher = new SpeechletRequestDispatcher(speechlet);
        SpeechletResponseEnvelope responseEnvelope =
                dispatcher.dispatchSpeechletCall(requestEnvelope, session);

        // Verify response
        for (SpeechletResponseVerifier verifier : responseVerifiers) {
            if (!verifier.verify(responseEnvelope, session)) {
                String message =
                        String.format("Could not validate SpeechletResponse %s using verifier %s, "
                                + "rejecting response", request.getRequestId(), verifier
                                .getClass()
                                .getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        return responseEnvelope.toJsonBytes();
    }

    private void addApplicationIdSpeechletRequestEnvelopeVerifierIfNecessary() {
        for (SpeechletRequestVerifier requestVerifier : requestVerifiers) {
            if (requestVerifier instanceof ApplicationIdSpeechletRequestVerifier) {
                Set<String> supportedApplicationIds =
                        ((ApplicationIdSpeechletRequestVerifier) requestVerifier)
                                .getSupportedApplicationIds();
                requestEnvelopeVerifiers.add(new ApplicationIdSpeechletRequestEnvelopeVerifier(
                        supportedApplicationIds));
            }
        }
    }
}
