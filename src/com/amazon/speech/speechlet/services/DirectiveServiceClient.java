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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DirectiveServiceClient will send the specified directiveObj to Alexa directive service.
 *
 */
public class DirectiveServiceClient implements DirectiveService {
    private static final String directiveApiPath = "/v1/directives";
    private ApiClient apiClient;

    /**
     * Public constructor calling private constructor to create an instance of DirectiveServiceClient.
     */
    public DirectiveServiceClient() {
        this(new ApiClient());
    }

    /**
     * Private constructor.
     * @param apiClient
     *              the Api client to call
     */
    protected DirectiveServiceClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Send the specified {@code DirectiveEnvelope} to Alexa directive service.
     *
     * @param directiveEnvelope
     *                  directive to send to service
     * @param apiEndpoint
     *                  API endpoint from Alexa request
     * @param token
     *                  bearer token for directive service
     * @throws ServiceException
     *                  if API call fails
     */
    public void enqueue(DirectiveEnvelope directiveEnvelope, String apiEndpoint, String token)
            throws ServiceException {
        String url = apiEndpoint + directiveApiPath;
        dispatch(directiveEnvelope, url, token);
    }

    /**
     * Call the directives api with the specified bearer token and directive object.
     * @private
     * @param directiveEnvelope
     *              directive directive to send to service
     * @param url
     *              http end point to call
     * @param token
     *              bearer token for directive service
     */
    private void dispatch(DirectiveEnvelope directiveEnvelope, String url, String token)
            throws ServiceException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");
        ApiClientResponse apiClientResponse;
        try {
            apiClientResponse = apiClient.post(url, headers, directiveEnvelope.toJsonString());
        } catch (IOException e) {
            throw new ServiceException("Call to directive service failed: " + e.getMessage(), e);
        }
        int apiClientResponseCode = apiClientResponse.getResponseCode();
        if (apiClientResponseCode < 200 || apiClientResponseCode >= 300) {
            throw new ServiceException("Directive Service returned an error with code "
                    + apiClientResponseCode + " and with body " + apiClientResponse.getResponseBody());
        }
    }

}
