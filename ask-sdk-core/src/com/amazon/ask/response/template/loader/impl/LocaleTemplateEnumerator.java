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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.response.template.loader.TemplateEnumerator;
import com.amazon.ask.util.ValidationUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Enumerate possible template name, given a locale "en-US" from the request, and a response template name "template",
 * the following combinations will be tried:
 * <ul>
 * <li>template/en/US</li>
 * <li>template/en_US</li>
 * <li>template/en</li>
 * <li>template_en_US</li>
 * <li>template_en</li>
 * <li>template</li>
 * </ul>
 */
public final class LocaleTemplateEnumerator implements TemplateEnumerator<HandlerInput> {

    private static final Logger LOGGER = getLogger(LocaleTemplateEnumerator.class);
    private static final String FILE_SEPARATOR = File.separator;
    private static final String UNDERSCORE = "_";
    private static final int NULL_LOCALE_ENUMERATION_SIZE = 1;
    private static final int NON_NULL_LOCALE_ENUMERATION_SIZE = 6;

    protected String templateName;
    protected HandlerInput handlerInput;
    private int enumerationSize;
    private Matcher matcher;
    private int cursor;

    protected LocaleTemplateEnumerator(String templateName, HandlerInput handlerInput) {
        this.templateName = ValidationUtils.assertNotNull(templateName, "templateName");
        this.handlerInput = ValidationUtils.assertNotNull(handlerInput, "handlerInput");
        this.enumerationSize = getEnumerationSize(handlerInput);
        this.cursor = 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Check whether next combination of template name is available.
     *
     * @return true if next combination of template name is available
     */
    @Override
    public boolean hasNext() {
        return cursor < enumerationSize;
    }

    /**
     * Generate the next combination of template name and return.
     *
     * @return the next combination of template name
     */
    @Override
    public String next() {
        if (hasNext()) {
            if (enumerationSize == NULL_LOCALE_ENUMERATION_SIZE) {
                cursor++;
                return templateName;
            }
            final String language = matcher.group(1);
            final String country = matcher.group(2);
            switch (cursor) {
                case 0: cursor++; return templateName + FILE_SEPARATOR + language + FILE_SEPARATOR + country;
                case 1: cursor++; return templateName + FILE_SEPARATOR + language + UNDERSCORE + country;
                case 2: cursor++; return templateName + FILE_SEPARATOR + language;
                case 3: cursor++; return templateName + UNDERSCORE + language + UNDERSCORE + country;
                case 4: cursor++; return templateName + UNDERSCORE + language;
                case 5: cursor++; return templateName;
            }
        }
        String message = "No next available template name combination.";
        LOGGER.error(message);
        throw new IllegalStateException(message);
    }

    private int getEnumerationSize(HandlerInput handlerInput) {
        String locale = handlerInput.getRequestEnvelope().getRequest().getLocale();
        if (locale == null || locale.isEmpty()) {
            return NULL_LOCALE_ENUMERATION_SIZE;
        }
        Pattern localeParser = Pattern.compile("^([a-z]{2})\\-([A-Z]{2})$");
        matcher = localeParser.matcher(locale);
        if (matcher.matches()) {
            return NON_NULL_LOCALE_ENUMERATION_SIZE;
        }
        String message = String.format("Invalid locale: %s", locale);
        LOGGER.error(message);
        throw new IllegalArgumentException(message);
    }

    public static final class Builder {
        private String templateName;
        private HandlerInput handlerInput;

        public Builder withTemplateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public Builder withHandlerInput(HandlerInput handlerInput) {
            this.handlerInput = handlerInput;
            return this;
        }

        public LocaleTemplateEnumerator build() {
            return new LocaleTemplateEnumerator(templateName, handlerInput);
        }
    }

}
