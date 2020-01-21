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

/**
 * Skill Response implements {@link SkillResponse} and provides utility methods to check existence of response, fetch
 * response and raw response.
 * @param <T> output type.
 */
public class BaseSkillResponse<T> implements SkillResponse<T> {

    /**
     * JSON marshaller.
     */
    private final JsonMarshaller<T> marshaller;

    /**
     * Response of type T.
     */
    private final T response;

    /**
     * Constructor to build an instance of BaseSkillResponse.
     * @param marshaller JSON marshaller.
     * @param response response of type T.
     */
    public BaseSkillResponse(final JsonMarshaller<T> marshaller, final T response) {
        this.marshaller = ValidationUtils.assertNotNull(marshaller, "marshaller");
        this.response = response;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean isPresent() {
        return response != null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public T getResponse() {
        return response;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public byte[] getRawResponse() {
        if (response != null) {
            return marshaller.marshall(response);
        } else {
            throw new AskSdkException("Attempting to serialize empty response");
        }
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void writeTo(final OutputStream outputStream) {
        if (response != null) {
            marshaller.marshall((response), outputStream);
        } else {
            throw new AskSdkException("Attempting to serialize empty response");
        }
    }

}
