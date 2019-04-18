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

    protected final TemplateContentData templateContentData;

    private long accessTimestamp;

    public AccessOrderedTemplateContentData(TemplateContentData templateContentData) {
        this.templateContentData = ValidationUtils.assertNotNull(templateContentData, "templateContentData");
        this.accessTimestamp = System.currentTimeMillis();
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getAccessTimestamp() {
        return this.accessTimestamp;
    }

    public TemplateContentData getTemplateContentData() {
        updateTimestamp();
        return this.templateContentData;
    }

    private void updateTimestamp() {
        this.accessTimestamp = System.currentTimeMillis();
    }

    public static final class Builder {
        private TemplateContentData templateContentData;

        public Builder withTemplateContentData(TemplateContentData templateContentData) {
            this.templateContentData = templateContentData;
            return this;
        }

        public AccessOrderedTemplateContentData build() {
            return new AccessOrderedTemplateContentData(templateContentData);
        }
    }

}
