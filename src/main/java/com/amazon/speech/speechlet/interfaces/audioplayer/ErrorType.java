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
 * Enum representing the type of the {@code Error}
 * 
 * <dl>
 * <dt>MEDIA_ERROR_INTERNAL_DEVICE_ERROR</dt>
 * <dd>The device accepted the request, but was unable to process the request as expected.</dd>
 * <dt>MEDIA_ERROR_INTERNAL_SERVER_ERROR</dt>
 * <dd>The server accepted the request, but was unable to process the request as expected.</dd>
 * <dt>MEDIA_ERROR_INVALID_REQUEST</dt>
 * <dd>The server recognized the request as being malformed. E.g. bad request, unauthorized,
 * forbidden, not found, etc.</dd>
 * <dt>MEDIA_ERROR_SERVICE_UNAVAILABLE</dt>
 * <dd>The device was unable to reach the service.</dd>
 * <dt>MEDIA_ERROR_UNKNOWN</dt>
 * <dd>An unknown error occurred.</dd>
 * </dl>
 * 
 * @see Error
 */
public enum ErrorType {
    MEDIA_ERROR_INTERNAL_DEVICE_ERROR,
    MEDIA_ERROR_INTERNAL_SERVER_ERROR,
    MEDIA_ERROR_INVALID_REQUEST,
    MEDIA_ERROR_SERVICE_UNAVAILABLE,
    MEDIA_ERROR_UNKNOWN;
}
