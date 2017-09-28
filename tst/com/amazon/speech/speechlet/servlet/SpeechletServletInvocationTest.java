package com.amazon.speech.speechlet.servlet;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;

import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;

/**
 * Tests that the {@link SpeechletServlet} respects the provided environment variables controlling
 * its behavior.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SpeechletRequestSignatureVerifier.class)
public class SpeechletServletInvocationTest extends SpeechletServletTestBase {
    private static final String ONLY_VALID_APPLICATION_ID = "ONLY_SUPPORTED_ID";
    private static final String WRONG_APPLICATION_ID = "WRONG_ID";
    private static final Locale LOCALE = Locale.forLanguageTag("en-US");
    private static Speechlet speechlet;

    @Before
    public void setup() {
        PowerMockito.mockStatic(SpeechletRequestSignatureVerifier.class);
        speechlet = Mockito.mock(Speechlet.class);
    }

    @Test
    public void getSpeechlet() {
        SpeechletServlet servlet = new SpeechletServlet();
        servlet.setSpeechlet(speechlet);
        assertEquals(speechlet, servlet.getSpeechlet());
    }

    @Test
    public void doPost_enableSignatureCheckWithGoodSignature_responseSuccess() throws Exception {
        System.setProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY,
                Boolean.FALSE.toString());
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
        verify(SpeechletRequestSignatureVerifier.class, times(1));
        SpeechletRequestSignatureVerifier.checkRequestSignature(any(byte[].class), anyString(),
                anyString());
    }

    @Test
    public void doPost_enableSignatureCheckWithBadSignature_responseBadRequest() throws Exception {
        System.setProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY,
                Boolean.FALSE.toString());
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        doThrow(new SecurityException()).when(SpeechletRequestSignatureVerifier.class);
        SpeechletRequestSignatureVerifier.checkRequestSignature(any(byte[].class), anyString(),
                anyString());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    public void doPost_disableSignatureCheckWithBadSignature_responseSuccess() throws Exception {
        System.setProperty(Sdk.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY,
                Boolean.TRUE.toString());
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
        verify(SpeechletRequestSignatureVerifier.class, never());
        SpeechletRequestSignatureVerifier.checkRequestSignature(any(byte[].class), anyString(),
                anyString());
    }

    @Test
    public void doPost_supportedApplicationIdMatch_responseSuccess() throws Exception {
        System
                .setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY,
                        ONLY_VALID_APPLICATION_ID);
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation =
                build(Sdk.VERSION, request, buildSession(ONLY_VALID_APPLICATION_ID));
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doPost_supportedApplicationIdMisMatch_responseBadRequest() throws Exception {
        System
                .setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY,
                        ONLY_VALID_APPLICATION_ID);
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation =
                build(Sdk.VERSION, request, buildSession(WRONG_APPLICATION_ID));
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    public void doPost_supportedApplicationIdEmpty_responseSuccess() throws Exception {
        System.setProperty(Sdk.SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY, "");
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("rId").withLocale(LOCALE).build();
        ServletInvocationParameters invocation =
                build(Sdk.VERSION, request, buildSession(WRONG_APPLICATION_ID));
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doPost_timestampTolerancePropertySetGoodRequest_responseSuccess() throws Exception {
        String tollerance = "60";
        int offset = 5;
        System.setProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, tollerance);
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        Date requestTimestamp = new Date(System.currentTimeMillis() - (offset * 1000 * 2));
        LaunchRequest request =
                LaunchRequest
                        .builder()
                        .withRequestId("rId")
                        .withTimestamp(requestTimestamp)
                        .withLocale(LOCALE)
                        .build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doPost_timestampTolerancePropertySetOldRequest_responseBadRequest()
            throws Exception {
        String tollerance = "60";
        int offset = 61;
        System.setProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, tollerance);
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        Date requestTimestamp = new Date(System.currentTimeMillis() - (offset * 1000 * 2));
        LaunchRequest request =
                LaunchRequest
                        .builder()
                        .withRequestId("rId")
                        .withTimestamp(requestTimestamp)
                        .withLocale(LOCALE)
                        .build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    public void doPost_timestampTolerancePropertyEmptyOldRequest_responseSuccess() throws Exception {
        String tollerance = "";
        int offset = 60 * 60 * 24 * 30; // A month old request
        System.setProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, tollerance);
        SpeechletServlet servelet = new SpeechletServlet();
        servelet.setSpeechlet(speechlet);
        Date requestTimestamp = new Date(System.currentTimeMillis() - (offset * 1000 * 2));
        LaunchRequest request =
                LaunchRequest
                        .builder()
                        .withRequestId("rId")
                        .withTimestamp(requestTimestamp)
                        .withLocale(LOCALE)
                        .build();
        ServletInvocationParameters invocation = build(Sdk.VERSION, request, buildSession());
        servelet.doPost(invocation.request, invocation.response);
        verify(invocation.response).setStatus(HttpServletResponse.SC_OK);
    }
}
