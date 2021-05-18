/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.lambdaContext;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * Empty lambda context used to initiate a local debugging session.
 */
public class DebugLambdaContext implements Context {

    /**
     * Defaults to returning null aws request Id.
     */
    @Override
    public String getAwsRequestId() {
        return null;
    }

    /**
     * Defaults to returning null log group name.
     */
    @Override
    public String getLogGroupName() {
        return null;
    }

    /**
     * Defaults to returning null log stream name.
     */
    @Override
    public String getLogStreamName() {
        return null;
    }

    /**
     * Defaults to returning null function name.
     */
    @Override
    public String getFunctionName() {
        return null;
    }

    /**
     * Defaults to returning null cognito identity.
     */
    @Override
    public String getFunctionVersion() {
        return null;
    }

    @Override
    public String getInvokedFunctionArn() {
        return null;
    }

    @Override
    public CognitoIdentity getIdentity() {
        return null;
    }

    /**
     * Defaults to returning null client context.
     */
    @Override
    public ClientContext getClientContext() {
        return null;
    }

    /**
     * Defaults to returning remaining time in milli seconds as 0.
     */
    @Override
    public int getRemainingTimeInMillis() {
        return 0;
    }

    /**
     * Defaults to returning memory limit of 0 MB.
     */
    @Override
    public int getMemoryLimitInMB() {
        return 0;
    }

    /**
     * Defaults to returning null lambda logger.
     */
    @Override
    public LambdaLogger getLogger() {
        return null;
    }
}

