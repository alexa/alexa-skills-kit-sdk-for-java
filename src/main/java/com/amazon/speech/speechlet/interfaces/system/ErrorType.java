/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.system;

/**
 * Represents the types of errors permissible
 * 
 * <dl>
 * <dt>INVALID_RESPONSE</dt>
 * <dd>The request was invalid. E.g. malformed, unauthorized, forbidden, not found, etc.</dd>
 * <dt>DEVICE_COMMUNICATION_ERROR</dt>
 * <dd>A problem occurred communicating with the device</dd>
 * <dt>INTERNAL_SERVICE_ERROR</dt>
 * <dd>The server was unable to process the request as expected</dd>
 * </dl>
 */
public enum ErrorType {
    INVALID_RESPONSE,
    DEVICE_COMMUNICATION_ERROR,
    INTERNAL_SERVICE_ERROR
}
