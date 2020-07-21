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

import com.amazon.ask.localdebug.config.ClientConfiguration;
import com.amazon.ask.localdebug.guice.ClientModule;
import com.amazon.ask.localdebug.guice.ConfigModule;
import com.amazon.ask.localdebug.util.ClientConfigurationBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 *
 */
public final class LocalDebuggerInvoker {
    /**
     * Private constructor.
     */
    private LocalDebuggerInvoker() {

    }
    /**
     * Main method invoked to start a debugging session. Bootstraps user provided configuration.
     * Invokes the web-socket client.
     *
     * @param args - user provided arguments.
     */
    public static void main(final String[] args) {
        ClientConfiguration clientConfiguration = ClientConfigurationBuilder.buildClientConfiguration(args);
        Injector injector = Guice.createInjector(new ConfigModule(clientConfiguration),
                new ClientModule());
        injector.getInstance(LocalDebuggerImpl.class).init();
    }
}
