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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DynamoDbPersistenceAdapterTest {

    private DynamoDbClient mockDdb;
    private RequestEnvelope requestEnvelope;
    private Function<RequestEnvelope, String> mockKeyGenerator;

    @Before
    public void setUp() {
        mockDdb = mock(DynamoDbClient.class);
        mockKeyGenerator = mock(Function.class);
        requestEnvelope = RequestEnvelope.builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void table_name_must_be_set() {
        DynamoDbPersistenceAdapter.builder()
                .withDynamoDbClient(mockDdb)
                .withAttributesKeyName("foo")
                .withPartitionKeyName("bar")
                .withAutoCreateTable(true)
                .build();
    }

    @Test
    public void get_attributes_calls_ddb() {
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().build());
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
        ArgumentCaptor<GetItemRequest> getItemRequestCaptor = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, times(1)).getItem(getItemRequestCaptor.capture());
        GetItemRequest request = getItemRequestCaptor.getValue();
        assertEquals(request.tableName(), "foo");
        assertEquals(request.key(), Collections.singletonMap("id", AttributeValue.builder().s("bar").build()));
    }

    @Test
    public void get_attributes_returns_expected_results() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("attributes", AttributeValue.builder().m(Collections.singletonMap("FooKey", AttributeValue.builder().s("FooValue").build())).build());
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(item).build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        Map<String, Object> attributes = adapter.getAttributes(requestEnvelope).get();
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        assertEquals(attributes.size(), 1);
        assertEquals(attributes.get("FooKey"), "FooValue");
    }

    @Test
    public void get_attributes_returns_empty_if_no_results_found() {
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        assertEquals(adapter.getAttributes(requestEnvelope), Optional.empty());
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
    }

    @Test
    public void get_attributes_returns_empty_if_key_not_present() {
        Map<String, AttributeValue> result = new HashMap<>();
        result.put("not_attributes", AttributeValue.builder().m(Collections.singletonMap("FooKey", AttributeValue.builder().s("FooValue").build())).build());
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(result).build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        assertEquals(adapter.getAttributes(requestEnvelope), Optional.empty());
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
    }

    @Test(expected = PersistenceException.class)
    public void get_attributes_exception_wrapped_in_persistence_exception() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        when(mockDdb.getItem(any(GetItemRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
    }

    @Test
    public void save_attributes_calls_ddb() {
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any(PutItemRequest.class))).thenReturn(PutItemResponse.builder().build());
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.saveAttributes(requestEnvelope, attr);
        ArgumentCaptor<PutItemRequest> putItemRequestCaptor = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, times(1)).putItem(putItemRequestCaptor.capture());
        PutItemRequest request = putItemRequestCaptor.getValue();
        assertEquals(request.tableName(), "foo");
        assertEquals(request.item().get("id"), AttributeValue.builder().s("bar").build());
        assertEquals(request.item().get("attributes"), AttributeValue.builder().m(Collections.singletonMap("Foo", AttributeValue.builder().s("Bar").build())).build());
    }

    @Test(expected = PersistenceException.class)
    public void save_attributes_exception_wrapped_in_persistence_exception() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any(PutItemRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.saveAttributes(requestEnvelope, attr);
    }

    @Test
    public void delete_attributes_calls_ddb() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("attributes", AttributeValue.builder().m(Collections.singletonMap("FooKey", AttributeValue.builder().s("FooValue").build())).build());
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(item).build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder()
                .withTableName("foo")
                .withDynamoDbClient(mockDdb)
                .withPartitionKeyGenerator(mockKeyGenerator)
                .build();
        adapter.deleteAttributes(requestEnvelope);
        ArgumentCaptor<DeleteItemRequest> deleteItemRequestCaptor = ArgumentCaptor.forClass(DeleteItemRequest.class);
        verify(mockDdb, times(1)).deleteItem(deleteItemRequestCaptor.capture());
        DeleteItemRequest request = deleteItemRequestCaptor.getValue();
        assertEquals(request.tableName(), "foo");
        assertEquals(request.key().get("attributes"), AttributeValue.builder().m(Collections.singletonMap("FooKey", AttributeValue.builder().s("FooValue").build())).build());
    }

    @Test(expected = PersistenceException.class)
    public void delete_attributes_exception_wrapped_in_persistence_exception() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("attributes", AttributeValue.builder().m(Collections.singletonMap("FooKey", AttributeValue.builder().s("FooValue").build())).build());
        when(mockDdb.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(item).build());
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        when(mockDdb.deleteItem(any(DeleteItemRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.deleteAttributes(requestEnvelope);
    }

    @Test
    public void create_table_called_on_instantiation_when_enabled() {
        when(mockDdb.createTable(any(CreateTableRequest.class))).thenReturn(CreateTableResponse.builder().build());
        DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("bar").withPartitionKeyName("baz").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        ArgumentCaptor<CreateTableRequest> createTableRequestCaptor = ArgumentCaptor.forClass(CreateTableRequest.class);
        verify(mockDdb, times(1)).createTable(createTableRequestCaptor.capture());
        assertEquals(createTableRequestCaptor.getValue().tableName(), "bar");

        List<AttributeDefinition> attributeDefinitions = createTableRequestCaptor.getValue().attributeDefinitions();
        assertEquals(1, attributeDefinitions.size());
        AttributeDefinition attributeDefinition = attributeDefinitions.get(0);
        assertEquals("baz", attributeDefinition.attributeName());
        assertEquals(ScalarAttributeType.S, attributeDefinition.attributeType());

        List<KeySchemaElement> keySchemas = createTableRequestCaptor.getValue().keySchema();
        assertEquals(1, keySchemas.size());
        KeySchemaElement keySchema = keySchemas.get(0);
        assertEquals("baz", keySchema.attributeName());
        assertEquals(KeyType.HASH, keySchema.keyType());

        ProvisionedThroughput throughput = createTableRequestCaptor.getValue().provisionedThroughput();
        assertEquals(Long.valueOf(5), throughput.readCapacityUnits());
        assertEquals(Long.valueOf(5), throughput.writeCapacityUnits());
    }

    @Test
    public void create_table_not_called_on_instantiation_when_not_enabled() {
        DynamoDbPersistenceAdapter.builder().withAutoCreateTable(false).withTableName("bar").withPartitionKeyName("baz").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        verify(mockDdb, never()).createTable(any(CreateTableRequest.class));
    }

    @Test(expected = PersistenceException.class)
    public void get_attributes_throws_exception_if_auto_create_enabled_and_table_does_not_exist() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        when(mockDdb.getItem(any(GetItemRequest.class))).thenThrow(ResourceNotFoundException.builder().build() );
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
    }

    @Test(expected = PersistenceException.class)
    public void get_attributes_throws_exception_if_auto_create_disabled_and_table_does_not_exist() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        when(mockDdb.getItem(any(GetItemRequest.class))).thenThrow(ResourceNotFoundException.builder().build() );
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(false).withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
    }

    @Test(expected = PersistenceException.class)
    public void save_attributes_throws_exception_if_auto_create_enabled_and_table_does_not_exist() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any(PutItemRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.saveAttributes(requestEnvelope, attr);
    }

    @Test(expected = PersistenceException.class)
    public void save_attributes_throws_exception_if_auto_create_disabled_and_table_does_not_exist() {
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any(PutItemRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(false).withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.saveAttributes(requestEnvelope, attr);
    }

    @Test(expected = PersistenceException.class)
    public void create_table_exception_wrapped_in_persistence_exception() {
        when(mockDdb.createTable(any(CreateTableRequest.class))).thenThrow(DynamoDbException.builder().build());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("foo").withDynamoDbClient(mockDdb).build();
        adapter.getAttributes(requestEnvelope);
    }

}