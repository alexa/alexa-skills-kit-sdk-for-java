/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.servlet.verifiers;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.servlet.ServletConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * Servlet specific implementation of {@link ServerRequest}.
 */
public class ServletRequest implements ServerRequest {

    private final byte[] serializedRequestEnvelope;
    private final RequestEnvelope deserializedRequestEnvelope;
    private final String baseEncoded64Signature;
    private final String signingCertificateChainUrl;

    public ServletRequest(final HttpServletRequest httpServletRequest, final byte[] serializedRequestEnvelope, final RequestEnvelope deserializedRequestEnvelope) {
        this.serializedRequestEnvelope = serializedRequestEnvelope;
        this.deserializedRequestEnvelope = deserializedRequestEnvelope;
        this.baseEncoded64Signature = httpServletRequest.getHeader(ServletConstants.SIGNATURE_REQUEST_HEADER);
        this.signingCertificateChainUrl = httpServletRequest.getHeader(ServletConstants.SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseEncoded64Signature() {
        return baseEncoded64Signature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSigningCertificateChainUrl() {
        return signingCertificateChainUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getSerializedRequestEnvelope() {
        return serializedRequestEnvelope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestEnvelope getDeserializedRequestEnvelope() {
        return deserializedRequestEnvelope;
    }
}
