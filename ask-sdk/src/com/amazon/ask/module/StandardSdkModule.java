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
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.function.Function;

/**
 * {@link SdkModule} that registers common add-on libraries shipped with the standard SDK distribution.
 */
public class StandardSdkModule implements SdkModule {

    protected ApacheHttpApiClient apiClient;
    protected DynamoDbPersistenceAdapter persistenceAdapter;

    public StandardSdkModule(ApacheHttpApiClient apiClient, DynamoDbPersistenceAdapter persistenceAdapter) {
        this.apiClient = apiClient;
        this.persistenceAdapter = persistenceAdapter;
    }

    @Override
    public void setupModule(SdkModuleContext context) {
        context.setApiClient(apiClient);
        context.setPersistenceAdapter(persistenceAdapter);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Builder() {}

        protected CloseableHttpClient customHttpClient;
        protected AmazonDynamoDB customDynamoDBClient;
        protected String tableName;
        protected Boolean autoCreateTable;
        protected Function<RequestEnvelope, String> partitionKeyGenerator;

        public Builder withHttpClient(CloseableHttpClient customHttpClient) {
            this.customHttpClient = customHttpClient;
            return this;
        }

        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withAutoCreateTable(boolean autoCreateTable) {
            this.autoCreateTable = autoCreateTable;
            return this;
        }

        public Builder withPartitionKeyGenerator(Function<RequestEnvelope, String> partitionKeyGenerator) {
            this.partitionKeyGenerator = partitionKeyGenerator;
            return this;
        }

        public Builder withDynamoDbClient(AmazonDynamoDB customDynamoDBClient) {
            this.customDynamoDBClient = customDynamoDBClient;
            return this;
        }

        public StandardSdkModule build() {
            DynamoDbPersistenceAdapter persistenceAdapter = null;

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
            return new StandardSdkModule(apiClient, persistenceAdapter);
        }

    }

}