/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet;

import static com.amazon.ask.util.SdkConstants.FORMAT_VERSION;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazon.ask.model.Application;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.User;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SkillServletTestBase {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Tuple for useful parameters for a servlet invocation.
     */
    protected static final class ServletInvocationParameters {
        public final HttpServletRequest request;
        public final HttpServletResponse response;
        public final ByteArrayOutputStream output;

        public ServletInvocationParameters(final HttpServletRequest request,
                                           final HttpServletResponse response, final ByteArrayOutputStream output) {
            this.request = request;
            this.response = response;
            this.output = output;
        }
    }

    /**
     * Trivial implementation of the ServletInputStream that wraps an InputStream.
     */
    private static class MyServletInputStream extends ServletInputStream {
        private final InputStream in;

        public MyServletInputStream(final InputStream in) {
            this.in = in;
        }

        @Override
        public int read() throws IOException {
            return in.read();
        }
    }

    /**
     * Trivial implementation of the ServletOutputStream that wraps an OutputStream.
     */
    private static class MyServletOutputStream extends ServletOutputStream {
        private final OutputStream out;

        public MyServletOutputStream(final OutputStream out) {
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
        }
    }

    // ------------------------------
    // Helper methods for subclasses

    /**
     * Common logic for all the tests.
     *
     * @param version
     *            the envelope version for the request envelope
     * @param request
     *            a SkillRequest
     * @param session
     *            a session for this invocation
     * @return invocation parameters for a Skill
     *
     * @throws IOException
     */
    protected ServletInvocationParameters build(final String version, final Request request, final Session session) throws IOException {
        // Creates a request envelope
        RequestEnvelope envelope =
                RequestEnvelope
                        .builder()
                        .withVersion(version)
                        .withSession(session)
                        .withRequest(request)
                        .build();

        // Serialize it
        final byte[] json = OBJECT_MAPPER.writeValueAsBytes(envelope);

        // Create a mock HttpServletRequest
        final HttpServletRequest hsRequest = Mockito.mock(HttpServletRequest.class);
        final ByteArrayInputStream bais = new ByteArrayInputStream(json);
        when(hsRequest.getInputStream()).thenReturn(new MyServletInputStream(bais));

        // Create a mock HttpServletResponse
        final HttpServletResponse hsResponse = Mockito.mock(HttpServletResponse.class);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(hsResponse.getOutputStream()).thenReturn(new MyServletOutputStream(baos));

        return new ServletInvocationParameters(hsRequest, hsResponse, baos);
    }

    /**
     * Same as above using:
     * <ul>
     * <li>The current SDK version as a version</li>
     * </ul>
     *
     * @param request
     *            a SkillRequest or OnSessionEndedRequest
     * @param session
     *            a session for this invocation
     * @return invocation parameters for a Skill
     *
     * @throws IOException
     */
    protected ServletInvocationParameters build(final Request request,
                                                final Session session) throws IOException {
        return build(FORMAT_VERSION, request, session);
    }

    /**
     * Same as above using a simple Session and User.
     *
     * @param request
     *            a skill request
     * @return invocation parameters for a Skill
     *
     * @throws IOException
     */
    protected ServletInvocationParameters build(final Request request)
            throws IOException {
        return build(request, buildSession());
    }

    /**
     * @return a test session.
     */
    protected Session buildSession() {
        return buildSession("applicationId");
    }

    protected Session buildSession(String applicationId) {
        return Session
                .builder()
                .withSessionId("sId")
                .withApplication(Application.builder().withApplicationId(applicationId).build())
                .withUser(User.builder().withUserId("UserId").build())
                .build();
    }

}