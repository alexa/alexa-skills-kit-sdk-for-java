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

import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentConfirmationStatus;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.amazon.ask.model.canfulfill.CanFulfillIntent;
import com.amazon.ask.model.canfulfill.CanFulfillIntentValues;
import com.amazon.ask.model.canfulfill.CanFulfillSlot;
import com.amazon.ask.model.canfulfill.CanFulfillSlotValues;
import com.amazon.ask.model.canfulfill.CanUnderstandSlotValues;
import com.amazon.ask.model.dialog.ConfirmIntentDirective;
import com.amazon.ask.model.dialog.ConfirmSlotDirective;
import com.amazon.ask.model.dialog.DelegateDirective;
import com.amazon.ask.model.dialog.ElicitSlotDirective;
import com.amazon.ask.model.interfaces.audioplayer.AudioItem;
import com.amazon.ask.model.interfaces.audioplayer.ClearBehavior;
import com.amazon.ask.model.interfaces.audioplayer.ClearQueueDirective;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import com.amazon.ask.model.interfaces.audioplayer.PlayDirective;
import com.amazon.ask.model.interfaces.audioplayer.StopDirective;
import com.amazon.ask.model.interfaces.audioplayer.Stream;
import com.amazon.ask.model.interfaces.display.BackButtonBehavior;
import com.amazon.ask.model.interfaces.display.BodyTemplate1;
import com.amazon.ask.model.interfaces.display.HintDirective;
import com.amazon.ask.model.interfaces.display.ImageInstance;
import com.amazon.ask.model.interfaces.display.PlainTextHint;
import com.amazon.ask.model.interfaces.display.RenderTemplateDirective;
import com.amazon.ask.model.interfaces.display.TextContent;
import com.amazon.ask.model.interfaces.videoapp.LaunchDirective;
import com.amazon.ask.model.interfaces.videoapp.Metadata;
import com.amazon.ask.model.interfaces.videoapp.VideoItem;

