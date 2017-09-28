/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu.entityresolution;

import com.amazon.speech.slu.Slot;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * {@code Resolutions} is part of an {@link Slot}.
 * An Resolutions object represents the results of resolving the words captured from the user’s utterance.
 * </p>
 *
 * For example:
 * <p>
 * User can define synonyms that should resolve to specific slot value
 * with the following sample json in their interaction model: <br>
 *
 * <pre>
 * {
 *      "types": [
 *          {
 *          "name": "WeatherTypes",
 *          "values": [
 *              {
 *              "id" : "RAIN",
 *              "name" : {
 *                  "value": "rain",
 *                  "synonyms": ["shower", "storm", "rainstorm"]
 *              }
 *              }
 *          ]
 *          }
 *      ]
 * }
 * </pre>
 *
 * <p>
 * In the above example, "shower" is defined as synonym for "rain".
 * When user says "shower", a response containing the following slot sample may be returned to the skill: <br>
 * <pre>
 * {
 *     "slots": {
 *          "WeatherType": {
 *              "name": "WeatherType",
 *              "value": "shower",
 *              "resolutions": {
 *                  "resolutionsPerAuthority": [{
 *                      "authority": "{authority-url}",
 *                      "status": {
 *                          "code": "ER_SUCCESS_MATCH"
 *                      },
 *                      "values": [{
 *                          "value": {
 *                              "name": "rain",
 *                              "id": "RAIN"
 *                              }
 *                           }]
 *                     }]
 *                 }
 *          }
 * }
 * </pre>
 *
 * <p>
 * In the above response json, "shower" is still passed as value to ensure backward-compatibility.
 * But "rain" appears as resolution value
 * </p>
 * <p>
 * {@code Resolutions} is included for slots that use a custom slot type
 *  or a built-in slot type that have been extended with custom values.
 * Note that resolutions is not included for built-in slot types that you have not extended.
 * </p>
 */
public final class Resolutions {

    private final List<Resolution> resolutionsPerAuthority;

    /**
     * Returns a new builder instance used to construct a new {@code Resolutions}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code Resolutions} from a {@code Builder}.
     *
     * @param builder
     *              the builder used to construct the {@code Resolutions}.
     */
    private Resolutions(final Builder builder) {
        resolutionsPerAuthority = Collections.unmodifiableList(builder.resolutionsPerAuthority);
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param resolutionsPerAuthority
     *                              the resolution result for each authority
     */
    private Resolutions(@JsonProperty("resolutionsPerAuthority") final List<Resolution> resolutionsPerAuthority) {
        if (resolutionsPerAuthority != null) {
            this.resolutionsPerAuthority = Collections.unmodifiableList(resolutionsPerAuthority);
        } else {
            this.resolutionsPerAuthority = Collections.emptyList();
        }
    }

    /**
     * Returns the ResolutionsPerAuthority for this {@code Resolutions}.
     *
     * @return An array of Resolution representing each possible authority for entity resolution.
     */
    public List<Resolution> getResolutionsPerAuthority() {
        return resolutionsPerAuthority;
    }

    /**
     * Returns the {@code Resolution} with the provided index.
     *
     * @param index
     *              the index of the resolution to retrieve
     * @return the resolution
     */
    public Resolution getResolutionAtIndex(final int index) {
        return resolutionsPerAuthority.get(index);
    }

    /**
     * Builder used to construct a new {@code Resolutions}.
     */
    public static final class Builder {
        private final List<Resolution> resolutionsPerAuthority = new ArrayList<>();

        public Builder withResolutionsPerAuthority(final List<Resolution> resolutionsPerAuthority) {
            this.resolutionsPerAuthority.addAll(resolutionsPerAuthority);
            return this;
        }

        public Resolutions build() {
            return new Resolutions(this);
        }
    }
}
