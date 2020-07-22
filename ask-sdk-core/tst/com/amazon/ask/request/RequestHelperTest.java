/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.ListSlotValue;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.SimpleSlotValue;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SupportedInterfaces;
import com.amazon.ask.model.User;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.request.intent.SlotValueWrapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RequestHelperTest {

    private SimpleSlotValue testSimpleSlotValue = SimpleSlotValue.builder()
            .build();
    private ListSlotValue testListSlotValue = ListSlotValue.builder()
            .build();
    private Slot testSlot = Slot.builder()
            .withValue("FooValue")
                .build();
    private Slot simpleSlot = Slot.builder()
            .withSlotValue(testSimpleSlotValue)
            .build();
    private Slot listSlot = Slot.builder()
            .withSlotValue(testListSlotValue)
            .build();
    private Map<String, Slot> testSlots = new HashMap<>();
    private Intent testIntent = Intent.builder()
            .withName("FooIntent")
            .withSlots(testSlots)
            .build();
    private IntentRequest testIntentRequest = IntentRequest.builder()
            .withLocale("FooLocale")
            .withIntent(testIntent)
            .withDialogState(DialogState.COMPLETED)
            .build();
    private LaunchRequest testLaunchRequest = LaunchRequest.builder()
            .build();
    private SupportedInterfaces testSupportedInterfaces = SupportedInterfaces.builder()
            .build();
    private Device testDevice = Device.builder()
            .withDeviceId("FooId")
            .withSupportedInterfaces(testSupportedInterfaces)
            .build();
    private User testUser = User.builder()
            .withAccessToken("FooUserToken")
            .build();
    private SystemState testSystemState = SystemState.builder()
            .withApiAccessToken("FooApiAccessToken")
            .withDevice(testDevice)
            .withUser(testUser)
            .build();
    private Context testContext = Context.builder()
            .withSystem(testSystemState).build();

    @Before
    public void setup() {
        testSlots.put("FooSlot", testSlot);
        testSlots.put("SimpleSlot", simpleSlot);
        testSlots.put("ListSlot", listSlot);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_handler_input_throws_exception() {
        RequestHelper.forHandlerInput(null);
    }

    @Test
    public void get_locale_returns_locale() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getLocale(), "FooLocale");
    }

    @Test
    public void get_request_type_returns_type() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getRequestType(), "IntentRequest");
    }

    @Test
    public void get_intent_name_returns_name() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getIntentName(), "FooIntent");
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_intent_name_throws_exception_non_intent_request() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testLaunchRequest)).getDialogState(), DialogState.COMPLETED);
    }

    @Test
    public void get_account_linking_access_token_returns_token() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getAccountLinkingAccessToken(), "FooUserToken");
    }

    @Test
    public void getApiAccessToken() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getApiAccessToken(), "FooApiAccessToken");
    }

    @Test
    public void get_device_id_returns_id() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getDeviceId(), "FooId");
    }

    @Test
    public void get_dialog_state_returns_state() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getDialogState(), DialogState.COMPLETED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_dialog_state_throws_exception_non_intent_request() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testLaunchRequest)).getDialogState(), DialogState.COMPLETED);
    }

    @Test
    public void get_slot_returns_slot() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlot("FooSlot").get(), testSlot);
    }

    @Test
    public void get_slot_returns_empty_slot_not_found() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlot("BarSlot"), Optional.empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_slot_throws_exception_non_intent_request() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testLaunchRequest)).getSlot("FooSlot").get(), testSlot);
    }

    @Test
    public void get_slot_value_returns_value() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlotValue("FooSlot").get(), "FooValue");
    }

    @Test
    public void get_slot_value_returns_optional_empty_slot_not_found() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlotValue("BarSlot"), Optional.empty());
    }

    @Test
    public void is_new_session_returns_true_new_session() {
        assertTrue(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest, Session.builder().withNew(true).build())).isNewSession());
    }

    @Test
    public void is_new_session_returns_false_not_new_session() {
        assertFalse(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest, Session.builder().withNew(false).build())).isNewSession());
    }

    @Test
    public void get_supported_interfaces_returns_interfaces() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSupportedInterfaces(), testSupportedInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void is_new_session_throws_exception_no_session_in_request() {
        assertTrue(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).isNewSession());
    }

    @Test
    public void get_user_id_returns_empty_when_no_user_id() {
        assertEquals(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getUserId(), Optional.empty());
    }

    @Test
    public void get_user_id_returns_id_when_user_id_is_present() {
        final Context context = Context.builder()
                .withSystem(SystemState.builder()
                        .withUser(User.builder()
                                .withUserId("userId").build())
                        .build())
                .build();
        final RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withRequest(testIntentRequest)
                .withContext(context)
                .build();
        final HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .build();
        assertEquals(RequestHelper.forHandlerInput(input).getUserId(), Optional.of("userId"));
    }

    @Test
    public void get_slot_value_wrapper_returns_empty_wrapper_if_slot_does_not_exist() {
        assertNull(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlotValueWrapper("Invalid").unwrap());
    }

    @Test
    public void get_slot_value_wrapper_returns_empty_wrapper_if_no_slot_value() {
        assertNull(RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlotValueWrapper("FooSlot").unwrap());
    }

    @Test
    public void get_slot_value_wrapper_returns_wrapped_value_if_exists() {
        SlotValueWrapper wrapper = RequestHelper.forHandlerInput(getHandlerInputForRequest(testIntentRequest)).getSlotValueWrapper("SimpleSlot");
        assertEquals(wrapper.unwrap(), testSimpleSlotValue);
    }

    private HandlerInput getHandlerInputForRequest(Request request) {
        return getHandlerInputForRequest(request, null);
    }

    private HandlerInput getHandlerInputForRequest(Request request, Session session) {
        RequestEnvelope testRequestEnvelope = RequestEnvelope.builder()
                .withRequest(request)
                .withContext(testContext)
                .withSession(session)
                .build();
        return HandlerInput.builder()
                .withRequestEnvelope(testRequestEnvelope)
                .build();
    }
}