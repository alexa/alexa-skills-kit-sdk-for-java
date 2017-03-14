/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

import com.amazon.speech.Sdk;

/**
 * Provides a utility method to verify the signature of a speechlet request.
 */
public final class SpeechletRequestSignatureVerifier {
    private static final Map<String, X509Certificate> CERTIFICATE_CACHE = new ConcurrentHashMap<>();
    private static final Integer DOMAIN_NAME_SUBJECT_ALTERNATIVE_NAME_ENTRY = 2;
    private static final String VALID_SIGNING_CERT_CHAIN_PROTOCOL = "https";
    private static final String VALID_SIGNING_CERT_CHAIN_URL_HOST_NAME = "s3.amazonaws.com";
    private static final String VALID_SIGNING_CERT_CHAING_URL_PATH_PREFIX = "/echo.api/";
    private static final int UNSPECIFIED_SIGNING_CERT_CHAIN_URL_PORT_VALUE = -1;

    private SpeechletRequestSignatureVerifier() {
    }

    /**
     * Verifies the certificate authenticity using the configured TrustStore and the signature of
     * the speechlet request.
     *
     * @param serializedSpeechletRequest
     *            speechlet request serialized as a string of JSON
     * @param baseEncoded64Signature
     *            the signature for provided in the request header
     * @param signingCertificateChainUrl
     *            the certificate chain URL provided in the request header
     */
    public static void checkRequestSignature(final byte[] serializedSpeechletRequest,
            final String baseEncoded64Signature, final String signingCertificateChainUrl) {
        if ((baseEncoded64Signature == null) || (signingCertificateChainUrl == null)) {
            throw new SecurityException(
                    "Missing signature/certificate for the provided speechlet request");
        }

        try {
            X509Certificate signingCertificate;
            if (CERTIFICATE_CACHE.containsKey(signingCertificateChainUrl)) {
                signingCertificate = CERTIFICATE_CACHE.get(signingCertificateChainUrl);
                /*
                 * check the before/after dates on the certificate are still valid for the present
                 * time
                 */
                signingCertificate.checkValidity();
            } else {
                signingCertificate = retrieveAndVerifyCertificateChain(signingCertificateChainUrl);

                // if certificate is valid, then add it to the cache
                CERTIFICATE_CACHE.put(signingCertificateChainUrl, signingCertificate);
            }

            // verify that the request was signed by the provided certificate
            Signature signature = Signature.getInstance(Sdk.SIGNATURE_ALGORITHM);
            signature.initVerify(signingCertificate.getPublicKey());
            signature.update(serializedSpeechletRequest);
            if (!signature.verify(Base64.decodeBase64(baseEncoded64Signature
                    .getBytes(Sdk.CHARACTER_ENCODING)))) {
                throw new SecurityException(
                        "Failed to verify the signature/certificate for the provided speechlet request");
            }
        } catch (CertificateException | SignatureException | NoSuchAlgorithmException
                | InvalidKeyException | IOException ex) {
            throw new SecurityException(
                    "Failed to verify the signature/certificate for the provided speechlet request",
                    ex);
        }
    }

    /**
     * Retrieves the certificate from the specified URL and confirms that the certificate is valid.
     *
     * @param signingCertificateChainUrl
     *            the URL to retrieve the certificate chain from
     * @return the certificate at the specified URL, if the certificate is valid
     * @throws CertificateException
     *             if the certificate cannot be retrieve or is invalid
     */
    public static X509Certificate retrieveAndVerifyCertificateChain(
            final String signingCertificateChainUrl) throws CertificateException {
        try (InputStream in =
                getAndVerifySigningCertificateChainUrl(signingCertificateChainUrl).openStream()) {
            CertificateFactory certificateFactory =
                    CertificateFactory.getInstance(Sdk.SIGNATURE_CERTIFICATE_TYPE);
            @SuppressWarnings("unchecked")
            Collection<X509Certificate> certificateChain =
                    (Collection<X509Certificate>) certificateFactory.generateCertificates(in);
            /*
             * check the before/after dates on the certificate date to confirm that it is valid on
             * the current date
             */
            X509Certificate signingCertificate = certificateChain.iterator().next();
            signingCertificate.checkValidity();

            // check the certificate chain
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                }
            }

