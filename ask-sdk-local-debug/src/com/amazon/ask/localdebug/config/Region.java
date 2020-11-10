/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.config;

/**
 * Enum for region to debug endpoint mapping.
 */
public enum Region {
    /**
     * North America endpoint.
     */
    NA("bob-dispatch-prod-na.amazon.com"),
    /**
     * Europe endpoint.
     */
    EU("bob-dispatch-prod-eu.amazon.com"),
    /**
     * Far East endpoint.
     */
    FE("bob-dispatch-prod-fe.amazon.com");
    /**
     * Endpoint url.
     */
    private final String endpoint;

    /**
     * Enum constructor.
     * @param endpoint endpoint for a given region.
     */
    private Region(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Get endpoint for a given region.
     * @return endpoint for a given region.
     */
    public String getEndpoint() {
        return this.endpoint;
    }
}
