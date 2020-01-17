/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.loader.impl;

import static org.slf4j.LoggerFactory.getLogger;

import com.amazon.ask.response.template.TemplateContentData;
import com.amazon.ask.response.template.loader.TemplateCache;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@inheritDoc}.
 *
 * {@link TemplateCache} implementation to cache {@link TemplateContentData} using LRU replacement policy based on access order.
 * If no capacity specified, use default value of 5 MB.
 * If no time to live threshold specified, use default value of 1 day.
 */
public class ConcurrentLRUTemplateCache implements TemplateCache {

    /**
     * Default cache capacity.
     */
    private static final long DEFAULT_CAPACITY = 1000 * 1000 * 5;

    /**
     * Default TTL for a cache entry.
     */
    private static final long DEFAULT_TIME_TO_LIVE_THRESHOLD = 1000 * 60 * 60 * 24;

    /**
     * Initial cache queue capacity.
     */
    private static final int INITIAL_QUEUE_CAPACITY = 11;

    /**
     * Logger to log information used for debugging purposes.
     */
    private static final Logger LOGGER = getLogger(ConcurrentLRUTemplateCache.class);

    /**
     * Custom capacity.
     */
    protected final long capacity;

    /**
     * Custom TTL.
     */
    protected final long timeToLiveThreshold;

    /**
     * Template data map.
     */
    protected final Map<String, AccessOrderedTemplateContentData> templateDataMap;

    /**
     * Template order queue.
     */
    protected final Queue<AccessOrderedTemplateContentData> templateOrderQueue;

    /**
     * Counter to estimate current cache occupancy.
     */
    private AtomicInteger capacityCounter;

    /**
     * Map to store locks on cache units.
     */
    private Map<String, Object> locksMap;

    /**
     * Constructor for ConcurrentLRUTemplateCache.
     * @param capacity custom capacity.
     * @param timeToLiveThreshold custom TTL.
     */
    protected ConcurrentLRUTemplateCache(final long capacity, final long timeToLiveThreshold) {
        this.capacity = capacity;
        this.timeToLiveThreshold = timeToLiveThreshold;
        this.templateDataMap = new ConcurrentHashMap<>();
        this.templateOrderQueue = new PriorityBlockingQueue<>(INITIAL_QUEUE_CAPACITY,
                (AccessOrderedTemplateContentData data1, AccessOrderedTemplateContentData data2) ->
                        (int) (data1.getAccessTimestamp() - data2.getAccessTimestamp()));
        this.capacityCounter = new AtomicInteger(0);
        this.locksMap = new ConcurrentHashMap<>();
    }

    /**
     * Static method which builds an instance of Builder.
     * @return {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get cache size.
     *
     * @return cache size
     */
    public int size() {
        int cacheSize = templateDataMap.size();
        return cacheSize;
    }

    /**
     * Check whether cache is empty.
     *
     * @return true if cache is empty
     */
    public boolean isEmpty() {
        return templateDataMap.isEmpty();
    }

    /**
     * Get cache current capacity.
     * @return cache current capacity
     */
    public int getCurrentCapacity() {
        return this.capacityCounter.get();
    }

    /**
     * {@inheritDoc}
     *
     * If template size is larger than total cache capacity, no caching.
     * If there's not enough capacity for new entry, remove eldest ones until have capacity to insert.
     */
    @Override
    synchronized public void put(final String identifier, final TemplateContentData templateContentData) {
        if (!locksMap.containsKey(identifier)) {
            locksMap.put(identifier, new Object());
        }
        Object lock = locksMap.get(identifier);
        synchronized (lock) {
            int size = templateContentData.getTemplateContent().length;
            if (size > capacity) {
                LOGGER.warn(String.format("No caching for template with size: %s larger than total capacity: %s.", size, capacity));
                return;
            }
            if (templateDataMap.containsKey(identifier)) {
                LOGGER.info(String.format("Try to put the same template with identifier: %s into cache, removing duplicate entry in queue.",
                        identifier));
                templateOrderQueue.remove(templateDataMap.get(identifier));
            }
            while (size + capacityCounter.get() > capacity) {
                AccessOrderedTemplateContentData eldest = templateOrderQueue.poll();
                TemplateContentData eldestTemplate = eldest.getTemplateContentData();
                templateDataMap.remove(eldestTemplate.getIdentifier());
                deductAndGet(eldestTemplate.getTemplateContent().length);
            }
            AccessOrderedTemplateContentData data = AccessOrderedTemplateContentData.builder()
                    .withTemplateContentData(templateContentData)
                    .build();
            templateOrderQueue.offer(data);
            templateDataMap.put(identifier, data);
            capacityCounter.addAndGet(size);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link TemplateContentData} if exists and it is fresh, otherwise return null
     */
    @Override
    public TemplateContentData get(final String identifier) {
        Object lock = locksMap.get(identifier);
        if (lock != null) {
            synchronized (lock) {
                AccessOrderedTemplateContentData data = templateDataMap.get(identifier);
                if ((data != null) && templateOrderQueue.contains(data)) {
                    templateOrderQueue.remove(data);
                    if (isFresh(data)) {
                        TemplateContentData templateContentData = data.getTemplateContentData();
                        templateOrderQueue.offer(data);
                        return templateContentData;
                    }
                    templateDataMap.remove(identifier);
                    deductAndGet(data.getTemplateContentData().getTemplateContent().length);
                    LOGGER.warn(String.format("Template: %s is out of date, removing.", identifier));
                }
            }
        }
        return null;
    }

    /**
     * Validates a cache entry.
     * @param data Template content data.
     * @return true is a cache entry is valid.
     */
    private boolean isFresh(final AccessOrderedTemplateContentData data) {
        long current = System.currentTimeMillis();
        long dataTimestamp = data.getAccessTimestamp();
        return (current - dataTimestamp) < timeToLiveThreshold;
    }

    /**
     * Get current cache remaining capacity.
     * @param delta difference between initial size and current size.
     * @return remaining capacity.
     */
    private int deductAndGet(final int delta) {
        return capacityCounter.addAndGet(Math.negateExact(delta));
    }

    /**
     * Concurrent LRU Template Cache Builder.
     */
    public static class Builder {
        /**
         * Custom cache capacity.
         */
        private long capacity = DEFAULT_CAPACITY;

        /**
         * Custom TTL.
         */
        private long timeToLiveThreshold = DEFAULT_TIME_TO_LIVE_THRESHOLD;

        /**
         * Add custom cache capacity to ConcurrentLRUTemplateCache.
         * @param capacity custom capacity.
         * @return {@link Builder}.
         */
        public Builder withCapacity(final long capacity) {
            this.capacity = capacity;
            return this;
        }

        /**
         * Add custom TTL to ConcurrentLRUTemplateCache.
         * @param liveTimeThreshold custom TTL.
         * @return {@link Builder}.
         */
        public Builder withLiveTimeThreshold(final long liveTimeThreshold) {
            this.timeToLiveThreshold = liveTimeThreshold;
            return this;
        }

        /**
         * Builder method to build an instance of ConcurrentLRUTemplateCache.
         * @return {@link ConcurrentLRUTemplateCache}.
         */
        public ConcurrentLRUTemplateCache build() {
            return new ConcurrentLRUTemplateCache(this.capacity, this.timeToLiveThreshold);
        }
    }

}
