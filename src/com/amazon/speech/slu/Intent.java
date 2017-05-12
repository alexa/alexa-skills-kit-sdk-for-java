/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * An {@code Intent} is the output of spoken language understanding (SLU) that represents what the
 * user wants to do based on a predefined schema definition.
 * </p>
 *
 * For example:
 * <p>
 * If the user said, "place a reservation," with an intent schema definition of: <br>
 *
 * <pre>
 * {
 *     "intents":[
 *         {
 *             "intent":"ReservationIntent",
 *             "slots":[ ]
 *         }
 *     ]
 * }
 * </pre>
 * <p>
 * An {@code Intent} named "ReservationIntent" would be sent to the Alexa skill.
 * </p>
 *
 * @see com.amazon.speech.speechlet.IntentRequest#getIntent()
 */
public final class Intent {

    private final String name;
    private final ConfirmationStatus confirmationStatus;
    private final Map<String, Slot> slots;

    /**
     * Returns a new builder instance used to construct a new {@code Intent}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Intent} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Intent}.
     */
    private Intent(final Builder builder) {
        name = builder.name;

        confirmationStatus = builder.confirmationStatus;

        slots = Collections.unmodifiableMap(builder.slots);
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param name
     *            the intent name
     * @param slots
     *            the slots associated with the intent
     */
    private Intent(@JsonProperty("name") final String name,
                   @JsonProperty("confirmationStatus") final ConfirmationStatus confirmationStatus,
                   @JsonProperty("slots") final Map<String, Slot> slots) {
        this.name = name;
        this.confirmationStatus = confirmationStatus;

        if (slots != null) {
            this.slots = Collections.unmodifiableMap(slots);
        } else {
            this.slots = Collections.emptyMap();
        }
    }

    /**
     * Returns the name of this intent.
     *
     * @return the intent name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns all the {@code Slot}s.
     *
     * @return an immutable map of all the slots
     */
    @JsonInclude(Include.NON_EMPTY)
    public Map<String, Slot> getSlots() {
        return slots;
    }

    /**
     * Returns the {@code Slot} with the provided name.
     *
     * @param name
     *            the name of the slot to retrieve
     * @return the value or {@code null}
     */
    public Slot getSlot(final String name) {
        return slots.get(name);
    }

    /**
     * Returns the confirmationStatus associated with this request.
     *
     * @return the confirmationStatus
     */
    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    /**
     * Builder used to construct a new {@code Intent}.
     */
    public static final class Builder {
        private String name;
        private final Map<String, Slot> slots = new HashMap<>();
        private ConfirmationStatus confirmationStatus;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withConfirmationStatus(final ConfirmationStatus confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
            return this;
        }

        public Builder withSlots(final Map<String, Slot> slots) {
            this.slots.putAll(slots);
            return this;
        }

        public Builder withSlot(final Slot slot) {
            this.slots.put(slot.getName(), slot);
            return this;
        }

        public Intent build() {
            Validate.notBlank(name, "Intent name must be defined");
            return new Intent(this);
        }
    }
}
