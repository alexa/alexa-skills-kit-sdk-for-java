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
import java.nio.charset.StandardCharsets;

/**
 * Abstraction of template content using byte array with unique identifier, base directory information will be used in resolving template import.
 */
public class TemplateContentData {

    /**
     * Identifier for each template content unit.
     */
    protected final String identifier;

    /**
     * Content in a template.
     */
    protected final byte[] templateContent;

    /**
     * Template's base directory.
     */
    protected final String templateBaseDir;

    /**
     * Constructor for TemplateContentData.
     * @param identifier Identifier for each template content unit.
     * @param templateContent Content in a template.
     * @param templateBaseDir Template's base directory.
     */
    public TemplateContentData(final String identifier, final byte[] templateContent, final String templateBaseDir) {
        this.identifier = ValidationUtils.assertNotNull(identifier, "identifier");
        this.templateContent = ValidationUtils.assertNotNull(templateContent, "templateContent");
        this.templateBaseDir = ValidationUtils.assertNotNull(templateBaseDir, "templateBaseDir");
    }

    /**
     * Static method returns an instance of Builder class.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get identifier.
     * @return identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get template content.
     * @return template content.
     */
    public byte[] getTemplateContent() {
        return templateContent;
    }

    /**
     * Get template base directory.
     * @return template base directory.
     */
    public String getTemplateBaseDir() {
        return templateBaseDir;
    }

    /**
     * Return string representation of TemplateContentData.
     * @return template content data.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TemplateContentData {\n");
        sb.append("    templateContent: ").append(this.toIndentedString(new String(this.templateContent, StandardCharsets.UTF_8))).append("\n");
        sb.append("    templateBaseDir: ").append(this.toIndentedString(this.templateBaseDir)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Get an object as an indented string.
     * @param o Input object.
     * @return input object as an indented string.
     */
    private String toIndentedString(final Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    /**
     * Template Content Data Builder.
     */
    public static final class Builder {

        /**
         * Identifier for each template content unit.
         */
        private String identifier;

        /**
         * Content in a template.
         */
        private byte[] templateContent;

        /**
         * Template's base directory.
         */
        private String templateBaseDir;

        /**
         * Adds identifier to template content data.
         * @param identifier Identifier for each template content unit.
         * @return {@link Builder}.
         */
        public Builder withIdentifier(final String identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Adds template content to template content data.
         * @param templateContent Content in a template.
         * @return {@link Builder}.
         */
        public Builder withTemplateContent(final byte[] templateContent) {
            this.templateContent = templateContent;
            return this;
        }

        /**
         * Adds template's base directory to template content data.
         * @param templateBaseDir Template's base directory.
         * @return {@link Builder}.
         */
        public Builder withTemplateBaseDir(final String templateBaseDir) {
            this.templateBaseDir = templateBaseDir;
            return this;
        }

        /**
         * Builder method to build an instance of TemplateContentData.
         * @return {@link TemplateContentData}.
         */
        public TemplateContentData build() {
            return new TemplateContentData(identifier, templateContent, templateBaseDir);
        }
    }

}
