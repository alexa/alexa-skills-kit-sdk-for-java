package com.amazon.speech.speechlet.authentication;

import static java.security.Security.addProvider;
import static com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier.checkRequestSignature;
import static com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier.getAndVerifySigningCertificateChainUrl;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazon.speech.Sdk;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.security.auth.x500.X500Principal")
@PrepareForTest(SpeechletRequestSignatureVerifier.class)
public class SpeechletRequestSignatureVerifierTest {
    private static final String PREPOPULATED_CERT_URL =
            "https://s3.amazonaws.com/echo.api/doesnotexist";
    private static final String VALID_URL = "https://s3.amazonaws.com/echo.api/cert";
    private static final String VALID_URL_WITH_PORT = "https://s3.amazonaws.com:443/echo.api/cert";
    private static final String VALID_URL_WITH_PATH_TRAVERSAL =
            "https://s3.amazonaws.com/echo.api/../echo.api/cert";
    private static final String INVALID_URL_WITH_INVALID_HOST_NAME =
            "https://very.bad/echo.api/cert";
    private static final String INVALID_URL_WITH_UNSUPPORTED_PROTOCOL =
            "http://s3.amazonaws.com/echo.api/cert";
    private static final String INVALID_URL_WITH_INVALID_PORT =
            "https://s3.amazonaws.com:563/echo.api/cert";
    private static final String INVALID_URL_WITH_INVALID_PATH = "https://s3.amazonaws.com/cert";
    private static final String INVALID_URL_WITH_INVALID_PATH_TRAVERSAL =
            "https://s3.amazonaws.com/echo.api/../cert";
    private static final String INVALID_URL_WITH_INVALID_UPPER_CASE_PATH =
            "https://s3.amazonaws.com/ECHO.API/cert";
    private static final String MALFORMED_URL = "badUrl";

    private static PrivateKey validPrivateKey = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    @SuppressWarnings("deprecation")
    public static void initializeCertMap() throws Exception {
        addProvider(new BouncyCastleProvider());
        KeyPair keyPair = generateKeyPair();
        validPrivateKey = keyPair.getPrivate();

        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        X500Principal self = new X500Principal("CN=Test Certificate");
        certGen.setIssuerDN(self);
        certGen.setSubjectDN(self);
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 60000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 60000));
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm(Sdk.SIGNATURE_ALGORITHM);
        // BC means the Bouncy Castle security provider.
        X509Certificate cert = certGen.generate(validPrivateKey, "BC");

        ConcurrentHashMap<String, X509Certificate> certCache = new ConcurrentHashMap<>();
        certCache.put(PREPOPULATED_CERT_URL, cert);
        whenNew(ConcurrentHashMap.class).withAnyArguments().thenReturn(certCache);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_validUrlValue_urlReturned() throws Exception {
        assertEquals(VALID_URL, getAndVerifySigningCertificateChainUrl(VALID_URL).toExternalForm());
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_validUrlWithPort_noExceptionThrown()
            throws Exception {
        assertEquals(VALID_URL_WITH_PORT,
                getAndVerifySigningCertificateChainUrl(VALID_URL_WITH_PORT).toExternalForm());
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_validUrlWithPathTraversal_noExceptionThrown()
            throws Exception {
        assertEquals(VALID_URL,
                getAndVerifySigningCertificateChainUrl(VALID_URL_WITH_PATH_TRAVERSAL)
                        .toExternalForm());
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_invalidHostnameInUrl_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("does not contain the required hostname");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_INVALID_HOST_NAME);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_unsupportedProtocolInUrl_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("contains an unsupported protocol");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_UNSUPPORTED_PROTOCOL);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_invalidPortInUrl_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("contains an invalid port");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_INVALID_PORT);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_malformedUrl_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectCause(isA(IllegalArgumentException.class));
        thrown.expectMessage("is malformed");

        getAndVerifySigningCertificateChainUrl(MALFORMED_URL);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_invalidPath_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("Expecting path to start with");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_INVALID_PATH);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_invalidPathTraversal_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("Expecting path to start with");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_INVALID_PATH_TRAVERSAL);
    }

    @Test
    public void getAndVerifySigningCertificateChainUrl_invalidUpperCasePath_certificateExceptionThrown()
            throws Exception {
        thrown.expect(CertificateException.class);
        thrown.expectMessage("Expecting path to start with");

        getAndVerifySigningCertificateChainUrl(INVALID_URL_WITH_INVALID_UPPER_CASE_PATH);
    }

    @Test
    public void checkRequestSignature_validSignatureValidPrivateKey_noSecurityExceptionThrown()
            throws Exception {
        String testContent = "This is some test content.";
        byte[] signature = signContent(testContent, validPrivateKey);

        checkRequestSignature(testContent.getBytes(), new Base64().encodeAsString(signature),
                PREPOPULATED_CERT_URL);
    }

    @Test
    public void checkRequestSignature_validSignatureInvalidPrivateKey_securityExceptionThrown()
            throws Exception {
        thrown.expect(SecurityException.class);
        thrown
                .expectMessage("Failed to verify the signature/certificate for the provided speechlet request");

        String testContent = "This is some test content.";
        byte[] signature = signContent(testContent, generateKeyPair().getPrivate());

        checkRequestSignature(testContent.getBytes(), new Base64().encodeAsString(signature),
                PREPOPULATED_CERT_URL);
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Sdk.SIGNATURE_KEY_TYPE);
        keyPairGenerator.initialize(512);
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] signContent(String content, PrivateKey key) throws Exception {
        Signature signature = Signature.getInstance(Sdk.SIGNATURE_ALGORITHM);
        signature.initSign(key);
        signature.update(content.getBytes());
        return signature.sign();
    }
}
