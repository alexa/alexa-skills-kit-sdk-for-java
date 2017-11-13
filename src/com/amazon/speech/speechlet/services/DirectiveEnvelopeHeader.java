/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.services;

public class DirectiveEnvelopeHeader {
    private String requestId = null;

    /**
     * Public constructor create an instance of DirectiveEnvelopeHeader.
     */
    public DirectiveEnvelopeHeader() {

    }

    /**
     * Returns a new builder instance used to construct a new {@code DirectiveEnvelopeHeader}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code DirectiveEnvelopeHeader} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code DirectiveEnvelopeHeader}
     */
    private DirectiveEnvelopeHeader(final Builder builder) {
        requestId = builder.requestId;
    }

    /**
     * Gets the requestId in DirectiveEnvelopeHeader.
     *
     * @return  requestId
     *
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Builder used to construct a new {@code DirectiveEnvelopeHeader}.
     */
    public static final class Builder {
        private String requestId;

        public Builder withRequestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        public DirectiveEnvelopeHeader build() {
            return new DirectiveEnvelopeHeader(this);
        }
    }
}