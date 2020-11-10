/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.client;

import com.amazon.ask.localdebug.certificate.AllTrustCertificateProvider;
import com.amazon.ask.localdebug.certificate.CertificateProvider;
import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.config.WebSocketClientConfig;
import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.localdebug.util.RequestResponseUtils;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Web socket client wrapper re-purposed for local debugging.
 */
public class WebSocketClientImpl extends org.java_websocket.client.WebSocketClient implements WebSocketClient {

    /**
     * TLS version to be set in the SSLContext.
     */
    private static final String TLS = "TLSv1.2";

    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientImpl.class);

    /**
     * Executor service instance for managing async tasks.
     */
    private ExecutorService executorService;

    /**
     * Certificate provider to be used for setting the SSL context
     * for the web socket connection.
     */
    private CertificateProvider certificateProvider;

    /**
     * {@link SkillInvokerConfiguration}. Reflection configuration to be used to
     * invoke skill code.
     */
    private SkillInvokerConfiguration skillInvokerConfiguration;

    /**
     * Initialize base WebSocket client instance.
     *
     * @param builder builder instance {@link WebSocketClientImpl.Builder}.
     */
    public WebSocketClientImpl(final Builder builder) {
        super(builder.webSocketClientConfig.getWebSocketServerUri(), builder.webSocketClientConfig.getHeaders());
        certificateProvider = new AllTrustCertificateProvider();
        this.skillInvokerConfiguration = builder.skillInvokerConfiguration;
        this.executorService = builder.executorService;
    }

    /**
     * Creates builder instance.
     * @return new Builder instance {@link WebSocketClientImpl.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets certificate for web socket connection.
     * @return {@link CertificateProvider}.
     */
    public CertificateProvider getCertificateProvider() {
        return certificateProvider;
    }

    /**
     * Triggers the web socket client to make a blocking
     * connection attempt on the provided websocket uri.
     */
    @Override
    public void invoke() {
        SSLContext sslContext = getSslContext();
        this.setSocketFactory(sslContext.getSocketFactory());
        try {
            if (!this.connectBlocking()) {
                LOG.error("Failed to establish web socket connection for debug session");
                throw new LocalDebugSdkException("Web socket connection was not established");
            }
        } catch (InterruptedException e) {
            LOG.error("Web socket connection interrupted", e);
            throw new LocalDebugSdkException("Web socket connection interrupted", e);
        }
    }

    /**
     * Creates SSL context for web socket connection.
     * @return {@link SSLContext}.
     */
    private SSLContext getSslContext() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance(TLS);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Encountered error when creating SSL context for client certificate");
            throw new LocalDebugSdkException(e.getMessage(), e);
        }

        final KeyManager[] keyManagers = getCertificateProvider().getKeyManager().isPresent()
                ? getCertificateProvider().getKeyManager().get().toArray(new KeyManager[0]) : null;
        final TrustManager[] trustManagers = getCertificateProvider().getTrustManager().isPresent()
                ? getCertificateProvider().getTrustManager().get().toArray(new TrustManager[0]) : null;
        try {
            sslContext.init(keyManagers, trustManagers, null);
        } catch (KeyManagementException e) {
            LOG.error("Encountered error when initializing SSL context for client certificate");
            throw new LocalDebugSdkException(e.getMessage(), e);
        }
        return sslContext;
    }

    /**
     * Event triggered on successfully establishing web socket handshake.
     * @param handshakeData {@link org.java_websocket.handshake.Handshakedata}.
     */
    @Override
    public void onOpen(final ServerHandshake handshakeData) {
        LOG.info("*****Starting Skill Debug Session*****");
        LOG.info("*****Session will last for 1 hour****");
    }

    /**
     * Event triggered when WebSocket client receives an incoming skill request payload.
     * @param skillRequestPayload request payload.
     */
    @Override
    public void onMessage(final String skillRequestPayload) {
        LOG.info("Received message \n" + skillRequestPayload);
        CompletableFuture.supplyAsync(() -> RequestResponseUtils
                .getSkillResponse(RequestResponseUtils
                        .getDeserializeRequest(skillRequestPayload), skillInvokerConfiguration), executorService)
                .thenAccept(this::sendSkillResponse);
    }

    /**
     * Event triggered on connection closure.
     * @param code closure code.
     * @param reason closure reason.
     * @param remote closing party.
     */
    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        String cause = remote ? "remote" : "us";
        LOG.info("Connection closed by " + cause + "\nCode: " + code + "\nReason: " + reason);
    }

    /**
     * Event triggered on error.
     * @param ex exception thrown.
     */
    @Override
    public void onError(final Exception ex) {
        LOG.info("Encountered error in websocket connection \n" + ex);
        throw new LocalDebugSdkException(ex.getMessage(), ex);
    }

    /**
     * Sends skill response over the established web-socket connection.
     *
     * @param localDebugASKResponse response envelope.
     */
    @Override
    public void sendSkillResponse(final String localDebugASKResponse) {
        LOG.info("Skill response: \n" + localDebugASKResponse);
        send(new String(localDebugASKResponse.getBytes(StandardCharsets.UTF_8)
                , StandardCharsets.UTF_8));
    }

    @Override
    public void onMessage(final ByteBuffer bytes) {
        onMessage(new String(bytes.array(), StandardCharsets.UTF_8));
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * Skill invoker configuration.
         */
        private SkillInvokerConfiguration skillInvokerConfiguration;
        /**
         * Thread executor service instance.
         */
        private ExecutorService executorService;
        /**
         * Client web socket configuration.
         */
        private WebSocketClientConfig webSocketClientConfig;

        /**
         * Initializes skill invoker configuration.
         * @param skillInvokerConfiguration  skill invoker configuration.
         * @return Builder instance {@link WebSocketClientImpl.Builder}
         */
        public Builder withSkillInvokerConfiguration(final SkillInvokerConfiguration skillInvokerConfiguration) {
            this.skillInvokerConfiguration = skillInvokerConfiguration;
            return this;
        }

        /**
         * Initializes Thread executor service instance.
         * @param executorService Thread executor service instance.
         * @return Builder instance {@link WebSocketClientImpl.Builder}
         */
        public Builder withExecutorService(final ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        /**
         * Initializes client web socket configuration.
         * @param webSocketClientConfig client web socket configuration.
         * @return Builder instance {@link WebSocketClientImpl.Builder}
         */
        public Builder withWebSocketClientConfig(final WebSocketClientConfig webSocketClientConfig) {
            this.webSocketClientConfig = webSocketClientConfig;
            return this;
        }

        /**
         * Builds instance of WebSocketClientImpl.
         * @return {@link WebSocketClientImpl}.
         */
        public WebSocketClientImpl build() {
            return new WebSocketClientImpl(this);
        }
    }
}
