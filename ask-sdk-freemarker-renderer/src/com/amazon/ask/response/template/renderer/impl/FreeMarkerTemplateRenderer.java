/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.renderer.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.exception.template.TemplateRendererException;
import com.amazon.ask.response.template.TemplateContentData;
import com.amazon.ask.response.template.renderer.TemplateRenderer;
import com.amazon.ask.util.JsonUnmarshaller;
import com.amazon.ask.util.ValidationUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@link TemplateRenderer} implementation to render a FreeMarker template, and deserialize to skill response output.
 */
public class FreeMarkerTemplateRenderer<Output> implements TemplateRenderer<Output> {

    private static final Logger LOGGER = getLogger(FreeMarkerTemplateRenderer.class);
    private static final Charset STANDARD_CHARSET = StandardCharsets.UTF_8;

    protected final Configuration configuration;
    protected final JsonUnmarshaller<Output> unmarshaller;

    protected FreeMarkerTemplateRenderer(Configuration configuration, JsonUnmarshaller<Output> unmarshaller) {
        this.configuration = configuration == null ? buildConfig() : configuration;
        this.unmarshaller = ValidationUtils.assertNotNull(unmarshaller, "unmarshaller");
    }

    private Configuration buildConfig() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_24);
        configuration.setDefaultEncoding(STANDARD_CHARSET.toString());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(true);
        return configuration;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Render {@link TemplateContentData} by processing data and deserialize to skill response output.
     *
     * @param templateContentData {@link TemplateContentData} that contains template content
     * @param dataMap map that contains injecting data
     * @return skill response output.
     * @throws TemplateRendererException if fail to render template or deserialize to skill response output.
     */
    @Override
    public Output render(TemplateContentData templateContentData,
                                   Map<String, Object> dataMap) throws TemplateRendererException {
        byte[] contentBytes = templateContentData.getTemplateContent();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(contentBytes), STANDARD_CHARSET);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(byteArrayOutputStream, STANDARD_CHARSET)) {
            updateLoadingConfig(templateContentData);
            Template template = new Template(templateContentData.getIdentifier(), reader, configuration);
            template.process(dataMap, writer);
            return unmarshaller.unmarshall(byteArrayOutputStream.toByteArray()).get().getUnmarshalledRequest();
        } catch (IOException e) {
            String exceptionMsg = "Unable to process invalid template";
            throw throwException(exceptionMsg, e, templateContentData, dataMap);
        } catch (TemplateException e) {
            String exceptionMsg = "Unable to process template";
            throw throwException(exceptionMsg, e, templateContentData, dataMap);
        } catch (AskSdkException e) {
            String exceptionMsg = "Unable to unmarshall template";
            throw throwException(exceptionMsg, e, templateContentData, dataMap);
        }
    }

    private TemplateRendererException throwException(String exceptionMessage, Exception e,
                                                     TemplateContentData templateContentData,
                                                     Map<String, Object> dataMap) {
        String exceptionMsg = exceptionMessage + " with identifier: %s using data model map: %s with error: %s ";
        if (LOGGER.isTraceEnabled()) {
            String traceMsg = String.format(exceptionMsg + "with template content data: %s.",
                    templateContentData.getIdentifier(), dataMap, e.getMessage(), templateContentData);
            LOGGER.trace(traceMsg);
        }
        return new TemplateRendererException(String.format(exceptionMessage,
                templateContentData.getIdentifier(), dataMap, e.getMessage()));
    }

    private void updateLoadingConfig(TemplateContentData templateContentData) {
        String baseDir = templateContentData.getTemplateBaseDir();
        ClassLoader classLoader = this.getClass().getClassLoader();
        configuration.setClassLoaderForTemplateLoading(classLoader, baseDir);
    }

    public static final class Builder {
        private Configuration configuration;
        private JsonUnmarshaller unmarshaller;

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withUnmarshaller(JsonUnmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
            return this;
        }

        public FreeMarkerTemplateRenderer build() {
            return new FreeMarkerTemplateRenderer(configuration, unmarshaller);
        }
    }

}
