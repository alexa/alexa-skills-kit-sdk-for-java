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

/**
 * Client configuration class for local debug.
 */
public final class ClientConfiguration {
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
     * ClientConfiguration constructor.
     * @param builder builder instance {@link ClientConfiguration.Builder}.
     */
    private ClientConfiguration(final Builder builder) {
        this.skillInvokerClassName = builder.skillInvokerClassName;
        this.skillId = builder.skillId;
        this.accessToken = builder.accessToken;
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
     * Builder class.
     */
    public static class Builder {
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
         * Builds instance of ClientConfiguration.
         * @return {@link ClientConfiguration}.
         */
        public ClientConfiguration build() {
            return new ClientConfiguration(this);
        }
    }

}
