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
import com.amazon.ask.attributes.persistence.impl.utils.ItemUtilsV2;
import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.util.ValidationUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Persistence adapter for storing skill persistence attributes in Amazon DynamoDB.
 */
public final class DynamoDbPersistenceAdapter implements PersistenceAdapter {

    /**
     * Amazon DynamoDB v2 client.
     */
    private final DynamoDbClient dynamoDb;

    /**
     * Table name to be used/created.
     */
    private final String tableName;

    /**
     * A simple primary key, composed of one attribute known as the partition key. DynamoDB uses the partition key's
     * value as input to an internal hash function. The output from the hash function determines the partition
     * (physical storage internal to DynamoDB) in which the item will be stored.
     */
    private final String partitionKeyName;

    /**
     * Referred to as a composite primary key, this type of key is composed of two attributes.
     * The first attribute is the partition key, and the second attribute is the sort key.
     */
    private final String attributesKeyName;

    /**
     * Partition key generator.
     */
    private final Function<RequestEnvelope, String> partitionKeyGenerator;

    /**
     * When set to true, creates table if table doesn't exist.
     */
    private boolean autoCreateTable;

    /**
     * Default partition key name.
     */
    private static final String DEFAULT_PARTITION_KEY_NAME = "id";

    /**
     * Default attributes key name.
     */
    private static final String DEFAULT_ATTRIBUTES_KEY_NAME = "attributes";

    /**
     * Default value for auto create table.
     */
    private static final boolean DEFAULT_AUTO_CREATE_TABLE = false;

    /**
     * Default partition key generator.
     */
    private static final Function<RequestEnvelope, String> DEFAULT_PARTITION_KEY_GENERATOR = PartitionKeyGenerators.userId();

    /**
     * Read capacity unit represents one strongly consistent read per second, or two eventually consistent reads per second,
     * for items up to 4 KB in size. If you need to read an item that is larger than 4 KB, DynamoDB will need to consume additional
     * read capacity units.
     */
    private static final Long DEFAULT_READ_CAPACITY_UNITS = 5L;

    /**
     * Private constructor to build an instance of {@link DynamoDbPersistenceAdapter}.
     * @param builder instance of {@link Builder}.
     */
    private DynamoDbPersistenceAdapter(final Builder builder) {
        this.tableName = ValidationUtils.assertStringNotEmpty(builder.tableName, "table name");
        this.dynamoDb = builder.dynamoDb != null ? builder.dynamoDb : DynamoDbClient.create();
        this.partitionKeyName = builder.partitionKeyName;
        this.attributesKeyName = builder.attributesKeyName;
        this.partitionKeyGenerator = builder.partitionKeyGenerator;
        this.autoCreateTable = builder.autoCreateTable;
        autoCreateTableIfNotExists();
    }

    /**
     * Static method to build an instance of Builder.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets attributes from DynamoDB table.
     * @param envelope instance of {@link RequestEnvelope}.
     * @throws PersistenceException if table doesn't exist or attributes retrieval fails.
     * @return {@link Map} of String, Object if attributes exist, or an empty {@link Optional} if not.
     */
    @Override
    public Optional<Map<String, Object>> getAttributes(final RequestEnvelope envelope) throws PersistenceException {
        String partitionKey = partitionKeyGenerator.apply(envelope);
        GetItemRequest request = GetItemRequest.builder()
            .tableName(tableName)
            .key(Collections.singletonMap(partitionKeyName,
                AttributeValue.builder().s(partitionKey).build()))
            .build();
        Map<String, AttributeValue> result = null;
        try {
            result = dynamoDb.getItem(request).item();
        } catch (ResourceNotFoundException e) {
            throw new PersistenceException(String.format("Table %s does not exist or is in the process of being created", tableName), e);
        } catch (DynamoDbException e) {
            throw new PersistenceException("Failed to retrieve attributes from DynamoDB", e);
        }
        if (result != null && result.containsKey(attributesKeyName)) {
            Map<String, Object> attributes = ItemUtilsV2.toSimpleMapValue(result.get(attributesKeyName).m());
            return Optional.of(attributes);
        }
        return Optional.empty();
    }

    /**
     * Saves attributes to a DynamoDB table.
     * @param envelope instance of {@link RequestEnvelope}.
     * @param attributes to be stored in the table.
     * @throws PersistenceException if table doesn't exist or save attributes operation fails.
     */
    @Override
    public void saveAttributes(final RequestEnvelope envelope, final Map<String, Object> attributes) throws PersistenceException {
        String partitionKey = partitionKeyGenerator.apply(envelope);
        PutItemRequest request = PutItemRequest.builder()
            .tableName(tableName)
            .item(getItem(partitionKey, attributes))
            .build();
            try {
                dynamoDb.putItem(request);
            } catch (ResourceNotFoundException e) {
                throw new PersistenceException(String.format("Table %s does not exist or is in the process of being created", tableName), e);
            } catch (DynamoDbException e) {
                throw new PersistenceException("Failed to save attributes to DynamoDB", e);
            }
    }

    /**
     * Deletes attributes from DynamoDB table.
     * @param envelope instance of {@link RequestEnvelope}.
     * @throws PersistenceException if table doesn't exist or save attributes operation fails.
     */
    @Override
    public void deleteAttributes(final RequestEnvelope envelope) throws PersistenceException {
        String partitionKey = partitionKeyGenerator.apply(envelope);
        DeleteItemRequest request = DeleteItemRequest.builder()
            .tableName(tableName)
            .key(getItem(partitionKey, getAttributes(envelope).get()))
            .build();
        try {
            dynamoDb.deleteItem(request);
        } catch (ResourceNotFoundException e) {
            throw new PersistenceException(String.format("Table %s does not exist", tableName), e);
        } catch (DynamoDbException e) {
            throw new PersistenceException("Failed to delete attributes from DynamoDB", e);
        }
    }

