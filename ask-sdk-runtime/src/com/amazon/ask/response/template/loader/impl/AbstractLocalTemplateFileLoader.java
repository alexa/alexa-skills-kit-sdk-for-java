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

import static org.slf4j.LoggerFactory.getLogger;

import com.amazon.ask.exception.template.TemplateLoaderException;
import com.amazon.ask.response.template.TemplateContentData;
import com.amazon.ask.response.template.loader.TemplateCache;
import com.amazon.ask.response.template.loader.TemplateEnumerator;
import com.amazon.ask.response.template.loader.TemplateLoader;
import com.amazon.ask.util.ValidationUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * {@link TemplateLoader} abstract implementation to load template file from local file system, build and return {@link TemplateContentData}.
 * @param <Input> Skill input type.
 */
public abstract class AbstractLocalTemplateFileLoader<Input> implements TemplateLoader<Input> {
    /**
     * Logger for logging information for debugging purposes.
     */
    private static final Logger LOGGER = getLogger(AbstractLocalTemplateFileLoader.class);

    /**
     * File extension delimiter.
     */
    private static final String FILE_EXTENSION_DELIMITER = ".";

    /**
     * Templates' directory path.
     */
    protected final String directoryPath;

    /**
     * Type of templates is determined by this file extension.
     */
    protected final String fileExtension;

    /**
     * Loads classes.
     */
    protected final ClassLoader classLoader;

    /**
     * Caches the template content.
     */
    protected final TemplateCache templateCache;

    /**
     * Template enumerator supplier.
     */
    protected final BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier;

    /**
     * Constructor for AbstractLocalTemplateFileLoader.
     * @param directoryPath Templates' directory path.
     * @param fileExtension Type of templates is determined by this file extension.
     * @param classLoader Loads classes.
     * @param templateCache Caches the template content.
     * @param templateEnumeratorSupplier Template enumerator supplier.
     */
    protected AbstractLocalTemplateFileLoader(final String directoryPath, final String fileExtension,
                                              final ClassLoader classLoader, final TemplateCache templateCache,
                                              final BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier) {
        this.directoryPath = ValidationUtils.assertNotNull(directoryPath, "directoryPath");
        this.fileExtension = ValidationUtils.assertNotNull(fileExtension, "fileExtension");
        this.classLoader = ValidationUtils.assertNotNull(classLoader, "classLoader");
        this.templateCache = templateCache == null ? ConcurrentLRUTemplateCache.builder().build() : templateCache;
        this.templateEnumeratorSupplier = ValidationUtils.assertNotNull(templateEnumeratorSupplier, "templateEnumeratorSupplier");
    }

    /**
     * {@inheritDoc}
     *
     * Load template file content from local file system, using {@link TemplateEnumerator} and {@link TemplateCache}.
     */
    @Override
    public Optional<TemplateContentData> load(final String responseTemplateName, final Input input) throws TemplateLoaderException {
        TemplateEnumerator templateEnumerator = templateEnumeratorSupplier.apply(responseTemplateName, input);
        while (templateEnumerator.hasNext()) {
            String templateName = (String) templateEnumerator.next();
            String templatePath = buildCompletePath(templateName);
            try {
                URI templateUri = getResourceURI(templatePath);
                if (templateUri != null) {
                    String templateIdentifier = templateUri.toString();
                    TemplateContentData templateContentData = templateCache.get(templateIdentifier);
                    if (templateContentData == null) {
                        try (InputStream inputStream = getTemplateAsStream(templatePath)) {
                            byte[] templateByteArray = new byte[inputStream.available()];
                            inputStream.read(templateByteArray);
                            templateContentData = TemplateContentData.builder()
                                    .withIdentifier(templateIdentifier)
                                    .withTemplateContent(templateByteArray)
                                    .withTemplateBaseDir(directoryPath)
                                    .build();
                        } catch (IOException e) {
                            String message = String.format("Fail to read template file: %s with error: %s", templatePath, e.getMessage());
                            LOGGER.error(message);
                            throw new TemplateLoaderException(message);
                        }
                        templateCache.put(templateIdentifier, templateContentData);
                    }
                    return Optional.of(templateContentData);
                }
            } catch (URISyntaxException e) {
                String message = String.format("Cannot get valid URI for template file path: %s with error: %s", templatePath, e.getMessage());
                LOGGER.error(message);
                throw new TemplateLoaderException(message);
            }
        }
        String message = String.format("Cannot find template file: %s given directory path: %s and file extension: %s, returning empty.",
                responseTemplateName, directoryPath, fileExtension);
        LOGGER.warn(message);
        return Optional.empty();
    }

