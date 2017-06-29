/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.videoapp;

import com.amazon.speech.speechlet.Interface;

/**
 * Represents the presence of VideoApp capabilities of the Alexa client.
 */
public class VideoAppInterface extends Interface {
    /**
     * Returns a new builder instance used to construct a new {@code VideoAppInterface}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Private constructor to return a new {@code VideoAppInterface} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code VideoAppInterface}
     */
    private VideoAppInterface(final Builder builder) {
    }

    /**
     * Private constructor to create a new {@code VideoAppInterface}; used for JSON serialization
     * and for extending this class.
     */
    private VideoAppInterface() {
    }

    /**
     * Builder used to construct a new {@code VideoAppInterface}.
     */
    public static final class Builder {
        private Builder() {
        }

        public VideoAppInterface build() {
            return new VideoAppInterface(this);
        }
    }
}
