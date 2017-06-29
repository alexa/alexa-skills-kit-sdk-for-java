/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.template;

import java.util.List;

/**
 * List templates contain a scrollable sequence of items with which a user may interact.
 *
 * @param <T>
 *            the specific type of list items included within a list template
 */
public abstract class ListTemplate<T extends ListItem> extends Template {
    /**
     * Returns the list of items.
     *
     * @return the list of items
     */
    public abstract List<T> getListItems();
}
