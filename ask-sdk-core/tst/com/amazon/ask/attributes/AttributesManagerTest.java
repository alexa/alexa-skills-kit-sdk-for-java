/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Session;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AttributesManagerTest {

    @Test
    public void get_request_attributes() {
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .build();
        assertEquals(attributesManager.getRequestAttributes(), Collections.emptyMap());
    }

    @Test
    public void put_request_attribute() {
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .build();
        Map<String, Object> requestAttributes = attributesManager.getRequestAttributes();
        requestAttributes.put("foo", "bar");
        assertEquals(attributesManager.getRequestAttributes().get("foo"), "bar");
    }

    @Test
    public void set_request_attributes() {
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .build();
        attributesManager.setRequestAttributes(attributes);
        assertEquals(attributesManager.getRequestAttributes(), attributes);
    }

    @Test
    public void session_attributes_retrieved() {
        Map<String, Object> attr = Collections.singletonMap("Foo", "Bar");
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(getRequestEnvelopeWithAttributes(attr))
                .build();
        assertNotNull(attributesManager.getSessionAttributes());
        assertEquals(attributesManager.getSessionAttributes(), attr);
    }

    @Test (expected = IllegalStateException.class)
    public void session_attributes_throws_exception_non_session_request() {
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(RequestEnvelope.builder().build())
                .build();
        attributesManager.getSessionAttributes();
    }

    @Test
    public void null_session_attributes_map_creates_new_map() {
        RequestEnvelope env = RequestEnvelope.builder().withSession(Session.builder().withAttributes(null).build()).build();
        AttributesManager attributesManager = AttributesManager.builder()
                .withRequestEnvelope(env)
                .build();
        assertEquals(attributesManager.getSessionAttributes(), Collections.emptyMap());
    }

    @Test
    public void put_session_attribute() {
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).withSession(Session.builder().build()).build())
                .build();
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        sessionAttributes.put("foo", "bar");
        assertEquals(input.getAttributesManager().getSessionAttributes().get("foo"), "bar");
    }

    @Test
    public void set_session_attributes() {
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).withSession(Session.builder().build()).build())
                .build();
        input.getAttributesManager().setSessionAttributes(attributes);
        assertEquals(input.getAttributesManager().getSessionAttributes(), attributes);
    }

    @Test (expected = IllegalStateException.class)
    public void set_session_attributes_throws_exception_non_session_request() {
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .build();
        input.getAttributesManager().setSessionAttributes(attributes);
    }

    @Test
    public void no_existing_persistence_attributes_returns_empty_map() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.empty());
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        assertEquals(input.getAttributesManager().getPersistentAttributes(), Collections.emptyMap());
    }

    @Test
    public void put_persistent_attribute() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.empty());
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
        persistentAttributes.put("foo", "bar");
        assertEquals(persistentAttributes.get("foo"), "bar");
    }

    @Test
    public void existing_persistence_attributes_returned() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.of(attributes));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        assertEquals(input.getAttributesManager().getPersistentAttributes(), attributes);
    }

    @Test
    public void set_persistence_attributes() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().setPersistentAttributes(attributes);
        assertEquals(input.getAttributesManager().getPersistentAttributes(), attributes);
    }

    @Test (expected = IllegalStateException.class)
    public void persistence_not_enabled_throws_exception_on_retrieve() {
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder()
                        .withRequest(IntentRequest.builder().build())
                        .build())
                .withPersistenceAdapter(null).build();
        input.getAttributesManager().getPersistentAttributes();
    }

    @Test (expected = IllegalStateException.class)
    public void persistence_not_enabled_throws_exception_on_set() {
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder()
                        .withRequest(IntentRequest.builder().build())
                        .build())
                .withPersistenceAdapter(null).build();
        input.getAttributesManager().setPersistentAttributes(Collections.emptyMap());
    }

    @Test
    public void get_calls_persistence_manager() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        RequestEnvelope request = RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build();
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.of(attributes));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(request)
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().getPersistentAttributes();
        input.getAttributesManager().savePersistentAttributes();
        verify(persistenceAdapter).getAttributes(request);
    }

    @Test
    public void save_noop_if_persistence_attributes_not_retrieved() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.of(attributes));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().savePersistentAttributes();
        verify(persistenceAdapter, never()).saveAttributes(any(), any());
    }

    @Test
    public void save_calls_persistence_manager_if_attributes_retrieved() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        ArgumentCaptor <Map<String, Object>> attributesCaptor = ArgumentCaptor.forClass((Class)Map.class);
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.of(attributes));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().getPersistentAttributes();
        input.getAttributesManager().savePersistentAttributes();
        verify(persistenceAdapter).saveAttributes(any(RequestEnvelope.class), attributesCaptor.capture());
        assertEquals(attributes, attributesCaptor.getValue());
    }

    @Test
    public void save_calls_persistence_manager_if_set_called() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        ArgumentCaptor <Map<String, Object>> attributesCaptor = ArgumentCaptor.forClass((Class)Map.class);
        Map<String, Object> attributes = (Collections.singletonMap("Foo", "Bar"));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().setPersistentAttributes(attributes);
        input.getAttributesManager().savePersistentAttributes();
        verify(persistenceAdapter).saveAttributes(any(RequestEnvelope.class), attributesCaptor.capture());
        assertEquals(attributes, attributesCaptor.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void delete_throws_exception_if_no_persistence_adapter() {
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .build();
        input.getAttributesManager().deletePersistentAttributes();
    }

    @Test
    public void delete_calls_persistence_manager() {
        PersistenceAdapter persistenceAdapter = mock(PersistenceAdapter.class);
        when(persistenceAdapter.getAttributes(any())).thenReturn(Optional.of(Collections.singletonMap("Foo", "Bar")));
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder().withRequest(IntentRequest.builder().build()).build())
                .withPersistenceAdapter(persistenceAdapter).build();
        input.getAttributesManager().deletePersistentAttributes();
        verify(persistenceAdapter).deleteAttributes(any(RequestEnvelope.class));
    }


    private RequestEnvelope getRequestEnvelopeWithAttributes(Map<String, Object> attributes) {
        return RequestEnvelope.builder().withSession(Session.builder().withAttributes(attributes).build()).build();
    }

}
