/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.services;

import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiClientRequest;
import com.amazon.ask.model.services.ApiClientResponse;
import com.amazon.ask.model.services.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Apache HTTP client backed implementation of the ASK Java SDK API Client.
 */
public final class ApacheHttpApiClient implements ApiClient {

    /**
     * Provision a HTTP client to send requests to external services.
     */
    private final CloseableHttpClient httpClient;

    /**
     * Private constructor to prevent instantiation from outside this class.
     * @param builder {@link Builder}
     */
    private ApacheHttpApiClient(final Builder builder) {
        this.httpClient = builder.httpClient;
    }

    /**
     * Creates an instance with a default configured HTTP client.
     * @return API client instance
     */
    public static ApacheHttpApiClient standard() {
        return new Builder().withHttpClient(HttpClients.createDefault()).build();
    }

    /**
     * Returns a builder that can be used to create a custom instance of this client.
     * @return builder
     */
    public static Builder custom() {
        return new Builder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApiClientResponse invoke(final ApiClientRequest request) {
        HttpUriRequest lowLevelRequest = generateRequest(request);
        try (CloseableHttpResponse lowLevelResponse = httpClient.execute(lowLevelRequest)) {
            return generateResponse(lowLevelResponse);
        } catch (IOException e) {
            //TODO: replace with API client internal exception type.
            throw new RuntimeException("There was an error executing the request", e);
        }
    }

    /**
     * Generates a {@link HttpUriRequest} instance based on request method.
     * @param request of type {@link ApiClientRequest}.
     * @return HttpUriRequest.
     */
    private HttpUriRequest generateRequest(final ApiClientRequest request) {
        HttpUriRequest lowLevelRequest;
        switch (request.getMethod()) {
            case "GET":
                lowLevelRequest = new HttpGet(request.getUrl());
                break;
            case "PUT":
                lowLevelRequest = new HttpPut(request.getUrl());
                break;
            case "POST":
                lowLevelRequest = new HttpPost(request.getUrl());
                break;
            case "DELETE":
                lowLevelRequest = new HttpDelete(request.getUrl());
                break;
            case "OPTIONS":
                lowLevelRequest = new HttpOptions(request.getUrl());
                break;
            case "HEAD":
                lowLevelRequest = new HttpHead(request.getUrl());
                break;
            case "PATCH":
                lowLevelRequest = new HttpPatch(request.getUrl());
                break;
            default:
                throw new RuntimeException("Invalid request method: " + request.getMethod());
        }

        if (request.getHeaders() != null) {
            for (Pair<String, String> header : request.getHeaders()) {
                lowLevelRequest.setHeader(header.getName(), header.getValue());
            }
        }

        if (lowLevelRequest instanceof HttpEntityEnclosingRequestBase && request.getBody() != null) {
            StringEntity entity = new StringEntity(request.getBody(), ContentType.APPLICATION_JSON);
            ((HttpEntityEnclosingRequestBase) lowLevelRequest).setEntity(entity);
        }

        return lowLevelRequest;
    }

    /**
     * Generates {@link ApiClientResponse} from an input low level response.
     * @param lowLevelResponse of type {@link CloseableHttpResponse}.
     * @throws IOException in case of an exception while processing the response.
     * @return apiClientResponse.
     */
    private ApiClientResponse generateResponse(final CloseableHttpResponse lowLevelResponse) throws IOException {
        ApiClientResponse response = new ApiClientResponse();
        response.setStatusCode(lowLevelResponse.getStatusLine().getStatusCode());
        response.setHeaders(Arrays.stream(lowLevelResponse.getAllHeaders())
                .map(h -> new Pair<>(h.getName(), h.getValue())).collect(Collectors.toList()));

        if (lowLevelResponse.getEntity() != null) {
            response.setBody(EntityUtils.toString(lowLevelResponse.getEntity()));
        }

        return response;
    }

    /**
     * Apache HTTP Api Client Builder.
     */
    public static final class Builder {

        /**
         * Provision a HTTP client to send requests to external services.
         */
        private CloseableHttpClient httpClient;

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Allows user to configure a custom HTTP client.
         * @param httpClient {@link CloseableHttpClient}.
         * @return {@link Builder}
         */
        public Builder withHttpClient(final CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Returns an instance of {@link ApacheHttpApiClient} with the given configuration.
         * @return ApacheHttpApiClient.
         */
        public ApacheHttpApiClient build() {
            return new ApacheHttpApiClient(this);
        }
    }
}
