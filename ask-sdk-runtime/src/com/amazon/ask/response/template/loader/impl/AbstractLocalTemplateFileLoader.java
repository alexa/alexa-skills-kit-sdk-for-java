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

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@link TemplateLoader} abstract implementation to load template file from local file system, build and return {@link TemplateContentData}.
 */
public abstract class AbstractLocalTemplateFileLoader<Input> implements TemplateLoader<Input> {

    private static final Logger LOGGER = getLogger(AbstractLocalTemplateFileLoader.class);
    private static final String FILE_EXTENSION_DELIMITER = ".";

    protected final String directoryPath;
    protected final String fileExtension;
    protected final ClassLoader classLoader;
    protected final TemplateCache templateCache;
    protected final BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier;

    protected AbstractLocalTemplateFileLoader(String directoryPath, String fileExtension,
                                              ClassLoader classLoader, TemplateCache templateCache,
                                              BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier) {
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
    public Optional<TemplateContentData> load(String responseTemplateName, Input input) throws TemplateLoaderException {
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

    private String buildCompletePath(String candidate) {
        char directoryPathLastChar = directoryPath.charAt(directoryPath.length() - 1);
        if(String.valueOf(directoryPathLastChar).equals(File.separator)) {
            return this.directoryPath + candidate + FILE_EXTENSION_DELIMITER + fileExtension;
        }
        return this.directoryPath + File.separator + candidate + FILE_EXTENSION_DELIMITER + fileExtension;
    }

    private InputStream getTemplateAsStream(String templatePath) {
        return this.classLoader.getResourceAsStream(templatePath);
    }

    private URI getResourceURI(String templatePath) throws URISyntaxException {
        URL url = this.classLoader.getResource(templatePath);
        return url == null ? null : url.toURI();
    }

    public abstract static class Builder<Input, Self extends Builder<Input, Self>> {
        protected String directoryPath;
        protected String fileExtension;
        protected ClassLoader classLoader;
        protected TemplateCache templateCache;
        protected BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier;

        protected Builder() {
            this.classLoader = this.getClass().getClassLoader();
        }

        public Self withDirectoryPath(String directoryPath) {
            this.directoryPath = directoryPath;
            return (Self) this;
        }
        public Self withFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
            return (Self)this;
        }

        public Self withClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return (Self)this;
        }

        public Self withTemplateCache(TemplateCache templateCache) {
            this.templateCache = templateCache;
            return (Self)this;
        }

        public Self withTemplateEnumeratorSupplier(BiFunction<String, Input, TemplateEnumerator<Input>> templateEnumeratorSupplier) {
            this.templateEnumeratorSupplier = templateEnumeratorSupplier;
            return (Self)this;
        }

        public abstract AbstractLocalTemplateFileLoader<Input> build();
    }

}
