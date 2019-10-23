/*
    Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.util.impl;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.util.JsonUnmarshaller;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Ignore
public class BaseUnmarshallerTest {

    protected JsonUnmarshaller<BaseFoo> jsonUnmarshaller;
    protected Map<String, Class> subtypes;

    protected void setUp() {
        subtypes = new HashMap<>();
        subtypes.put("foo.member", FooMember.class);
        subtypes.put("foo.sub", SubFoo.class);
    }

    @Test(expected = AskSdkException.class)
    public void unmarshaller_exception_wrapped_in_sdk_exception() {
        jsonUnmarshaller.unmarshall("{foo}".getBytes());
    }


    protected static class BaseFoo {
        private Map<String, String> header;

        private FooMember foo;

        public Map<String, String> getHeader() {
            return header;
        }

        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        public FooMember getFoo() { return foo; }

        public void setFoo(FooMember foo) { this.foo = foo; }
    }

    protected static class FooMember {
        private String validField;

        public String getValidField() {
            return validField;
        }

        public void setValidField(String validField) {
            this.validField = validField;
        }
    }

    protected static class SubFoo extends BaseFoo {
        private String validField;

        public String getValidField() { return validField; }

        public void setValidField(String validField) { this.validField = validField; }
    }

}
