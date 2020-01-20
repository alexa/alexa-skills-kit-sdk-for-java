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
 * Determines the device a request originated from.
 */
public enum ViewportProfile {
    /**
     * Hub Round small.
     */
    HUB_ROUND_SMALL,

    /**
     * Hub Landscape small.
     */
    HUB_LANDSCAPE_SMALL,

    /**
     * Hub Landscape medium.
     */
    HUB_LANDSCAPE_MEDIUM,

    /**
     * Hub Landscape large.
     */
    HUB_LANDSCAPE_LARGE,

    /**
     * Mobile Landscape small.
     */
    MOBILE_LANDSCAPE_SMALL,

    /**
     * Mobile Portrait small.
     */
    MOBILE_PORTRAIT_SMALL,

    /**
     * Mobile Landscape medium.
     */
    MOBILE_LANDSCAPE_MEDIUM,

    /**
     * Mobile Portrait medium.
     */
    MOBILE_PORTRAIT_MEDIUM,

    /**
     * TV landscape XLarge.
     */
    TV_LANDSCAPE_XLARGE,

    /**
     * TV Portrait medium.
     */
    TV_PORTRAIT_MEDIUM,

    /**
     * TV landscape medium.
     */
    TV_LANDSCAPE_MEDIUM,

    /**
     * Unknown viewport.
     */
    UNKNOWN_VIEWPORT_PROFILE;
}
