/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.response.SkillResponse;
import com.amazon.ask.util.JsonMarshaller;
import com.amazon.ask.util.ValidationUtils;

import java.io.OutputStream;

public class BaseSkillResponse<T> implements SkillResponse<T> {

    private final JsonMarshaller<T> marshaller;
    private final T response;

    public BaseSkillResponse(JsonMarshaller<T> marshaller, T response) {
        this.marshaller = ValidationUtils.assertNotNull(marshaller, "marshaller");
        this.response = response;
    }

    @Override
    public boolean isPresent() {
        return response != null;
    }

    @Override
    public T getResponse() {
        return response;
    }

    @Override
    public byte[] getRawResponse() {
        if (response != null) {
            return marshaller.marshall(response);
        } else {
            throw new AskSdkException("Attempting to serialize empty response");
        }
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        if (response != null) {
            marshaller.marshall((response), outputStream);
        } else {
            throw new AskSdkException("Attempting to serialize empty response");
        }
    }

}
