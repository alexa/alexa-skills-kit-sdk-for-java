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

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.State;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

class ContextDeserializer extends StdDeserializer<Context> {
    ContextDeserializer() {
        super(Context.class);
    }

    @Override
    public Context deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper underlyingMapper = ((ObjectMapper) parser.getCodec());
        JsonNode messageNode = parser.readValueAsTree();
        Context.Builder contextBuilder = Context.builder();

        for (SerializedInterface interfaceName : SerializedInterface.values()) {
            if (messageNode.has(interfaceName.name())) {
                State<?> state =
                        underlyingMapper.convertValue(messageNode.get(interfaceName.name()),
                                interfaceName.getStateClass());
                contextBuilder.addState(state);
            }
        }

        return contextBuilder.build();
    }
}
