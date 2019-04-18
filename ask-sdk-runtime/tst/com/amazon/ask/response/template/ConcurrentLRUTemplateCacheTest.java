/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template;

import com.amazon.ask.response.template.loader.impl.ConcurrentLRUTemplateCache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ConcurrentLRUTemplateCacheTest {

    private static final String IDENTIFIER = "identifier";
    private static final int DEFAULT_CAPACITY = 1000 * 1000 * 5;
    private static final String BASE_DIR = "basedir";

    private ConcurrentLRUTemplateCache cache;
    private TemplateContentData smallTemplate;

    @Before
    public void setup() {
        cache = ConcurrentLRUTemplateCache.builder().withCapacity(DEFAULT_CAPACITY).build();
        smallTemplate = TemplateContentData.builder()
                .withIdentifier(IDENTIFIER)
                .withTemplateContent(new byte[10])
                .withTemplateBaseDir(BASE_DIR)
                .build();
    }

    @Test
    public void put_template_within_capacity() {
        assertTrue(cache.isEmpty());
        cache.put(IDENTIFIER, smallTemplate);
        assertEquals(cache.size(), 1);
    }

    @Test
    public void put_template_verify_remove_elder_when_out_of_capacity() {
        assertTrue(cache.isEmpty());
        cache.put(IDENTIFIER, smallTemplate);
        assertEquals(cache.size(), 1);
        String identifier2 = "identifier2";
        TemplateContentData largeTemplate = TemplateContentData.builder()
                .withIdentifier(identifier2)
                .withTemplateContent(new byte[DEFAULT_CAPACITY])
                .withTemplateBaseDir(BASE_DIR)
                .build();
        cache.put(identifier2, largeTemplate);
        assertEquals(cache.size(), 1);
        assertNull(cache.get(IDENTIFIER));
        assertNotNull(cache.get(identifier2));
    }

    @Test
    public void get_fresh_template() {
        cache.put(IDENTIFIER, smallTemplate);
        assertEquals(cache.get(IDENTIFIER), smallTemplate);
    }

    @Test
    public void get_stale_template_return_null() throws InterruptedException {
        ConcurrentLRUTemplateCache cache = ConcurrentLRUTemplateCache.builder()
                .withLiveTimeThreshold(0)
                .build();
        cache.put(IDENTIFIER, smallTemplate);
        assertNull(cache.get(IDENTIFIER));
    }

    @Test
    public void get_stale_template_verify_capacity_counter() {
        ConcurrentLRUTemplateCache cache = ConcurrentLRUTemplateCache.builder()
                .withLiveTimeThreshold(0)
                .build();
        cache.put(IDENTIFIER, smallTemplate);
        assertNotEquals(cache.getCurrentCapacity(), 0);
        assertNull(cache.get(IDENTIFIER));
        assertEquals(cache.getCurrentCapacity(), 0);
    }

}
