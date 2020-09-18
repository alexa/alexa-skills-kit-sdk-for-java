/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.verifiers;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.events.skillevents.SkillEnabledRequest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.amazon.ask.servlet.ServletConstants.MAXIMUM_TOLERANCE_MILLIS;
import static com.amazon.ask.servlet.ServletConstants.TOLERANCE_SKILL_EVENTS_MILLIS;;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class SkillRequestTimestampVerifierTest {

    private SkillRequestTimestampVerifier verifier;

    private static final long TOLERANCE_IN_MILLIS = 2000;

    private static HttpServletRequest mockServletRequest;
    private static byte[] serializedRequestEnvelope;

    public SkillRequestTimestampVerifierTest(SkillRequestTimestampVerifier verifier) {
        this.verifier = verifier;
    }

    @Parameterized.Parameters
    public static Collection verifiers() {
        return Arrays.asList(new SkillRequestTimestampVerifier(TOLERANCE_IN_MILLIS), new SkillRequestTimestampVerifier(TOLERANCE_IN_MILLIS / 1000, TimeUnit.SECONDS));
    }

    @BeforeClass
    public static void setUp() {
        mockServletRequest = mock(HttpServletRequest.class);
        serializedRequestEnvelope = "".getBytes();
    }

    @Test (expected = IllegalArgumentException.class)
    public void construct_withNegativeTolerance_throwsIllegalArgumentException() {
        new SkillRequestTimestampVerifier(-1);
    }

    @Test
    public void construct_withToleranceBeyondMaximum_latchesToMaxTolerance() {
        new SkillRequestTimestampVerifier(MAXIMUM_TOLERANCE_MILLIS + 1);
    }

    @Test
    public void verify_currentDate_no_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date())));
    }

    @Test
    public void verify_recentOldDate_no_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date(System.currentTimeMillis() - TOLERANCE_IN_MILLIS / 2))));
    }

    @Test
    public void verify_upcomingNewDate_no_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date(System.currentTimeMillis() + TOLERANCE_IN_MILLIS / 2))));
    }

    @Test(expected = SecurityException.class)
    public void verify_tooOldDate_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date(System.currentTimeMillis() - TOLERANCE_IN_MILLIS * 2))));
    }

    @Test(expected = SecurityException.class)
    public void verify_tooNewDate_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date(System.currentTimeMillis() + TOLERANCE_IN_MILLIS * 2))));
    }

    @Test(expected = SecurityException.class)
    public void verify_nullDate_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(null)));
    }

    @Test(expected = SecurityException.class)
    public void verify_nullRequestEnvelope_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, null));
    }

    @Test(expected = SecurityException.class)
    public void verify_nullRequest_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, RequestEnvelope.builder().build()));
    }

    @Test(expected = SecurityException.class)
    public void verify_nullTimestamp_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void construct_withNullTimeUnit_throwsIllegalArgumentException() {
        new SkillRequestTimestampVerifier(TOLERANCE_IN_MILLIS / 1000, null);
    }

    @Test(expected = SecurityException.class)
    public void verify_withGreaterRequestTimeDelta_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getRequestEnvelope(new Date(System.currentTimeMillis() + TOLERANCE_IN_MILLIS + 200))));
    }

    @Test
    public void verify_skillEventRequestWithinTolerance_no_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getSkillEventRequestEnvelope(new Date(System.currentTimeMillis() - TOLERANCE_SKILL_EVENTS_MILLIS / 2))));
    }

    @Test(expected = SecurityException.class)
    public void verify_skillEventRequestOutsideTolerance_throws_exception() {
        verifier.verify(new ServletRequest(mockServletRequest, serializedRequestEnvelope, getSkillEventRequestEnvelope(new Date(System.currentTimeMillis() - TOLERANCE_SKILL_EVENTS_MILLIS * 2))));
    }

    private RequestEnvelope getRequestEnvelope(Date timestamp) {
        return RequestEnvelope.builder().withRequest(LaunchRequest
                .builder()
                .withRequestId("rId")
                .withTimestamp(timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null)
                .withLocale("en-US")
                .build()).build();
    }

    private RequestEnvelope getSkillEventRequestEnvelope(Date timestamp) {
        return RequestEnvelope.builder().withRequest(SkillEnabledRequest
                .builder()
                .withRequestId("rId")
                .withTimestamp(timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null)
                .withLocale("en-US")
                .build()).build();
    }

}
