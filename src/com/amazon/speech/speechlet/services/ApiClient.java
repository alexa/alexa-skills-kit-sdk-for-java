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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * The {@code ApiClient} is responsible for making API call and receiving response.
 * It is called by Alexa service and returns the {@code ApiClientResponse} object to service.
 *
 */
class ApiClient {

    /**
     * Make an API call to the specified uri with headers and optional body.
     * @param uri
     *          http endpoint to call
     * @param headers
     *          Key value pair of headers
     * @param body
     *          post body to send
     * @return the apiClientResponse includes the response code and response body
     * @throws IOException
     *          may occur while making HTTP call
     */
    public ApiClientResponse post(String uri, Map<String, String> headers, String body) throws IOException {
        return this.dispatch(uri, headers, body, "POST");
    }

    /**
     * Make an API call to the specified uri with headers and optional body.
     * @param uri
     *          http endpoint to call
     * @param headers
     *          Key value pair of headers
     * @param body
     *          put body to send
     * @return the apiClientResponse includes the response code and response body
     * @throws IOException
     *          may occur while making HTTP call
     */
    public ApiClientResponse put(String uri, Map<String, String> headers, String body) throws IOException {
        return this.dispatch(uri, headers, body, "PUT");
    }

    /**
     * Make an API call to the specified uri with headers.
     * @param uri
     *          http endpoint to call
     * @param headers
     *          Key value pair of headers
     * @return the apiClientResponse includes the response code and response body
     * @throws IOException
     *          may occur while making HTTP call
     */
    public ApiClientResponse get(String uri, Map<String, String> headers) throws IOException {
        return this.dispatch(uri, headers, null, "GET");
    }

    /**
     * Make an API call to the specified uri with headers.
     * @param uri
     *          http endpoint to call
     * @param headers
     *          Key value pair of headers
     * @return the apiClientResponse includes the response code and response body
     * @throws IOException
     *          may occur while making HTTP call
     */
    public ApiClientResponse delete(String uri, Map<String, String> headers) throws IOException {
        return this.dispatch(uri, headers, null, "DELETE");
    }

    /**
     * Makes an API call given the specified api client options.
     * @private
     * @param uri
     *          http endpoint to call
     * @param headers
     *          key value pair of headers
     * @param body
     *          post body to send
     * @param method
     *          the method for the URL request
     * @return the apiClientResponse includes the response code and response body
     * @throws IOException
     *          may occur while making HTTP call
     */
    private ApiClientResponse dispatch(String uri, Map<String, String> headers, String body, String method)
            throws IOException {
        HttpURLConnection connection = getHttpConnection(uri);
        try {
            writeHeaders(connection, headers, method);
            if (body != null) {
                writeRequestBody(connection, body);
            }
            ApiClientResponse apiClientResponse = readResponseBody(connection);
            return apiClientResponse;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * get the http connection for the HTTP call.
     * @private
     * @param uri
     *         http endpoint to call
     * @throws IOException
     *          may occur while creating the URL and open http connection
     */
    private HttpURLConnection getHttpConnection(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection;
    }
    /**
     * set the headers for the HTTP call.
     * @private
     * @param connection
     *         HTTP URL connection
     * @param headers
     *          key value pair of headers
     * @param method
     *          the method for the URL request
     * @throws IOException
     *          may occur while setting URL request Method
     */
    private void writeHeaders(HttpURLConnection connection, Map<String, String> headers, String method)
            throws IOException {
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    /**
     * send the request body for the HTTP call.
     * @private
     * @param connection
     *         HTTP URL connection
     * @param body
     *          post body to send
     * @throws IOException
     *          may occur while writing dataoutputstream
     */
    private void writeRequestBody(HttpURLConnection connection, String body) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())) {
            dataOutputStream.write(body.getBytes("UTF-8"));
        }
    }

    /**
     * read the response from the HTTP call.
     * @private
     * @param connection
     *         HTTP URL connection
     * @throws IOException
     *          may occur while reading dataInputStream
     */
    private ApiClientResponse readResponseBody(HttpURLConnection connection) throws IOException {
        StringBuffer responseSB = new StringBuffer();
        String line;
        try (BufferedReader dataInputStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            while ((line = dataInputStream.readLine()) != null) {
                responseSB.append(line);
            }
        }
        return new ApiClientResponse(connection.getResponseCode(), responseSB.toString());
    }
}
