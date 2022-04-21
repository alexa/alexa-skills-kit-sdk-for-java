/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.amazon.ask.model.Directive;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.canfulfill.CanFulfillIntent;
import com.amazon.ask.model.dialog.ConfirmIntentDirective;
import com.amazon.ask.model.dialog.ConfirmSlotDirective;
import com.amazon.ask.model.dialog.DelegateDirective;
import com.amazon.ask.model.dialog.ElicitSlotDirective;
import com.amazon.ask.model.interfaces.alexa.experimentation.ExperimentTriggerResponse;
import com.amazon.ask.model.interfaces.audioplayer.AudioItem;
import com.amazon.ask.model.interfaces.audioplayer.AudioItemMetadata;
import com.amazon.ask.model.interfaces.audioplayer.ClearBehavior;
import com.amazon.ask.model.interfaces.audioplayer.ClearQueueDirective;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import com.amazon.ask.model.interfaces.audioplayer.PlayDirective;
import com.amazon.ask.model.interfaces.audioplayer.StopDirective;
import com.amazon.ask.model.interfaces.audioplayer.Stream;
import com.amazon.ask.model.interfaces.display.HintDirective;
import com.amazon.ask.model.interfaces.display.PlainTextHint;
import com.amazon.ask.model.interfaces.display.RenderTemplateDirective;
import com.amazon.ask.model.interfaces.display.Template;
import com.amazon.ask.model.interfaces.videoapp.LaunchDirective;
import com.amazon.ask.model.interfaces.videoapp.Metadata;
import com.amazon.ask.model.interfaces.videoapp.VideoItem;
import com.amazon.ask.model.ui.AskForPermissionsConsentCard;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.Image;
import com.amazon.ask.model.ui.LinkAccountCard;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.Reprompt;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.model.ui.StandardCard;

/**
 * A builder that can be used to construct a complete skill response containing speech, directives, etc.
 */
public class ResponseBuilder {

    /**
     * SSML Speak open tag.
     */
    private static final String SPEAK_OPEN_TAG = "<speak>";

    /**
     * SSML Speak close tag.
     */
    private static final String SPEAK_CLOSE_TAG = "</speak>";

    /**
     * Output speech.
     */
    protected OutputSpeech speech;

    /**
     * Output in card format.
     */
    protected Card card;

    /**
     * List of device directives.
     */
    protected List<Directive> directiveList;

    /**
     * Determines if the current session should end or not.
     */
    protected Boolean shouldEndSession;

    /**
     * Reprompt message to keep the session open.
     */
    protected Reprompt reprompt;

    /**
     * Instance of {@link CanFulfillIntent}.
     */
    protected CanFulfillIntent canFulfillIntent;

    /**
     * {@link Object} to include in response.
     */
    protected Object apiResponse;

    /**
     * {@link ExperimentTriggerResponse} to include in response.
     */
    protected ExperimentTriggerResponse experimentTriggerResponse;

    /**
     * Builds an instance of Response to be returned to the client.
     * @return {@link Response}.
     */
    public Optional<Response> build() {
        return Optional.of(Response.builder()
                .withOutputSpeech(speech)
                .withCard(card)
                .withReprompt(reprompt)
                .withDirectives(directiveList)
                .withCanFulfillIntent(canFulfillIntent)
                .withShouldEndSession(shouldEndSession)
                .withApiResponse(apiResponse)
                .withExperimentation(experimentTriggerResponse)
                .build());
    }

    /**
     * Sets {@link OutputSpeech} on the response. Speech is always wrapped in SSML tags.
     *
     * @param speechText speech text
     * @return response builder
     */
    public ResponseBuilder withSpeech(final String speechText) {
        return withSpeech(speechText, null);
    }

    /**
     * Sets {@link OutputSpeech} on the response. Speech is always wrapped in SSML tags.
     *
     * @param speechText speech text
     * @param playBehavior determines the queuing and playback of this output speech
     * @return response builder
     */
    public ResponseBuilder withSpeech(final String speechText, final com.amazon.ask.model.ui.PlayBehavior playBehavior) {
        this.speech = SsmlOutputSpeech.builder()
                .withSsml("<speak>" + trimOutputSpeech(speechText) + "</speak>")
                .withPlayBehavior(playBehavior)
                .build();
        return this;
    }

    /**
     * Sets a {@link Card} object on the response.
     *
     * @param card card object
     * @return response builder
     */
    public ResponseBuilder withCard(final Card card) {
        this.card = card;
        return this;
    }

