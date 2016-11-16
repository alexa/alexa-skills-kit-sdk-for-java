/*
    Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.amazon.speech.json.SpeechletRequestEnvelope;

/**
 * This class is intended for internal use only.
 *
 * It adapts a {@link Speechlet} into a {@link SpeechletV2}.
 */
public class SpeechletToSpeechletV2Adapter implements SpeechletV2 {
    private final Speechlet speechlet;

    public SpeechletToSpeechletV2Adapter(Speechlet speechlet) {
        this.speechlet = speechlet;
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        try {
            speechlet.onSessionStarted(requestEnvelope.getRequest(), requestEnvelope.getSession());
        } catch (SpeechletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        try {
            return speechlet.onLaunch(requestEnvelope.getRequest(), requestEnvelope.getSession());
        } catch (SpeechletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        try {
            return speechlet.onIntent(requestEnvelope.getRequest(), requestEnvelope.getSession());
        } catch (SpeechletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        try {
            speechlet.onSessionEnded(requestEnvelope.getRequest(), requestEnvelope.getSession());
        } catch (SpeechletException e) {
            throw new RuntimeException(e);
        }
    }

    public Speechlet getSpeechlet() {
        return speechlet;
    }
}
