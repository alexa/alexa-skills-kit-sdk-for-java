package com.amazon.speech.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Super class for serialization tests.
 *
 * Note: we use our own Jackson ObjectMapper here instead of the ones provided by
 * {@link SpeechletRequestEnvelope} / {@link SpeechletResponseEnvelope} to have custom or more
 * stringent verification rules.
 */
public class SpeechletSerializationTestBase {
    protected static final Logger log = LoggerFactory
            .getLogger(SpeechletSerializationTestBase.class);
    protected static final ObjectMapper OBJECT_MAPPER;
    protected static final ObjectWriter OBJECT_WRITER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new SpeechletRequestModule());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Device interface classes have no properties
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();
    }

    /**
     * Utility method to test serialization to JSON and deserialization from JSON.
     *
     * @param envelope1
     *            the envelope to serialize.
     * @throws IOException
     *             in case of error.
     */
    protected void testSerialization(final Object envelope1) throws Exception {

        final String json1 = OBJECT_WRITER.writeValueAsString(envelope1);
        final Object envelope2 = OBJECT_MAPPER.readValue(json1, envelope1.getClass());
        final String json2 = OBJECT_WRITER.writeValueAsString(envelope2);

        assertNotNull(envelope2);
        assertEquals(json1, json2);
        assertTrue(deepEquals(envelope1, envelope2));
    }

    /**
     * Utility method to do a deep comparison of objects that don't implement the "equals" method.
     * Comparison is done by calling all the getters and recursively comparing the values returned
     * by the getters.
     *
     * @param o1
     *            first object to compare.
     * @param o2
     *            second object to compare.
     * @return true if identical.
     * @throws Exception
     *             if reflection fails.
     */
    private boolean deepEquals(final Object o1, final Object o2) throws Exception {
        // ----------
        // null case

        if (o1 == null) {
            return (o2 == null);
        }

        if (o2 == null) {
            return false;
        }

        final Class<?> clazz = o1.getClass();

        // ----------
        // Maps case

        if (Map.class.isAssignableFrom(clazz)) {
            log.debug("Comparing maps {} and {}", o1, o2);
            final Map<?, ?> map1 = (Map<?, ?>) o1;
            final Map<?, ?> map2 = (Map<?, ?>) o2;

            if (map1.size() != map2.size()) {
                log.debug("Maps differ in size");
                return false;
            }

            for (final Map.Entry<?, ?> entry1 : map1.entrySet()) {
                if (!deepEquals(entry1.getValue(), map2.get(entry1.getKey()))) {
                    log.debug("Maps have a different value for key {}: {} and {}", entry1.getKey(),
                            entry1.getValue(), map2.get(entry1.getKey()));
                    return false;
                }
            }

            return true;
        }

        // -----------------------------------------------------------------------
        // Perform date comparison with second (rather than millisecond) precision

        if (clazz.equals(Date.class)) {
            log.debug("Using second precision to compare {} and {}", o1, o2);
            return (((Date) o1).getTime() - ((Date) o2).getTime()) / 1000 == 0;
        }

        // ------------------
        // Lists

        if (List.class.isAssignableFrom(clazz)) {
            log.debug("Comparing lists {} and {}", o1, o2);
            final List<?> list1 = (List<?>) o1;
            final List<?> list2 = (List<?>) o2;

            if (list1.size() != list2.size()) {
                log.debug("Lists differ in size");
                return false;
            }

            for (int i = 0; i < list1.size(); i++) {
                if (!deepEquals(list1.get(i), list2.get(i))) {
                    log.debug("Lists have a different value for index {}: {} and {}", i,
                            list1.get(i), list2.get(i));
                    return false;
                }
            }

            return true;
        }

        // ------------------
        // TODO Sets

        // ------------------------------------------------
        // Enums and classes that do implement equals case

        boolean hasEqualsMethod = false;
        try {
            final Method equalsMethod = clazz.getMethod("equals", Object.class);
            hasEqualsMethod = !equalsMethod.getDeclaringClass().equals(Object.class);
        } catch (NoSuchMethodException nsme) {
        }

        if (clazz.isEnum() || hasEqualsMethod) {
            log.debug("Using equals to compare {} and {}", o1, o2);
            return o1.equals(o2);
        }

        // ------------------------------------------
        // Classes that do not implement equals case

        if (!clazz.equals(o2.getClass())) {
            return false;
        }

        for (final Method method : clazz.getMethods()) {
            final String name = method.getName();

            // Non-static methods with no parameters
            if (((method.getModifiers() & Modifier.STATIC) == 0)
                    && (method.getParameterTypes().length == 0)) {
                if ((
                // Getters
                (!method.getReturnType().equals(Void.class)) && (name.length() >= 4)
                        && (name.startsWith("get")) && (!name.equals("getClass")))
                        || (
                        // isXXX hasXXX methods
                        (method.getReturnType().equals(Boolean.TYPE)) && (((name.length() >= 3) && name
                                .startsWith("is")) || ((name.length() >= 3) && name
                                .startsWith("has"))))) {
                    log.debug("Calling {} on {} and {}", method.getName(), o1, o2);
                    final Object member1 = method.invoke(o1);
                    final Object member2 = method.invoke(o2);
                    if (!deepEquals(member1, member2)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // -----------------------
    // Helper factory methods

    /**
     * @return a test session attributes map.
     */
    protected Map<String, Object> buildSessionAttributes() {
        final Map<String, Object> sesssionAttributes = new HashMap<String, Object>();
        sesssionAttributes.put("my_session_attribute_key", "my_session_attribute_value");
        return sesssionAttributes;
    }

    /**
     * @return a test user attributes map.
     */
    protected Map<String, String> buildUserAttributes() {
        final Map<String, String> sesssionAttributes = new HashMap<String, String>();
        sesssionAttributes.put("my_user_attribute_key", "my_user_attribute_value");
        return sesssionAttributes;
    }

    /**
     * @return a test user authentication tokens map.
     */
    protected Map<String, String> buildUserAuthenticationTokens() {
        final Map<String, String> userAuthenticationTokens = new HashMap<String, String>();
        userAuthenticationTokens.put("my_access_token", "abcd");
        userAuthenticationTokens.put("my_refresh_token", "1234");
        return userAuthenticationTokens;
    }

    /**
     * Given a path like "tst/com/amazon/speech/json/ExceptionEncounteredRequest.normal.json", it
     * will read in that file and return its contents as a string. This mostly exists because it's a
     * pain to do multi-line strings in Java.
     */
    protected String readFileAsString(String filePath) {
        try {
            File f = new File(filePath);
            Scanner s = new Scanner(f);
            String endOfStringAnchor = "\\Z";
            String string = s.useDelimiter(endOfStringAnchor).next();
            s.close();
            return string;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
