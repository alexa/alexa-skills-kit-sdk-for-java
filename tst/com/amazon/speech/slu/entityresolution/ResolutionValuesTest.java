package com.amazon.speech.slu.entityresolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * This class tests the utility methods to retrieve values from the {@link Resolution} class.
 */
@RunWith(PowerMockRunner.class)
public class ResolutionValuesTest {
    private static final String RESOLUTION_AUTHORITY = "dummy-authority-url";
    private static final Status RESOLUTION_STATUS = Status.builder()
            .withCode(StatusCode.ER_SUCCESS_MATCH)
            .build();
    private static final String VALUE_ID = "dummy-value-id";
    private static final String VALUE_NAME = "dummy-value-name";
    private static final Value VALUE = Value.builder()
            .withId(VALUE_ID)
            .withName(VALUE_NAME)
            .build();
    private static final ValueWrapper VALUE_WRAPPER = ValueWrapper.builder()
            .withValue(VALUE)
            .build();

    private static final List<ValueWrapper> VALUES_LIST = Collections.singletonList(VALUE_WRAPPER);
    /**
     * Tests that resolution.getValues() is an empty list
     * when resolution is constructed with no value.
     */
    @Test
    public void getValuesFromResolutionWithNoValue() {
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .build();

        assertTrue(resolution.getValueWrappers().isEmpty());
    }

    /**
     * Tests that resolution.getValues() is an empty list
     * when resolution is constructed with an empty list.
     */
    @Test
    public void getValuesFromEmptyList() {
        List<ValueWrapper> values = Collections.emptyList();
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .withValues(values)
                .build();

        assertTrue(resolution.getValueWrappers().isEmpty());
    }

    /**
     * Tests that resolution.getValues() is the correct list
     * when resolution is constructed with a list.
     */
    @Test
    public void getValuesFromList() {
        List<ValueWrapper> values = new ArrayList<>();
        values.add(VALUE_WRAPPER);
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .withValues(values)
                .build();

        assertEquals(resolution.getValueWrappers(), values);
        assertEquals(resolution.getValueWrapperAtIndex(0), VALUE_WRAPPER);
    }

    /**
     * Tests that resolution.getValues() is immutable
     * when resolution is constructed with no value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getValuesFromEmptyListImmutable() {
        List<ValueWrapper> values = Collections.emptyList();
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .withValues(values)
                .build();

        resolution.getValueWrappers().add(VALUE_WRAPPER);
    }

    /**
     * Tests that resolution.getValues() is immutable
     * when resolution is constructed with a mutable list.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getValuesFromListImmutable() {
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .withValues(VALUES_LIST)
                .build();

        resolution.getValueWrappers().remove(VALUE_WRAPPER);
    }

    /**
     * Tests that resolution.getValues() returns the correct ValueWrapper
     */
    @Test
    public void getValues() {
        Resolution resolution = Resolution.builder()
                .withAuthority(RESOLUTION_AUTHORITY)
                .withStatus(RESOLUTION_STATUS)
                .withValues(VALUES_LIST)
                .build();

        assertEquals(resolution.getValueWrappers().size(), 1);
        assertEquals(resolution.getValueWrapperAtIndex(0).getValue().getId(), VALUE_ID);
        assertEquals(resolution.getValueWrapperAtIndex(0).getValue().getName(), VALUE_NAME);
    }
}
