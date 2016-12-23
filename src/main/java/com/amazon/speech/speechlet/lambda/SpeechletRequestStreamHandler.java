/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletToSpeechletV2Adapter;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

/**
 * This class provides the handler required when hosting the service as an AWS Lambda function.
 *
 * <p>
 * Lambda functions written in Java must have a {@code RequestStreamHandler} class for the handler.
 * That handler must include a zero argument constructor and implement {@code RequestStreamHandler}.
 *
 * <p>
 * The {@code SpeechletRequestStreamHandler} abstract class can be used to create such a class.
 * Extend {@code SpeechletRequestStreamHandler} and implement a new zero-argument constructor. Pass
 * the appropriate {@code SpeechletV2} and {@code Set} of supported {@code ApplicationId}s to the
 * super constructor from your new constructor.
 *
 * <p>
 * When configuring your Lambda function in the AWS Lambda console, specify your new class as the
 * <strong>Handler</strong>.
 */
public abstract class SpeechletRequestStreamHandler implements RequestStreamHandler {
    private final SpeechletV2 speechlet;
    private final SpeechletRequestHandler speechletRequestHandler;

    /**
     * When extending this class, use a zero argument constructor and pass the appropriate
     * {@code SpeechletV2} and {@code Set} of supported {@code ApplicationId}s to this method.
     *
     * @param speechlet
     *            the {@code SpeechletV2} that handles the requests
     * @param supportedApplicationIds
     *            a {@code Set} of supported {@code ApplicationId}s used to validate that the
     *            requests are intended for your service
     */
    public SpeechletRequestStreamHandler(SpeechletV2 speechlet, Set<String> supportedApplicationIds) {
        this.speechlet = speechlet;
        this.speechletRequestHandler = new LambdaSpeechletRequestHandler(supportedApplicationIds);
    }

    public SpeechletRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
        this(new SpeechletToSpeechletV2Adapter(speechlet), supportedApplicationIds);
    }

    /**
     * This method is the primary entry point when executing your Lambda function. The configured
     * {@code SpeechletRequestHandler} determines the type of request and dispatches the request to
     * the configured {@code SpeechletV2}.
     *
     * <p>
     * Any errors that occur in either the {@code SpeechletV2} or the {@code SpeechletRequestHandler}
     * are converted into a {@code RuntimeException}, causing the Lambda call to fail. Details on
     * the failure are then available in the Lambda console logs within CloudWatch. {@inheritDoc}
     */
    @Override
    public final void handleRequest(InputStream input, OutputStream output, Context context)
            throws IOException {
        byte[] serializedSpeechletRequest = IOUtils.toByteArray(input);
        byte[] outputBytes;
        try {
            outputBytes =
                    speechletRequestHandler.handleSpeechletCall(speechlet,
                            serializedSpeechletRequest);
        } catch (SpeechletRequestHandlerException | SpeechletException ex) {
            throw new RuntimeException(ex);
        }

        output.write(outputBytes);
    }
}
