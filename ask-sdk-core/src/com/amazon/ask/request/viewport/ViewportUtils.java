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

/**
 * Helps to determine which device is making the request.
 */
public final class ViewportUtils {

    // Density ranges.

    /**
     * XLow density upper bound.
     */
    private static final int XLOW_DENSITY_UPPER_BOUND = 121;

    /**
     * Low density upper bound.
     */
    private static final int LOW_DENSITY_UPPER_BOUND = 161;

    /**
     * Medium density upper bound.
     */
    private static final int MEDIUM_DENSITY_UPPER_BOUND = 241;

    /**
     * High density upper bound.
     */
    private static final int HIGH_DENSITY_UPPER_BOUND = 321;

    /**
     * XHigh density upper bound.
     */
    private static final int XHIGH_DENSITY_UPPER_BOUND = 481;

    // Size ranges.

    /**
     * XSmall size upper bound.
     */
    private static final int XSMALL_SIZE_UPPER_BOUND = 600;

    /**
     * Small size upper bound.
     */
    private static final int SMALL_SIZE_UPPER_BOUND = 960;

    /**
     * Medium size upper bound.
     */
    private static final int MEDIUM_SIZE_UPPER_BOUND = 1280;

    /**
     * Large size upper bound.
     */
    private static final int LARGE_SIZE_UPPER_BOUND = 1920;

    /**
     * Prevent instantiation.
     */
    private ViewportUtils() { }

