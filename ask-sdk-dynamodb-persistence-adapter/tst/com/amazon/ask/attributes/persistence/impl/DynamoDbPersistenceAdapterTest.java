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
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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

    private AmazonDynamoDB mockDdb;
    private RequestEnvelope requestEnvelope;
    private Function<RequestEnvelope, String> mockKeyGenerator;

    @Before
    public void setUp() {
        mockDdb = mock(AmazonDynamoDB.class);
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
        when(mockDdb.getItem(any())).thenReturn(new GetItemResult());
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
        ArgumentCaptor<GetItemRequest> getItemRequestCaptor = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, times(1)).getItem(getItemRequestCaptor.capture());
        GetItemRequest request = getItemRequestCaptor.getValue();
        assertEquals(request.getTableName(), "foo");
        assertEquals(request.getKey(), Collections.singletonMap("id", new AttributeValue().withS("bar")));
    }

    @Test
    public void get_attributes_returns_expected_results() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("attributes", new AttributeValue().withM(Collections.singletonMap("FooKey", new AttributeValue().withS("FooValue"))));
        when(mockDdb.getItem(any())).thenReturn(new GetItemResult().withItem(item));
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        Map<String, Object> attributes = adapter.getAttributes(requestEnvelope).get();
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        assertEquals(attributes.size(), 1);
        assertEquals(attributes.get("FooKey"), "FooValue");
    }

    @Test
    public void get_attributes_returns_empty_if_no_results_found() {
        when(mockDdb.getItem(any())).thenReturn(new GetItemResult());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        assertEquals(adapter.getAttributes(requestEnvelope), Optional.empty());
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
    }

    @Test(expected = PersistenceException.class)
    public void get_attributes_exception_wrapped_in_persistence_exception() {
        when(mockDdb.getItem(any())).thenThrow(new AmazonDynamoDBException(""));
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).build();
        adapter.getAttributes(requestEnvelope);
    }

    @Test
    public void save_attributes_calls_ddb() {
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any())).thenReturn(new PutItemResult());
        when(mockKeyGenerator.apply(requestEnvelope)).thenReturn("bar");
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.saveAttributes(requestEnvelope, attr);
        ArgumentCaptor<PutItemRequest> putItemRequestCaptor = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, times(1)).putItem(putItemRequestCaptor.capture());
        PutItemRequest request = putItemRequestCaptor.getValue();
        assertEquals(request.getTableName(), "foo");
        assertEquals(request.getItem().get("id"), new AttributeValue().withS("bar"));
        assertEquals(request.getItem().get("attributes"), new AttributeValue().withM(Collections.singletonMap("Foo", new AttributeValue().withS("Bar"))));
    }

    @Test(expected = PersistenceException.class)
    public void save_attributes_exception_wrapped_in_persistence_exception() {
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        when(mockDdb.putItem(any())).thenThrow(new AmazonDynamoDBException(""));
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withTableName("foo").withDynamoDbClient(mockDdb).build();
        adapter.saveAttributes(requestEnvelope, attr);
    }

    @Test
    public void create_table_called_once_when_enabled() {
        when(mockDdb.getItem(any())).thenReturn(new GetItemResult());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("bar").withPartitionKeyName("baz").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
        adapter.getAttributes(requestEnvelope);
        ArgumentCaptor<CreateTableRequest> createTableRequestCaptor = ArgumentCaptor.forClass(CreateTableRequest.class);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, times(1)).createTable(createTableRequestCaptor.capture());
        assertEquals(createTableRequestCaptor.getValue().getTableName(), "bar");

        List<AttributeDefinition> attributeDefinitions = createTableRequestCaptor.getValue().getAttributeDefinitions();
        assertEquals(1, attributeDefinitions.size());
        AttributeDefinition attributeDefinition = attributeDefinitions.get(0);
        assertEquals("baz", attributeDefinition.getAttributeName());
        assertEquals("S", attributeDefinition.getAttributeType());

        List<KeySchemaElement> keySchemas = createTableRequestCaptor.getValue().getKeySchema();
        assertEquals(1, keySchemas.size());
        KeySchemaElement keySchema = keySchemas.get(0);
        assertEquals("baz", keySchema.getAttributeName());
        assertEquals("HASH", keySchema.getKeyType());

        ProvisionedThroughput throughput = createTableRequestCaptor.getValue().getProvisionedThroughput();
        assertEquals(new Long(5), throughput.getReadCapacityUnits());
        assertEquals(new Long(5), throughput.getWriteCapacityUnits());
    }

    @Test
    public void create_table_not_called_when_not_enabled() {
        when(mockDdb.getItem(any())).thenReturn(new GetItemResult());
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(false).withTableName("bar").withDynamoDbClient(mockDdb).withPartitionKeyGenerator(mockKeyGenerator).build();
        adapter.getAttributes(requestEnvelope);
        verify(mockKeyGenerator, times(1)).apply(requestEnvelope);
        verify(mockDdb, never()).createTable(any());
    }

    @Test(expected = PersistenceException.class)
    public void create_table_exception_wrapped_in_persistence_exception() {
        when(mockDdb.createTable(any())).thenThrow(new AmazonDynamoDBException(""));
        PersistenceAdapter adapter = DynamoDbPersistenceAdapter.builder().withAutoCreateTable(true).withTableName("foo").withDynamoDbClient(mockDdb).build();
        adapter.getAttributes(requestEnvelope);
    }

}
