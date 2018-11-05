/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.canfulfill.CanFulfillIntentRequest;
import com.amazon.ask.model.interfaces.display.ElementSelectedRequest;
import com.amazon.ask.model.interfaces.viewport.Shape;
import com.amazon.ask.model.interfaces.viewport.ViewportState;
import com.amazon.ask.request.viewport.ViewportProfile;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.amazon.ask.request.Predicates.*;

public class PredicatesTest {

    @Test
    public void request_type_predicate_matching_request_type_returns_true() {
        HandlerInput input = getInputForRequest(SessionEndedRequest.builder().build());
        assertTrue(requestType(SessionEndedRequest.class).test(input));
    }

    @Test
    public void request_type_predicate_non_matching_request_type_returns_false() {
        HandlerInput input = getInputForRequest(LaunchRequest.builder().build());
        assertFalse(requestType(SessionEndedRequest.class).test(input));
    }

    @Test
    public void selected_element_predicate_non_element_selected_request_returns_false() {
        HandlerInput input = getInputForRequest(SessionEndedRequest.builder().build());
        assertFalse(selectedElementToken("foo").test(input));
    }

    @Test
    public void selected_element_predicate_matching_token_returns_true() {
        HandlerInput input = getInputForRequest(ElementSelectedRequest.builder().withToken("foo").build());
        assertTrue(selectedElementToken("foo").test(input));
    }

    @Test
    public void selected_element_predicate_non_matching_token_returns_false() {
        HandlerInput input = getInputForRequest(ElementSelectedRequest.builder().withToken("foo").build());
        assertFalse(selectedElementToken("bar").test(input));
    }

    @Test
    public void intent_name_predicate_non_intent_request_returns_false() {
        HandlerInput input = getInputForRequest(SessionEndedRequest.builder().build());
        assertFalse(intentName("foo").test(input));
    }

