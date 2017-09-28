package com.amazon.speech.speechlet.servlet;

import static com.amazon.speech.Sdk.VERSION;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;

import com.amazon.speech.Sdk;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Application;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpeechletServletTestBase {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Before
    @After
    public void clearSupportedApplicationIdsSystemProperty() {
        System.setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY, "");
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
     *            a SpeechletRequest
     * @param session
     *            a session for this invocation
     * @return invocation parameters for a Speechlet
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected ServletInvocationParameters build(final String version,
            final SpeechletRequest request, final Session session) throws ServletException,
            IOException {
        // Creates a request envelope
        SpeechletRequestEnvelope<?> envelope =
                SpeechletRequestEnvelope
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
     *            a SpeechletRequest or OnSessionEndedRequest
     * @param session
     *            a session for this invocation
     * @return invocation parameters for a Speechlet
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected ServletInvocationParameters build(final SpeechletRequest request,
            final Session session) throws ServletException, IOException {
        return build(VERSION, request, session);
    }

    /**
     * Same as above using a simple Session and User.
     * 
     * @param request
     *            a speechlet request
     * @return invocation parameters for a Speechlet
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected ServletInvocationParameters build(final SpeechletRequest request)
            throws ServletException, IOException {
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
                .withApplication(new Application(applicationId))
                .withUser(new User("UserId"))
                .build();
    }
}
