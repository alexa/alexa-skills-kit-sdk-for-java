package com.amazon.ask.cityguide.utils;

import com.amazon.ask.exception.AskSdkException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {

    private static CloseableHttpClient httpClient;

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    public static CloseableHttpResponse doGet(Map<String, String> requestParameters, String endpoint) throws IOException {
        return getHttpClient().execute(buildGet(requestParameters, endpoint));
    }

    public static HttpGet buildGet(Map<String, String> requestParameters, String endpoint) {
        try {
            final URI uri = new URIBuilder(endpoint)
                    .addParameters(requestParameters.entrySet().stream()
                            .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList()))
                    .build();
            return new HttpGet(uri);
        } catch (URISyntaxException ex) {
            throw new AskSdkException("Unable to form a valid endpoint with request parameters", ex);
        }
    }
}
