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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;

import javax.servlet.http.HttpServletResponse;

import com.amazon.ask.Skill;
import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

/**
 * Tests that the {@link SkillServlet} respects the provided environment variables controlling
 * its behavior.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SkillRequestSignatureVerifier.class, SkillServlet.class})
public class SkillServletTest extends SkillServletTestBase {
    private static final String LOCALE = "en-US";
    private static Skill skill;

    @Before
    public void setup() {
        PowerMockito.mockStatic(SkillRequestSignatureVerifier.class);
        skill = mock(Skill.class);
    }

    @Test
    public void doPost_passedVerification_responseSuccess() throws Exception {
        SkillServletVerifier mockVerifier = mock(SkillServletVerifier.class);
        SkillServlet servlet = new SkillServlet(skill, Collections.singletonList(mockVerifier));
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LaunchRequest request = LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).withTimestamp(timestamp).build();
        ServletInvocationParameters invocation = build(FORMAT_VERSION, request, buildSession());
        servlet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
        verify(mockVerifier).verify(any());
    }

    @Test
    public void doPost_failedVerification_responseBadRequest() throws Exception {
        SkillServletVerifier mockVerifier = mock(SkillServletVerifier.class);
        doThrow(new SecurityException("foo")).when(mockVerifier).verify(any());
        SkillServlet servlet = new SkillServlet(skill, Collections.singletonList(mockVerifier));
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LaunchRequest request = LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).withTimestamp(timestamp).build();
        ServletInvocationParameters invocation = build(FORMAT_VERSION, request, buildSession());
        servlet.doPost(invocation.request, invocation.response);
        verify(invocation.response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
        verify(mockVerifier).verify(any());
    }

    @Test
    public void doPost_sdkException_responseInternalServiceException() throws Exception {
        SkillServlet servlet = new SkillServlet(skill, Collections.emptyList());
        when(skill.invoke(any())).thenThrow(new AskSdkException("foo"));
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LaunchRequest request = LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).withTimestamp(timestamp).build();
        ServletInvocationParameters invocation = build(FORMAT_VERSION, request, buildSession());
        servlet.doPost(invocation.request, invocation.response);
        verify(invocation.response).sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), anyString());
    }

    @Test
    public void skill_response_returns_payload() throws Exception {
        SkillServlet servlet = new SkillServlet(skill, Collections.emptyList());
        Response response = Response.builder().build();
        ResponseEnvelope responseEnvelope = ResponseEnvelope.builder().withResponse(response).build();
        when(skill.invoke(any())).thenReturn(responseEnvelope);
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LaunchRequest request = LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).withTimestamp(timestamp).build();
        ServletInvocationParameters invocation = build(FORMAT_VERSION, request, buildSession());
        servlet.doPost(invocation.request, invocation.response);
        byte[] output = invocation.output.toByteArray();
        assertTrue(output.length > 0);
    }

    @Test
    public void custom_proxy_updates_signature_verifier() throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
        SkillRequestSignatureVerifier mockVerifier = mock(SkillRequestSignatureVerifier.class);
        PowerMockito.whenNew(SkillRequestSignatureVerifier.class)
                .withAnyArguments()
                .thenReturn(mockVerifier);
        SkillServlet servlet = new SkillServlet(skill);
        servlet.setProxy(proxy);
        PowerMockito.verifyNew(SkillRequestSignatureVerifier.class).withNoArguments();
        PowerMockito.verifyNew(SkillRequestSignatureVerifier.class).withArguments(proxy);
    }

}
