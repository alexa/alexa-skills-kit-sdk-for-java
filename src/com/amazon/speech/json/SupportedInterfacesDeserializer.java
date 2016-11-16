/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.json;

import java.io.IOException;

import com.amazon.speech.speechlet.Interface;
import com.amazon.speech.speechlet.SupportedInterfaces;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

class SupportedInterfacesDeserializer extends StdDeserializer<SupportedInterfaces> {
    SupportedInterfacesDeserializer() {
        super(SupportedInterfaces.class);
    }

    @Override
    public SupportedInterfaces deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper underlyingMapper = ((ObjectMapper) parser.getCodec());
        JsonNode messageNode = parser.readValueAsTree();
        SupportedInterfaces.Builder supportedInterfacesBuilder = SupportedInterfaces.builder();

        for (SerializedInterface interfaceName : SerializedInterface.values()) {
            if (messageNode.has(interfaceName.name())) {
                Interface supportedInterface =
                        underlyingMapper.convertValue(messageNode.get(interfaceName.name()),
                                interfaceName.getInterfaceClass());
                supportedInterfacesBuilder.addSupportedInterface(supportedInterface);
            }
        }

        return supportedInterfacesBuilder.build();
    }
}