    /**
     * Sets a simple {@link Card} on the response with the specified title and content.
     *
     * @param cardTitle title for card
     * @param cardText text in the card
     * @return response builder
     */
    public ResponseBuilder withSimpleCard(final String cardTitle, final String cardText) {
        this.card = SimpleCard.builder()
                .withContent(cardText)
                .withTitle(cardTitle)
                .build();
        return this;
    }

    /**
     * Sets a standard {@link Card} on the response with the specified title, content and image.
     *
     * @param cardTitle title for card
     * @param cardText text in the card
     * @param image image
     * @return response builder
     */
    public ResponseBuilder withStandardCard(final String cardTitle, final String cardText, final Image image) {
        this.card = StandardCard.builder()
                .withText(cardText)
                .withImage(image)
                .withTitle(cardTitle)
                .build();
        return this;
    }

    /**
     * Sets a {@link LinkAccountCard} on the response.
     *
     * @return response builder
     */
    public ResponseBuilder withLinkAccountCard() {
        this.card = LinkAccountCard.builder().build();
        return this;
    }

    /**
     * Sets a {@link AskForPermissionsConsentCard} card on the response.
     *
     * @param permissions permission array
     * @return response builder
     */
    public ResponseBuilder withAskForPermissionsConsentCard(final List<String> permissions) {
        this.card = AskForPermissionsConsentCard.builder()
                .withPermissions(permissions)
                .build();
        return this;
    }

    /**
     * Sets {@link Reprompt} speech on the response. Speech is always wrapped in SSML tags.
     *
     * @param text reprompt text
     * @return response builder
     */
    public ResponseBuilder withReprompt(final String text) {
        return withReprompt(text, null, null);
    }

    /**
     * Sets {@link Reprompt} speech on the response. Speech is always wrapped in SSML tags.
     *
     * @param text reprompt text
     * @param directive Directive object
     * @return response builder
     */
    public ResponseBuilder withReprompt(final String text, final Directive directive) {
        return withReprompt(text, directive, null);
    }

    /**
     * Sets {@link Reprompt} speech on the response. Speech is always wrapped in SSML tags.
     *
     * @param text reprompt text
     * @param playBehavior determines the queuing and playback of this output speech
     * @return response builder
     */
    public ResponseBuilder withReprompt(final String text, final com.amazon.ask.model.ui.PlayBehavior playBehavior) {
        return withReprompt(text, null, playBehavior);
    }

    /**
     * Sets {@link Reprompt} speech on the response. Speech is always wrapped in SSML tags.
     *
     * @param text reprompt text
     * @param directive Directive object
     * @param playBehavior determines the queuing and playback of this output speech
     * @return response builder
     */
    public ResponseBuilder withReprompt(final String text, final Directive directive, final com.amazon.ask.model.ui.PlayBehavior playBehavior) {
        if (directive != null) {

            SsmlOutputSpeech outputSpeech = null;
            if ((text == null || text.isEmpty())) {
                if (playBehavior != null) {
                    outputSpeech = SsmlOutputSpeech.builder()
                            .withPlayBehavior(playBehavior)
                            .build();
                }
            } else {
                outputSpeech = SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + trimOutputSpeech(text) + "</speak>")
                        .withPlayBehavior(playBehavior)
                        .build();
            }

            this.reprompt = Reprompt.builder()
                    .withOutputSpeech(outputSpeech)
                    .addDirectivesItem(directive)
                    .build();
        } else {
            this.reprompt = Reprompt.builder()
                    .withOutputSpeech(SsmlOutputSpeech.builder()
                            .withSsml("<speak>" + trimOutputSpeech(text) + "</speak>")
                            .withPlayBehavior(playBehavior)
                            .build())
                    .build();
        }

        this.withShouldEndSession(false);