import com.amazon.ask.model.ui.AskForPermissionsConsentCard;
import com.amazon.ask.model.ui.Image;
import com.amazon.ask.model.ui.LinkAccountCard;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.Reprompt;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.model.ui.StandardCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ResponseBuilderTest {
    ResponseBuilder builder;

    @Before
    public void setup() {
        builder = new ResponseBuilder();
    }

    @Test
    public void build_empty_response() {
        Optional<Response> responseWithBuilder = builder.build();
        assertEquals(responseWithBuilder.get(), Response.builder().build());
    }

    @Test
    public void build_response_with_hint_directive() {
        Optional<Response> responseWithBuilder = builder
                .addHintDirective("fooHint")
                .build();

        HintDirective hintDirective = HintDirective.builder()
                .withHint(PlainTextHint.builder().withText("fooHint").build())
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(hintDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_video_directive() {
        Optional<Response> responseWithBuilder = builder
                .addVideoAppLaunchDirective("fooVideoUrl", "fooTitle", "fooSubtitle")
                .build();
        assertEquals(responseWithBuilder.get().getShouldEndSession(), null);

        Metadata metadata = Metadata.builder()
                .withSubtitle("fooSubtitle")
                .withTitle("fooTitle")
                .build();

        VideoItem videoItem = VideoItem.builder()
                .withSource("fooVideoUrl")
                .withMetadata(metadata)
                .build();

        LaunchDirective videoDirective = LaunchDirective.builder()
                .withVideoItem(videoItem)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(videoDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_speech_and_playbehavior() {
        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + "foo" + "</speak>")
                        .withPlayBehavior(com.amazon.ask.model.ui.PlayBehavior.ENQUEUE)
                        .build())
                .build();
        Optional<Response> responseWithBuilder = builder
                .withSpeech("foo", com.amazon.ask.model.ui.PlayBehavior.ENQUEUE)
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_reprompt_and_playbehavior() {
        Response response = Response.builder()
                .withReprompt(Reprompt.builder()
                    .withOutputSpeech(SsmlOutputSpeech.builder()
                            .withSsml("<speak>" + "foo" + "</speak>")
                            .withPlayBehavior(com.amazon.ask.model.ui.PlayBehavior.ENQUEUE)
                            .build())
                        .build())
                .withShouldEndSession(false)
                .build();
        Optional<Response> responseWithBuilder = builder
                .withReprompt("foo", com.amazon.ask.model.ui.PlayBehavior.ENQUEUE)
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_video_directive_with_reprompt_before() {
        Optional<Response> responseWithBuilder = builder
                .withReprompt("fooReprompt")
                .addVideoAppLaunchDirective("fooVideoUrl", "fooTitle", "fooSubtitle")
                .build();
        assertEquals(responseWithBuilder.get().getShouldEndSession(), null);

        Metadata metadata = Metadata.builder()
                .withSubtitle("fooSubtitle")
                .withTitle("fooTitle")
                .build();

        VideoItem videoItem = VideoItem.builder()
                .withSource("fooVideoUrl")
                .withMetadata(metadata)
                .build();

        LaunchDirective videoDirective = LaunchDirective.builder()
                .withVideoItem(videoItem)
                .build();

        OutputSpeech outputSpeech = SsmlOutputSpeech.builder()
                .withSsml("<speak>fooReprompt</speak>")
                .build();

        Reprompt reprompt = Reprompt.builder()
                .withOutputSpeech(outputSpeech)
                .build();

        Response response = Response.builder()
                .withReprompt(reprompt)
                .withDirectives(Collections.singletonList(videoDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_video_directive_with_reprompt_after() {
        Optional<Response> responseWithBuilder = builder
                .addVideoAppLaunchDirective("fooVideoUrl", "fooTitle", "fooSubtitle")
                .withReprompt("fooReprompt")
                .build();
        assertEquals(responseWithBuilder.get().getShouldEndSession(), null);

        Metadata metadata = Metadata.builder()
                .withSubtitle("fooSubtitle")
                .withTitle("fooTitle")
                .build();

        VideoItem videoItem = VideoItem.builder()
                .withSource("fooVideoUrl")
                .withMetadata(metadata)
                .build();

        LaunchDirective videoDirective = LaunchDirective.builder()
                .withVideoItem(videoItem)
                .build();

        OutputSpeech outputSpeech = SsmlOutputSpeech.builder()
                .withSsml("<speak>fooReprompt</speak>")
                .build();

        Reprompt reprompt = Reprompt.builder()
                .withOutputSpeech(outputSpeech)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(videoDirective))
                .withReprompt(reprompt)
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_delegate_directive() {
        Slot slot1 = Slot.builder()
                .withConfirmationStatus(SlotConfirmationStatus.NONE)
                .withName("slot1")
                .withValue("value1")
                .build();

        Intent updatedIntent = Intent.builder()
                .withConfirmationStatus(IntentConfirmationStatus.NONE)
                .withName("intentName")
                .withSlots(Collections.singletonMap("slot1", slot1))
                .build();

        Optional<Response> responseWithBuilder = builder
                .addDelegateDirective(updatedIntent)
                .build();

        DelegateDirective delegateDirective = DelegateDirective.builder()
                .withUpdatedIntent(updatedIntent)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(delegateDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_elicit_slot_directive() {
        Slot slot1 = Slot.builder()
                .withConfirmationStatus(SlotConfirmationStatus.NONE)
                .withName("slot1")
                .withValue("value1")
                .build();

        Intent updatedIntent = Intent.builder()
                .withConfirmationStatus(IntentConfirmationStatus.NONE)
                .withName("intentName")
                .withSlots(Collections.singletonMap("slot1", slot1))
                .build();

        Optional<Response> responseWithBuilder = builder
                .addElicitSlotDirective("slotName", updatedIntent)
                .build();

        ElicitSlotDirective elicitSlotDirective = ElicitSlotDirective.builder()
                .withSlotToElicit("slotName")
                .withUpdatedIntent(updatedIntent)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(elicitSlotDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_confirm_slot_directive() {
        Slot slot1 = Slot.builder()
                .withConfirmationStatus(SlotConfirmationStatus.NONE)
                .withName("slot1")
                .withValue("value1")
                .build();

        Intent updatedIntent = Intent.builder()
                .withConfirmationStatus(IntentConfirmationStatus.NONE)
                .withName("intentName")
                .withSlots(Collections.singletonMap("slot1", slot1))
                .build();

        Optional<Response> responseWithBuilder = builder
                .addConfirmSlotDirective("slotName", updatedIntent)
                .build();

        ConfirmSlotDirective confirmSlotDirective = ConfirmSlotDirective.builder()
                .withSlotToConfirm("slotName")
                .withUpdatedIntent(updatedIntent)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(confirmSlotDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_confirm_intent_directive() {
        Slot slot1 = Slot.builder()
                .withConfirmationStatus(SlotConfirmationStatus.NONE)
                .withName("slot1")
                .withValue("value1")
                .build();

        Intent updatedIntent = Intent.builder()
                .withConfirmationStatus(IntentConfirmationStatus.NONE)
                .withName("intentName")
                .withSlots(Collections.singletonMap("slot1", slot1))
                .build();

        Optional<Response> responseWithBuilder = builder
                .addConfirmIntentDirective(updatedIntent)
                .build();

        ConfirmIntentDirective confirmIntentDirective = ConfirmIntentDirective.builder()
                .withUpdatedIntent(updatedIntent)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(confirmIntentDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_play_directive() {
        Optional<Response> responseWithBuilder = builder
                .addAudioPlayerPlayDirective(PlayBehavior.REPLACE_ALL, 0L, "fooPreviousToken",
                        "fooToken", "fooUrl")
                .build();

        Stream stream = Stream.builder()
                .withUrl("fooUrl")
                .withToken("fooToken")
                .withExpectedPreviousToken("fooPreviousToken")
                .withOffsetInMilliseconds(0L)
                .build();

        AudioItem audioItem = AudioItem.builder()
                .withStream(stream)
                .build();

        PlayDirective playDirective = PlayDirective.builder()
                .withPlayBehavior(PlayBehavior.REPLACE_ALL)
                .withAudioItem(audioItem)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(playDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_stop_directive() {
        Optional<Response> responseWithBuilder = builder
                .addAudioPlayerStopDirective()
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(StopDirective.builder().build()))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_clear_queue_directive() {
        Optional<Response> responseWithBuilder = builder
                .addAudioPlayerClearQueueDirective(ClearBehavior.CLEAR_ALL)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(ClearQueueDirective.builder()
                        .withClearBehavior(ClearBehavior.CLEAR_ALL)
                        .build()))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_null_directive() {
        Optional<Response> responseWithBuilder = builder
                .addDirective(null)
                .withShouldEndSession(true)
                .build();

        Response response = Response.builder()
                .addDirectivesItem(null)
                .withShouldEndSession(true)
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_render_template_directive() {
        ImageInstance imageInstance = ImageInstance.builder()
                .withUrl("fooUrl")
                .withHeightPixels(2)
                .withWidthPixels(2)
                .build();

        List<ImageInstance> imageSources = new ArrayList<>();
        imageSources.add(imageInstance);

        com.amazon.ask.model.interfaces.display.Image image = com.amazon.ask.model.interfaces.display.Image.builder()
                .withSources(imageSources)
                .build();

        BodyTemplate1 bodyTemplate1 = BodyTemplate1.builder()
                .withBackButton(BackButtonBehavior.HIDDEN)
                .withBackgroundImage(image)
                .withTextContent(TextContent.builder().build())
                .withTitle("fooTitle")
                .withToken("fooToken")
                .build();

        Optional<Response> responseWithBuilder = builder
                .addRenderTemplateDirective(bodyTemplate1)
                .build();

        RenderTemplateDirective renderTemplateDirective = RenderTemplateDirective.builder()
                .withTemplate(bodyTemplate1)
                .build();

        Response response = Response.builder()
                .withDirectives(Collections.singletonList(renderTemplateDirective))
                .build();

        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_setters() {
        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + "foo" + "</speak>")
                        .build())
                .withCard(SimpleCard.builder()
                        .withContent("fooText")
                        .withTitle("fooTitle")
                        .build())
                .withReprompt(Reprompt.builder()
                        .withOutputSpeech(SsmlOutputSpeech.builder()
                                .withSsml("<speak>" + "fooReprompt"  + "</speak>")
                                .build())
                        .build())
                .withShouldEndSession(false)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withSpeech("<speak>foo</speak>")
                .withSimpleCard("fooTitle", "fooText")
                .withReprompt("fooReprompt")
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_setters_and_speak_tag() {
        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + "foo" + "</speak>")
                        .build())
                .withCard(SimpleCard.builder()
                        .withContent("fooText")
                        .withTitle("fooTitle")
                        .build())
                .withReprompt(Reprompt.builder()
                        .withOutputSpeech(SsmlOutputSpeech.builder()
                                .withSsml("<speak>" + "fooReprompt"  + "</speak>")
                                .build())
                        .build())
                .withShouldEndSession(false)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withSpeech("foo")
                .withSimpleCard("fooTitle", "fooText")
                .withReprompt("<speak> fooReprompt </speak>")
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_setters_standard_card() {
        Image image = Image.builder()
                .withSmallImageUrl("fooUrl")
                .build();

        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + "foo" + "</speak>")
                        .build())
                .withCard(StandardCard.builder()
                        .withText("fooText")
                        .withTitle("fooTitle")
                        .withImage(image)
                        .build())
                .withReprompt(Reprompt.builder()
                        .withOutputSpeech(SsmlOutputSpeech.builder()
                                .withSsml("<speak>" + "fooReprompt"  + "</speak>")
                                .build())
                        .build())
                .withShouldEndSession(false)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withSpeech("foo")
                .withStandardCard("fooTitle", "fooText", image)
                .withReprompt("fooReprompt")
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_card() {
        SimpleCard simpleCard = SimpleCard.builder()
                .withTitle("fooTitle")
                .withContent("fooContent")
                .build();

        Response response = Response.builder()
                .withCard(simpleCard)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withCard(simpleCard)
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_link_account_card() {
        Response response = Response.builder()
                .withCard(LinkAccountCard.builder().build())
                .build();

        Optional<Response> responseWithBuilder = builder
                .withLinkAccountCard()
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_ask_for_permissions_card() {
        AskForPermissionsConsentCard askForPermissionsConsentCard = AskForPermissionsConsentCard.builder()
                .withPermissions(Collections.singletonList("fooPermission"))
                .build();

        Response response = Response.builder()
                .withCard(askForPermissionsConsentCard)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withAskForPermissionsConsentCard(Collections.singletonList("fooPermission"))
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_empty_speech() {
        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak></speak>")
                        .build())
                .build();
        Optional<Response> responseWithBuilder = builder
                .withSpeech(null)
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_canFulfillIntent() {
        CanFulfillSlot soundSlot = CanFulfillSlot.builder()
                .withCanUnderstand(CanUnderstandSlotValues.YES)
                .withCanFulfill(CanFulfillSlotValues.YES)
                .build();
        CanFulfillIntent skillQueryResponse = CanFulfillIntent.builder()
                .withCanFulfill(CanFulfillIntentValues.YES)
                .putSlotsItem("Sound", soundSlot)
                .build();

        Response response = Response.builder()
                .withCanFulfillIntent(skillQueryResponse)
                .build();

        Optional<Response> responseWithBuilder = builder
                .withCanFulfillIntent(skillQueryResponse)
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }
    
    @Test
    public void build_response_testing_trim() {
        Response response = Response.builder()
                .withOutputSpeech(SsmlOutputSpeech.builder()
                        .withSsml("<speak>" + "foo" + "</speak>")
                        .build())
                .withReprompt(Reprompt.builder()
                        .withOutputSpeech(SsmlOutputSpeech.builder()
                                .withSsml("<speak>" + "fooReprompt"  + "</speak>")
                                .build())
                        .build())
                .withShouldEndSession(false)
                .build();
 
        Optional<Response> responseWithBuilder = builder
                .withSpeech("   <speak>     foo     </speak>")
                .withReprompt("fooReprompt")
                .build();
        assertEquals(responseWithBuilder.get(), response);
    }

    @Test
    public void build_response_with_api_response() {
            String apiResponse = "foo";
            Response response = Response.builder()
                .withApiResponse(apiResponse)
                .build();
            Optional<Response> responseWithBuilder = builder
                .withApiResponse(apiResponse)
                .build();
            assertEquals(responseWithBuilder.get(), response);
    }
}
