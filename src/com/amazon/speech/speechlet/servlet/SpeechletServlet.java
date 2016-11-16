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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletToSpeechletV2Adapter;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;

/**
 * <p>
 * Simple implementation of a skill in the form of a Java EE servlet. Use this class when coding the
 * skill as a web service.
 * </p>
 * <p>
 * This class takes care of the JSON serialization / deserialization of the HTTP body and the
 * invocation of the right method of the provided {@code SpeechletV2} . It also handles sending back
 * modified session attributes, user attributes and authentication tokens when needed and handles
 * exception cases.
 *
 * @see SpeechletV2
 * @see #setSpeechlet(SpeechletV2)
 */
public class SpeechletServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SpeechletServlet.class);
    private static final long serialVersionUID = 3257254794185762002L;

    private transient SpeechletV2 speechlet;
    private transient ServletSpeechletRequestHandler speechletRequestHandler;
    private final boolean disableRequestSignatureCheck;

    public SpeechletServlet() {
        // An invalid value or null will turn signature checking on.
        disableRequestSignatureCheck =
                Boolean.parseBoolean(System
                        .getProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY));
        speechletRequestHandler = new ServletSpeechletRequestHandler();
    }

    /**
     * Handles a POST request. Based on the request parameters, invokes the right method on the
     * {@code SpeechletV2}.
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
     * @return the {@code Speechlet} associated with this servlet, or null if there is not one
     */
    public Speechlet getSpeechlet() {
        if (speechlet instanceof SpeechletToSpeechletV2Adapter) {
            return ((SpeechletToSpeechletV2Adapter) speechlet).getSpeechlet();
        }

        return null;
    }

    /**
     * Returns the {@code SpeechletV2} object that this servlet uses.
     *
     * @return the {@code SpeechletV2} object that this servlet uses.
     */
    public SpeechletV2 getSpeechletV2() {
        return speechlet;
    }

    /**
     * Sets the {@code Speechlet} object that this servlet uses.
     *
     * @param speechlet
     *            the {@code Speechlet} to associate with this servlet
     */
    public void setSpeechlet(final Speechlet speechlet) {
        this.speechlet = new SpeechletToSpeechletV2Adapter(speechlet);
    }

    /**
     * Sets the {@code SpeechletV2} object that this servlet uses.
     *
     * @param speechlet
     *            the {@code SpeechletV2} to associate with this servlet
     */
    public void setSpeechlet(final SpeechletV2 speechlet) {
        this.speechlet = speechlet;
    }
}
