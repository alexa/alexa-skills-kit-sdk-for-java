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

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * A {@code Slot} is part of an {@link Intent} that provides arguments need to process the intent
 * This is based on a predefined domain definition, or schema.
 * </p>
 *
 * For example:
 * <p>
 * If the user said, "place a reservation for two," with the intent schema: <br>
 *
 * <pre>
 * {
 *     "intents":[
 *         {
 *             "intent":"ReservationIntent",
 *             "slots":[
 *                 {"name":"Seats", "type":"NUMBER"},
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 * <p>
 * A ReservationIntent {@code Intent} would contain a <b>Seats</b> {@code Slot} with the value of
 * "2".
 * </p>
 *
 * <p>
 * A {@code Slot} can be {@code null} if:
 * <ul>
 * <li>If what the user said is not in the definition, meaning that it does not match any predefined
 * {@code Slot}, or no {@code Slot}s are defined for the Intent. Example: "place a reservation
 * tonight"</li>
 * <li>If the {@code Slot} is specified in the domain definition, but the user did not provide
 * enough information in their request. Example: "place a reservation."</li>
 * </ul>
 *
 * @see Intent#getSlots()
 * @see Intent#getSlot(String)
 */
public final class Slot {

    private final String name;
    private final String value;
    private final ConfirmationStatus confirmationStatus;

    /**
     * Returns a new builder instance used to construct a new {@code Slot}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Slot} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code Slot}.
     */
    private Slot(final Builder builder) {
        name = builder.name;
        value = builder.value;
        confirmationStatus = builder.confirmationStatus;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param name
     *            the slot name
     * @param value
     *            the resolved value
     */
    private Slot(@JsonProperty("name") final String name,
            @JsonProperty("value") final String value,
            @JsonProperty("confirmationStatus") final ConfirmationStatus confirmationStatus) {
        this.name = name;
        this.value = value;
        this.confirmationStatus = confirmationStatus;
    }

    /**
     * Returns the name of this {@code Slot}.
     *
     * @return the slot name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of this {@code Slot}.
     *
     * @return the slot value, or {@code null} if undefined
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the confirmationStatus for this {@code Slot}.
     *
     * @return the confirmation status
     */
    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    /**
     * Builder used to construct a new {@code Slot}.
     */
    public static final class Builder {
        private String name;
        private String value;
        private ConfirmationStatus confirmationStatus;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withValue(final String value) {
            this.value = value;
            return this;
        }

        public Builder withConfirmationStatus(final ConfirmationStatus confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
            return this;
        }

        public Slot build() {
            Validate.notBlank(name, "Slot name must be defined");
            return new Slot(this);
        }
    }
}
