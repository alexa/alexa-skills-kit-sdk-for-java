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
import com.amazon.ask.module.StandardSdkModule;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.services.ApacheHttpApiClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.function.Function;

/**
 * Builder used to construct a new {@link Skill} using the default {@link DynamoDbPersistenceAdapter} and {@link ApacheHttpApiClient}.
 */
public class StandardSkillBuilder extends SkillBuilder<StandardSkillBuilder> {

    private StandardSdkModule.Builder standardSdkModuleBuilder = StandardSdkModule.builder();

    public StandardSkillBuilder withHttpClient(CloseableHttpClient customHttpClient) {
        standardSdkModuleBuilder.withHttpClient(customHttpClient);
        return this;
    }

    public StandardSkillBuilder withTableName(String tableName) {
        standardSdkModuleBuilder.withTableName(tableName);
        return this;
    }

    public StandardSkillBuilder withAutoCreateTable(boolean autoCreateTable) {
        standardSdkModuleBuilder.withAutoCreateTable(autoCreateTable);
        return this;
    }

    public StandardSkillBuilder withPartitionKeyGenerator(Function<RequestEnvelope, String> partitionKeyGenerator) {
        standardSdkModuleBuilder.withPartitionKeyGenerator(partitionKeyGenerator);
        return this;
    }

    public StandardSkillBuilder withDynamoDbClient(AmazonDynamoDB customDynamoDBClient) {
        standardSdkModuleBuilder.withDynamoDbClient(customDynamoDBClient);
        return this;
    }

    public StandardSkillBuilder withTemplateDirectoryPath(String templateDirectoryPath) {
        standardSdkModuleBuilder.withTemplateDirectoryPath(templateDirectoryPath);
        return this;
    }

    protected SkillConfiguration.Builder getConfigBuilder() {
        registerSdkModule(standardSdkModuleBuilder.build());
        return super.getConfigBuilder();
    }

    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}
