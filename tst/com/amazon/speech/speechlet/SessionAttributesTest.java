package com.amazon.speech.speechlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * This class tests the utility methods to set / get / remove attributes in the {@link Session}
 * class.
 */
@RunWith(PowerMockRunner.class)
public class SessionAttributesTest {
    private static final String ATTRIBUTE_NAME = "my_attribute_name";
    private static final String ATTRIBUTE_VALUE = "my_attribute_value";
    private static final Map<String, String> attributeMap = Collections.singletonMap(
            ATTRIBUTE_NAME, ATTRIBUTE_VALUE);

    @Test
    public void attributesNotNull() {
        final Session session = Session.builder().withSessionId("sId").build();

        // Will throw a NullPointerException if getAttributes() returns null
        assertTrue(session.getAttributes().isEmpty());
    }

    @Test
    public void setAttribute() {
        final Session session = Session.builder().withSessionId("sId").build();
        session.setAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);

        assertEquals(session.getAttributes(), attributeMap);
    }

    @Test
    public void getAttribute() {
        final Session session = Session.builder().withSessionId("sId").build();
        session.getAttributes().put(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);

        assertEquals(session.getAttributes().get(ATTRIBUTE_NAME), ATTRIBUTE_VALUE);
    }

    @Test
    public void removeAttribute() {
        final Session session = Session.builder().withSessionId("sId").build();
        session.setAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        session.removeAttribute(ATTRIBUTE_NAME);

        assertNull(session.getAttribute(ATTRIBUTE_NAME));
        assertTrue(session.getAttributes().isEmpty());
    }
}
