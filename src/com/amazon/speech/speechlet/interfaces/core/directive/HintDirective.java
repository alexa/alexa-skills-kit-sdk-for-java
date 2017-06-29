/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.core.directive;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.interfaces.core.Hint;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The hint directive renders a hint to customer when there is an appropriate rendering mechanism.
 */
@JsonTypeName("Hint")
public class HintDirective extends Directive {
    private Hint hint;

    /**
     * Returns the hint.
     *
     * @return the hint
     */
    public Hint getHint() {
        return hint;
    }

    /**
     * Sets the hint to be rendered.
     *
     * @param hint
     *            the hint to render
     */
    public void setHint(Hint hint) {
        this.hint = hint;
    }
}
