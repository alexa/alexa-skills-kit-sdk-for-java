/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.smapi.utils;

import com.amazon.ask.model.services.Pair;
import com.amazon.ask.utils.Utils;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    private static final String KEY = "key";

    @Test
    public void should_return_empty_list_for_null_headers_list() {
        assertEquals(Utils.getValuesByKey(null, KEY).size(), 0);
    }

    @Test
    public void should_return_empty_list_for_null_key() {
        assertEquals(Utils.getValuesByKey(Collections.emptyList(), null).size(), 0);
    }

    @Test
    public void should_return_empty_list_for_empty_header_list() {
        assertEquals(Utils.getValuesByKey(Collections.emptyList(), KEY).size(), 0);
    }

    @Test
    public void should_return_value_as_list_for_single_key_occurance() {
        final List<Pair<String, String>> headers = new LinkedList<>();
        headers.add(new Pair<>("key", "value"));
        headers.add(new Pair<>("key1", "value1"));
        final List<String> valuesWithKey = Utils.getValuesByKey(headers, KEY);
        assertEquals(valuesWithKey.size(), 1);
        assertEquals("value", valuesWithKey.get(0));
    }

    @Test
    public void should_return_value_as_list_for_multiple_key_occurance() {
        final List<Pair<String, String>> headers = new LinkedList<>();
        headers.add(new Pair<>("key", "value"));
        headers.add(new Pair<>("key", "value0"));
        headers.add(new Pair<>("key1", "value1"));
        final List<String> valuesWithKey = Utils.getValuesByKey(headers, KEY);
        assertEquals(valuesWithKey.size(), 2);
        assertEquals("value", valuesWithKey.get(0));
        assertEquals("value0", valuesWithKey.get(1));
    }
}
