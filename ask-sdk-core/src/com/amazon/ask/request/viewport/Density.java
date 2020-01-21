/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.viewport;

/**
 * Enum holds the DPI values for specific devices.
 * Dots per inch (DPI) is a measure of spatial printing, video or image scanner dot density, in particular the number of
 * individual dots that can be placed in a line within the span of 1 inch (2.54 cm).
 */
public enum Density {

    /**
     * Extra low.
     */
    XLOW,

    /**
     * Low.
     */
    LOW,

    /**
     * Medium.
     */
    MEDIUM,

    /**
     * High.
     */
    HIGH,

    /**
     * Extra High.
     */
    XHIGH,

    /**
     * Extra Extra High.
     */
    XXHIGH;
}
