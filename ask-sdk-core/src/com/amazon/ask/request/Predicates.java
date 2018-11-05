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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.canfulfill.CanFulfillIntentRequest;
import com.amazon.ask.model.interfaces.display.ElementSelectedRequest;
import com.amazon.ask.request.viewport.ViewportUtils;
import com.amazon.ask.request.viewport.ViewportProfile;

import java.util.function.Predicate;

/**
 * A collection of built in Predicates that can be used to evaluate properties of an incoming {@link HandlerInput}.
 */
public class Predicates {

    /** Prevent instantiation */
    private Predicates() {}

    /**
     * Returns a predicate that returns to true if the incoming request is an instance
     * of the given request class.
     * @param <T> class of the request to evaluate against
     * @param requestType request type to evaluate against
     * @return true if the incoming request is an instance of the given request class
     */
    public static <T extends Request> Predicate<HandlerInput> requestType(Class<T> requestType) {
        return i -> requestType.isInstance(i.getRequestEnvelope().getRequest());
    }

    /**
     * Returns a predicate that returns to true if the incoming request is an {@link IntentRequest}
     * for the given intent name.
     * @param intentName intent name to evaluate against
     * @return true if the incoming request is an {@link IntentRequest} for the given intent name
     */
    public static Predicate<HandlerInput> intentName(String intentName) {
        return i -> i.getRequestEnvelope().getRequest() instanceof IntentRequest
                && intentName.equals(((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getName());
    }

    /**
     * Returns a predicate that returns to true if the incoming request is an {@link CanFulfillIntentRequest}
     * for the given intent name.
     * @param intentName intent name to evaluate against
     * @return true if the incoming request is an {@link CanFulfillIntentRequest} for the given intent name
     */
    public static Predicate<HandlerInput> canFulfillIntentName(String intentName) {
        return i -> i.getRequestEnvelope().getRequest() instanceof CanFulfillIntentRequest
                && intentName.equals(((CanFulfillIntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getName());
    }

    /**
     * Returns a predicate that returns to true if the incoming request is an {@link IntentRequest}
     * and contains the given slot name and value.
     * @param slotName expected intent slot name
     * @param slotValue expected intent slot value
     * @return true if the incoming request is an {@link IntentRequest} and contains the given slot name and value
     */
    public static Predicate<HandlerInput> slotValue(String slotName, String slotValue) {
        return i -> i.getRequestEnvelope().getRequest() instanceof IntentRequest
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots() != null
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().containsKey(slotName)
                && slotValue.equals(((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(slotName).getValue());
    }

    /**
     * Returns a predicate that returns to true if the incoming request is an {@link CanFulfillIntentRequest}
     * and contains the given slot name and value.
     * @param slotName expected intent slot name
     * @param slotValue expected intent slot value
     * @return true if the incoming request is an {@link CanFulfillIntentRequest} and contains the given slot name and value
     */
    public static Predicate<HandlerInput> canFulfillSlotValue(String slotName, String slotValue) {
        return i -> i.getRequestEnvelope().getRequest() instanceof CanFulfillIntentRequest
                && ((CanFulfillIntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots() != null
                && ((CanFulfillIntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().containsKey(slotName)
                && slotValue.equals(((CanFulfillIntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(slotName).getValue());
    }

    /**
     * Returns a predicate that returns to true if the incoming request is an {@link ElementSelectedRequest}
     * with the given token.
     * @param elementToken token to evaluate against
     * @return true if the incoming request is an {@link ElementSelectedRequest} with the given token
     */
    public static Predicate<HandlerInput> selectedElementToken(String elementToken) {
        return i -> i.getRequestEnvelope().getRequest() instanceof ElementSelectedRequest
                && elementToken.equals(((ElementSelectedRequest) i.getRequestEnvelope().getRequest()).getToken());
    }

    /**
     * Returns a predicate that returns to true if the request attributes included with the {@link HandlerInput}
     * contain the expected attribute value.
     * @param key key of the attribute to evaluate
     * @param value value of the attribute to evaluate
     * @return true if the request attributes included with the {@link HandlerInput} contain the expected
     *         attribute value
     */
    public static Predicate<HandlerInput> requestAttribute(String key, Object value) {
        return i -> i.getAttributesManager().getRequestAttributes().containsKey(key)
                && value.equals(i.getAttributesManager().getRequestAttributes().get(key));
    }

    /**
     * Returns a predicate that returns to true if session attributes are included with the {@link HandlerInput}
     * and contain the expected attribute value.
     * @param key key of the attribute to evaluate
     * @param value value of the attribute to evaluate
     * @return true if session attributes are included with the {@link HandlerInput} and contain the expected
     *         attribute value.
     */
    public static Predicate<HandlerInput> sessionAttribute(String key, Object value) {
        return i -> i.getRequestEnvelope().getSession() != null
                && i.getAttributesManager().getSessionAttributes().containsKey(key)
                && value.equals(i.getAttributesManager().getSessionAttributes().get(key));
    }

    /**
     * Returns a predicate that returns to true if the persistent attributes included with the {@link HandlerInput}
     * contain the expected attribute value.
     * @param key key of the attribute to evaluate
     * @param value value of the attribute to evaluate
     * @return true if the persistent attributes included with the {@link HandlerInput} contain the expected
     *         attribute value
     */
    public static Predicate<HandlerInput> persistentAttribute(String key, Object value) {
        return i -> i.getAttributesManager().getPersistentAttributes().containsKey(key)
                && value.equals(i.getAttributesManager().getPersistentAttributes().get(key));
    }

    public static Predicate<HandlerInput> viewportProfile(ViewportProfile viewportProfile) {
        return i -> viewportProfile.equals(ViewportUtils.getViewportProfile(i.getRequestEnvelope()));
    }

}
