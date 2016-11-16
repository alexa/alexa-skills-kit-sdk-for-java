/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.audioplayer;

/**
 * Represents a stream for an {@code AudioItem}.
 */
public class Stream {
    private String expectedPreviousToken;
    private String token;
    private String url;
    private long offsetInMilliseconds;

    /**
     * Returns the expected previous token, or {@code null} if not present. If present, the
     * {@code Play} directive will only execute if it matches the token of the active stream on the
     * device.
     * 
     * @return the expected previous token
     */
    public String getExpectedPreviousToken() {
        return expectedPreviousToken;
    }

    /**
     * This property is required to be set and allowed only when the {@code playBehavior} is
     * {@code ENQUEUE}. Setting this property when {@code playBehavior} is any other value (
     * {@code REPLACE_ALL} or {@code REPLACE_ENQUEUED}) causes an error.
     * 
     * @param expectedPreviousToken
     *            an opaque token that represents the expected previous stream.
     * 
     * @see Stream#getExpectedPreviousToken()
     */
    public void setExpectedPreviousToken(String expectedPreviousToken) {
        this.expectedPreviousToken = expectedPreviousToken;
    }

    /**
     * Returns the token associated with the {@code Stream}. The token is an arbitrary string that
     * may be used for identification purposes.
     * 
     * @see Stream#setExpectedPreviousToken(String)
     * 
     * @return the token associated with the {@code Stream}
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token associated with the {@code Stream}.
     * 
     * @see Stream#getToken()
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the URL for the stream.
     * 
     * @return the URL for the stream
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the stream.
     * 
     * @param url
     *            the URL of the stream
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the offset in milliseconds from the start of the stream
     * 
     * @return the offset in milliseconds from the start of the stream
     */
    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    /**
     * Set the offset in milliseconds from the start of the stream
     * 
     * @param offsetInMilliseconds
     *            the offset in milliseconds from the start of the stream
     */
    public void setOffsetInMilliseconds(final long offsetInMilliseconds) {
        this.offsetInMilliseconds = offsetInMilliseconds;
    }
}
