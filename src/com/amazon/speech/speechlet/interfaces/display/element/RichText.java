/*
    Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet.interfaces.display.element;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * <p>
 * This class represents a rich text field that can be embedded within a display template.
 * </p>
 *
 * Supported markup includes:
 * <table summary="Supported Markup" border="1px solid black" cellspacing="0px">
 * <tr>
 * <td><b>Name</b></td>
 * <td><b>Element</b></td>
 * <td><b>Example Markup</b></td>
 * <td><b>Output</b></td>
 * </tr>
 * <tr>
 * <td><b>Line Break</b></td>
 * <td>{@code <br/>}</td>
 * <td>{@code First line<br/>
 * Second line}</td>
 * <td>
 * First line<br>
 * Second line</td>
 * </tr>
 * <tr>
 * <td><b>Bold</b></td>
 * <td>{@code <b>}</td>
 * <td>{@code This is a <b>ladybird</b> beetle}</td>
 * <td>This is a <b>ladybird</b> beetle</td>
 * </tr>
 * <tr>
 * <td><b>Italics</b></td>
 * <td>{@code <i>}</td>
 * <td>{@code Scientific name <i>Coccienellidae</i>}</td>
 * <td>Scientific name <i>Coccienellidae</i></td>
 * </tr>
 * <tr>
 * <td><b>Underline</b></td>
 * <td>{@code <u>}</td>
 * <td>{@code Always <u>feed</u> your ladybird tasty aphids.}</td>
 * <td>Always <u>feed</u> your ladybird tasty aphids.</td>
 * </tr>
 * <tr>
 * <td><b>Font</b></td>
 * <td>{@code <font>}</td>
 * <td>
 * {@code <font size="1">Font size 1 <!--28px--></font>}<br>
 * {@code <font size="2">Font size 2 <!--28px--></font>}<br>
 * {@code <font size="3">Font size 3 <!--32px--></font>}<br>
 * {@code <font size="4">Font size 4 <!--32px--></font>}<br>
 * {@code <font size="5">Font size 5 <!--32px--></font>}<br>
 * {@code <font size="6">Font size 6 <!--32px--></font>}<br>
 * {@code <font size="7">Font size 7 <!--68px--></font>}<br>
 * <td>
 * <p style="font-size:28px;">
 * Font size 1<br>
 * Font size 2<br>
 * </p>
 * <p style="font-size:32px;">
 * Font size 3<br>
 * Font size 4<br>
 * Font size 5<br>
 * Font size 6
 * </p>
 * <p style="font-size:68px;">
 * Font size 7
 * </p>
 * </td>
 * <tr>
 * </tr>
 * <tr>
 * <td><b>Action</b></td>
 * <td>{@code <action token="VALUE"></action>}</td>
 * <td>{@code Learn the <action token="2347">history</action> of ladybirds.}</td>
 * <td>Learn the <font style="color:blue;">history</font> of ladybirds.</td>
 * </tr>
 * </table>
 */
@JsonTypeName("RichText")
public class RichText implements TextField {
    private String richText;

    @Override
    public String getText() {
        return richText;
    }

    /**
     * Sets the rich text on this field.
     *
     * @param richText
     *            the rich text on this field
     */
    public void setText(String richText) {
        this.richText = richText;
    }
}
