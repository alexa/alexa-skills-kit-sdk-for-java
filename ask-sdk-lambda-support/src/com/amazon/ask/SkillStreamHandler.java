/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.util.JacksonSerializer;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * This class provides the handler required when hosting the service as an AWS Lambda function.
 *
 * <p>
 * Lambda functions written in Java must have a {@code RequestStreamHandler} class for the handler.
 * That handler must include a zero argument constructor and implement {@code RequestStreamHandler}.
 *
 * <p>
 * The {@code SkillStreamHandler} abstract class can be used to create such a class that extends
 * {@code RequestStreamHandler}
 *
 * <p>
 * When configuring your Lambda function in the AWS Lambda console, specify your new class as the
 * <strong>Handler</strong>.
 */
public abstract class SkillStreamHandler implements RequestStreamHandler {
    private final Skill skill;
    private final Serializer serializer;

    public SkillStreamHandler(Skill skill) {
        this.skill = skill;
        this.serializer = new JacksonSerializer();
    }

    /**
     * This method is the primary entry point when executing your Lambda function. The configured
     * {@code SkillStreamHandler} determines the type of request and passes the request to
     * the configured {@code Skill}.
     *
     * <p>
     * Any errors that occur in either the {@code Skill} or the {@code SkillStreamHandler}
     * are converted into a {@code RuntimeException}, causing the Lambda call to fail. Details on
     * the failure are then available in the Lambda console logs within CloudWatch. {@inheritDoc}
     */
    @Override
    public final void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        String serializedRequest = IOUtils.toString(input, StandardCharsets.UTF_8);
        RequestEnvelope requestEnvelope = serializer.deserialize(serializedRequest, RequestEnvelope.class);
        ResponseEnvelope response = skill.invoke(requestEnvelope);
        byte[] serializedResponse = serializer.serialize(response).getBytes(StandardCharsets.UTF_8);
        output.write(serializedResponse);
    }
}
