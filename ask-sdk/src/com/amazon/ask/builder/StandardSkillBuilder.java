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
import com.amazon.ask.model.RequestEnvelope;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.function.Function;

/**
 * Builder used to construct a new {@link Skill} using the default {@link com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter}
 * and {@link com.amazon.ask.services.ApacheHttpApiClient}.
 */
public class StandardSkillBuilder extends SkillBuilder<StandardSkillBuilder> {

    /**
     * Stores an instance of Standard SDK module's builder.
     */
    private StandardSdkModule.Builder standardSdkModuleBuilder = StandardSdkModule.builder();

    /**
     * Allows the user to configure custom HTTP client.
     * @param customHttpClient which extends CloseableHttpClient.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withHttpClient(final CloseableHttpClient customHttpClient) {
        standardSdkModuleBuilder.withHttpClient(customHttpClient);
        return this;
    }

    /**
     * Allows the user to configure a name for the DynamoDB table.
     * @param tableName name for the DynamoDB table.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withTableName(final String tableName) {
        standardSdkModuleBuilder.withTableName(tableName);
        return this;
    }

    /**
     * Allows SDK to create a table if the table doesn't exist.
     * @param autoCreateTable if set to true, allows SDK to auto create table.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withAutoCreateTable(final boolean autoCreateTable) {
        standardSdkModuleBuilder.withAutoCreateTable(autoCreateTable);
        return this;
    }

    /**
     * Allows the user to specify a partition key generator.
     * @param partitionKeyGenerator DynamoDB uses the partition key as input for a hash function to determine storage.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withPartitionKeyGenerator(final Function<RequestEnvelope, String> partitionKeyGenerator) {
        standardSdkModuleBuilder.withPartitionKeyGenerator(partitionKeyGenerator);
        return this;
    }

    /**
     * Allows the user to specify a custom DynamoDB client.
     * @param customDynamoDBClient should implement {@link DynamoDbClient}.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withDynamoDbClient(final DynamoDbClient customDynamoDBClient) {
        standardSdkModuleBuilder.withDynamoDbClient(customDynamoDBClient);
        return this;
    }

    /**
     * Allows the user to specify directory path where the templates are stored.
     * @param templateDirectoryPath string.
     * @return {@link StandardSkillBuilder}
     */
    public StandardSkillBuilder withTemplateDirectoryPath(final String templateDirectoryPath) {
        standardSdkModuleBuilder.withTemplateDirectoryPath(templateDirectoryPath);
        return this;
    }

    /**
     * Returns instance of {@link SkillConfiguration} Builder.
     * @return SkillConfiguration.Builder
     */
    protected SkillConfiguration.Builder getConfigBuilder() {
        registerSdkModule(standardSdkModuleBuilder.build());
        return super.getConfigBuilder();
    }

    /**
     * Returns a new {@link Skill} instance.
     * @return Skill.
     */
    public Skill build() {
        return new Skill(getConfigBuilder().build());
    }

}
