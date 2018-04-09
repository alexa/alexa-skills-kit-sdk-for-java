/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.persistence.impl;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.util.ValidationUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Persistence adapter for storing skill persistence attributes in Amazon DynamoDB.
 */
public final class DynamoDbPersistenceAdapter implements PersistenceAdapter {

    private final AmazonDynamoDB dynamoDb;
    private final String tableName;
    private final String partitionKeyName;
    private final String attributesKeyName;
    private final Function<RequestEnvelope, String> partitionKeyGenerator;
    private boolean createTable;

    private static final String DEFAULT_PARTITION_KEY_NAME = "id";
    private static final String DEFAULT_ATTRIBUTES_KEY_NAME = "attributes";
    private static final boolean DEFAULT_AUTO_CREATE_TABLE = false;
    private static final Function<RequestEnvelope, String> DEFAULT_PARTITION_KEY_GENERATOR = PartitionKeyGenerators.userId();

    private DynamoDbPersistenceAdapter(Builder builder) {
        this.tableName = ValidationUtils.assertStringNotEmpty(builder.tableName, "table name");
        this.dynamoDb = builder.dynamoDb != null ? builder.dynamoDb : AmazonDynamoDBClientBuilder.standard().build();
        this.partitionKeyName = builder.partitionKeyName;
        this.attributesKeyName = builder.attributesKeyName;
        this.partitionKeyGenerator = builder.partitionKeyGenerator;
        this.createTable = builder.autoCreateTable;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Optional<Map<String, Object>> getAttributes(RequestEnvelope envelope) throws PersistenceException {
        if (!createTableIfNotExists()) {
            String partitionKey = partitionKeyGenerator.apply(envelope);
            GetItemRequest request = new GetItemRequest()
                    .withTableName(tableName)
                    .withKey(Collections.singletonMap(partitionKeyName, new AttributeValue().withS(partitionKey)));
            Map<String, AttributeValue> result;
            try {
                result = dynamoDb.getItem(request).getItem();
            } catch (AmazonDynamoDBException e) {
                throw new PersistenceException("Failed to retrieve attributes from DynamoDB", e);
            }
            if (result != null && result.containsKey(attributesKeyName)) {
                Map<String, Object> attributes = InternalUtils.toSimpleMapValue(result.get(attributesKeyName).getM());
                return Optional.of(attributes);
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveAttributes(RequestEnvelope envelope, Map<String, Object> attributes) throws PersistenceException {
        String partitionKey = partitionKeyGenerator.apply(envelope);
        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(getItem(partitionKey, attributes));
        try {
            dynamoDb.putItem(request);
        } catch (AmazonDynamoDBException e) {
            throw new PersistenceException("Failed to save attributes to DynamoDB", e);
        }
    }

    private Map<String, AttributeValue> getItem(String id, Map<String, Object> attributes) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(partitionKeyName, new AttributeValue().withS(id));
        item.put(attributesKeyName, new AttributeValue().withM(InternalUtils.fromSimpleMap(attributes)));
        return item;
    }

    private boolean createTableIfNotExists() {
        if (createTable) {
            AttributeDefinition partitionKeyDefinition = new AttributeDefinition()
                    .withAttributeName(partitionKeyName)
                    .withAttributeType(ScalarAttributeType.S);
            KeySchemaElement partitionKeySchema = new KeySchemaElement()
                    .withAttributeName(partitionKeyName)
                    .withKeyType(KeyType.HASH);
            ProvisionedThroughput throughput = new ProvisionedThroughput()
                    .withReadCapacityUnits(5L)
                    .withWriteCapacityUnits(5L);
            try {
                createTable = false;
                return TableUtils.createTableIfNotExists(dynamoDb, new CreateTableRequest()
                        .withTableName(tableName)
                        .withAttributeDefinitions(partitionKeyDefinition)
                        .withKeySchema(partitionKeySchema)
                        .withProvisionedThroughput(throughput));
            } catch (AmazonDynamoDBException e) {
                throw new PersistenceException("Create table request failed", e);
            }
        }
        return false;
    }

    public static final class Builder {
        private AmazonDynamoDB dynamoDb;
        private String tableName;
        private String partitionKeyName = DEFAULT_PARTITION_KEY_NAME;
        private String attributesKeyName = DEFAULT_ATTRIBUTES_KEY_NAME;
        private Function<RequestEnvelope, String> partitionKeyGenerator = DEFAULT_PARTITION_KEY_GENERATOR;
        private boolean autoCreateTable = DEFAULT_AUTO_CREATE_TABLE;

        private Builder() {
        }

        /**
         * Optional DynamoDB client instance to use. If not provided, a default DynamoDB client instance
         * will be constructed and used.
         * @param dynamoDb client instance
         * @return builder
         */
        public Builder withDynamoDbClient(AmazonDynamoDB dynamoDb) {
            this.dynamoDb = dynamoDb;
            return this;
        }

        /**
         * Name of the DynamoDB table to use for storing Skill persistence attributes.
         * @param tableName name of the DynamoDB table to use
         * @return builder
         */
        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Optional name of the partition key used to key attributes. By default {@value #DEFAULT_PARTITION_KEY_NAME}
         * is used.
         * @param partitionKeyName name of the partition key
         * @return builder
         */
        public Builder withPartitionKeyName(String partitionKeyName) {
            this.partitionKeyName = partitionKeyName;
            return this;
        }

        /**
         * Optional name of the attributes key. By default {@value #DEFAULT_ATTRIBUTES_KEY_NAME} is used.
         * @param attributesKeyName name of the attribute key
         * @return builder
         */
        public Builder withAttributesKeyName(String attributesKeyName) {
            this.attributesKeyName = attributesKeyName;
            return this;
        }

        /**
         * Optional partition key generator function used to derive partition key value from one or more
         * attributes of a {@link RequestEnvelope}. By default, {@link PartitionKeyGenerators#userId()} is used.
         * @param partitionKeyGenerator partition key generator function
         * @return builder
         */
        public Builder withPartitionKeyGenerator(Function<RequestEnvelope, String> partitionKeyGenerator) {
            this.partitionKeyGenerator = partitionKeyGenerator;
            return this;
        }

        /**
         * Optional flag specifying whether the adapter should automatically create a table with the configured name
         * if it does not already exist. If not specified, this behavior defaults to true.
         * @param autoCreateTable true if the table should be automatically created if it does not already exist
         * @return builder
         */
        public Builder withAutoCreateTable(boolean autoCreateTable) {
            this.autoCreateTable = autoCreateTable;
            return this;
        }

        public DynamoDbPersistenceAdapter build() {
            return new DynamoDbPersistenceAdapter(this);
        }
    }

}