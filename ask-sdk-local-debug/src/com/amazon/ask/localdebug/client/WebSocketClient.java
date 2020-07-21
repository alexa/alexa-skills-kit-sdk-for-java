/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.client;

/**
 * Represents a basic contract for websocket request execution.
 */
public interface WebSocketClient {
    /**
     * Invokes the underlying web-socket client to initiate a connection.
     */
    void invoke();

    /**
     * Sends skill response over the established web-socket connection.
     *
     * @param localDebugASKResponse sends response of the serialized
     * {@link com.amazon.ask.model.dynamicEndpoints.SuccessResponse}
     * or {@link com.amazon.ask.model.dynamicEndpoints.FailureResponse}
     * over the established web-socket connection.
     */
    void sendSkillResponse(String localDebugASKResponse);
}