    /**
     * Get a single item with a given id.
     * @param id of the item.
     * @param attributes DynamoDB table's attributes.
     * @return {@link Map} of id as key with corresponding attributeValue as value.
     */
    private Map<String, AttributeValue> getItem(final String id, final Map<String, Object> attributes) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(partitionKeyName, AttributeValue.builder().s(id).build());
        item.put(attributesKeyName, AttributeValue.builder().m(ItemUtilsV2.fromSimpleMap(attributes)).build());
        return item;
    }

    /**
     * Auto creates table if the table doesn't exist.
     */
    private void autoCreateTableIfNotExists() {
        if (autoCreateTable) {
            AttributeDefinition partitionKeyDefinition = AttributeDefinition.builder()
                    .attributeName(partitionKeyName)
                    .attributeType(ScalarAttributeType.S)
                    .build();
            KeySchemaElement partitionKeySchema = KeySchemaElement.builder()
                    .attributeName(partitionKeyName)
                    .keyType(KeyType.HASH)
                    .build();
            ProvisionedThroughput throughput = ProvisionedThroughput.builder()
                    .readCapacityUnits(DEFAULT_READ_CAPACITY_UNITS)
                    .writeCapacityUnits(DEFAULT_READ_CAPACITY_UNITS)
                    .build();
            try {
                dynamoDb.createTable(CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(partitionKeyDefinition)
                    .keySchema(partitionKeySchema)
                    .provisionedThroughput(throughput)
                    .build());
            } catch (ResourceInUseException e) {
                // table already exists - continue
                return;
            } catch (DynamoDbException e) {
                throw new PersistenceException("Create table request failed", e);
            }
        }
    }

    /**
     * Static builder class to build an instance of {@link DynamoDbPersistenceAdapter}.
     */
    public static final class Builder {

        /**
         * Amazon DynamoDB v2 client.
         */
        private DynamoDbClient dynamoDb;

        /**
         * Table name to be used/created.
         */
        private String tableName;

        /**
         * A simple primary key, composed of one attribute known as the partition key. DynamoDB uses the partition key's
         * value as input to an internal hash function. The output from the hash function determines the partition
         * (physical storage internal to DynamoDB) in which the item will be stored.
         */
        private String partitionKeyName = DEFAULT_PARTITION_KEY_NAME;

        /**
         * Referred to as a composite primary key, this type of key is composed of two attributes.
         * The first attribute is the partition key, and the second attribute is the sort key.
         */
        private String attributesKeyName = DEFAULT_ATTRIBUTES_KEY_NAME;

        /**
         * Partition key generator.
         */
        private Function<RequestEnvelope, String> partitionKeyGenerator = DEFAULT_PARTITION_KEY_GENERATOR;

        /**
         * When set to true, creates table if table doesn't exist.
         */
        private boolean autoCreateTable = DEFAULT_AUTO_CREATE_TABLE;

        /**
         * Prevent instantiation.
         */
        private Builder() { }

        /**
         * Optional DynamoDB client instance to use. If not provided, a default DynamoDB client instance
         * will be constructed and used.
         * @param dynamoDb client instance
         * @return builder
         */
        public Builder withDynamoDbClient(final DynamoDbClient dynamoDb) {
            this.dynamoDb = dynamoDb;
            return this;
        }

        /**
         * Name of the DynamoDB table to use for storing Skill persistence attributes.
         * @param tableName name of the DynamoDB table to use
         * @return builder
         */
        public Builder withTableName(final String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Optional name of the partition key used to key attributes. By default {@value #DEFAULT_PARTITION_KEY_NAME}
         * is used.
         * @param partitionKeyName name of the partition key
         * @return builder
         */
        public Builder withPartitionKeyName(final String partitionKeyName) {
            this.partitionKeyName = partitionKeyName;
            return this;
        }

        /**
         * Optional name of the attributes key. By default {@value #DEFAULT_ATTRIBUTES_KEY_NAME} is used.
         * @param attributesKeyName name of the attribute key
         * @return builder
         */
        public Builder withAttributesKeyName(final String attributesKeyName) {
            this.attributesKeyName = attributesKeyName;
            return this;
        }

        /**
         * Optional partition key generator function used to derive partition key value from one or more
         * attributes of a {@link RequestEnvelope}. By default, {@link PartitionKeyGenerators#userId()} is used.
         * @param partitionKeyGenerator partition key generator function
         * @return builder
         */
        public Builder withPartitionKeyGenerator(final Function<RequestEnvelope, String> partitionKeyGenerator) {
            this.partitionKeyGenerator = partitionKeyGenerator;
            return this;
        }

        /**
         * Optional flag specifying whether the adapter should automatically create a table with the configured name
         * if it does not already exist. If not specified, this behavior defaults to true.
         * @param autoCreateTable true if the table should be automatically created if it does not already exist
         * @return builder
         */
        public Builder withAutoCreateTable(final boolean autoCreateTable) {
            this.autoCreateTable = autoCreateTable;
            return this;
        }

        /**
         * Builder method to build an instance of DynamoDbPersistenceAdapter.
         * @return {@link DynamoDbPersistenceAdapter}.
         */
        public DynamoDbPersistenceAdapter build() {
            return new DynamoDbPersistenceAdapter(this);
        }
    }

}
