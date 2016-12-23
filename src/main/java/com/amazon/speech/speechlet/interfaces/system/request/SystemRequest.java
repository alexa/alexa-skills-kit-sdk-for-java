/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system.request;

import java.util.Date;
import java.util.Locale;

import com.amazon.speech.speechlet.SpeechletRequest;

/**
 * A superclass for System requests.
 */
public class SystemRequest extends SpeechletRequest {
    protected SystemRequest(SpeechletRequestBuilder builder) {
        super(builder);
    }

    protected SystemRequest(final String requestId, final Date timestamp, final Locale locale) {
        super(requestId, timestamp, locale);
    }
}
