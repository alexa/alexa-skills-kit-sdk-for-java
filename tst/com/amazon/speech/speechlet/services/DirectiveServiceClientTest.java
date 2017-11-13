package com.amazon.speech.speechlet.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class tests the enqueue method in the {@link DirectiveServiceClient} class.
 *
 */
public class DirectiveServiceClientTest {
    private DirectiveEnvelopeHeader testDirectiveDirectiveEnvelopeHeader;
    private SpeakDirective testDirective;
    private DirectiveEnvelope testDirectiveEnvelope;

    private class TestableDirectiveService extends DirectiveServiceClient{
        public TestableDirectiveService(ApiClient mockedApiClient) {
            super(mockedApiClient);
        }
    }

    @Before
    public void setUp() throws IOException {
        testDirectiveDirectiveEnvelopeHeader = DirectiveEnvelopeHeader.builder().withRequestId("requestId").build();
        testDirective = SpeakDirective.builder().withSpeech("loading").build();
        testDirectiveEnvelope = DirectiveEnvelope.builder().withHeader(testDirectiveDirectiveEnvelopeHeader).withDirective(testDirective).build();
    }

    @Test
    public void enqueue_directiveEnvelope_callApiClientWithJsonString() throws IOException, ServiceException {
        ApiClient testApiClient = Mockito.mock(ApiClient.class);
        TestableDirectiveService service = new TestableDirectiveService(testApiClient);
        ApiClientResponse apiClientResponse = new ApiClientResponse(204, null);
        Mockito.when(testApiClient.post(anyString(),anyMap(),eq(testDirectiveEnvelope.toJsonString()))).thenReturn(apiClientResponse);

        service.enqueue(testDirectiveEnvelope, "", "");

        verify(testApiClient, times(1)).post(anyString(),anyMap(),eq(testDirectiveEnvelope.toJsonString()));
    }

    @Test
    public void enqueue_apiEndpoint_callApiClientWithJsonString() throws IOException, ServiceException {
        ApiClient testApiClient = Mockito.mock(ApiClient.class);
        TestableDirectiveService service = new TestableDirectiveService(testApiClient);
        String apiEndPoint = "apiEndPoint";
        String url = apiEndPoint + "/v1/directives";
        ApiClientResponse apiClientResponse = new ApiClientResponse(204, null);
        Mockito.when(testApiClient.post(eq(url),anyMap(),anyString())).thenReturn(apiClientResponse);

        service.enqueue(testDirectiveEnvelope, apiEndPoint, "");

        verify(testApiClient, times(1)).post(eq(url), anyMap(), anyString());
    }

    @Test
    public void enqueue_token_callApiClientWithJsonString() throws IOException, ServiceException {
        ApiClient testApiClient = Mockito.mock(ApiClient.class);
        TestableDirectiveService service = new TestableDirectiveService(testApiClient);
        String token = "token";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token);
        headers.put("Content-Type","application/json");
        ApiClientResponse apiClientResponse = new ApiClientResponse(204, null);
        Mockito.when(testApiClient.post(anyString(),eq(headers),anyString())).thenReturn(apiClientResponse);

        service.enqueue(testDirectiveEnvelope, "", token);

        verify(testApiClient, times(1)).post(anyString(), eq(headers), anyString());
    }

    @Test(expected = ServiceException.class)
    public void enqueue_serviceexception_allApiClientWithJsonString() throws IOException, ServiceException {
        ApiClient testApiClient = Mockito.mock(ApiClient.class);
        TestableDirectiveService service = new TestableDirectiveService(testApiClient);
        ApiClientResponse apiClientResponse = new ApiClientResponse(400, null);
        when(testApiClient.post(anyString(),anyMap(),anyString())).thenReturn(apiClientResponse);

        service.enqueue(testDirectiveEnvelope,"", "");
    }

    @Test(expected = ServiceException.class)
    public void enqueue_ioexception_allApiClientWithJsonString() throws IOException, ServiceException {
        ApiClient testApiClient = Mockito.mock(ApiClient.class);
        TestableDirectiveService service = new TestableDirectiveService(testApiClient);
        when(testApiClient.post(anyString(),anyMap(),anyString())).thenThrow(new IOException());

        service.enqueue(testDirectiveEnvelope,"", "");
    }

}
