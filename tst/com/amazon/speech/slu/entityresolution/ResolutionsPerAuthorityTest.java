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
 * This class tests the utility methods to retrieve resolutionsPerAuthority from the {@link Resolutions} class.
 */
@RunWith(PowerMockRunner.class)
public class ResolutionsPerAuthorityTest {
    // --------------------------------
    // Test Resolution attributes
    private static final String RESOLUTION_AUTHORITY = "dummy-authority-url";
    private static final Status RESOLUTION_STATUS = Status.builder()
            .withCode(StatusCode.ER_SUCCESS_MATCH)
            .build();
    private static final Resolution RESOLUTION = Resolution.builder()
            .withAuthority(RESOLUTION_AUTHORITY)
            .withStatus(RESOLUTION_STATUS)
            .build();
    private static final List<Resolution> RESOLUTION_LIST = Collections.singletonList(RESOLUTION);

    /**
     * Tests that resolutions.getResolutionsPerAuthority() is an empty list
     * when resolutions is constructed with no ResolutionsPerAuthority.
     */
    @Test
    public void getResolutionsPerAuthorityFromResolutionsWithNoResolution() {
        Resolutions resolutions = Resolutions.builder().build();

        assertTrue(resolutions.getResolutionsPerAuthority().isEmpty());
    }

    /**
     * Tests that resolutions.getResolutionsPerAuthority() is an empty list
     * when resolutions is constructed with an empty list.
     */
    @Test
    public void getResolutionsPerAuthorityFromEmptyList() {
        List<Resolution> resolutionsPerAuthority = Collections.emptyList();
        Resolutions resolutions = Resolutions.builder()
                .withResolutionsPerAuthority(resolutionsPerAuthority)
                .build();

        assertTrue(resolutions.getResolutionsPerAuthority().isEmpty());
    }

    /**
     * Tests that resolutions.getResolutionsPerAuthority() is the correct list
     * when resolutions is constructed with a list.
     */
    @Test
    public void getResolutionsPerAuthorityFromList() {
        List<Resolution> resolutionsPerAuthority = new ArrayList<>();
        resolutionsPerAuthority.add(RESOLUTION);
        Resolutions resolutions = Resolutions.builder()
                .withResolutionsPerAuthority(resolutionsPerAuthority)
                .build();

        assertEquals(resolutions.getResolutionAtIndex(0), resolutionsPerAuthority.get(0));
        assertEquals(resolutions.getResolutionsPerAuthority(),resolutionsPerAuthority);
    }

    /**
     * Tests that resolutions.getResolutionsPerAuthority() is immutable
     * when resolutions is constructed with no ResolutionsPerAuthority.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getResolutionsPerAuthorityFromEmptyListImmutable() {
        Resolutions resolutions = Resolutions.builder().build();

        resolutions.getResolutionsPerAuthority().add(RESOLUTION);
    }

    /**
     * Tests that resolutions.getResolutionsPerAuthority() is immutable
     * when resolutions is constructed with a mutable list.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getResolutionsPerAuthorityFromListImmutable() {
        Resolutions resolutions = Resolutions.builder()
                .withResolutionsPerAuthority(RESOLUTION_LIST)
                .build();
        resolutions.getResolutionsPerAuthority().remove(RESOLUTION);
    }

    /**
     * Tests that resolutions.getResolutionPerAuthority() returns the correct resolution.
     */
    @Test
    public void getResolution() {
        Resolutions resolutions = Resolutions.builder()
                .withResolutionsPerAuthority(RESOLUTION_LIST)
                .build();

        assertEquals(resolutions.getResolutionsPerAuthority().size(), 1);
        assertEquals(resolutions.getResolutionAtIndex(0).getAuthority(), RESOLUTION_AUTHORITY);
        assertEquals(resolutions.getResolutionAtIndex(0).getStatus().getCode(), StatusCode.ER_SUCCESS_MATCH);
    }
}
