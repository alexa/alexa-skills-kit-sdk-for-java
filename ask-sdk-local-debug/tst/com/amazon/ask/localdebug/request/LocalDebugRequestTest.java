/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.request;

import com.amazon.ask.model.dynamicEndpoints.Request;
import com.amazon.ask.localdebug.util.RequestResponseUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocalDebugRequestTest {

    private final String validSampleData = "{\n" + "    \"version\": \"fooversion\",\n" + "    \"type\": \"SkillRequestMessage\",\n" + "    \"requestId\": \"foorequestid\",\n" + "    \"requestPayload\": \"foorequestpayload\"\n" + "}";

    @Test
    public void ValidRequestTest() {
        Request skillRequest = RequestResponseUtils.getDeserializeRequest(validSampleData);
        Assert.assertEquals("fooversion", skillRequest.getVersion());
        Assert.assertEquals("SkillRequestMessage", skillRequest.getType());
        Assert.assertEquals("foorequestid", skillRequest.getRequestId());
        Assert.assertEquals("foorequestpayload", skillRequest.getRequestPayload());
    }

}
