/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response.template;

import com.amazon.ask.exception.template.TemplateFactoryException;

import java.util.Map;

/**
 * Template Factory interface to process template and data to generate skill response.
 * @param <Input> Skill input type.
 * @param <Output> Skill output type.
 */
public interface TemplateFactory<Input, Output> {

    /**
     * Process response template and data to generate skill response.
     *
     * @param responseTemplateName template name
     * @param dataMap map that contains template injecting data
     * @param input skill request input
     * @return skill response output
     * @throws TemplateFactoryException if fail to process template
     */
    Output processTemplate(String responseTemplateName, Map<String, Object> dataMap, Input input) throws TemplateFactoryException;

}
