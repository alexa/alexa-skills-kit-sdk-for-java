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

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.SupportedInterfaces;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

class SpeechletRequestBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    @SuppressWarnings("unchecked")
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
            JsonSerializer<?> serializer) {
        if (Context.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new ContextSerializer();
        } else if (SupportedInterfaces.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new SupportedInterfacesSerializer();
        } else {
            return serializer;
        }
    }
}
