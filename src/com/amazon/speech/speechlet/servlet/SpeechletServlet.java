/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.ApplicationIdSpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.CardSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.OutputSpeechSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.ResponseSizeSpeechletResponseVerifier;
import com.amazon.speech.speechlet.verifier.SpeechletRequestVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;

/**
 * <p>
 * Simple implementation of {@code Speechlet} in the form of a Java EE servlet. Use this class when
 * coding the skill as a web service.
 * </p>
 * <p>
 * This class takes care of the JSON serialization / deserialization of the HTTP body and the
 * invocation of the right method of the provided {@code Speechlet} . It also handles sending back
 * modified session attributes, user attributes and authentication tokens when needed and handles
 * exception cases.
 *
 * @see Speechlet
 * @see #setSpeechlet(Speechlet)
 */
public class SpeechletServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SpeechletServlet.class);
    private static final long serialVersionUID = 3257254794185762002L;

    private transient Speechlet speechlet;
    private final transient SpeechletRequestHandler speechletRequestHandler;
    private final boolean disableRequestSignatureCheck;

    public SpeechletServlet() {
        // An invalid value or null will turn signature checking on.
        disableRequestSignatureCheck =
                Boolean.parseBoolean(System
                        .getProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY));

        List<SpeechletRequestVerifier> requestVerifiers = new ArrayList<SpeechletRequestVerifier>();
        requestVerifiers.add(getApplicationIdVerifier());
        TimestampSpeechletRequestVerifier timestampVerifier = getTimetampVerifier();
        if (timestampVerifier != null) {
            requestVerifiers.add(timestampVerifier);
        }

        speechletRequestHandler =
                new SpeechletRequestHandler(requestVerifiers, Arrays.asList(
                        new ResponseSizeSpeechletResponseVerifier(),
                        new OutputSpeechSpeechletResponseVerifier(),
                        new CardSpeechletResponseVerifier()));
    }

    /**
     * Handles a POST request. Based on the request parameters, invokes the right method on the
     * {@code Speechlet}.
     *
     * @param request
     *            the object that contains the request the client has made of the servlet
     * @param response
     *            object that contains the response the servlet sends to the client
     * @throws IOException
     *             if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        byte[] serializedSpeechletRequest = IOUtils.toByteArray(request.getInputStream());
        byte[] outputBytes = null;

        try {
            if (disableRequestSignatureCheck) {
                log.warn("Warning: Speechlet request signature verification has been disabled!");
            } else {
                // Verify the authenticity of the request by checking the provided signature &
                // certificate.
                SpeechletRequestSignatureVerifier.checkRequestSignature(serializedSpeechletRequest,
                        request.getHeader(Sdk.SIGNATURE_REQUEST_HEADER),
                        request.getHeader(Sdk.SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER));
            }

            outputBytes =
                    speechletRequestHandler.handleSpeechletCall(speechlet,
                            serializedSpeechletRequest);
        } catch (SpeechletRequestHandlerException | SecurityException ex) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            log.error("Exception occurred in doPost, returning status code {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
            return;
        } catch (Exception ex) {
            int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            log.error("Exception occurred in doPost, returning status code {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
            return;
        }

        // Generate JSON and send back the response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try (final OutputStream out = response.getOutputStream()) {
            response.setContentLength(outputBytes.length);
            out.write(outputBytes);
        }
    }

    /**
     * Returns the {@code Speechlet} object that this servlet uses.
     *
     * @return the {@code Speechlet} associated with this servlet
     */
    public Speechlet getSpeechlet() {
        return speechlet;
    }

    /**
     * Sets the {@code Speechlet} object that this servlet uses.
     *
     * @param speechlet
     *            the {@code Speechlet} to associate with this servlet
     */
    public void setSpeechlet(final Speechlet speechlet) {
        this.speechlet = speechlet;
    }

    /**
     * Returns a {@link ApplicationIdSpeechletRequestVerifier} configured using the supported
     * application IDs provided by the system property
     * {@link Sdk#SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY}. If the supported application IDs are
     * not defined, then an {@link ApplicationIdSpeechletRequestVerifier} initialized with an empty
     * set is returned.
     *
     * @return a configured {@link ApplicationIdSpeechletRequestVerifier}
     */
    private ApplicationIdSpeechletRequestVerifier getApplicationIdVerifier() {
        // Build an application ID verifier from a comma-delimited system property value
        Set<String> supportedApplicationIds = Collections.emptySet();
        String commaDelimitedListOfSupportedApplicationIds =
                System.getProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY);
        if (!StringUtils.isBlank(commaDelimitedListOfSupportedApplicationIds)) {
            supportedApplicationIds =
                    new HashSet<String>(Arrays.asList(commaDelimitedListOfSupportedApplicationIds
                            .split(",")));
        }

        return new ApplicationIdSpeechletRequestVerifier(supportedApplicationIds);
    }

    /**
     * Returns a {@link TimestampSpeechletRequestVerifier} configured using timestamp tolerance
     * defined by the system property {@link Sdk#TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY}. If a valid
     * timestamp tolerance is missing in the system properties, then {@code null} is returned.
     *
     * @return a configured TimestampSpeechletRequestVerifier or null
     */
    private TimestampSpeechletRequestVerifier getTimetampVerifier() {
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
}
