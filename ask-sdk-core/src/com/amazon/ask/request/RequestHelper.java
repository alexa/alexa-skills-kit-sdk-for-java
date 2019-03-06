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
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SupportedInterfaces;
import com.amazon.ask.util.ValidationUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Helper class that makes it easy to retrieve request properties from a {@link HandlerInput}.
 */
public class RequestHelper {

    private final HandlerInput handlerInput;

    private RequestHelper(HandlerInput handlerInput) {
        this.handlerInput = ValidationUtils.assertNotNull(handlerInput, "handlerInput");
    }

    /**
     * Creates an {@link RequestHelper} instance against the provided handler input.
     *
     * @param input handler input
     * @return an RequestHelper, providing convience methods for retrieving request properties out of the provided handler input.
     */
    public static RequestHelper forHandlerInput(HandlerInput input) {
        return new RequestHelper(input);
    }

    /**
     * Retrieves the locale from the the request.
     *
     * The method returns the locale value present in the request. More information about the locale can be found
     * here: https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#request-locale
     *
     * @return locale property value
     */
    public String getLocale() {
        return handlerInput.getRequest().getLocale();
    }

    /**
     * Retrieves the type of the request.
     *
     * The method retrieves the request type of the input request. More information about the different request
     * types are mentioned here:
     * https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#request-body-parameters
     *
     * @return request type property value
     */
    public String getRequestType() {
        return handlerInput.getRequest().getType();
    }

    /**
     * Retrieves the name of the {@link com.amazon.ask.model.Intent} from the request.
     *
     * The method retrieves the intent name from the input request, only if the input request is an {@link IntentRequest}.
     * An {@link IllegalArgumentException} is thrown if not.
     *
     * @return intent name
     */
    public String getIntentName() {
        return castRequestType(handlerInput, IntentRequest.class).getIntent().getName();
    }

    /**
     * Retrieves the account linking access token from the request.
     *
     * The method retrieves the user's accessToken from the input request. Once a user successfully enables a skill
     * and links their Alexa account to the skill, the input request will have the user's access token. A null value
     * is returned if there is no access token in the input request. More information on this can be found here:
     * https://developer.amazon.com/docs/account-linking/add-account-linking-logic-custom-skill.html
     *
     * @return account linking access token
     */
    public String getAccountLinkingAccessToken() {
        return handlerInput.getRequestEnvelope().getContext().getSystem().getUser().getAccessToken();
    }

    /**
     * Retrieves the API access token from the request.
     *
     * The method retrieves the apiAccessToken from the input request, which has the encapsulated information of
     * permissions granted by the user. This token can be used to call Alexa-specific APIs. More information
     * about this can be found here:
     * https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#system-object
     *
     * The SDK automatically injects this value into service client instances retrieved from the
     * {@link com.amazon.ask.model.services.ServiceClientFactory}.
     *
     * @return API access token
     */
    public String getApiAccessToken() {
        return handlerInput.getRequestEnvelope().getContext().getSystem().getApiAccessToken();
    }

    /**
     * Retrieves the device ID from the request.
     *
     * The method retrieves the deviceId property from the input request. This value uniquely identifies the device
     * and is generally used as input for some Alexa-specific API calls. More information about this can be found here:
     * https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#system-object
     *
     * @return device ID
     */
    public String getDeviceId() {
        return handlerInput.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
    }

    /**
     * Retrieves the dialog state from the request.
     *
     * The method retrieves the `dialogState` from the intent request, if the skill's interaction model includes a
     * dialog model. This can be used to determine the current status of user conversation and return the appropriate
     * dialog directives if the conversation is not yet complete. More information on dialog management can be found here:
     * https://developer.amazon.com/docs/custom-skills/define-the-dialog-to-collect-and-confirm-required-information.html
     *
     * This method returns an {@link IllegalArgumentException} if the incoming request is not an {@link IntentRequest}.
     *
     * @return dialog state
     */
    public DialogState getDialogState() {
        return castRequestType(handlerInput, IntentRequest.class).getDialogState();
    }

    /**
     * Returns the {@link Slot} for the given slot name from the request.
     *
     * This method attempts to retrieve the requested {@link Slot} from the incoming request. If the slot does not
     * exist in the request, an {@link Optional} empty is returned.
     *
     * This method returns an {@link IllegalArgumentException} if the incoming request is not an {@link IntentRequest}.
     *
     * @param slotName name of the slot to retrieve
     * @return an {@link Optional} containing the target slot if it exists in the request, else an empty {@link Optional}
     */
    public Optional<Slot> getSlot(String slotName) {
         Map<String, Slot> slots = castRequestType(handlerInput, IntentRequest.class).getIntent().getSlots();
         if (slots != null) {
             return Optional.ofNullable(slots.get(slotName));
         }
         return Optional.empty();
    }

    /**
     * Returns the value from the given {@link Slot} in the request.
     *
     * This method attempts to retrieve the requested {@link Slot}'s value from the incoming request. If the slot does
     * exist in the request, an {@link Optional} empty is returned.
     *
     * This method returns an {@link IllegalArgumentException} if the incoming request is not an {@link IntentRequest}.
     *
     * @param slotName name of the slot to retrieve
     * @return an {@link Optional} containing the target slot's value if it exists in the request, else an empty {@link Optional}
     */
    public Optional<String> getSlotValue(String slotName) {
        return getSlot(slotName).map(Slot::getValue);
    }

    /**
     * Retrieves the {@link SupportedInterfaces} from the request.
     *
     * This method returns an object listing each interface that the device supports. For example, if
     * supportedInterfaces includes AudioPlayer, then you know that the device supports streaming audio using the
     * AudioPlayer interface.
     *
     * @return supported interfaces
     */
    public SupportedInterfaces getSupportedInterfaces() {
        return handlerInput.getRequestEnvelope().getContext().getSystem().getDevice().getSupportedInterfaces();
    }

    /**
     * Returns whether the request is a new session.
     *
     * The method retrieves the new value from the input request's session, which indicates if it's a new session or
     * not. More information can be found here :
     * https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#session-object
     *
     * This method returns an {@link IllegalArgumentException} if the request is not an in-session request.
     *
     * @return true if the request is a new session
     */
    public boolean isNewSession() {
        Session session = handlerInput.getRequestEnvelope().getSession();
        if (session == null) {
            throw new IllegalArgumentException("The provided request doesn't contain a session");
        }
        return session.getNew();
    }

    private static <T> T castRequestType(HandlerInput input, Class<T> targetType) {
        if (!targetType.isInstance(input.getRequest())) {
            throw new IllegalArgumentException("Provided request is not a " + targetType.getName());
        }
        return targetType.cast(input.getRequest());
    }

}