            if (x509TrustManager == null) {
                throw new IllegalStateException(
                        "No X509 TrustManager available. Unable to check certificate chain");
            } else {
                x509TrustManager.checkServerTrusted(
                        certificateChain.toArray(new X509Certificate[certificateChain.size()]),
                        Sdk.SIGNATURE_KEY_TYPE);
            }

            /*
             * verify Echo API's hostname is specified as one of subject alternative names on the
             * signing certificate
             */
            if (!subjectAlernativeNameListContainsEchoSdkDomainName(signingCertificate
                    .getSubjectAlternativeNames())) {
                throw new CertificateException(
                        "The provided certificate is not valid for the Echo SDK");
            }

            return signingCertificate;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException ex) {
            throw new CertificateException("Unable to verify certificate at URL: "
                    + signingCertificateChainUrl, ex);
        }
    }

    private static boolean subjectAlernativeNameListContainsEchoSdkDomainName(
            final Collection<List<?>> subjectAlternativeNameEntries) {
        for (List<?> entry : subjectAlternativeNameEntries) {
            // first ensure that the subject alternative entry is in the expected form
            if (entry.get(0) instanceof Integer && entry.get(1) instanceof String) {
                /*
                 * if the entry is for a domain name and that domain name matches the domain name
                 * for the echo sdk then return true
                 */
                if (DOMAIN_NAME_SUBJECT_ALTERNATIVE_NAME_ENTRY.equals(entry.get(0))
                        && Sdk.ECHO_API_DOMAIN_NAME.equals((entry.get(1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifies the signing certificate chain URL and returns a {@code URL} object.
     *
     * @param signingCertificateChainUrl
     *            the external form of the URL
     * @return the URL
     * @throws CertificateException
     *             if the URL is malformed or contains an invalid hostname, an unsupported protocol,
     *             or an invalid port (if specified)
     */
    static URL getAndVerifySigningCertificateChainUrl(final String signingCertificateChainUrl)
            throws CertificateException {
        try {
            URL url = new URI(signingCertificateChainUrl).normalize().toURL();
            // Validate the hostname
            if (!VALID_SIGNING_CERT_CHAIN_URL_HOST_NAME.equalsIgnoreCase(url.getHost())) {
                throw new CertificateException(String.format(
                        "SigningCertificateChainUrl [%s] does not contain the required hostname"
                                + " of [%s]", signingCertificateChainUrl,
                        VALID_SIGNING_CERT_CHAIN_URL_HOST_NAME));
            }

            // Validate the path prefix
            String path = url.getPath();
            if (!path.startsWith(VALID_SIGNING_CERT_CHAING_URL_PATH_PREFIX)) {
                throw new CertificateException(String.format(
                        "SigningCertificateChainUrl path [%s] is invalid. Expecting path to "
                                + "start with [%s]", signingCertificateChainUrl,
                        VALID_SIGNING_CERT_CHAING_URL_PATH_PREFIX));
            }

            // Validate the protocol
            String urlProtocol = url.getProtocol();
            if (!VALID_SIGNING_CERT_CHAIN_PROTOCOL.equalsIgnoreCase(urlProtocol)) {
                throw new CertificateException(String.format(
                        "SigningCertificateChainUrl [%s] contains an unsupported protocol [%s]",
                        signingCertificateChainUrl, urlProtocol));
            }

            // Validate the port uses the default of 443 for HTTPS if explicitly defined in the URL
            int urlPort = url.getPort();
            if ((urlPort != UNSPECIFIED_SIGNING_CERT_CHAIN_URL_PORT_VALUE)
                    && (urlPort != url.getDefaultPort())) {
                throw new CertificateException(String.format(
                        "SigningCertificateChainUrl [%s] contains an invalid port [%d]",
                        signingCertificateChainUrl, urlPort));
            }

            return url;
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException ex) {
            throw new CertificateException(String.format(
                    "SigningCertificateChainUrl [%s] is malformed", signingCertificateChainUrl), ex);
        }
    }
}