        return this;
    }

    /**
     * Sets whether the session should end after this response.
     *
     * @param shouldEndSession whether session should end or not, or null
     * @return response builder
     */
    public ResponseBuilder withShouldEndSession(final Boolean shouldEndSession) {
        if (!this.isVideoAppLaunchDirectivePresent()) {
            this.shouldEndSession = shouldEndSession;
        }

        return this;
    }

    /**
     * Sets the {@link Object} object in the response.
     *
     * @param apiResponse {@link Object} to include in the response.
     *
     * The API response is a free form type. Values provided should be
     * either primitive Java types or complex types serializable by Jackson's
     * ObjectMapper.
     *
     * @return response builder
     */
    public ResponseBuilder withApiResponse(final Object apiResponse) {
        this.apiResponse = apiResponse;
        return this;
    }

    /**
     * Adds a {@link HintDirective} to the response.
     *
     * @param hintText hint text
     * @return response builder
     */
    public ResponseBuilder addHintDirective(final String hintText) {
        PlainTextHint hint = PlainTextHint.builder()
                .withText(hintText)
                .build();
        HintDirective hintDirective = HintDirective.builder()
                .withHint(hint)
                .build();
        return addDirective(hintDirective);
    }

    /**
     * Adds a VideoApp {@link LaunchDirective} to the response to play a video.
     *
     * @param source location of video content at a remote HTTPS location
     * @param title title that can be displayed on VideoApp
     * @param subtitle subtitle that can be displayed on VideoApp
     * @return response builder
     */
    public ResponseBuilder addVideoAppLaunchDirective(final String source, final String title, final String subtitle) {
        Metadata metadata = Metadata.builder()
                .withSubtitle(subtitle)
                .withTitle(title)
                .build();

        VideoItem videoItem = VideoItem.builder()
                .withSource(source)
                .withMetadata(metadata)
                .build();
        LaunchDirective videoDirective = LaunchDirective.builder()
                .withVideoItem(videoItem)
                .build();

        this.shouldEndSession = null;
        return addDirective(videoDirective);
    }

    /**
     * Adds a Display {@link RenderTemplateDirective} to the response.
     *
     * @param template display template
     * @return response builder
     */
    public ResponseBuilder addRenderTemplateDirective(final Template template) {
        RenderTemplateDirective templateDirective = RenderTemplateDirective.builder()
                .withTemplate(template)
                .build();
        return addDirective(templateDirective);
    }

    /**
     * Adds a Dialog {@link DelegateDirective} to the response.
     *
     * @param updatedIntent updated intent
     * @return response builder
     */
    public ResponseBuilder addDelegateDirective(final Intent updatedIntent) {
        DelegateDirective delegateDirective = DelegateDirective.builder()
                .withUpdatedIntent(updatedIntent)
                .build();
        return addDirective(delegateDirective);
    }

    /**
     * Adds a Dialog {@link ElicitSlotDirective} to the response.
     *
     * @param slotName name of slot to elicit
     * @param updatedIntent updated intent
     * @return response builder
     */
    public ResponseBuilder addElicitSlotDirective(final String slotName, final Intent updatedIntent) {
        ElicitSlotDirective elicitSlotDirective = ElicitSlotDirective.builder()
                .withUpdatedIntent(updatedIntent)
                .withSlotToElicit(slotName)
                .build();
        return addDirective(elicitSlotDirective);
    }

    /**
     * Adds a Dialog {@link ConfirmSlotDirective} to the response.
     *
     * @param slotName name of slot to elicit
     * @param updatedIntent updated intent
     * @return response builder
     */
    public ResponseBuilder addConfirmSlotDirective(final String slotName, final Intent updatedIntent) {
        ConfirmSlotDirective confirmSlotDirective = ConfirmSlotDirective.builder()
                .withSlotToConfirm(slotName)
                .withUpdatedIntent(updatedIntent)
                .build();
        return addDirective(confirmSlotDirective);
    }

    /**
     * Adds a Dialog {@link ConfirmIntentDirective} to the response.
     *
     * @param updatedIntent updated intent
     * @return response builder
     */
    public ResponseBuilder addConfirmIntentDirective(final Intent updatedIntent) {
        ConfirmIntentDirective confirmIntentDirective = ConfirmIntentDirective.builder()
                .withUpdatedIntent(updatedIntent)
                .build();
        return addDirective(confirmIntentDirective);
    }

    /**
     * Adds an AudioPlayer {@link PlayDirective} to the response.
     *
     * @param playBehavior Describes playback behavior
     * @param offsetInMilliseconds The timestamp in the stream from which Alexa should begin playback
     * @param expectedPreviousToken A token that represents the expected previous stream
     * @param token A token that represents the audio stream. This token cannot exceed 1024 characters
     * @param url Identifies the location of audio content at a remote HTTPS location
     * @return response builder
     */
    public ResponseBuilder addAudioPlayerPlayDirective(final PlayBehavior playBehavior, final Long offsetInMilliseconds,
                                                       final String expectedPreviousToken, final String token, final String url) {
        return addAudioPlayerPlayDirective(playBehavior, offsetInMilliseconds, expectedPreviousToken, token, url, null);
    }

    /**
     * Adds an AudioPlayer {@link PlayDirective} to the response.
     *
     * @param playBehavior Describes playback behavior
     * @param offsetInMilliseconds The timestamp in the stream from which Alexa should begin playback
     * @param expectedPreviousToken A token that represents the expected previous stream
     * @param token A token that represents the audio stream. This token cannot exceed 1024 characters
     * @param url Identifies the location of audio content at a remote HTTPS location
     * @param metadata Stream's metadata that can be displayed on AudioPlayer
     * @return response builder
     */
    public ResponseBuilder addAudioPlayerPlayDirective(final PlayBehavior playBehavior, final Long offsetInMilliseconds,
            final String expectedPreviousToken, final String token, final String url,
            final AudioItemMetadata metadata) {

        return addDirective(buildAudioPlayerPlayDirective(playBehavior, offsetInMilliseconds, expectedPreviousToken,
                token, url, metadata));
    }

    /**
     * Adds an AudioPlayer {@link StopDirective} to the response.
     *
     * @return response builder
     */
    public ResponseBuilder addAudioPlayerStopDirective() {
        StopDirective stopDirective = StopDirective.builder().build();
        return addDirective(stopDirective);
    }

    /**
     * Adds an AudioPlayer {@link ClearQueueDirective} to the response.
     *
     * @param clearBehavior  Describes the clear queue behavior
     * @return response builder
     */
    public ResponseBuilder addAudioPlayerClearQueueDirective(final ClearBehavior clearBehavior) {
        ClearQueueDirective clearQueueDirective = ClearQueueDirective.builder()
                .withClearBehavior(clearBehavior)
                .build();
        return addDirective(clearQueueDirective);
    }

    /**
     * Adds a given {@link Directive} to the response.
     *
     * @param directive directive to send with the response back to the Alexa device
     * @return response builder
     */
    public ResponseBuilder addDirective(final Directive directive) {
        if (directiveList == null) {
            directiveList = new ArrayList<>();
        }
        directiveList.add(directive);

        return this;
    }

    /**
     * Adds a given experimentId to the Experiment Trigger response.
     *
     * @param experimentId the identifier of the experiment that is executed
     * @return response builder
     */
    public ResponseBuilder addExperimentTrigger(final String experimentId) {
        if (experimentTriggerResponse == null) {
            experimentTriggerResponse = ExperimentTriggerResponse.builder().build();
        }
        experimentTriggerResponse.getTriggeredExperiments().add(experimentId);

        return this;
    }

    /**
     * Helper method for adding canFulfillIntent to response.
     * @param canFulfillIntent canFulfillIntent.
     * @return response builder
     */
    public ResponseBuilder withCanFulfillIntent(final CanFulfillIntent canFulfillIntent) {
        this.canFulfillIntent = canFulfillIntent;
        return this;
    }

    /**
     * Checks the presence of VideoApp Launch Directive.
     * @return true if the directive is present.
     */
    private boolean isVideoAppLaunchDirectivePresent() {
        if (directiveList == null || directiveList.isEmpty()) {
            return false;
        }

        for (Directive directive : directiveList) {
            if (directive != null && directive.getType().equals("VideoApp.Launch")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes <speak> opening and closing XML tags in speechOutput.
     * @param outputSpeech output speech
     * @return trimmed output speech
     */
    private String trimOutputSpeech(final String outputSpeech) {
        if (outputSpeech == null) {
            return "";
        }
        String trimmedOutputSpeech = outputSpeech.trim();
        if (trimmedOutputSpeech.startsWith(SPEAK_OPEN_TAG) && trimmedOutputSpeech.endsWith(SPEAK_CLOSE_TAG)) {
            return trimmedOutputSpeech.substring(SPEAK_OPEN_TAG.length(), trimmedOutputSpeech.length() - SPEAK_CLOSE_TAG.length())
                    .trim();
        }
        return trimmedOutputSpeech;
    }

    /**
     * Builds an AudioPlayer {@link PlayDirective}.
     *
     * @param playBehavior Describes playback behavior
     * @param offsetInMilliseconds The timestamp in the stream from which Alexa should begin playback
     * @param expectedPreviousToken A token that represents the expected previous stream
     * @param token A token that represents the audio stream. This token cannot exceed 1024 characters
     * @param url Identifies the location of audio content at a remote HTTPS location
     * @param metadata Metadata that can be displayed on AudioPlayer
     * @return the play directive
     */
    private PlayDirective buildAudioPlayerPlayDirective(final PlayBehavior playBehavior,
            final Long offsetInMilliseconds,
            final String expectedPreviousToken, final String token, final String url,
            final AudioItemMetadata metadata) {
        Stream stream = Stream.builder()
                .withOffsetInMilliseconds(offsetInMilliseconds)
                .withExpectedPreviousToken(expectedPreviousToken)
                .withToken(token)
                .withUrl(url)
                .build();

        AudioItem.Builder audioItemBuilder = AudioItem.builder();
        if (metadata != null) {
            audioItemBuilder.withMetadata(metadata);
        }
        AudioItem audioItem = audioItemBuilder
                .withStream(stream)
                .build();

        return PlayDirective.builder()
                .withPlayBehavior(playBehavior)
                .withAudioItem(audioItem)
                .build();
    }
}
