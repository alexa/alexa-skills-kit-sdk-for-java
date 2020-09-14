/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.loader.impl;

import com.amazon.ask.response.template.TemplateContentData;
import com.amazon.ask.util.ValidationUtils;

/**
 * Time based wrapper of {@link TemplateContentData} for {@link ConcurrentLRUTemplateCache} to manage.
 */
public class AccessOrderedTemplateContentData {

    /**
     * Template content data.
     */
    protected final TemplateContentData templateContentData;

    /**
     * Timestamp of last access of template content data.
     */
    private long accessTimestamp;

    /**
     * Constructor for AccessOrderedTemplateContentData.
     * @param templateContentData template content data.
     */
    public AccessOrderedTemplateContentData(final TemplateContentData templateContentData) {
        this.templateContentData = ValidationUtils.assertNotNull(templateContentData, "templateContentData");
        this.accessTimestamp = System.currentTimeMillis();
    }

    /**
     * Static method returns an instance of Builder class.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get access time stamp.
     * @return access time stamp.
     */
    public long getAccessTimestamp() {
        return this.accessTimestamp;
    }

    /**
     * Get template content data.
     * @return {@link TemplateContentData}.
     */
    public TemplateContentData getTemplateContentData() {
        updateTimestamp();
        return this.templateContentData;
    }

    /**
     * Updates the timestamp.
     */
    private void updateTimestamp() {
        this.accessTimestamp = System.currentTimeMillis();
    }

    /**
     * Access Ordered Template Content Data Builder.
     */
    public static final class Builder {
        /**
         * Template content data.
         */
        private TemplateContentData templateContentData;

        /**
         * Adds template content data to AccessOrderedTemplateContentData.
         * @param templateContentData template content data.
         * @return {@link Builder}.
         */
        public Builder withTemplateContentData(final TemplateContentData templateContentData) {
            this.templateContentData = templateContentData;
            return this;
        }

        /**
         * Builder method to build an instance of AccessOrderedTemplateContentData.
         * @return {@link AccessOrderedTemplateContentData}.
         */
        public AccessOrderedTemplateContentData build() {
            return new AccessOrderedTemplateContentData(templateContentData);
        }
    }

}
