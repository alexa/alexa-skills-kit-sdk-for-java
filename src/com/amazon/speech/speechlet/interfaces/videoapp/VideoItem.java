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
 * This class represents a single video item. The
 * {@link com.amazon.speech.speechlet.interfaces.videoapp.directive.LaunchDirective} will contain a
 * {@code VideoItem} that will be played on user devices. The video item will contain the source and
 * the metadata information of the video.
 *
 * @see com.amazon.speech.speechlet.interfaces.videoapp.directive.LaunchDirective#setVideoItem(VideoItem)
 */
public class VideoItem {
    private String source;

    private Metadata metadata;

    /**
     * Returns the video source.
     *
     * @return the video source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the video source, which must be an HTTPS URL to the content.
     *
     * @param source
     *            the video source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Returns the video metadata.
     *
     * @return the video metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Sets the video metadata. This metadata may be displayed on-device.
     *
     * @param metadata
     *            the video metadata
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
