/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.slu.entityresolution;


/**
 * Indication of the results of attempting to resolve the user utterance against the defined slot types.
 * This can be one of the following:
 * ER_SUCCESS_MATCH: The spoken value matched a value or synonym explicitly defined in your custom slot type.
 * ER_SUCCESS_NO_MATCH: The spoken value did not match any values or synonyms explicitly defined in your custom slot type.
 * ER_ERROR_TIMEOUT: An error occurred due to a timeout.
 * ER_ERROR_EXCEPTION: An error occurred due to an exception during processing.
 */
public enum StatusCode {
    ER_SUCCESS_MATCH,
    ER_SUCCESS_NO_MATCH,
    ER_ERROR_TIMEOUT,
    ER_ERROR_EXCEPTION
}
