/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.util;

import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import com.amazon.ask.model.dynamicEndpoints.Request;
import com.amazon.ask.model.dynamicEndpoints.FailureResponse;
import com.amazon.ask.model.dynamicEndpoints.SuccessResponse;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.model.services.util.JacksonSerializer;
import com.amazon.ask.localdebug.lambdaContext.DebugLambdaContext;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for ask-sdk-local-debug module.
 */
public final class RequestResponseUtils {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseUtils.class);
    /**
     * Serializer instance to be used to serialize or deserialize request or response.
     */
    private static final Serializer SERIALIZER = new JacksonSerializer();

    /**
     * Private constructor.
     */
    private RequestResponseUtils() {
    }

    /**
     * Builds a failure response for runtime exception encountered when invoking skill code.
     *
     * @param localDebugRequest - skill request parameter of type {@link Request}.
     * @param ex - exception object available when skill invocation results in an error.
     * @return skill failure response of type {@link FailureResponse}.
     */
    public static FailureResponse getLocalDebugFailureResponse(
            final Request localDebugRequest, final Exception ex) {
        return FailureResponse.builder()
                .withVersion(localDebugRequest.getVersion())
                .withOriginalRequestId(localDebugRequest.getRequestId())
                /**
                 * Any exception encountered when invoking skill code, is relayed
                 * back as a '500' error.
                 */
                .withErrorCode(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
                .withErrorMessage(ex.toString())
                .build();
    }

    /**
     * Builds a success response for response payload obtained when invoking skill code.
     *
     * @param skillSuccessResponse -  skill response.
     * @param localDebugRequest      - skill request parameter of type {@link Request}.
     * @return skill success response of type {@link SuccessResponse}.
     */
    public static SuccessResponse getLocalDebugSuccessResponse(
            final Request localDebugRequest,
            final String skillSuccessResponse) {
        return SuccessResponse.builder()
                .withOriginalRequestId(localDebugRequest.getRequestId())
                .withResponsePayload(skillSuccessResponse)
                .withVersion(localDebugRequest.getVersion())
                .build();
    }

    /**
     * Invokes skill code with skill request payload.
     *
     * @param localDebugRequest - skill request parameter of type {@link Request}.
     * @param skillInvokerConfiguration - Reflection configuration to invoke skill code.
     * @return response payload.
     */
    public static String getSkillResponse(final Request localDebugRequest,
                                          final SkillInvokerConfiguration skillInvokerConfiguration) {
        try (ByteArrayOutputStream skillResponseByteArray = new ByteArrayOutputStream()) {
            switch (skillInvokerConfiguration.getType()) {
                case SKILL_SERVLET_TYPE:
                    ReflectionUtils.callMethodAndGetResult(skillInvokerConfiguration.getSkillInvokerMethod(),
                            skillInvokerConfiguration.getSkillInvokerInstance(),
                            new ByteArrayInputStream(
                                    localDebugRequest.getRequestPayload()
                                            .getBytes(StandardCharsets.UTF_8)) {
                                @Override
                                public void close() throws IOException {
                                    super.close();
                                }
                            }, skillResponseByteArray);
                    break;
                case SKILL_STREAM_HANDLER_TYPE:
                    ReflectionUtils.callMethodAndGetResult(skillInvokerConfiguration.getSkillInvokerMethod(),
                            skillInvokerConfiguration.getSkillInvokerInstance(),
                            new ByteArrayInputStream(
                                    localDebugRequest.getRequestPayload()
                                            .getBytes(StandardCharsets.UTF_8)) {
                                @Override
                                public void close() throws IOException {
                                    super.close();
                                }
                            }, skillResponseByteArray, new DebugLambdaContext());
                    break;
                default:
                    final String errorMessage = String.format("Unknown skill configuration type - %s",
                            skillInvokerConfiguration.getType());
                    LOG.error(errorMessage);
                    throw new LocalDebugSdkException(errorMessage);
            }
            final String skillResponse = skillResponseByteArray.toString(StandardCharsets.UTF_8.toString());
            SuccessResponse successResponse = getLocalDebugSuccessResponse(localDebugRequest, skillResponse);
            return SERIALIZER.serialize(successResponse);
        } catch (Exception ex) {
            LOG.error("Encountered exception when invoking skill code", ex);
            FailureResponse failureResponse = getLocalDebugFailureResponse(localDebugRequest, ex);
            return SERIALIZER.serialize(failureResponse);
        }
    }

    /**
     * Deserialize the incoming request payload into type {@link Request}.
     *
     * @param skillRequestPayload - incoming skill request payload.
     * @return serialized request of {@link Request}.
     */
    public static Request getDeserializeRequest(final String skillRequestPayload) {
        return SERIALIZER.deserialize(skillRequestPayload, Request.class);
    }

}

