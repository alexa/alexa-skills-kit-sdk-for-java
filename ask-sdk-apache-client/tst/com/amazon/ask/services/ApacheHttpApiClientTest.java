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
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApacheHttpApiClientTest {

    private static final String TEST_URI = "http://foo.com/bar?baz=true";
    private static final String TEST_PAYLOAD = "{foo: \"bar\"}";

    @Test
    public void get_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("GET", TEST_URI);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpGet);
        assertEquals(capturedRequest.getMethod(), "GET");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
    }

    @Test
    public void put_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("PUT", TEST_URI, TEST_PAYLOAD);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpPut);
        assertEquals(capturedRequest.getMethod(), "PUT");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
        assertEntityMatchesPayload(((HttpEntityEnclosingRequestBase)capturedRequest).getEntity(), TEST_PAYLOAD);
    }

    @Test
    public void post_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("POST", TEST_URI, TEST_PAYLOAD);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpPost);
        assertEquals(capturedRequest.getMethod(), "POST");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
        assertEntityMatchesPayload(((HttpEntityEnclosingRequestBase)capturedRequest).getEntity(), TEST_PAYLOAD);
    }

    @Test
    public void delete_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("DELETE", TEST_URI);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpDelete);
        assertEquals(capturedRequest.getMethod(), "DELETE");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
    }

    @Test
    public void options_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("OPTIONS", TEST_URI);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpOptions);
        assertEquals(capturedRequest.getMethod(), "OPTIONS");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
    }

    @Test
    public void head_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("HEAD", TEST_URI);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpHead);
        assertEquals(capturedRequest.getMethod(), "HEAD");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
    }

    @Test
    public void patch_request_executed() throws Exception {
        ApiClientRequest request = generateRequest("PATCH", TEST_URI, TEST_PAYLOAD);
        HttpUriRequest capturedRequest = captureLowLevelRequest(request);
        assertTrue(capturedRequest instanceof HttpPatch);
        assertEquals(capturedRequest.getMethod(), "PATCH");
        assertEquals(capturedRequest.getURI(), URI.create(TEST_URI));
        assertEntityMatchesPayload(((HttpEntityEnclosingRequestBase)capturedRequest).getEntity(), TEST_PAYLOAD);
    }

    @Test(expected = RuntimeException.class)
    public void invalid_method_throws_exception() throws Exception {
        ApiClientRequest request = generateRequest("INVALID_METHOD", TEST_URI);
        captureLowLevelRequest(request);
    }

    @Test
    public void api_client_response_contains_expected_data() throws Exception {
        ApiClientRequest request = generateRequest("PUT", TEST_URI, TEST_PAYLOAD);
        CloseableHttpClient apacheClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = generateResponse(request);
        when(apacheClient.execute(any())).thenReturn(mockResponse);
        ApiClient client = ApacheHttpApiClient.custom().withHttpClient(apacheClient).build();
        ApiClientResponse response = client.invoke(request);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getBody(), TEST_PAYLOAD);
        List<Pair<String, String>> headers = response.getHeaders();
        assertEquals(headers.size(), 1);
        Pair<String, String> header = headers.get(0);
        assertEquals(header.getName(), "foo");
        assertEquals(header.getValue(), "bar");
    }

    @Test(expected = RuntimeException.class)
    public void exception_during_execution_wrapped_in_client_exception() throws Exception {
        ApiClientRequest request = generateRequest("GET", TEST_URI);
        CloseableHttpClient apacheClient = mock(CloseableHttpClient.class);
        when(apacheClient.execute(any())).thenThrow(new IOException());
        ApiClient client = ApacheHttpApiClient.custom().withHttpClient(apacheClient).build();
        client.invoke(request);
    }

    private HttpUriRequest captureLowLevelRequest(ApiClientRequest request) throws IOException {
        CloseableHttpClient apacheClient = mock(CloseableHttpClient.class);
        ArgumentCaptor<HttpUriRequest> capturedRequest = ArgumentCaptor.forClass(HttpUriRequest.class);
        CloseableHttpResponse mockResponse = generateResponse(request);
        when(apacheClient.execute(capturedRequest.capture())).thenReturn(mockResponse);
        ApiClient client = ApacheHttpApiClient.custom().withHttpClient(apacheClient).build();
        client.invoke(request);
        return capturedRequest.getValue();
    }

    private ApiClientRequest generateRequest(String method, String uri) {
        return generateRequest(method, uri, null);
    }

    private ApiClientRequest generateRequest(String method, String uri, String body) {
        ApiClientRequest request = new ApiClientRequest();
        request.setMethod(method);
        request.setUrl(uri);
        request.setBody(body);
        return request;
    }
    private CloseableHttpResponse generateResponse(ApiClientRequest request) throws IOException {
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(mockResponse.getStatusLine()).thenReturn(statusLine);
        Header[] headers = {new BasicHeader("foo", "bar")};
        when(mockResponse.getAllHeaders()).thenReturn(headers);
        if (request.getBody() != null) {
            when(mockResponse.getEntity()).thenReturn(new StringEntity(request.getBody()));
        }
        return mockResponse;
    }

    private void assertEntityMatchesPayload(HttpEntity entity, String expectedPayload) throws IOException {
        String payload = EntityUtils.toString(entity);
        assertEquals(payload, expectedPayload);
    }
}
