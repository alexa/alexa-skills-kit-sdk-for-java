/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.videoapp;

/**
 * This class encapsulates a title and a subordinate title, and provides metadata to the video.
 *
 * @see VideoItem#setMetadata(Metadata)
 */
public class Metadata {
    private String title;

    private String subtitle;

    /**
     * Returns the title of video.
     *
     * @return the title of the video
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the subordinate title below main title of video.
     *
     * @return the subordinate title of video
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the title of video.
     *
     * @param title
     *            the title of video
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the subordinate title below main title for video.
     *
     * @param subtitle
     *            the subordinate title of video
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
