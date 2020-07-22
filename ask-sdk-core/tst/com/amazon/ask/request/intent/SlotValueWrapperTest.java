/*
    Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 
    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at
 
        http://aws.amazon.com/apache2.0/
 
    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */
 
package com.amazon.ask.request.intent;
 
import com.amazon.ask.model.ListSlotValue;
import com.amazon.ask.model.SimpleSlotValue;
import org.junit.Test;
 
import java.util.List;
import java.util.Optional;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
 
public class SlotValueWrapperTest {
 
    private SimpleSlotValue testSimpleSlotValue = SimpleSlotValue.builder()
            .build();
    private ListSlotValue testListSlotValue = ListSlotValue.builder()
            .addValuesItem(testSimpleSlotValue)
            .addValuesItem(testSimpleSlotValue)
            .build();
 
    @Test
    public void get_unwrapped_value_returns_underlying_slot_value() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testSimpleSlotValue);
        assertEquals(wrapper.unwrap(), testSimpleSlotValue);
    }
 
    @Test
    public void values_returns_wrapped_list_values_for_list_type() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testListSlotValue);
        List<SlotValueWrapper> values = wrapper.values();
        assertEquals(values.size(), 2);
        SlotValueWrapper listWrapper = values.get(1);
        assertEquals(listWrapper.unwrap(), testSimpleSlotValue);
    }
 
    @Test
    public void values_returns_singleton_list_for_non_list_type() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testSimpleSlotValue);
        List<SlotValueWrapper> values = wrapper.values();
        assertEquals(values.size(), 1);
        SlotValueWrapper listWrapper = values.get(0);
        assertEquals(listWrapper.unwrap(), testSimpleSlotValue);
    }
 
    @Test
    public void get_as_simple_slot_returns_for_simple_slot_value() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testSimpleSlotValue);
        Optional<SimpleSlotValue> simpleSlotValue = wrapper.asSimple();
        assertTrue(simpleSlotValue.isPresent());
        assertEquals(simpleSlotValue.get(), testSimpleSlotValue);
    }
 
    @Test
    public void get_as_simple_slot_returns_empty_for_list_slot_value() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testListSlotValue);
        Optional<SimpleSlotValue> simpleSlotValue = wrapper.asSimple();
        assertFalse(simpleSlotValue.isPresent());
    }
 
    @Test
    public void get_as_list_slot_returns_for_list_slot_value() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testListSlotValue);
        Optional<ListSlotValue> listSlotValue = wrapper.asList();
        assertTrue(listSlotValue.isPresent());
        assertEquals(listSlotValue.get(), testListSlotValue);
    }
 
    @Test
    public void get_as_list_slot_returns_empty_for_simple_slot_value() {
        SlotValueWrapper wrapper = SlotValueWrapper.createFrom(testSimpleSlotValue);
        Optional<ListSlotValue> listSlotValue = wrapper.asList();
        assertFalse(listSlotValue.isPresent());
    }
 
}