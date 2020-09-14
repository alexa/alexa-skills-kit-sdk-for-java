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

import java.net.URI;
import java.util.Map;

/**
 * Configuration class for webSocket connection. Allows the  connectionUri and request withHeaders to be configured.
 */
public final class WebSocketClientConfig {
    /**
     * Uri for establishing web socket connection.
     */
    private final URI webSocketServerUri;
    /**
     * Web socket connection request headers.
     */
    private final Map<String, String> headers;

    /**
     * Constructor of WebSocketClientConfig.
     * @param builder Builder for WebSocketClientConfig {@link WebSocketClientConfig.Builder}.
     */
    private WebSocketClientConfig(final Builder builder) {
        this.webSocketServerUri = builder.webSocketServerUri;
        this.headers = builder.headers;
    }

    /**
     * Creates builder instance.
     * @return new Builder instance {@link WebSocketClientConfig.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets web socket URI.
     * @return web socket URI.
     */
    public URI getWebSocketServerUri() {
        return this.webSocketServerUri;
    }

    /**
     * Gets the request headers.
     * @return request headers.
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * Web socket uri.
         */
        private URI webSocketServerUri;
        /**
         * Request headers.
         */
        private Map<String, String> headers;

        /**
         * Initializes web socket Uri.
         * @param webSocketServerUri Initializes uri that will be used to
         *                           start a web-socket connection.
         * @return Builder instance {@link WebSocketClientConfig.Builder}
         */
        public Builder withWebSocketServerUri(final URI webSocketServerUri) {
            this.webSocketServerUri = webSocketServerUri;
            return this;
        }

        /**
         * Initializes Map of headers for the web socket connection.
         * @param headers Initializes Header &lt;K,V&gt; collection.
         * @return Builder instance {@link WebSocketClientConfig.Builder}
         */
        public Builder withHeaders(final Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Creates WebSocketClientConfig instance.
         * @return {@link WebSocketClientConfig}
         */
        public WebSocketClientConfig build() {
            return new WebSocketClientConfig(this);
        }
    }
}
