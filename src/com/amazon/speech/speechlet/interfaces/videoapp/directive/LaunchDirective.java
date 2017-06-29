/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.videoapp.directive;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.interfaces.videoapp.VideoItem;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * This class represents the directive to launch the video player application.
 */
@JsonTypeName("VideoApp.Launch")
public class LaunchDirective extends Directive {
    private VideoItem videoItem;

    /**
     * Returns the video item.
     *
     * @return the video item
     */
    public VideoItem getVideoItem() {
        return videoItem;
    }

    /**
     * Sets the video item.
     *
     * @param videoItem
     *            the video item
     */
    public void setVideoItem(VideoItem videoItem) {
        this.videoItem = videoItem;
    }
}
