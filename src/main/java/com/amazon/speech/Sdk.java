/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech;

/**
 * Constants for the Alexa Skills Kit.
 */
public final class Sdk {
    /**
     * The version number for this library.
     */
    public static final String VERSION = "1.0";

    /**
     * The character encoding used.
     */
    public static final String CHARACTER_ENCODING = "UTF-8";

    /**
     * The name of the request header that contains the signature.
     */
    public static final String SIGNATURE_REQUEST_HEADER = "Signature";

    /**
     * The name of the request header that contains the URL for the certificate chain needed to
     * verify the request signature.
     */
    public static final String SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER =
            "SignatureCertChainUrl";

    /**
     * The format of the certificate needed to verify the request signature.
     */
    public static final String SIGNATURE_CERTIFICATE_TYPE = "X.509";

    /**
     * The algorithm used to generate the signature.
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * The type of encryption key used to generate the signature.
     */
    public static final String SIGNATURE_KEY_TYPE = "RSA";

    /**
     * The domain name used by the Alexa Skills Kit API.
     */
    public static final String ECHO_API_DOMAIN_NAME = "echo-api.amazon.com";

    /**
     * Supported protocols for connections between the Alexa service and SpeechletServlets.
     */
    public static final String[] SUPPORTED_PROTOCOLS = new String[] {
            "TLSv1",
            "TLSv1.1",
            "TLSv1.2"
    };

    /**
     * Supported cipher suites for connections between the Alexa service and SpeechletServlets.
     */
    public static final String[] SUPPORTED_CIPHER_SUITES = new String[] {
            "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_RSA_WITH_RC4_128_SHA",

            "TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDH_ECDSA_WITH_RC4_128_SHA",

            "TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDH_RSA_WITH_RC4_128_SHA",

            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",

            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_RSA_WITH_RC4_128_SHA",

            "TLS_EMPTY_RENEGOTIATION_INFO_SCSV",

            "TLS_RSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_WITH_AES_256_CBC_SHA",
            "TLS_RSA_WITH_AES_256_CBC_SHA256",
    };

    /**
     * <p>
     * The {@link com.amazon.speech.speechlet.servlet.SpeechletServlet} class enforces request
     * signing to ensure that all requests processed by your web service are legitimate and come
     * from the Alexa service. In order to test your service with a tool like {@code curl}, you need
     * to disable this mechanism. This is controlled by the
     * {@code com.amazon.speech.speechlet.servlet.disableRequestSignatureCheck} system property,
     * which is defined by this constant.
     * </p>
     *
     * <p>
     * If you are starting your service from Eclipse, add the following to the VM arguments for the
     * run configuration of Launcher:
     * </p>
     * {@code -Dcom.amazon.speech.speechlet.servlet.disableRequestSignatureCheck=true}
     *
     * <p>
     * If you are starting your service using an Ant script, add the following property within the
     * {@code <java>} tag of your script:
     *
     * <pre>
     * {@code
     * <java classname="Launcher" classpathref="java.sdk.classpath" fork="true">
     *     <sysproperty key="com.amazon.speech.speechlet.servlet.disableRequestSignatureCheck"
     *                  value="true" />
     * </java>
     * }
     * </pre>
     *
     * <p>
     * Disabling request signing verification is only acceptable for developing and testing. Your
     * production system should always verify incoming requests.
     * </p>
     */
    public static final String DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY =
            "com.amazon.speech.speechlet.servlet.disableRequestSignatureCheck";

    /**
     * <p>
     * The name of the system property that can be used to provide a comma-delimited list of
     * application IDs that this web service supports. The
     * {@link com.amazon.speech.speechlet.servlet.SpeechletServlet} uses these identifiers to verify
     * that incoming requests are indeed associated with one of the configured application IDs.
     * Otherwise, the requests are rejected. By default, if this system property is missing or
     * consists of a blank string, any application ID (including null values) is accepted.
     * </p>
     *
     * <p>
     * If your web service is expected to support two Alexa skills, with application IDs
     * {@code amzn1.echo-sdk-ams.app.1234} and {@code amzn1.echo-sdk-ams.app.5678}, you should
     * configure Eclipse by adding the following VM argument to the run configuration of the
     * {@code Launcher}:
     * </p>
     * {@code -Dcom.amazon.speech.speechlet.servlet.supportedApplicationIds=amzn1.echo-sdk-ams.app.1234,amzn1.echo-sdk-ams.app.5678}
     *
     *
     * <p>
     * If you are starting your service using an Ant script, add the following property within the
     * {@code <java>} tag of your script:
     *
     * <pre>
     * {@code
     * <java classname="Launcher" classpathref="java.sdk.classpath" fork="true">
     *     <sysproperty key="com.amazon.speech.speechlet.servlet.supportedApplicationIds"
     *                  value="amzn1.echo-sdk-ams.app.1234,amzn1.echo-sdk-ams.app.5678" />
     * </java>
     * }
     * </pre>
     */
    public static final String SUPPORTED_APPLICATION_IDS_SYSTEM_PROPERTY =
            "com.amazon.speech.speechlet.servlet.supportedApplicationIds";

    /**
     * <p>
     * The name of the system property that can be used to configure the timestamp tolerance (in
     * seconds) of the {@link com.amazon.speech.speechlet.servlet.SpeechletServlet}. Requests with
     * timestamps outside of this inclusive tolerance range, either in the past or future, are
     * rejected. If this property is missing or cannot be parsed as a {@code long} value, timestamp
     * verification is disabled.
     * </p>
     *
     * <p>
     * If you are starting your service from Eclipse, add the following to the VM arguments for the
     * run configuration of Launcher to configure a tolerance of 150 seconds:
     * </p>
     * {@code -Dcom.amazon.speech.speechlet.servlet.timestampTolerance=150}
     *
     * <p>
     * If you are starting your service using an Ant script, add the following property within the
     * {@code <java>} tag of your script:
     *
     * <pre>
     * {@code
     * <java classname="Launcher" classpathref="java.sdk.classpath" fork="true">
     *     <sysproperty key="com.amazon.speech.speechlet.servlet.timestampTolerance"
     *                  value="150" />
     * </java>
     * }
     * </pre>
     *
     * <p>
     * Disabling timestamp verification is only acceptable for developing and testing. Your
     * production system should always verify timestamps on incoming requests. We permit a tolerance
     * of two minutes and thirty seconds (150 seconds).
     * </p>
     */
    public static final String TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY =
            "com.amazon.speech.speechlet.servlet.timestampTolerance";

    private Sdk() {
    }
}
