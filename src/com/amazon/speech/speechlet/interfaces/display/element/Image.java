/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.element;

import java.util.List;

/**
 * This class represents an image that can be embedded within a display template. When including the
 * image, you may provide a number of image instances with different sizes. The image size selected
 * will be the smallest possible size that matches the desired aspect ratio and gives a clear image
 * for the size of the screen where the image is being rendered. If an appropriately sized URL has
 * not been provided, the next larger image size will be used and scaled down.
 */
public class Image {
    private String contentDescription;

    @Deprecated
    private String smallSourceUrl;

    @Deprecated
    private String largeSourceUrl;

    private List<ImageInstance> sources;

    /**
     * Returns the content description for use by screen readers.
     *
     * @return the content description for use by screen readers
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * Sets the content description for use by screen readers.
     *
     * @param contentDescription
     *            the content description for use by screen readers
     */

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    /**
     * Returns the small source URL.
     *
     * @return the small source URL
     */
    public String getSmallSourceUrl() {
        return smallSourceUrl;
    }

    /**
     * Sets the small source URL.<b> This field is deprecated. Use {@link Image#setSources(List)}
     * instead </b>.
     *
     * @param smallSourceUrl
     *            the small source URL
     */
    @Deprecated
    public void setSmallSourceUrl(String smallSourceUrl) {
        this.smallSourceUrl = smallSourceUrl;
    }

    /**
     * Returns the large source URL.
     *
     * @return the large source URL
     */
    public String getLargeSourceUrl() {
        return largeSourceUrl;
    }

    /**
     * Sets the large source URL. <b> This field is deprecated. Use {@link Image#setSources(List)}
     * instead </b>.
     *
     * @param largeSourceUrl
     *            the large source URL
     */
    @Deprecated
    public void setLargeSourceUrl(String largeSourceUrl) {
        this.largeSourceUrl = largeSourceUrl;
    }

    /**
     * Gets the list of image instances.
     * 
     * @return the list of image instances
     */
    public List<ImageInstance> getSources() {
        return sources;
    }

    /**
     * Sets the list of image instances.
     * 
     * @param sources
     *            the list of image instances
     */
    public void setSources(List<ImageInstance> sources) {
        this.sources = sources;
    }
}
