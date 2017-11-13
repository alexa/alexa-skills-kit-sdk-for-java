/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.services;

/**
 * Represents response from the {@code ApiClient} call.
 * @see com.amazon.speech.speechlet.services.ApiClient
 *
 */
class ApiClientResponse {
    private int responseCode;
    private String responseBody;

    /**
     * Construct an instance of ApiClientResponse.
     *
     * @param responseCode the response code
     * @param responseBody the response body
     */
    public ApiClientResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    /**
     * Returns the response code from ApiClient.
     *
     * @return responseCode the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Returns the response body.
     *
     * @return responseBody the response body
     */
    public String getResponseBody() {
        return responseBody;
    }
}
