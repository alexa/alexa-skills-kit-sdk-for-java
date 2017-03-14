/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.ui;

/**
 * Contains information about an image that is going to be displayed on a card.
 */
public class Image {
    private String smallImageUrl;
    private String largeImageUrl;

    /**
     * Returns the URL of the small image.
     *
     * @return the URL of the small image
     */
    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    /**
     * Sets the URL of the small image.
     *
     * @param smallImageUrl
     *            the URL to be set
     */
    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    /**
     * Returns the URL of the large image.
     *
     * @return the URL of the large image
     */
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    /**
     * Sets the URL of the large image.
     * 
     * @param largeImageUrl
     *            the URL to be set
     */
    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }
}