    @Test
    public void intent_name_predicate_matching_intent_name_returns_true() {
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().withName("foo").build()).build());
        assertTrue(intentName("foo").test(input));
    }

    @Test
    public void intent_name_predicate_non_matching_intent_name_returns_false() {
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().withName("foo").build()).build());
        assertFalse(intentName("bar").test(input));
    }

    @Test
    public void canfulfill_intent_name_predicate_non_canfulfill_intent_request_returns_false() {
        HandlerInput input = getInputForRequest(SessionEndedRequest.builder().build());
        assertFalse(canFulfillIntentName("foo").test(input));
    }

    @Test
    public void canfulfill_intent_name_predicate_matching_canfulfill_intent_name_returns_true() {
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().withName("foo").build()).build());
        assertTrue(canFulfillIntentName("foo").test(input));
    }

    @Test
    public void canfulfill_intent_name_predicate_non_matching_canfulfill_intent_name_returns_false() {
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().withName("foo").build()).build());
        assertFalse(canFulfillIntentName("bar").test(input));
    }

    @Test
    public void request_attributes_predicate_request_attributes_missing_key_returns_false() {
        HandlerInput input = getInputWithRequestAttributes(Collections.singletonMap("foo", "bar"));
        assertFalse(requestAttribute("baz", "bar").test(input));
    }

    @Test
    public void request_attributes_predicate_request_attributes_missing_value_returns_false() {
        HandlerInput input = getInputWithRequestAttributes(Collections.singletonMap("foo", "bar"));
        assertFalse(requestAttribute("foo", "baz").test(input));
    }

    @Test
    public void request_attributes_predicate_request_attributes_matching_returns_true() {
        HandlerInput input = getInputWithRequestAttributes(Collections.singletonMap("foo", "bar"));
        assertTrue(requestAttribute("foo", "bar").test(input));
    }

    @Test
    public void session_attributes_predicate_out_of_session_request_returns_false() {
        HandlerInput input = getInputWithSessionAttributes(null, false);
        assertFalse(sessionAttribute("foo", "bar").test(input));
    }

    @Test
    public void session_attributes_predicate_session_attributes_present_missing_key_returns_false() {
        HandlerInput input = getInputWithSessionAttributes(Collections.singletonMap("foo", "bar"), true);
        assertFalse(sessionAttribute("baz", "bar").test(input));
    }

    @Test
    public void session_attributes_predicate_session_attributes_present_missing_value_returns_false() {
        HandlerInput input = getInputWithSessionAttributes(Collections.singletonMap("foo", "bar"), true);
        assertFalse(sessionAttribute("foo", "baz").test(input));
    }

    @Test
    public void session_attributes_predicate_session_attributes_present_matching_returns_true() {
        HandlerInput input = getInputWithSessionAttributes(Collections.singletonMap("foo", "bar"), true);
        assertTrue(sessionAttribute("foo", "bar").test(input));
    }

    @Test
    public void persistent_attributes_predicate_persistent_attributes_missing_key_returns_false() {
        HandlerInput input = getInputWithPersistentAttributes(Collections.singletonMap("foo", "bar"));
        assertFalse(persistentAttribute("baz", "bar").test(input));
    }

    @Test
    public void persistent_attributes_predicate_persistent_attributes_missing_value_returns_false() {
        HandlerInput input = getInputWithPersistentAttributes(Collections.singletonMap("foo", "bar"));
        assertFalse(persistentAttribute("foo", "baz").test(input));
    }

    @Test
    public void persistent_attributes_predicate_persistent_attributes_matching_returns_true() {
        HandlerInput input = getInputWithPersistentAttributes(Collections.singletonMap("foo", "bar"));
        assertTrue(persistentAttribute("foo", "bar").test(input));
    }

    @Test
    public void slot_value_predicate_non_intent_request_returns_false() {
        HandlerInput input = getInputForRequest(LaunchRequest.builder().build());
        assertFalse(slotValue("foo", "bar").test(input));
    }

    @Test
    public void slot_value_predicate_null_slots_returns_false() {
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().build()).build());
        assertFalse(slotValue("foo", "bar").test(input));
    }

    @Test
    public void slot_value_predicate_non_matching_slot_key_returns_false() {
        Map<String, Slot> slots = Collections.singletonMap("baz", Slot.builder().withValue("bar").build());
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertFalse(slotValue("foo", "bar").test(input));
    }

    @Test
    public void slot_value_predicate_non_matching_slot_value_returns_false() {
        Map<String, Slot> slots = Collections.singletonMap("foo", Slot.builder().withValue("baz").build());
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertFalse(slotValue("foo", "bar").test(input));
    }

    @Test
    public void slot_value_predicate_matching_slot_value_returns_true() {
        Map<String, Slot> slots = Collections.singletonMap("foo", Slot.builder().withValue("bar").build());
        HandlerInput input = getInputForRequest(IntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertTrue(slotValue("foo", "bar").test(input));
    }

    @Test
    public void viewport_profile_predicate_matching_viewport_profile_returns_true() {
        HandlerInput input = getInputWithViewport();
        assertTrue(viewportProfile(ViewportProfile.HUB_ROUND_SMALL).test(input));
    }

    @Test
    public void viewport_profile_predicate_non_matching_viewport_profile_returns_false() {
        HandlerInput input = getInputWithViewport();
        assertFalse(viewportProfile(ViewportProfile.MOBILE_LANDSCAPE_MEDIUM).test(input));
    }
    
    public void canfulfill_slot_value_predicate_non_canfulfill_intent_request_returns_false() {
        HandlerInput input = getInputForRequest(LaunchRequest.builder().build());
        assertFalse(canFulfillSlotValue("foo", "bar").test(input));
    }

    @Test
    public void canfulfill_slot_value_predicate_null_slots_returns_false() {
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().build()).build());
        assertFalse(canFulfillSlotValue("foo", "bar").test(input));
    }

    @Test
    public void canfulfill_slot_value_predicate_non_matching_slot_key_returns_false() {
        Map<String, Slot> slots = Collections.singletonMap("baz", Slot.builder().withValue("bar").build());
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertFalse(slotValue("foo", "bar").test(input));
    }

    @Test
    public void canfulfill_slot_value_predicate_non_matching_slot_value_returns_false() {
        Map<String, Slot> slots = Collections.singletonMap("foo", Slot.builder().withValue("baz").build());
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertFalse(canFulfillSlotValue("foo", "bar").test(input));
    }

    @Test
    public void canfulfill_slot_value_predicate_matching_slot_value_returns_true() {
        Map<String, Slot> slots = Collections.singletonMap("foo", Slot.builder().withValue("bar").build());
        HandlerInput input = getInputForRequest(CanFulfillIntentRequest.builder().withIntent(Intent.builder().withSlots(slots).build()).build());
        assertTrue(canFulfillSlotValue("foo", "bar").test(input));
    }

    private HandlerInput getInputWithSessionAttributes(Map<String, Object> attributes, boolean inSessionRequest) {
        AttributesManager manager = mock(AttributesManager.class);
        when(manager.getSessionAttributes()).thenReturn(attributes);
        HandlerInput input = mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(manager);
        RequestEnvelope envelope = RequestEnvelope.builder()
                .withSession(inSessionRequest ? Session.builder().build() : null)
                .build();
        when(input.getRequestEnvelope()).thenReturn(envelope);
        return input;
    }

    private HandlerInput getInputWithRequestAttributes(Map<String, Object> attributes) {
        AttributesManager manager = mock(AttributesManager.class);
        when(manager.getRequestAttributes()).thenReturn(attributes);
        HandlerInput input = mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(manager);
        return input;
    }

    private HandlerInput getInputWithPersistentAttributes(Map<String, Object> attributes) {
        AttributesManager manager = mock(AttributesManager.class);
        when(manager.getPersistentAttributes()).thenReturn(attributes);
        HandlerInput input = mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(manager);
        return input;
    }


    private HandlerInput getInputForRequest(Request request) {
        HandlerInput input = mock(HandlerInput.class);
        RequestEnvelope envelope = RequestEnvelope.builder().withRequest(request).build();
        when(input.getRequestEnvelope()).thenReturn(envelope);
        return input;
    }

    private HandlerInput getInputWithViewport() {
        ViewportState viewportState = ViewportState.builder()
                .withShape(Shape.ROUND)
                .withDpi(BigDecimal.valueOf(160))
                .withCurrentPixelHeight(BigDecimal.valueOf(300))
                .withCurrentPixelWidth(BigDecimal.valueOf(300))
                .build();

        HandlerInput input = mock(HandlerInput.class);
        RequestEnvelope envelope = RequestEnvelope.builder().withContext(Context.builder().withViewport(viewportState).build()).build();
        when(input.getRequestEnvelope()).thenReturn(envelope);
        return input;
    }

}
