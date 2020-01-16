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

/**
 * Provides container for server request that should be validated.
 */
public interface AlexaHttpRequest {
    /**
     * @return the signature, base64 encoded.
     */
    String getBaseEncoded64Signature();

    /**
     * @return URL for the certificate chain needed to verify the request signature.
     */
    String getSigningCertificateChainUrl();

    /**
     * @return the request envelope, in serialized form.
     */
    byte[] getSerializedRequestEnvelope();

    /**
     * @return the request envelope, in deserialized form.
     */
    RequestEnvelope getDeserializedRequestEnvelope();
}
