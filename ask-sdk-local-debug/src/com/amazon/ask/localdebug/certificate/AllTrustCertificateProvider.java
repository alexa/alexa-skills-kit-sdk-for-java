/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * All trust certificate provider class.
 */
public final class AllTrustCertificateProvider implements CertificateProvider {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AllTrustCertificateProvider.class);

    /**
     * Certificate trust manager.
     */
    private TrustManager[] trustManagers;

    /**
     * Constructor.
     * Sets all trust key and trust managers.
     */
    public AllTrustCertificateProvider() {
            trustManagers = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }
            }
        };
    }

    @Override
    public Optional<List<KeyManager>> getKeyManager() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<List<TrustManager>> getTrustManager() {
        return Optional.ofNullable(Collections.unmodifiableList(Arrays.asList(trustManagers)));
    }

}
