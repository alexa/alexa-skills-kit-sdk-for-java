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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.amazon.ask.Skill;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.servlet.util.ServletUtils;
import com.amazon.ask.servlet.verifiers.ServerRequest;
import com.amazon.ask.servlet.verifiers.ServletRequest;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.servlet.verifiers.SkillRequestTimestampVerifier;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import com.amazon.ask.util.JacksonSerializer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.amazon.ask.servlet.ServletConstants.DEFAULT_TOLERANCE_MILLIS;
import static com.amazon.ask.servlet.ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY;

/**
 * <p>
 * Simple implementation of a skill in the form of a Java EE servlet. Use this class when coding the
 * skill as a web service.
 * </p>
 * <p>
 * This class takes care of the JSON serialization / deserialization of the HTTP body and the
 * invocation of the right method of the provided {@code Skill} . It also handles sending back
 * modified session attributes, user attributes and authentication tokens when needed and handles
 * exception cases.
 *
 */
public class SkillServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SkillServlet.class);
    private static final long serialVersionUID = 3257254794185762002L;

    private transient final Skill skill;
    private transient final List<SkillServletVerifier> verifiers;
    private transient final Serializer serializer = new JacksonSerializer();

    public SkillServlet(Skill skill) {
        List<SkillServletVerifier> defaultVerifiers = new ArrayList<>();
        if (!ServletUtils.isRequestSignatureCheckSystemPropertyDisabled()) {
            defaultVerifiers.add(new SkillRequestSignatureVerifier());
        }
        Long timestampToleranceProperty = ServletUtils.getTimeStampToleranceSystemProperty();
        defaultVerifiers.add(new SkillRequestTimestampVerifier(timestampToleranceProperty != null
                ? timestampToleranceProperty : DEFAULT_TOLERANCE_MILLIS));
        this.skill = skill;
        this.verifiers = defaultVerifiers;
    }

    SkillServlet(Skill skill, List<SkillServletVerifier> verifiers) {
        this.skill = skill;
        this.verifiers = verifiers;
    }

    /**
     * Handles a POST request. Based on the request parameters, invokes the right method on the
     * {@code Skill}.
     *
     * @param request  the object that contains the request the client has made of the servlet
     * @param response object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        byte[] serializedRequestEnvelope = IOUtils.toByteArray(request.getInputStream());
        try {
            final RequestEnvelope deserializedRequestEnvelope = serializer.deserialize(IOUtils.toString(
                    serializedRequestEnvelope, ServletConstants.CHARACTER_ENCODING), RequestEnvelope.class);

            final ServerRequest serverRequest = new ServletRequest(request, serializedRequestEnvelope, deserializedRequestEnvelope);

            // Verify the authenticity of the request by executing configured verifiers.
            for (SkillServletVerifier verifier : verifiers) {
                verifier.verify(serverRequest);
            }

            ResponseEnvelope skillResponse = skill.invoke(deserializedRequestEnvelope);
            // Generate JSON and send back the response
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            if (skillResponse != null) {
                byte[] serializedResponse = serializer.serialize(skillResponse).getBytes(StandardCharsets.UTF_8);
                try (final OutputStream out = response.getOutputStream()) {
                    response.setContentLength(serializedResponse.length);
                    out.write(serializedResponse);
                }
            }
        } catch (SecurityException ex) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            log.error("Incoming request failed verification {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
        } catch (AskSdkException ex) {
            int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            log.error("Exception occurred in doPost, returning status code {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
        }
    }

    /**
     * Sets a {@code Proxy} object that this servlet may use if Request Signature Verification is enabled.
     *
     * @param proxy the {@code Proxy} to associate with this servlet.
     */
    public void setProxy(Proxy proxy) {
        if (verifiers.removeIf(verifier -> verifier instanceof SkillRequestSignatureVerifier)) {
            verifiers.add(new SkillRequestSignatureVerifier(proxy));
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException("Skill servlet is not serializable");
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("Skill servlet is not serializable");
    }
}