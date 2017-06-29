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

/**
 * An image resource to be displayed with optional width, height and size properties. See
 * {@link Image#setSources(java.util.List)} for usage.
 */
public class ImageInstance {
    private String url;
    private String size;
    private int widthPixels;
    private int heightPixels;

    /**
     * Returns the image URL.
     * 
     * @return the image URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the image URL.
     * 
     * @param url
     *            the image URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the image size.
     * 
     * @return the image size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the image size.
     * 
     * @param size
     *            the image size
     */
    public void setSize(ImageSize size) {
        this.size = size.getSize();
    }

    /**
     * Gets the image width in pixels.
     * 
     * @return the image width
     */
    public int getWidthPixels() {
        return widthPixels;
    }

    /**
     * Sets the image width in pixels. <i> If not known do not populate as this should be the exact
     * image dimensions.</i>
     * 
     * @param widthPixels
     *            the image width
     */
    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }

    /**
     * Gets the image height in pixels.
     * 
     * @return the image height
     */
    public int getHeightPixels() {
        return heightPixels;
    }

    /**
     * Sets the image height in pixels. <i> If not known do not populate as this should be the exact
     * image dimensions.</i>
     * 
     * @param heightPixels
     *            the image height
     */
    public void setHeightPixels(int heightPixels) {
        this.heightPixels = heightPixels;
    }
}
