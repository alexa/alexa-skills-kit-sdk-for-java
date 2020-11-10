/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request;

import com.amazon.ask.model.Context;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.interfaces.viewport.Shape;
import com.amazon.ask.model.interfaces.viewport.ViewportState;
import com.amazon.ask.request.viewport.ViewportUtils;
import com.amazon.ask.request.viewport.ViewportProfile;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ViewportUtilsTest {
    @Test
    public void viewport_not_in_request_maps_to_unknown_viewport() {
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
        .withContext(Context.builder().build())
        .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.UNKNOWN_VIEWPORT_PROFILE);
    }

    @Test
    public void rook_maps_to_hub_round_small() {
        ViewportState viewportState = getViewportState("Rook");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.HUB_ROUND_SMALL);
    }

    @Test
    public void knight_maps_to_hub_landscape_medium() {
        ViewportState viewportState = getViewportState("Knight");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.HUB_LANDSCAPE_MEDIUM);
    }

    @Test
    public void bishop_maps_to_hub_landscape_large() {
        ViewportState viewportState = getViewportState("Bishop");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.HUB_LANDSCAPE_LARGE);
    }

    @Test
    public void echo_show_5_maps_to_hub_landscape_small() {
        ViewportState viewportState = getViewportState("EchoShow5");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.HUB_LANDSCAPE_SMALL);
    }
 
    @Test
    public void vox_landscape_maps_to_mobile_landscape_small() {
        ViewportState viewportState = getViewportState("VoxLandscape");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.MOBILE_LANDSCAPE_SMALL);
    }

    @Test
    public void vox_portrait_maps_to_mobile_portrait_small() {
        ViewportState viewportState = getViewportState("VoxPortrait");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.MOBILE_PORTRAIT_SMALL);
    }

    @Test
    public void fire_tablet_landscape_maps_to_mobile_landscape_medium() {
        ViewportState viewportState = getViewportState("FireTabletLandscape");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.MOBILE_LANDSCAPE_MEDIUM);
    }

    @Test
    public void fire_tablet_portrait_maps_to_mobile_portrait_medium() {
        ViewportState viewportState = getViewportState("FireTabletPortrait");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.MOBILE_PORTRAIT_MEDIUM);
    }

    @Test
    public void tv_maps_to_tv_landscape_extra_large() {
        ViewportState viewportState = getViewportState("1080pTV");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.TV_LANDSCAPE_XLARGE);
    }

    @Test
    public void tv_side_panel_maps_to_tv_portrait_medium() {
        ViewportState viewportState = getViewportState("1080pTVSidePanel");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.TV_PORTRAIT_MEDIUM);
    }

    @Test
    public void tv_bottom_panel_maps_to_tv_landscape_medium() {
        ViewportState viewportState = getViewportState("1080pTVBottomPanel");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.TV_LANDSCAPE_MEDIUM);
    }

    @Test
    public void unknown_profile_mapped() {
        ViewportState viewportState = getViewportState("foo");
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withContext(Context.builder().withViewport(viewportState).build())
                .build();
        ViewportProfile profile = ViewportUtils.getViewportProfile(requestEnvelope);
        assertEquals(profile, ViewportProfile.UNKNOWN_VIEWPORT_PROFILE);
    }

    private ViewportState getViewportState(String device) {
        if(device.equals("Rook")) {
            return ViewportState.builder()
                    .withShape(Shape.ROUND)
                    .withDpi(BigDecimal.valueOf(160))
                    .withCurrentPixelHeight(BigDecimal.valueOf(300))
                    .withCurrentPixelWidth(BigDecimal.valueOf(300))
                    .build();
        } else if (device.equals("Knight")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(160))
                    .withCurrentPixelHeight(BigDecimal.valueOf(600))
                    .withCurrentPixelWidth(BigDecimal.valueOf(960))
                    .build();
        } else if (device.equals("Bishop")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(160))
                    .withCurrentPixelHeight(BigDecimal.valueOf(960))
                    .withCurrentPixelWidth(BigDecimal.valueOf(1280))
                    .build();
        } else if (device.equals("VoxLandscape")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(240))
                    .withCurrentPixelHeight(BigDecimal.valueOf(300))
                    .withCurrentPixelWidth(BigDecimal.valueOf(600))
                    .build();
        } else if (device.equals("VoxPortrait")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(240))
                    .withCurrentPixelHeight(BigDecimal.valueOf(600))
                    .withCurrentPixelWidth(BigDecimal.valueOf(300))
                    .build();
        } else if (device.equals("FireTabletLandscape")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(240))
                    .withCurrentPixelHeight(BigDecimal.valueOf(600))
                    .withCurrentPixelWidth(BigDecimal.valueOf(960))
                    .build();
        } else if (device.equals("FireTabletPortrait")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(240))
                    .withCurrentPixelHeight(BigDecimal.valueOf(960))
                    .withCurrentPixelWidth(BigDecimal.valueOf(600))
                    .build();
        } else if (device.equals("1080pTV")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(320))
                    .withCurrentPixelHeight(BigDecimal.valueOf(960))
                    .withCurrentPixelWidth(BigDecimal.valueOf(1920))
                    .build();
        } else if (device.equals("1080pTVSidePanel")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(320))
                    .withCurrentPixelHeight(BigDecimal.valueOf(1920))
                    .withCurrentPixelWidth(BigDecimal.valueOf(300))
                    .build();
        } else if (device.equals("1080pTVBottomPanel")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(320))
                    .withCurrentPixelHeight(BigDecimal.valueOf(600))
                    .withCurrentPixelWidth(BigDecimal.valueOf(960))
                    .build();
        } else if (device.equals("EchoShow5")) {
            return ViewportState.builder()
                    .withShape(Shape.RECTANGLE)
                    .withDpi(BigDecimal.valueOf(160))
                    .withCurrentPixelHeight(BigDecimal.valueOf(480))
                    .withCurrentPixelWidth(BigDecimal.valueOf(960))
                    .build();
        }
        return ViewportState.builder()
                .withShape(Shape.RECTANGLE)
                .withDpi(BigDecimal.valueOf(130))
                .withCurrentPixelHeight(BigDecimal.valueOf(1000))
                .withCurrentPixelWidth(BigDecimal.valueOf(1100))
                .build();
    }

}
