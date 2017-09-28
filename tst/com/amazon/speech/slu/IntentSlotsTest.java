package com.amazon.speech.slu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * This class tests the utility methods to retrieve slots the {@link Intent} class.
 */
@RunWith(PowerMockRunner.class)
public class IntentSlotsTest {
    // --------------------------------
    // Test intent and slot attributes
    private static final String INTENT_NAME = "MyName";

    private static final String SLOT_NAME = "MySlot";
    private static final Slot SLOT = Slot
            .builder()
            .withName(SLOT_NAME)
            .withValue("SlotValue")
            .build();
    private static final Map<String, Slot> SLOT_MAP = Collections.singletonMap(SLOT_NAME, SLOT);

    /**
     * Tests that intent.getSlots() is an empty map when intent is constructed with no slots.
     */
    @Test
    public void getSlotsFromIntentWithNoSlots() {
        Intent intent = Intent.builder().withName(INTENT_NAME).build();

        assertTrue(intent.getSlots().isEmpty());
    }

    /**
     * Tests that intent.getSlots() is an empty map when intent is constructed with an empty map.
     */
    @Test
    public void getSlotsFromEmptyMap() {
        Map<String, Slot> slots = Collections.emptyMap();
        Intent intent = Intent.builder().withName(INTENT_NAME).withSlots(slots).build();

        assertTrue(intent.getSlots().isEmpty());
    }

    /**
     * Tests that intent.getSlots() is the correct map when intent is constructed with a map.
     */
    @Test
    public void getSlotsFromMap() {
        Map<String, Slot> slots = new HashMap<>();
        slots.put(SLOT_NAME, SLOT);
        Intent intent = Intent.builder().withName(INTENT_NAME).withSlots(slots).build();

        assertEquals(intent.getSlots(), SLOT_MAP);
    }

    /**
     * Tests that intent.getSlots() is immutable when intent is constructed with no slots.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getSlotsFromEmptyMapImmutable() {
        Intent intent = Intent.builder().withName(INTENT_NAME).build();
        intent.getSlots().put(SLOT_NAME, SLOT);
    }

    /**
     * Tests that intent.getSlots() is immutable when intent is constructed with a mutable map.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getSlotsFromMapImmutable() {
        Intent intent = Intent.builder().withName(INTENT_NAME).withSlots(SLOT_MAP).build();

        intent.getSlots().remove(SLOT_NAME);
    }

    /**
     * Tests that intent.getSlot(String) returns the correct slot.
     */
    @Test
    public void getSlot() {
        Intent intent = Intent.builder().withName(INTENT_NAME).withSlots(SLOT_MAP).build();

        assertEquals(intent.getSlot(SLOT_NAME), SLOT);
        assertEquals(intent.getSlot("dummySlotName"), null);
    }
}