    /**
     * Get viewport profile which helps to determine type of device.
     * @param requestEnvelope request envelope.
     * @return {@link ViewportProfile}.
     */
    public static ViewportProfile getViewportProfile(final RequestEnvelope requestEnvelope) {
        if (requestEnvelope.getContext().getViewport() != null) {
            ViewportState viewportState = requestEnvelope.getContext().getViewport();
            Shape shape = viewportState.getShape();
            int currentPixelWidth = viewportState.getCurrentPixelWidth().intValueExact();
            int currentPixelHeight = viewportState.getCurrentPixelHeight().intValueExact();
            int dpi = viewportState.getDpi().intValueExact();
            Orientation orientation = getOrientation(currentPixelHeight, currentPixelWidth);

            if (shape == Shape.ROUND
                    && orientation == Orientation.EQUAL
                    && getSize(currentPixelHeight) == Size.XSMALL
                    && getSize(currentPixelWidth) == Size.XSMALL
                    && getDensity(dpi) == Density.LOW) {
                return ViewportProfile.HUB_ROUND_SMALL;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() <= Size.MEDIUM.ordinal()
                    && getSize(currentPixelHeight).ordinal() <= Size.XSMALL.ordinal()
                    && getDensity(dpi) == Density.LOW) {
                return ViewportProfile.HUB_LANDSCAPE_SMALL;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() <= Size.MEDIUM.ordinal()
                    && getSize(currentPixelHeight).ordinal() <= Size.SMALL.ordinal()
                    && getDensity(dpi) == Density.LOW) {
                return ViewportProfile.HUB_LANDSCAPE_MEDIUM;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() >= Size.LARGE.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal()
                    && getDensity(dpi) == Density.LOW) {
                return ViewportProfile.HUB_LANDSCAPE_LARGE;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() >= Size.MEDIUM.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal()
                    && getDensity(dpi) == Density.MEDIUM) {
                return ViewportProfile.MOBILE_LANDSCAPE_MEDIUM;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.PORTRAIT
                    && getSize(currentPixelWidth).ordinal() >= Size.SMALL.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.MEDIUM.ordinal()
                    && getDensity(dpi) == Density.MEDIUM) {
                return ViewportProfile.MOBILE_PORTRAIT_MEDIUM;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() >= Size.SMALL.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.XSMALL.ordinal()
                    && getDensity(dpi) == Density.MEDIUM) {
                return ViewportProfile.MOBILE_LANDSCAPE_SMALL;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.PORTRAIT
                    && getSize(currentPixelWidth).ordinal() >= Size.XSMALL.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.SMALL.ordinal()
                    && getDensity(dpi) == Density.MEDIUM) {
                return ViewportProfile.MOBILE_PORTRAIT_SMALL;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth).ordinal() >= Size.XLARGE.ordinal()
                    && getSize(currentPixelHeight).ordinal() >= Size.MEDIUM.ordinal()
                    && getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
                return ViewportProfile.TV_LANDSCAPE_XLARGE;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.PORTRAIT
                    && getSize(currentPixelWidth) == Size.XSMALL
                    && getSize(currentPixelHeight) == Size.XLARGE
                    && getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
                return ViewportProfile.TV_PORTRAIT_MEDIUM;

            } else if (shape == Shape.RECTANGLE
                    && orientation == Orientation.LANDSCAPE
                    && getSize(currentPixelWidth) == Size.MEDIUM
                    && getSize(currentPixelHeight) == Size.SMALL
                    && getDensity(dpi).ordinal() >= Density.HIGH.ordinal()) {
                return ViewportProfile.TV_LANDSCAPE_MEDIUM;
            }
        }
        return ViewportProfile.UNKNOWN_VIEWPORT_PROFILE;
    }

    /**
     * Get dpi density.
     * @param dpi dots per inch.
     * @return {@link Density}.
     */
    private static Density getDensity(final int dpi) {
        if (isBetween(dpi, 0, XLOW_DENSITY_UPPER_BOUND)) {
            return Density.XLOW;
        } else if (isBetween(dpi, XLOW_DENSITY_UPPER_BOUND, LOW_DENSITY_UPPER_BOUND)) {
            return Density.LOW;
        } else if (isBetween(dpi, LOW_DENSITY_UPPER_BOUND, MEDIUM_DENSITY_UPPER_BOUND)) {
            return Density.MEDIUM;
        } else if (isBetween(dpi, MEDIUM_DENSITY_UPPER_BOUND, HIGH_DENSITY_UPPER_BOUND)) {
            return Density.HIGH;
        } else if (isBetween(dpi, HIGH_DENSITY_UPPER_BOUND, XHIGH_DENSITY_UPPER_BOUND)) {
            return Density.XHIGH;
        } else if (dpi >= XHIGH_DENSITY_UPPER_BOUND) {
            return Density.XXHIGH;
        }
        throw new AskSdkException("Unknown density dpi value " + dpi);
    }

    /**
     * Gte device orientation.
     * @param height height.
     * @param width width.
     * @return {@link Orientation}.
     */
    private static Orientation getOrientation(final int height, final int width) {
        if (height > width) {
            return Orientation.PORTRAIT;
        } else if (height < width) {
            return Orientation.LANDSCAPE;
        }
        return Orientation.EQUAL;
    }

    /**
     * Gets the size of the device in use.
     * @param dpi dots per inch.
     * @return {@link Size}.
     */
    private static Size getSize(final int dpi) {
        if (isBetween(dpi, 0, XSMALL_SIZE_UPPER_BOUND)) {
            return Size.XSMALL;
        } else if (isBetween(dpi, XSMALL_SIZE_UPPER_BOUND, SMALL_SIZE_UPPER_BOUND)) {
            return Size.SMALL;
        } else if (isBetween(dpi, SMALL_SIZE_UPPER_BOUND, MEDIUM_SIZE_UPPER_BOUND)) {
            return Size.MEDIUM;
        } else if (isBetween(dpi, MEDIUM_SIZE_UPPER_BOUND, LARGE_SIZE_UPPER_BOUND)) {
            return Size.LARGE;
        } else if (dpi >= LARGE_SIZE_UPPER_BOUND) {
            return Size.XLARGE;
        }
        throw new AskSdkException("Unknown size group value " + dpi);
    }

    /**
     * Checks range of input x.
     * @param x current input.
     * @param lower lower end.
     * @param upper upper end.
     * @return true if x is within lower and upper bounds.
     */
    private static boolean isBetween(final int x, final int lower, final int upper) {
        return lower <= x && x < upper;
    }
}
