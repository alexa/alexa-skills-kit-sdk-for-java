/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug;

import com.amazon.ask.localdebug.config.SkillInvokerConfiguration;
import com.amazon.ask.localdebug.client.WebSocketClientImpl;
import com.amazon.ask.localdebug.client.WebSocketClient;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link LocalDebugger} implementation.
 */
public class LocalDebuggerImpl implements LocalDebugger {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(LocalDebuggerImpl.class);

    /**
     * Web socket client.
     */
    private final WebSocketClient webSocketClient;
    /**
     * Skill invoker configuration determined via reflection.
     */
    private final SkillInvokerConfiguration skillInvokerConfiguration;

    /**
     * Local debugger implementation constructor.
     * Guice dependency injection used to configure {@link SkillInvokerConfiguration}
     * and {@link WebSocketClientImpl}.
     * @param skillInvokerConfiguration - reflection configuration for invoking skill code.
     * @param webSocketClient - web socket client to establish connection for debug session.
     */
    @Inject
    public LocalDebuggerImpl(final SkillInvokerConfiguration skillInvokerConfiguration,
                             final WebSocketClientImpl webSocketClient) {
        this.webSocketClient = webSocketClient;
        this.skillInvokerConfiguration = skillInvokerConfiguration;
    }

    @Override
    public void init() {
        webSocketClient.invoke();
    }
}