    /**
     * Builds complete directory path.
     * @param candidate template file name.
     * @return complete path.
     */
    private String buildCompletePath(final String candidate) {
        char directoryPathLastChar = directoryPath.charAt(directoryPath.length() - 1);
        if (String.valueOf(directoryPathLastChar).equals(File.separator)) {
            return this.directoryPath + candidate + FILE_EXTENSION_DELIMITER + fileExtension;
        }
        return this.directoryPath + File.separator + candidate + FILE_EXTENSION_DELIMITER + fileExtension;
    }

    /**
     * Returns template content as InputStream.
     * @param templatePath full template path.
     * @return input stream of read content.
     */
    private InputStream getTemplateAsStream(final String templatePath) {
        return this.classLoader.getResourceAsStream(templatePath);
    }

    /**
     * Retrieve resource URI.
     * @param templatePath template path.
     * @return resource URI.
     * @throws URISyntaxException if unable to get resource.
     */
    private URI getResourceURI(final String templatePath) throws URISyntaxException {
        URL url = this.classLoader.getResource(templatePath);
        return url == null ? null : url.toURI();
    }

    /**
     * Abstract Local Template File Loader Builder.
     * @param <Input> Skill input type.
     * @param <Self> of type Builder.
     */
    public abstract static class Builder<Input, Self extends Builder<Input, Self>> {
        /**
         * Templates' directory path.
         */
        protected String directoryPath;

        /**
         * Type of templates is determined by this file extension.
         */
        protected String fileExtension;

        /**
         * Loads classes.
         */
        protected ClassLoader classLoader;

        /**
         * Caches the template content.
         */
        protected TemplateCache templateCache;

        /**
         * Template enumerator supplier.
         */
        protected BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier;

        /**
         * Constructor for Builder class.
         */
        protected Builder() {
            this.classLoader = this.getClass().getClassLoader();
        }

        /**
         * Adds template directory path to TemplateFileLoader.
         * @param directoryPath directory path.
         * @return {@link Builder}.
         */
        public Self withDirectoryPath(final String directoryPath) {
            this.directoryPath = directoryPath;
            return (Self) this;
        }

        /**
         * Adds fileExtension to TemplateFileLoader.
         * @param fileExtension template file extension.
         * @return {@link Builder}.
         */
        public Self withFileExtension(final String fileExtension) {
            this.fileExtension = fileExtension;
            return (Self) this;
        }

        /**
         * Adds class loader to TemplateFileLoader.
         * @param classLoader Loads classes.
         * @return {@link Builder}.
         */
        public Self withClassLoader(final ClassLoader classLoader) {
            this.classLoader = classLoader;
            return (Self) this;
        }

        /**
         * Adds template cache to TemplateFileLoader.
         * @param templateCache template cache.
         * @return {@link Builder}.
         */
        public Self withTemplateCache(final TemplateCache templateCache) {
            this.templateCache = templateCache;
            return (Self) this;
        }

        /**
         * Adds template enumerator supplier. to TemplateFileLoader.
         * @param templateEnumeratorSupplier template enumerator supplier.
         * @return {@link Builder}.
         */
        public Self withTemplateEnumeratorSupplier(final BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier) {
            this.templateEnumeratorSupplier = templateEnumeratorSupplier;
            return (Self) this;
        }

        /**
         * Builder method to build AbstractLocalTemplateFileLoader with the provided config.
         * @return {@link AbstractLocalTemplateFileLoader}.
         */
        public abstract AbstractLocalTemplateFileLoader<Input> build();
    }

}
