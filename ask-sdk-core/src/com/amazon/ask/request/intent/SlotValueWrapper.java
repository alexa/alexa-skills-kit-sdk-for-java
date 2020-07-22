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
import com.amazon.ask.model.SlotValue;
 
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
 
public class SlotValueWrapper {
 
    private SlotValue slotValue;
 
    private SlotValueWrapper(final SlotValue slotValue) {
        this.slotValue = slotValue;
    }
 
    /**
     * Creates a wrapper instance wrapping the given {@link SlotValue}.
     *
     * @param slotValue slot value to wrap
     * @return wrapper instance
     */
    public static SlotValueWrapper createFrom(final SlotValue slotValue) {
        return new SlotValueWrapper(slotValue);
    }
 
    /**
     * Returns the underlying {@link SlotValue} this instance wraps.
     *
     * @return underlying {@link SlotValue}.
     */
    public SlotValue unwrap() {
        return slotValue;
    }
 
    /**
     * If the underlying slot value is a {@link ListSlotValue} type, returns a {@link List} containing
     * all its values wrapped with SlotValueWrappers. If the underlying value is not a list type,
     * returns a singleton list containing the current wrapper.
     *
     * @return list containing underlying {@link ListSlotValue} values wrapped in SlotValueWrappers
     *         or a singleton list consisting of the current wrapper.
     */
    public List<SlotValueWrapper> values() {
        if (slotValue instanceof ListSlotValue) {
            return ((ListSlotValue)slotValue).getValues().stream()
                    .map(SlotValueWrapper::createFrom)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(this);
    }
 
    /**
     * If the underlying slot type is a {@link SimpleSlotValue}, returns an {@link Optional} containing it.
     * If not, returns an {@link Optional} empty.
     *
     * @return underlying slot type as {@link SimpleSlotValue} or {@link Optional} empty.
     */
    public Optional<SimpleSlotValue> asSimple() {
     return slotValue instanceof SimpleSlotValue ? Optional.of((SimpleSlotValue) slotValue) : Optional.empty();
     }
 
     /**
     * If the underlying slot type is a {@link ListSlotValue}, returns an {@link Optional} containing it.
     * If not, returns an {@link Optional} empty.
     *
     * @return underlying slot type as {@link ListSlotValue} or {@link Optional} empty.
     */
    public Optional<ListSlotValue> asList() {
        return slotValue instanceof ListSlotValue ? Optional.of((ListSlotValue) slotValue) : Optional.empty();
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotValueWrapper that = (SlotValueWrapper) o;
        return Objects.equals(slotValue, that.slotValue);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(slotValue);
    }
 
}
