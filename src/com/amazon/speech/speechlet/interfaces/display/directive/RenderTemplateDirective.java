/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.directive;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.interfaces.display.template.Template;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * This class represents the directive sent to render a template.
 */
@JsonTypeName("Display.RenderTemplate")
public class RenderTemplateDirective extends Directive {
    private Template template;

    /**
     * Returns the template to be rendered by this directive.
     *
     * @return the template to be rendered by this directive
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Sets the template to be rendered by this directive
     *
     * @param template
     *            the template to be rendered by this directive
     */
    public void setTemplate(Template template) {
        this.template = template;
    }
}
