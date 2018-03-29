/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.builder;

import com.amazon.ask.Skill;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.function.Function;

/**
 * Builder used to construct a new {@link Skill} using the default {@link DynamoDbPersistenceAdapter} and {@link ApacheHttpApiClient}.
 */
public class StandardSkillBuilder extends SkillBuilder<StandardSkillBuilder> {

    protected CloseableHttpClient customHttpClient;
    protected AmazonDynamoDB customDynamoDBClient;
    protected String tableName;
    protected Boolean autoCreateTable;
    protected Function<RequestEnvelope, String> partitionKeyGenerator;

    public StandardSkillBuilder withHttpClient(CloseableHttpClient customHttpClient) {
        this.customHttpClient = customHttpClient;
        return this;
    }

    public StandardSkillBuilder withTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public StandardSkillBuilder withAutoCreateTable(boolean autoCreateTable) {
        this.autoCreateTable = autoCreateTable;
        return this;
    }

    public StandardSkillBuilder withPartitionKeyGenerator(Function<RequestEnvelope, String> partitionKeyGenerator) {
        this.partitionKeyGenerator = partitionKeyGenerator;
        return this;
    }

    public StandardSkillBuilder withDynamoDbClient(AmazonDynamoDB customDynamoDBClient) {
        this.customDynamoDBClient = customDynamoDBClient;
        return this;
    }

    protected SkillConfiguration.Builder getConfigBuilder() {
        PersistenceAdapter persistenceAdapter = null;

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

        ApiClient apiClient = ApacheHttpApiClient.custom()
                .withHttpClient(customHttpClient != null ? customHttpClient : HttpClients.createDefault())
                .build();

        return super.getConfigBuilder()
                .withApiClient(apiClient)
                .withPersistenceAdapter(persistenceAdapter);
    }

    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}
