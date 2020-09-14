/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.module;

import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.template.TemplateFactory;
import com.amazon.ask.response.template.impl.BaseTemplateFactory;
import com.amazon.ask.response.template.loader.TemplateLoader;
import com.amazon.ask.response.template.loader.impl.LocalTemplateFileLoader;
import com.amazon.ask.response.template.renderer.TemplateRenderer;
import com.amazon.ask.response.template.renderer.impl.FreeMarkerTemplateRenderer;
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazon.ask.util.impl.JacksonJsonUnmarshaller;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.function.Function;

/**
 * {@link SdkModule} that registers common add-on libraries shipped with the standard SDK distribution.
 */
public class StandardSdkModule implements SdkModule {

    /**
     * Apache HTTP client backed implementation of the ASK Java SDK API Client.
     */
    protected ApacheHttpApiClient apiClient;

    /**
     * Persistence adapter for storing skill persistence attributes in Amazon DynamoDB.
     */
    protected DynamoDbPersistenceAdapter persistenceAdapter;

    /**
     * Template Factory interface to process template and data to generate skill response.
     */
    protected TemplateFactory templateFactory;


    /**
     * Constructs an instance of {@link StandardSdkModule} with given {@link ApacheHttpApiClient}, {@link DynamoDbPersistenceAdapter}
     * and {@link TemplateFactory}.
     * @param apiClient ApacheHttpApiClient
     * @param persistenceAdapter DynamoDbPersistenceAdapter
     * @param templateFactory TemplateFactory
     */
    public StandardSdkModule(final ApacheHttpApiClient apiClient, final DynamoDbPersistenceAdapter persistenceAdapter,
                             final TemplateFactory templateFactory) {
        this.apiClient = apiClient;
        this.persistenceAdapter = persistenceAdapter;
        this.templateFactory = templateFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupModule(final SdkModuleContext context) {
        context.setApiClient(apiClient);
        context.setPersistenceAdapter(persistenceAdapter);
        context.setTemplateFactory(templateFactory);
    }

    /**
     * Returns an instance of {@link Builder}.
     * @return Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Standard SDK Module Builder.
     */
    public static final class Builder {

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Custom HTTP client.
         */
        protected CloseableHttpClient customHttpClient;

        /**
         * Custom DynamoDB client.
         */
        protected AmazonDynamoDB customDynamoDBClient;

        /**
         * DynamoDB table name.
         */
        protected String tableName;

        /**
         * Boolean value to specify auto creation of a DynamoDB table.
         */
        protected Boolean autoCreateTable;

        /**
         * Partition Key Generator, the key of which is used as input for a hash function to determine storage.
         */
        protected Function<RequestEnvelope, String> partitionKeyGenerator;

        /**
         *  Directory path where the templates are stored.
         */
        protected String templateDirectoryPath;

        /**
         * Allows the user to configure custom HTTP client.
         * @param customHttpClient which extends CloseableHttpClient.
         * @return {@link Builder}
         */
        public Builder withHttpClient(final CloseableHttpClient customHttpClient) {
            this.customHttpClient = customHttpClient;
            return this;
        }

        /**
         * Allows the user to configure a name for the DynamoDB table.
         * @param tableName name for the DynamoDB table.
         * @return {@link Builder}
         */
        public Builder withTableName(final String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Allows SDK to create a table if the table doesn't exist.
         * @param autoCreateTable boolean. If set to true, allows SDK to auto create table.
         * @return {@link Builder}
         */
        public Builder withAutoCreateTable(final boolean autoCreateTable) {
            this.autoCreateTable = autoCreateTable;
            return this;
        }

        /**
         * Allows the user to specify a partition key generator.
         * @param partitionKeyGenerator DynamoDB uses the partition key as input for a hash function to determine storage.
         * @return {@link Builder}
         */
        public Builder withPartitionKeyGenerator(final Function<RequestEnvelope, String> partitionKeyGenerator) {
            this.partitionKeyGenerator = partitionKeyGenerator;
            return this;
        }

        /**
         * Allows the user to specify a custom DynamoDB client.
         * @param customDynamoDBClient should implement {@link AmazonDynamoDB}.
         * @return {@link Builder}
         */
        public Builder withDynamoDbClient(final AmazonDynamoDB customDynamoDBClient) {
            this.customDynamoDBClient = customDynamoDBClient;
            return this;
        }

        /**
         * Allows the user to specify directory path where the templates are stored.
         * @param templateDirectoryPath string.
         * @return {@link Builder}
         */
        public Builder withTemplateDirectoryPath(final String templateDirectoryPath) {
            this.templateDirectoryPath = templateDirectoryPath;
            return this;
        }

        /**
         * Returns a new {@link StandardSdkModule} instance.
         * @return StandardSdkModule.
         */
        public StandardSdkModule build() {
            DynamoDbPersistenceAdapter persistenceAdapter = null;
            TemplateFactory templateFactory = null;

            if (tableName != null) {
                DynamoDbPersistenceAdapter.Builder persistenceAdapterBuilder = DynamoDbPersistenceAdapter.builder()
                        .withTableName(tableName);
                if (autoCreateTable != null) {
                    persistenceAdapterBuilder.withAutoCreateTable(autoCreateTable);
                }
                if (partitionKeyGenerator != null) {
                    persistenceAdapterBuilder.withPartitionKeyGenerator(partitionKeyGenerator);
                }
                if (customDynamoDBClient != null) {
                    persistenceAdapterBuilder.withDynamoDbClient(customDynamoDBClient);
                }
                persistenceAdapter = persistenceAdapterBuilder.build();
            }

            ApacheHttpApiClient apiClient = ApacheHttpApiClient.custom()
                    .withHttpClient(customHttpClient != null ? customHttpClient : HttpClients.createDefault())
                    .build();

            if (templateDirectoryPath != null) {
                final TemplateLoader loader = LocalTemplateFileLoader.builder()
                        .withDirectoryPath(templateDirectoryPath)
                        .withFileExtension("ftl")
                        .build();
                final JacksonJsonUnmarshaller jacksonJsonUnmarshaller = JacksonJsonUnmarshaller.withTypeBinding(Response.class);
                final TemplateRenderer renderer = FreeMarkerTemplateRenderer.builder().withUnmarshaller(jacksonJsonUnmarshaller).build();
                templateFactory = BaseTemplateFactory.builder().addTemplateLoader(loader).withTemplateRenderer(renderer).build();
            }

            return new StandardSdkModule(apiClient, persistenceAdapter, templateFactory);
        }

    }

}
