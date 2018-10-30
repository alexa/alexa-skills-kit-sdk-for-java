/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.request.viewport;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.interfaces.viewport.Shape;
import com.amazon.ask.model.interfaces.viewport.ViewportState;

public class ViewportUtils {

    public static ViewportProfile getViewportProfile(RequestEnvelope requestEnvelope) {
        ViewportState viewportState = requestEnvelope.getContext().getViewport();
        Shape shape = viewportState.getShape();
        int currentPixelWidth = viewportState.getCurrentPixelWidth().intValueExact();
        int currentPixelHeight = viewportState.getCurrentPixelHeight().intValueExact();
        int dpi = viewportState.getDpi().intValueExact();
        Orientation orientation = getOrientation(currentPixelHeight, currentPixelWidth);

        if (shape == Shape.ROUND &&
                orientation == Orientation.EQUAL &&
                getSize(currentPixelHeight) == Size.XSMALL &&
                getSize(currentPixelWidth) == Size.XSMALL &&
                getDensity(dpi) == Density.LOW) {
            return ViewportProfile.HUB_ROUND_SMALL;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth).ordinal() <= Size.MEDIUM.ordinal() &&
                getSize(currentPixelHeight).ordinal() <= Size.SMALL.ordinal() &&
                getDensity(dpi) == Density.LOW) {
            return ViewportProfile.HUB_LANDSCAPE_MEDIUM;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth).ordinal() >= Size.LARGE.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal() &&
                getDensity(dpi) == Density.LOW) {
            return ViewportProfile.HUB_LANDSCAPE_LARGE;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth).ordinal() >= Size.MEDIUM.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal() &&
                getDensity(dpi) == Density.MEDIUM) {
            return ViewportProfile.MOBILE_LANDSCAPE_MEDIUM;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.PORTRAIT &&
                getSize(currentPixelWidth).ordinal() >= Size.SMALL.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.MEDIUM.ordinal() &&
                getDensity(dpi) == Density.MEDIUM) {
            return ViewportProfile.MOBILE_PORTRAIT_MEDIUM;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth).ordinal() >= Size.SMALL.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.XSMALL.ordinal() &&
                getDensity(dpi) == Density.MEDIUM) {
            return ViewportProfile.MOBILE_LANDSCAPE_SMALL;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.PORTRAIT &&
                getSize(currentPixelWidth).ordinal() >= Size.XSMALL.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal() &&
                getDensity(dpi) == Density.MEDIUM) {
            return ViewportProfile.MOBILE_PORTRAIT_SMALL;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth).ordinal() >= Size.XLARGE.ordinal() &&
                getSize(currentPixelHeight).ordinal() >= Size.MEDIUM.ordinal() &&
                getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
            return ViewportProfile.TV_LANDSCAPE_XLARGE;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.PORTRAIT &&
                getSize(currentPixelWidth) == Size.XSMALL &&
                getSize(currentPixelHeight) == Size.XLARGE &&
                getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
            return ViewportProfile.TV_PORTRAIT_MEDIUM;

        } else if (shape == Shape.RECTANGLE &&
                orientation == Orientation.LANDSCAPE &&
                getSize(currentPixelWidth) == Size.MEDIUM &&
                getSize(currentPixelHeight) == Size.SMALL &&
                getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
            return ViewportProfile.TV_LANDSCAPE_MEDIUM;
        }
        return ViewportProfile.UNKNOWN_VIEWPORT_PROFILE;
    }

    private static Density getDensity(int dpi) {
        if (isBetween(dpi, 0, 121)) {
            return Density.XLOW;
        } else if (isBetween(dpi, 121, 161)) {
            return Density.LOW;
        } else if (isBetween(dpi, 161, 241)) {
            return Density.MEDIUM;
        } else if (isBetween(dpi, 241, 321)) {
            return Density.HIGH;
        } else if (isBetween(dpi, 321, 481)) {
            return Density.XHIGH;
        } else if (dpi >= 481) {
            return Density.XXHIGH;
        }
        throw new AskSdkException("Unknown density dpi value " + dpi);
    }

    private static Orientation getOrientation(int height, int width) {
        if (height > width) {
            return Orientation.PORTRAIT;
        } else if (height < width) {
            return Orientation.LANDSCAPE;
        }
        return Orientation.EQUAL;
    }

    private static Size getSize(int dpi) {
        if (isBetween(dpi, 0, 600)) {
            return Size.XSMALL;
        } else if (isBetween(dpi, 600, 960)) {
            return Size.SMALL;
        } else if (isBetween(dpi, 960, 1280)) {
            return Size.MEDIUM;
        } else if (isBetween(dpi, 1280, 1920)) {
            return Size.LARGE;
        } else if (dpi >= 1920) {
            return Size.XLARGE;
        }
        throw new AskSdkException("Unknown size group value " + dpi);
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}