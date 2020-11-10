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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client configuration class for local debug.
 */
public final class ClientConfiguration {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);
    /**
     * Fully qualified name for skill entry class.
     */
    private final String skillInvokerClassName;
    /**
     * Skill id for the skill being debugged.
     */
    private final String skillId;

    /**
     * LWA access token.
     */
    private final String accessToken;

    /**
     * Thread pool size.
     */
    private final int threadPoolSize;

    /**
     * Region for debug session.
     */
    private final String region;

    /**
     * ClientConfiguration constructor.
     * @param builder builder instance {@link ClientConfiguration.Builder}.
     */
    private ClientConfiguration(final Builder builder) {
        this.skillInvokerClassName = builder.skillInvokerClassName;
        this.skillId = builder.skillId;
        this.accessToken = builder.accessToken;
        this.threadPoolSize = builder.threadPoolSize;
        this.region = builder.region;
    }

    /**
     * Creates builder instance.
     * @return new Builder instance {@link ClientConfiguration.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets fully qualified class name for the skill entry class.
     * @return fully qualified class name.
     */
    public String getSkillInvokerClassName() {
        return this.skillInvokerClassName;
    }

    /**
     * Gets skill id.
     * @return skill id.
     */
    public String getSkillId() {
        return this.skillId;
    }

    /**
     * Gets access token.
     * @return access token.
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets thread pool size.
     * @return thread pool size.
     */
    public int getThreadPoolSize() {
        return this.threadPoolSize;
    }

    /**
     * Gets the region.
     * @return region.
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * Region for the debug session.
         */
        private String region;
        /**
         * Thread pool size.
         */
        private int threadPoolSize;
        /**
         * LWA access token.
         */
        private String accessToken;
        /**
         * Fully qualified name for skill entry class.
         */
        private String skillInvokerClassName;
        /**
         * Skill id for the skill being debugged.
         */
        private String skillId;

        /**
         * Initializes class reference of the skill stream handler.
         * @param skillInvokerClassName fully qualified class name of the
         * @return Builder instance {@link ClientConfiguration.Builder}
         */
        public Builder withSkillInvokerClassName(final String skillInvokerClassName) {
            this.skillInvokerClassName = skillInvokerClassName;
            return this;
        }

        /**
         * Initializes skill id for the skill being debugged.
         * @param skillId Skill id of the skill being debugged.
         * @return Builder instance {@link ClientConfiguration.Builder}
         */
        public Builder withSkillId(final String skillId) {
            this.skillId = skillId;
            return this;
        }

        /**
         * Initializes access token for the debug session.
         * @param accessToken LWA access token.
         * @return Builder instance {@link ClientConfiguration.Builder}
         */
        public Builder withAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        /**
         * Initializes number of worker threads for the debug session.
         * @param threadPoolSize Thread pool size.
         * @return Builder instance {@link ClientConfiguration.Builder}
         */
        public Builder withThreadPoolSize(final int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        /**
         * Initializes region for the debug session.
         * @param region Thread pool size.
         * @return Builder instance {@link ClientConfiguration.Builder}
         */
        public Builder withRegion(final String region) {
            this.region = region;
            return this;
        }

        /**
         * Builds instance of ClientConfiguration.
         * @return {@link ClientConfiguration}.
         */
        public ClientConfiguration build() {
            return new ClientConfiguration(this);
        }
    }

}
