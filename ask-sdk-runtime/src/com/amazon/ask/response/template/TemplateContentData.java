/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template;

import com.amazon.ask.util.ValidationUtils;

/**
 * Abstraction of template content using byte array with unique identifier, base directory information will be used in resolving template import.
 */
public class TemplateContentData {

    protected final String identifier;
    protected final byte[] templateContent;
    protected final String templateBaseDir;

    public TemplateContentData(String identifier, byte[] templateContent, String templateBaseDir) {
        this.identifier = ValidationUtils.assertNotNull(identifier, "identifier");
        this.templateContent = ValidationUtils.assertNotNull(templateContent, "templateContent");
        this.templateBaseDir = ValidationUtils.assertNotNull(templateBaseDir, "templateBaseDir");
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getIdentifier() {
        return identifier;
    }

    public byte[] getTemplateContent() {
        return templateContent;
    }

    public String getTemplateBaseDir() {
        return templateBaseDir;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TemplateContentData {\n");
        sb.append("    templateContent: ").append(this.toIndentedString(new String(this.templateContent))).append("\n");
        sb.append("    templateBaseDir: ").append(this.toIndentedString(this.templateBaseDir)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static final class Builder {
        private String identifier;
        private byte[] templateContent;
        private String templateBaseDir;

        public Builder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder withTemplateContent(byte[] templateContent) {
            this.templateContent = templateContent;
            return this;
        }

        public Builder withTemplateBaseDir(String templateBaseDir) {
            this.templateBaseDir = templateBaseDir;
            return this;
        }

        public TemplateContentData build() {
            return new TemplateContentData(identifier, templateContent, templateBaseDir);
        }
    }

}
