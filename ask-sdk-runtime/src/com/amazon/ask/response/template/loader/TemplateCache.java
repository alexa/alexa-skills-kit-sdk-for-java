/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template.loader;

import com.amazon.ask.response.template.TemplateContentData;

/**
 * Cache interface for template caching.
 */
public interface TemplateCache {

    /**
     * Retrieve {@link TemplateContentData} from cache.
     *
     * @param identifier template identifier
     * @return return {@link TemplateContentData} if cache hits
     */
    TemplateContentData get(String identifier);

    /**
     * Insert {@link TemplateContentData} into cache, assign identifier to entry.
     *
     * @param identifier template identifier
     * @param templateContentData {@link TemplateContentData}
     */
    void put(String identifier, TemplateContentData templateContentData);

}
